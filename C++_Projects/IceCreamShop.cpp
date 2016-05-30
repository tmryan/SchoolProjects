/*
 * IceCreamShop.cpp
 *
 *  Created on: Feb 2, 2015
 *      Author: tryan
 */

#include <iostream>
#include <string>
#include <vector>

using namespace std;

//////////////////////
//other declarations
///////////////////

const int MAXCONES = 10;
const int MAXSCOOPS = 5;

//ice cream flavors
const string STRAWBERRY = "@";
const string CHOCOLATE = "&";
const string VANILLA = "-";

//prices
const double oneScoop = 1.00;
const double twoScoop = 2.00;
const double extraScoop = 0.75;

struct scoop {
	string flavor;
};

struct cone {
	vector<scoop> scoops;
	int numScoops;
	double price;
};

//////////////////////
//method declarations
///////////////////

void displayMenu();
int pickNumCones();
vector<scoop> pickFlavors(int, vector<scoop>&);
vector<cone> pickScoops(int);
void drawCones(vector<cone>&);
void displayBill(vector<cone>&);
double calculatePrice(int);

/////////
//main()
//////

int main() {
	vector<cone> cones;
	vector<cone> &conesRef = cones;
	int numCones = 0;

	//display the main menu to user
	displayMenu();

	//get number of cones from user
	numCones = pickNumCones();

	//get flavors and number of scoops from user
	cones = pickScoops(numCones);

	//draw cones
	drawCones(conesRef);

	//show user the bill
	displayBill(cones);

	//show appreciation for their patronage
	cout << endl;
	cout << "[]======================================[]" << endl;
	cout << "     Thank you very much! Come again!" << endl;
	cout << "[]======================================[]" << endl;

	return 0;
}

/////////////////////
//method definitions
//////////////////

void displayMenu() {
	cout << "/========================================\\" << endl;
	cout << " Welcome to I Scream for Ice Scream! Woo!" << endl;
	cout << "[]======================================[]" << endl;
	cout << " ][      Our daily specials are:       ][" << endl;
	cout << " ][         -----------------          ][" << endl;
	cout << " ][       1  scoop  for $1.00          ][" << endl;
	cout << " ][       2  scoops for $2.00          ][" << endl;
	cout << " ][       3+ scoops for $ .75 each     ][" << endl;
	cout << "==========================================" << endl;
}

int pickNumCones(){
	int numCones = 0;
	bool loop = true;

	cout << "Ah! A fine day for some ice cream, you say?" << endl;

	//get number of cones from user
	do {
		cout << "How many cones would you like (at max 10 cones)?" << endl;
		cin >> numCones;
		//validate input
		if (!cin || (numCones > 10 || numCones < 1)) {
			cin.clear();
			cin.ignore(30, '\n');
		} else {
			loop = false;
		}
	} while (loop);

	if (numCones == 10 ) {
			cout << "You must LOVE ice cream! That's cool!" << endl;
	} else if (numCones >=8 ) {
		cout << "Whew! You sure are hungry!" << endl;
	}

	return numCones;
}

vector<cone> pickScoops(int numCones) {
	vector<cone> cones;
	vector<cone>& conesRef = cones;

	//reserve space for cones so the vector doesn't need to adjust
	cones.reserve(numCones);

	//for each cone
	for (int i = 1; i <= numCones; i++) {
		vector<scoop> scoops;
		vector<scoop>& scoopsRef = scoops;
		cone newCone = {scoopsRef, 0, 0.0};
		bool loop = true;

		cones.push_back(newCone);

		//get number of scoops from user
		do {
			cout << endl << "How many scoops for cone #" << i << "?" << endl;
			cin >> cones.at(i-1).numScoops;
			//validate input
			if (!cin || (cones.at(i-1).numScoops > 5 || cones.at(i-1).numScoops < 1)) {
				cin.clear();
			cin.ignore(30, '\n');
			} else {
				loop = false;
			}
		} while (loop);

		//reserve space for scoops so the vector doesn't need to adjust
		cones.at(i-1).scoops.reserve(cones.at(i-1).numScoops);
		cones.at(i-1).price = calculatePrice(cones.at(i-1).numScoops);
		cones.at(i-1).scoops = pickFlavors(cones.at(i-1).numScoops, scoopsRef);
	}

	return conesRef;
}

vector<scoop> pickFlavors(int numScoops, vector<scoop> &scoops) {

	cout << endl;
	cout << "[]================================[]" << endl
	     << "||    [1] Strawberry!     (@)     ||" << endl
		 << "||    [2] Chocolate!      (&)     ||" << endl
		 << "||    [3] Vanilla...      (-)     ||" << endl
		 << "[]===(enter [number] to choose)===[]" << endl;

	//for each scoop
	for (int i = 1; i <= numScoops; i++) {
		scoop newScoop;
		int pickedFlavor = 0;
		bool loop = true;

		//get flavors from user
		do {
			cout << "What flavor would you like for scoop #" << i << "?" << endl;
			cin >> pickedFlavor;
			//validate input
			if (!cin || (pickedFlavor > 3 || pickedFlavor < 1)) {
				cin.clear();
				cin.ignore(30, '\n');
				cout << "I'm afraid that's not a real flavor..." << endl;
			} else {
				loop = false;
			}
		} while (loop);

		switch (pickedFlavor) {
			case 1:
				newScoop.flavor = STRAWBERRY;
				break;
			case 2:
				newScoop.flavor = CHOCOLATE;
				break;
			case 3:
				newScoop.flavor = VANILLA;
		}

		//put new scoop into cone.scoops[] vector
		scoops.push_back(newScoop);
	}

	return scoops;
}

void drawCones(vector<cone>& cones) {
	cout << endl << "Here's your ice cream!" << endl << endl;

	//for each cone
	for (unsigned int i = 0; i < cones.size(); i++) {
		//for each scoop
		for (unsigned int k = 0; k < cones.at(i).scoops.size(); k++) {
			//scoop art
			cout << "      ( " << cones.at(i).scoops.at(k).flavor << ")" << endl;
		}
		//cone art
		cout << "      \\\\//" << endl;
		cout << "       \\/" << endl << endl;
	}
}

void displayBill(vector<cone>& cones) {
	double totalPrice = 0.0;

	//for each cone
	for (unsigned int i = 0; i < cones.size(); i++) {
		//summing cone.price values
		totalPrice += cones.at(i).price;
	}

	//setting money format for double output
	cout.setf(ios::fixed);
	cout.setf(ios::showpoint);
	cout.precision(2);

	cout << "Your total comes to: $" << totalPrice << endl;

}

double calculatePrice(int numScoops) {
	//does what it says
	if (numScoops == 1) {
		return (double)numScoops;
	} else if (numScoops == 2) {
		return (double)numScoops * 2;
	} else {
		return (double)(((numScoops - 2) * .75) + 2);
	}
}

//////
//END
////
