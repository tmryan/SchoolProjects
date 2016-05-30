/*
 * Poker.cpp
 *
 *  Created on: Mar 4th, 2015
 *      Author: Thomas Ryan
 */

#include <iostream>
#include <vector>
#include <string>
#include <ctime>
#include <cstdlib>
#include <algorithm>
#include <map>

using namespace std;

/////////////////////////////
//method/class declarations
//////////////////////////

class Card {
	private:
		//suits are  HEARTS=3, DIAMONDS=4, CLUBS=5, SPADES=6
		//to match ASCII codes
		string suit;
		//faces are JACK, QUEEN, KING, ACE
		string face;
		int value;
	public:
		Card(){suit = ""; face = ""; value = 0;};
		Card(string newSuit, string newFace, int newValue){suit = newSuit; face = newFace; value = newValue;};
		int getValue();
		string getSuit();
		pair<int,string> getFace();
};

class Cards {
	protected:
		vector<Card> cards;
	public:
		Cards(){};
		vector<Card> getCards();
		int getCount();
};

class Deck: public Cards {
	public:
		Deck(){Cards::cards.reserve(52);};
		void buildDeck();
		void shuffleDeck();
		Card dealCard();
};

class Hand: public Cards {
	private:
		string name;
		pair<string,int> score;
	public:
		Hand(string newName){name = newName; Cards::cards.reserve(5);};
		void discard(int);
		void drawCard(Deck);
		string getName();
		pair<string,int> getScore();
		void setScore(string, int);
};

class Poker {
	private:
		vector<Hand> players;
		Deck deck;
		string scoreHand(int);
		void dealHands();
		void showNewGameMenu();
		void showHandsMenu();
		void showWinner();
		void showMainMenu();
		void showDiscardMenu(int);
		void printHand(int);

	public:
		//constructor starts the game loop and initializes RNG
		Poker(){srand(time(NULL)); deck = Deck(); showMainMenu();};
};

/////////
//main()
//////

int main() {
	Poker pokerGame = Poker();

	return 0;
}

/////////////////////
//method definitions
//////////////////

/////////
//POKER
//////

void Poker::showMainMenu() {
	char input;

	while(true) {
		cout << "Do you wish to play poker or quit? (p or q)" << endl;
		cin >> input;

		if(input == 'q') {
			cout << "Goodbye..." << endl;
			break;
		} else if (input == 'p') {
			cout << "Let's play!" << endl;
			cin.clear();
			showNewGameMenu();
		}
	}
}

void Poker::showNewGameMenu() {
	int numPlayers;
	bool run = true;
	string name;

	do {
		cout << "Enter number of players (2-5): " << endl;
		cin >> numPlayers;
		//validate input
		if(!cin || (numPlayers > 5 || numPlayers < 2)) {
			cin.clear();
			cin.ignore(200, '\n');
		} else {
			run = false;
		}
	} while(run);

	players.reserve(numPlayers);

	//add players to game
	for(int i = 1; i <= numPlayers; i++) {
		cout << "Enter player " << i << " name: " << endl;
		cin >> name;
		players.push_back(Hand(name));
	}

	deck.buildDeck();

	//deal hands
	for(int j = 0; j < 5; j++) {
		for(int k = 0; k < numPlayers; k++) {
			players.at(k).drawCard(deck);
		}
	}

	cout << endl;
	showHandsMenu();
}

void Poker::showHandsMenu() {
	for(unsigned int i = 0; i < players.size(); i++){
		//ask for discards
		showDiscardMenu(i);
	}
	for(unsigned int k = 0; k < players.size(); k++){
		//once discards are complete list all hands and score
		printHand(k);
	}
	showWinner();
}

void Poker::showDiscardMenu(int player) {
	bool run = true;
	bool emptyDeck = false;
	int numDiscards = 0;
	vector<int> discards;
	discards.resize(5,0);

	do {
		printHand(player);
		cout << "Enter number of cards to discard (0-5): " << endl;
		cin >> numDiscards;
		//validate input
		if(!cin || (numDiscards > 5 || numDiscards < 0)) {
			cin.clear();
			cin.ignore(200, '\n');
		} else {
			run = false;
		}
	} while(run);

	//pick cards to discard
	for(int i = 0; i < numDiscards; i++) {
		int cardNum = 0;

		if(deck.getCards().size() > 1) {
			cout << "Enter the number of a card you would like to discard: " << endl;
			cin >> cardNum;
		} else {
			cout << "The deck is out of cards!";
			emptyDeck = true;
			break;
		}

		//discard card
		players.at(player).discard(cardNum);
	}

	if(!emptyDeck) {
		//draw cards
		for(int k = 0; k < numDiscards; k++) {
			//draw card uses deck's dealCard method
			players.at(player).drawCard(deck);
		}
	}

	cout << endl;
}

void Poker::printHand(int player) {
	//this players hand
	vector<Card> hand = players.at(player).getCards();

	cout << players.at(player).getName() << "'s hand:" << endl;

	for(unsigned int i = 0; i < hand.size(); i++) {
		Card card = hand.at(i);
		cout << "[" << i+1 << "]";
		if(card.getFace().second == "none") {
			cout << card.getValue() << card.getSuit() << endl;
		} else {
			cout << card.getFace().second << card.getSuit() << endl;
		}
	}

	cout << scoreHand(player) << endl << endl;
}

