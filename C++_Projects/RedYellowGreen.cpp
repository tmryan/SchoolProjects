
/*
 * RedYellowGreen.cpp
 *
 *  Created on: Feb 24, 2015
 *      Author: Tom Ryan
 */

#include <iostream>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <sstream>

using namespace std;

////////////
//constants
/////////

const int GAME_NUMBER_SIZE = 3;

/////////////////////////////
//method/class declarations
//////////////////////////

class Game {
	private:
		string playerGuess;
		string randomNumber;

		string generateRandNum();
		vector<int> compareNumbers();

	public:
		Game(){playerGuess = ""; randomNumber = generateRandNum();}

		vector<int> playGame(string);
		//for debugging
		//string debugRandomNumber();
};

string intToString(int);

/////////
//main()
//////

int main() {
	//seed RNG with time
	srand(time(NULL));

	int guessNum = 0;
	int guessCount = 0;
	bool run = true;
	Game rygGame = Game();

	//main game screen
	cout << " /^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\" << endl
		 << " R  E  D      Y  E  L  L  O  W      G  R  E  E  N" << endl
		 << "/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\/^\\" << endl;

	while (run) {
		guessCount++;

		//ask player for a number between 100 and 999
		cout << "\nPick a number between 100 and 999:" << endl;
		cin >> guessNum;
		//cout << rygGame.debugRandomNumber() << endl;

		//validate input
		if (!cin || guessNum < 100 || guessNum > 999) {
			cin.clear();
			cin.ignore(30, '\n');
		} else {
			//play game and print results
			vector<int> results = rygGame.playGame(intToString(guessNum));

			//if not 3 greens
			if (results.at(2) != 3) {
				cout << "You have: " << results.at(0) << " red" << endl
					       << "\t  " << results.at(2) << " green" << endl
						   << "\t  " << results.at(1) << " yellow" << endl;
			} else {
				//make the player feel good about themselves so they will want to play again
				//and help them feel victorious so they will carry that feeling over into
				//other areas of their life where they will inevitably have their dreams
				//shattered by reality but at least here they can bask in their ethereal glory
				cout << endl << ":D  CONGRATULATIONS! YOU'RE A WINNER!  C:" << endl;
				cout << ":D  IT TOOK YOU ONLY " << guessCount << " GUESSES! WOW!  C:" << endl;
				break;
			}
		}
	}

	return 0;
}

//////////////////////
//method definitions
///////////////////

vector<int> Game::playGame(string guess) {
	playerGuess = guess;

	//compare guess and randomized number
	return compareNumbers();
}

string Game::generateRandNum() {
	//generate a random number between 100 and 999
	return intToString(rand() % 900 + 100);
}

vector<int> Game::compareNumbers() {
	vector<int> results;
	//to prevent out duplicate counts
	bool compareFlags[GAME_NUMBER_SIZE*2] = {false,false,false,false,false,false};

	//initializing results vector
	results.resize(3, 0);

	for (unsigned int i = 0; i < playerGuess.size(); i++) {
		//compare same index for greens
		if (randomNumber[i] == playerGuess[i] && !compareFlags[i] && !compareFlags[i+3]) {
			results.at(2)++;
			compareFlags[i] = true;
			compareFlags[i+3] = true;
		} else {
			//compare different indices for yellows
			for (unsigned int j = 0; j < playerGuess.size(); j++) {
				if (randomNumber[i] == playerGuess[j] && !compareFlags[i] && !compareFlags[j+3]) {
					results.at(1)++;
					compareFlags[i] = true;
					compareFlags[j+3] = true;
					break;
				}
			}
		}
	}
	//reds = 3 - yellow + green
	results.at(0) = 3 - (results.at(1) + results.at(2));

	return results;
}

string intToString(int stringToBe) {
	stringstream strStream;
	strStream << stringToBe;
	return strStream.str();
}

//////
//END
////
