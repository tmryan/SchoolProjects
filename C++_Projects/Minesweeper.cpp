/*
 * Minesweeper.cpp
 *
 *  Created on: Apr 13, 2015
 *      Author: Thomas Ryan
 */

#include <iostream>
#include <sstream>
#include <fstream>
#include <vector>
#include <string>

using namespace std;

////////////////////////////
//method/class declarations
/////////////////////////

class Minesweeper {
	private:
		vector<vector<string> > gameBoard;
		vector<vector<string> > solvedBoard;

		string countMines(int, int, int);

	public:
		Minesweeper(){};
		void populateGameBoard(string, int);
		void printGameBoard();

};

string intToString(int);

/////////
//main()
//////

int main() {
	Minesweeper game5 = Minesweeper();
	Minesweeper game7 = Minesweeper();

	string board5 = "*--*-----*---**--*---****";
	string board7 = "*-*--******----**-------*-*--*-*-**-----*----*---";

	game5.populateGameBoard(board5, 5);
	game5.printGameBoard();
	game7.populateGameBoard(board7, 7);
	game7.printGameBoard();

	return 0;
}

/////////////////////
//method definitions
//////////////////

string Minesweeper::countMines(int row, int col, int dimension) {
	int count = 0;
	int startRow = row;
	int startCol = col;
	int maxRow = 2;
	int maxCol = 2;

	//if cell is not an edge case... literally...
	if(row != 0) {
		if(row != dimension-1) {
			maxRow = 3;
		}
		startRow -= 1;
	}
	if(col != 0) {
		if(col != dimension-1) {
			maxCol = 3;
		}
		startCol -= 1;
	}

	//count mines in eight surrounding cells
	for(int cursorRow = startRow; cursorRow < startRow+maxRow; cursorRow++) {
		int cursorCol = 0;
		for(cursorCol = startCol; cursorCol < startCol+maxCol; cursorCol++) {
			//if mine, count mine
			if(gameBoard.at(cursorRow).at(cursorCol) == "*") {
				count++;
			}
		}
	}

	return intToString(count);
}

void Minesweeper::populateGameBoard(string minefield, int dimension) {
	gameBoard.resize(dimension, vector<string>(dimension, "0"));
	solvedBoard.resize(dimension, vector<string>(dimension, "0"));

	//populate unsolved board
	for(int row = 0; row < dimension; row++) {
		string line = minefield.substr(row*dimension, dimension);
		for(int col = 0; col < dimension; col++) {
			gameBoard.at(row).at(col) = line[col];
		}
	}

	//populate solved board
	for(int row = 0; row < dimension; row++) {
		string line = minefield.substr(row*dimension, dimension);
		for(int col = 0; col < dimension; col++) {
			if(line[col] == '-') {
				solvedBoard.at(row).at(col) = countMines(row, col, dimension);
			} else {
				solvedBoard.at(row).at(col) = line[col];
			}
		}
	}
}

void Minesweeper::printGameBoard() {
	unsigned int dimension = solvedBoard.size();
	string header = "";

	//building header based on dimension
	for(unsigned int j = 0; j < dimension; j++) {
		header = header + "____";
	}

	cout << endl << dimension << " X " << dimension << endl << header << endl;

	//print unsolved matrix into a table
	cout << "Unsolved:" << endl;

	for(unsigned int row = 0; row < dimension; row++) {
		cout << "|";

		for(unsigned int col = 0; col < dimension; col++) {
			string cellValue = gameBoard.at(row).at(col);
			cout << " " << cellValue << " |";
		}

		cout << endl;
	}

	cout << header << endl;

	//print solved matrix into a table
	cout << "Solved:" << endl;

	for(unsigned int row = 0; row < dimension; row++) {
		cout << "|";

		for(unsigned int col = 0; col < dimension; col++) {
			string cellValue = solvedBoard.at(row).at(col);
			cout << " " << cellValue << " |";
		}

		cout << endl;
	}

	cout << header << endl;
}

string intToString(int stringToBe) {
	stringstream strStream;
	strStream << stringToBe;
	return strStream.str();
}

//////
//END
////