string Poker::scoreHand(int player) {
	vector<Card> hand = players.at(player).getCards();
	map<int,int> handCount = map<int,int>();
	int sumHand = 0;
	bool sameSuit = false;
	string handType = "";
	int scoreCount = 0;

	//sum value of hand
	for(unsigned int i = 0; i < hand.size(); i++) {
		sumHand += hand.at(i).getValue();
	}

	//test for same suit
	string lastSuit = hand.at(0).getSuit();
	for(unsigned int k = 0; k < hand.size(); k++) {
		if(hand.at(k).getSuit() == lastSuit) {
			sameSuit = true;
			lastSuit = hand.at(k).getSuit();
		} else {
			sameSuit = false;
			break;
		}
	}

	//test for multiples
	vector<Card>::iterator iter;
	for(iter = hand.begin(); iter != hand.end(); ++iter) {
		if(handCount.count((*iter).getValue() == 1)){
			handCount[(*iter).getValue()] += 1;
		} else {
			handCount.insert(pair<int,int>((*iter).getValue(),1));
		}
	}

	//scoring -> efficiency down the drain
	map<int,int>::iterator iterm;
	if(sameSuit == true) {
		if(sumHand == 60) {
			handType = "Royal Flush!";
			scoreCount = 10;
		} else if(sumHand % 5 == 0 && ((sumHand / 5) >= 3 && (sumHand / 5) <= 11)) {
			handType = "Straight Flush!";
			scoreCount = 9;
		} else {
			handType = "Flush";
			scoreCount = 6;
		}
	} else {
		if(handCount.size() == 5) {
			handType = "Nothing to speak of...";
			scoreCount = 1;
		} else if(handCount.size() == 2) {
			for(iterm = handCount.begin(); iterm != handCount.end(); ++iterm) {
				if((*iterm).second == 4) {
					handType = "Four of a Kind!";
					scoreCount = 8;
					break;
				} else {
					handType = "Full House!";
					scoreCount = 7;
				}
			}
		} else if(handCount.size() == 4) {
			handType = "One Pair";
			scoreCount = 2;
		} else if(handCount.size() == 3) {
			int pairCount = 0;
			for(iterm = handCount.begin(); iterm != handCount.end(); ++iterm) {
				if((*iterm).second == 2) {
					pairCount++;
				}
			}
			if (pairCount == 2) {
				handType = "Two Pair";
				scoreCount = 3;
			} else {
				handType = "Three of a Kind";
				scoreCount = 4;
			}
		} else if(sumHand % 5 == 0 && ((sumHand / 5) >= 3 && (sumHand / 5) <= 11)) {
			handType = "Straight";
			scoreCount = 5;
		}
	}

	players.at(player).setScore(handType,scoreCount);
	return handType;
}

void Poker::showWinner() {
	Hand lastWinner = Hand("that guy");

	//find highest player score, there is the victor
	for(unsigned int i = 0; i < players.size(); i++) {
		if(players.at(i).getScore().second > lastWinner.getScore().second) {
			lastWinner = players.at(i);
		}
	}

	cout << lastWinner.getName() << " is the winner!!!!!!!" << endl << endl;
}

///////
//CARD
/////

int Card::getValue() {
	return value;
}

string Card::getSuit() {
	return suit;
}

pair<int,string> Card::getFace() {
	pair<int,string> facePair;
	facePair.first = value;
	facePair.second = face;
	return facePair;
}

///////
//HAND
/////

void Hand::discard(int disCard) {
	cards.erase(cards.begin()+disCard-1);
}

void Hand::drawCard(Deck deck) {
	cards.push_back(deck.dealCard());
}

string Hand::getName() {
	return name;
}

pair<string,int> Hand::getScore() {
	return score;
}

void Hand::setScore(string newScore, int count) {
	score.first = newScore;
	score.second = count;
}


///////
//DECK
/////

void Deck::buildDeck() {
	string faces[] = {"J", "Q", "K", "A"};
	string suits[] = {"D", "C", "H", "S"};

	//create each suit
	for(int suit = 0; suit < 4; suit++) {
		//create each value 1-10
		for(int value = 1; value <= 10; value++) {
			cards.push_back(Card(suits[suit], "none", value));
		}

		//create each face card
		for(int face = 11; face <= 14; face++) {
			cards.push_back(Card(suits[suit], faces[face-11], face));
		}
	}

	//shuffle 'em!
	shuffleDeck();
}

void Deck::shuffleDeck() {
	//Fisher-Yates
	for(int i = cards.size()-1; i >= 1; i--) {
		int rnd = i + rand() % (cards.size() - i);
		swap(cards.at(i), cards.at(rnd));
	}
}

Card Deck::dealCard() {
	Card newCard;

	//pick random card to return and erase from deck
	int rnd = rand() % cards.size();
	newCard = cards.at(rnd);
	cards.erase(cards.begin()+rnd);

	return newCard;
}

////////
//CARDS
//////

vector<Card> Cards::getCards() {
	return cards;
}

int Cards::getCount() {
	return cards.size();
}

//////
//END
////
