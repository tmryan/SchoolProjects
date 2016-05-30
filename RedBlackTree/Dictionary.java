package sjsu.ryan.cs146.project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *	Dictionary class for testing runtimes of RedBlackTree methods.
 * 
 *  @author Thomas Ryan
 *  
 */

public class Dictionary {
	private RedBlackTree<String> dict;
	ArrayList<String> wordList;
	
	public Dictionary() {
		dict = new RedBlackTree<String>();
		wordList = new ArrayList<String>();
	}
	
	/*
	 * Loads a line break delimited list of words into the tree
	 */
	public String loadFile(String path) {
		Scanner fScanner = null;
		File eventsFile = new File(path);
		String word = "";
		
		if(eventsFile.exists()) {
			try {
				fScanner = new Scanner(eventsFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			while(fScanner.hasNextLine()) {
				word = fScanner.nextLine().trim();
				
				dict.insert(word);
				wordList.add(word);
			}
			
			fScanner.close();
			return "Dictionary loaded successfully.";
		} else {
			return "No file by that name was found.";
		}
	}
	
	/*
	 * Searches the tree for String word and returns true if found
	 */
	public boolean lookUp(String word) {
		boolean found = false;
		
		if(dict.search(dict.getRoot(), word).getData().equals(word)) {
			found = true;
		}
		
		return found;
	}
	
	/*
	 * Returns an ArrayList of stored words
	 */
	public ArrayList<String> getWordList() {
		return wordList;
	}
	
	/*
	 * Prints contents of tree to console inOrder
	 */
	public void printDictionary() {
		dict.inorderPrint(dict.getRoot());
	}
}
