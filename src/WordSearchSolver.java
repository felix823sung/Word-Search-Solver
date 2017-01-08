import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * Program to solve a word search given the word search and a set of words to
 * search for
 * @author Felix
 * @version Date: September 29, 2015
 */
public class WordSearchSolver
{
	public static void main(String[] args) throws IOException
	{
		// Asks the user for the name of the input file, which will contain the
		// list of words and also the word search
		String fileName = JOptionPane.showInputDialog(null,
				"What is the name of the text file?");

		// Initializes the scanner to read in the words
		Scanner fileIn = new Scanner(new File(fileName));

		// Initializes a variable to record the number of words and the side
		// length of the word search
		int wordCount = 0;
		int characterCount = 0;

		// Reads in the first line of the file
		String line = fileIn.nextLine();
		// Continues reading in the next line until the first line of the word
		// search is reached
		while (fileIn.hasNextLine() && !line.contains(" "))
		{
			wordCount++;
			line = fileIn.nextLine();
		}

		// Goes through the last line read in (the first line of the word
		// search), and counts the number of characters
		for (int pos = 0; pos < line.length(); pos++)
		{
			if (line.charAt(pos) == ' ')
			{
				characterCount++;
			}
		}
		// Creates a 2D character array with a border, with a dimension of
		// characterCount + 2 squared
		char[][] wordSearch = new char[characterCount + 2][characterCount + 2];

		// Assigns the first line of the word search array to the characters in
		// the current read line
		for (int pos = 1; pos < wordSearch[0].length - 1; pos++)
		{
			wordSearch[1][pos] = line.charAt((pos - 1) * 2);

		}
		// Reads in the rest of the word search, assigning the characters into
		// their relative positions within the array
		while (fileIn.hasNextLine())
		{
			for (int row = 2; row < wordSearch.length - 1; row++)
			{
				line = fileIn.nextLine();
				for (int column = 1; column < wordSearch[row].length - 1; column++)
				{

					wordSearch[row][column] = line.charAt((column - 1) * 2);

				}
			}
		}

		// Closes the file reader
		fileIn.close();
		// Opens a new file scanner, in order to go through the words again, as
		// well as creating a 1D array to store them, with the length given by
		// the wordCount variable
		Scanner scanWords = new Scanner(new File(fileName));
		String[] words = new String[wordCount];
		// Creates a related array to the words array, which will keep track of
		// the number of times it occurs in the word search
		int[] numberOfWordFound = new int[wordCount];

		// Reads in the words and assigns them into the array
		for (int index = 0; index < words.length; index++)
		{
			words[index] = scanWords.nextLine();
		}
		// Closes the file scanner
		scanWords.close();

		// Sorts the array in reverse alphabetical order, which is important if
		// words were not allowed to appear within another word (eg. finds
		// "business" before finding "bus")
		words = sortArray(words);

		// Initializes a variable to keep track of direction
		int match;
		// Runs through the array of the words
		for (int index = 0; index < words.length; index++)
		{
			// Runs through the word search array
			for (int row = 1; row < wordSearch.length - 1; row++)
			{
				for (int column = 1; column < wordSearch[row].length - 1; column++)
				{
					// Checks each letter using the verifyWord method
					match = verifyWord(wordSearch, words[index], row, column);
					// Should a word be found, the word is capitalizes in the
					// proper direction
					if (match != 9)
					{
						capitalize(wordSearch, match, row, column, words[index]);
						numberOfWordFound[index]++;
					}
				}
			}
		}
		// Creates a new PrintWriter in order to output the completed word
		// search to a file
		PrintWriter fileOut = new PrintWriter(new FileWriter(
				"CompletedWordSearch.txt"));

		// Creates a heading for the words, as well as the number of occurrences
		// of the word
		fileOut.println("WORD                   NUMBER OF OCCURRENCES");
		// Prints the words from the input file to the output file, along with
		// the number of occurrences
		for (int word = 0; word < words.length; word++)
		{
			fileOut.printf("%-20s %13d", words[word], numberOfWordFound[word]);
			fileOut.println();
		}
		// Prints out the characters within the word search array in the file
		for (int row = 1; row < wordSearch.length - 1; row++)
		{
			for (int column = 1; column < wordSearch[row].length - 1; column++)
			{
				fileOut.print(wordSearch[row][column] + " ");
			}
			// Should the row be the last row, no new line is printed
			if (row != wordSearch.length - 2)
			{

				fileOut.println();
			}
		}
		// Closes the PrintWriter
		fileOut.close();
	}

	/**
	 * Capitalizes the word within the word search, given the direction, as well
	 * as the coordinates of the first letter
	 * @param board the word search board
	 * @param direction the direction that the word is extending
	 * @param row the row of the word search that the first letter is in
	 * @param column the column of the word search that the first letter is in
	 * @param word the word to be capitalized
	 */
	public static void capitalize(char[][] board, int direction, int row,
			int column, String word)
	{
		// Changes the characters starting from the first letter, in the
		// direction specified by numbers 1 to 8
		// Direction of 1 indicates upwards, capitalizes the letters of the word
		// to that direction
		if (direction == 1)
		{
			for (int repeat = 0; repeat < word.length(); repeat++)
			{
				board[row - repeat][column] = Character.toUpperCase(board[row
						- repeat][column]);
			}
		}
		// Direction of 2 indicates the upper right diagonal, capitalizes the
		// letters of the word to that direction
		else if (direction == 2)
		{
			for (int repeat = 0; repeat < word.length(); repeat++)
			{
				board[row - repeat][column + repeat] = Character
						.toUpperCase(board[row - repeat][column + repeat]);
			}
		}
		// Direction of 3 indicates the right, capitalizes the letters of the
		// word to that direction
		else if (direction == 3)
		{
			for (int repeat = 0; repeat < word.length(); repeat++)
			{
				board[row][column + repeat] = Character
						.toUpperCase(board[row][column + repeat]);
			}
		}
		// Direction of 4 indicates the lower right diagonal, capitalizes the
		// letters of the word to that direction
		else if (direction == 4)
		{
			for (int repeat = 0; repeat < word.length(); repeat++)
			{
				board[row + repeat][column + repeat] = Character
						.toUpperCase(board[row + repeat][column + repeat]);
			}
		}
		// Direction of 5 indicates downwards, capitalizes the letters of the
		// word to that direction
		else if (direction == 5)
		{
			for (int repeat = 0; repeat < word.length(); repeat++)
			{
				board[row + repeat][column] = Character.toUpperCase(board[row
						+ repeat][column]);
			}
		}
		// Direction of 6 indicates lower left diagonal, capitalizes the letters
		// of the word to that direction
		else if (direction == 6)
		{
			for (int repeat = 0; repeat < word.length(); repeat++)
			{
				board[row + repeat][column - repeat] = Character
						.toUpperCase(board[row + repeat][column - repeat]);
			}
		}
		// Direction of 7 indicates the left, capitalizes the letters of the
		// word to that direction
		else if (direction == 7)
		{
			for (int repeat = 0; repeat < word.length(); repeat++)
			{
				board[row][column - repeat] = Character
						.toUpperCase(board[row][column - repeat]);
			}
		}
		// Direction of 8 indicates the upper left diagonal, capitalizes the
		// letters of the word to that direction
		else if (direction == 8)
		{
			for (int repeat = 0; repeat < word.length(); repeat++)
			{
				board[row - repeat][column - repeat] = Character
						.toUpperCase(board[row - repeat][column - repeat]);
			}
		}

	}

	/**
	 * Sorts the given String array in reverse alphabetical order
	 * @param array the given String array to be sorted
	 * @return the sorted array
	 */
	public static String[] sortArray(String[] array)
	{
		// Sets the sortedArray to the given array
		String[] sortedArray = new String[array.length];
		sortedArray = array;

		// Sets the largest String index to the String at next
		for (int next = 0; next < sortedArray.length - 1; next++)
		{
			int largestIndex = next;
			// Runs through the indexes of the given array starting from one
			// over from the "next" index
			for (int check = next + 1; check < sortedArray.length; check++)
			{
				// Compares the String at check to the largest String, and sets
				// the largest String index to the check if the String is
				// largest
				if (sortedArray[check].compareTo(sortedArray[largestIndex]) > 0)
				{
					largestIndex = check;
				}
			}
			// After going through the array, swaps the current largest String
			// with the one at next
			String temporary = sortedArray[next];
			sortedArray[next] = sortedArray[largestIndex];
			sortedArray[largestIndex] = temporary;
		}
		// Returns the sorted array
		return sortedArray;

	}

	/**
	 * A method that checks for a word in the word search given the coordinates
	 * of a letter
	 * @param board the word search board to be searched
	 * @param word the word that the method is searching for
	 * @param row the row of the word search that the letter is in
	 * @param column the column of the word search that the letter is in
	 * @return a number from 1 to 9, depending on the direction of the word. 1
	 *         indicates upwards, goes clockwise around. 9 indicates that the
	 *         word has not been found
	 */
	public static int verifyWord(char[][] board, String word, int row,
			int column)
	{
		// First checks if the letter in question is the first letter of the
		// given word
		// Checks between characters are made with both upper and lower case
		// letters in mind
		if (board[row][column] == Character.toLowerCase(word.charAt(0))
				|| board[row][column] == Character.toUpperCase(word.charAt(0)))
		{
			// Sets a variable called increment, which will be used to test in
			// the given direction
			int increment = 1;
			// Searches for the corresponding letters of the word in the upwards
			// direction, while ensuring that no index out of bounds error
			// occurs
			while (increment < word.length()
					&& ((board[row - increment][column] == Character
							.toLowerCase(word
									.charAt(increment)) || (board[row
							- increment][column] == Character
							.toUpperCase(word
									.charAt(increment))))))
			{
				increment++;
			}
			// Should the value of increment equal the length of the word,
			// returns 1 to indicate direction
			if (increment == word.length())
			{
				return 1;
			}
			// Resets the value of increment to 1
			increment = 1;
			// Searches for the corresponding letters of the word in the upper
			// right diagonal direction, while ensuring that no index out of
			// bounds error occurs
			while (increment < word.length()
					&& ((board[row - increment][column + increment] == Character
							.toLowerCase(word
									.charAt(increment)))
					|| (board[row - increment][column + increment] == Character
							.toUpperCase(word
									.charAt(increment)))))
			{
				increment++;
			}
			// Should the value of increment equal the length of the word,
			// returns 2 to indicate direction
			if (increment == word.length())
			{
				return 2;
			}
			// Resets the value of increment to 1
			increment = 1;
			// Searches for the corresponding letters of the word in the right
			// direction, while ensuring that no index out of bounds error
			// occurs
			while (increment < word.length()
					&& ((board[row][column + increment] == Character
							.toLowerCase(word
									.charAt(increment)))
					|| (board[row][column + increment] == Character
							.toUpperCase(word
									.charAt(increment)))))
			{
				increment++;
			}
			// Should the value of increment equal the length of the word,
			// returns 3 to indicate direction
			if (increment == word.length())
			{
				return 3;
			}
			// Resets the value of increment to 1
			increment = 1;
			// Searches for the corresponding letters of the word in the lower
			// right diagonal direction, while ensuring that no index out of
			// bounds error
			// occurs
			while (increment < word.length()
					&& ((board[row + increment][column + increment] == Character
							.toLowerCase(word
									.charAt(increment)))
					|| (board[row + increment][column + increment] == Character
							.toUpperCase(word.charAt(increment)))))
			{
				increment++;
			}
			// Should the value of increment equal the length of the word,
			// returns 4 to indicate direction
			if (increment == word.length())
			{
				return 4;
			}
			// Resets the value of increment to 1
			increment = 1;
			// Searches for the corresponding letters of the word in the
			// downwards direction, while ensuring that no index out of bounds
			// error occurs
			while (increment < word.length()
					&& ((board[row + increment][column] == Character
							.toLowerCase(word
									.charAt(increment)))
					|| (board[row + increment][column] == Character
							.toUpperCase(word
									.charAt(increment)))))
			{
				increment++;
			}
			// Should the value of increment equal the length of the word,
			// returns 5 to indicate direction
			if (increment == word.length())
			{
				return 5;
			}
			// Resets the value of increment to 1
			increment = 1;
			// Searches for the corresponding letters of the word in the lower
			// left diagonal direction, while ensuring that no index out of
			// bounds error occurs
			while (increment < word.length()
					&& ((board[row + increment][column - increment] == Character
							.toLowerCase(word
									.charAt(increment)))
					|| (board[row + increment][column - increment] == Character
							.toUpperCase(word
									.charAt(increment)))))
			{
				increment++;
			}
			// Should the value of increment equal the length of the word,
			// returns 6 to indicate direction
			if (increment == word.length())
			{
				return 6;
			}
			// Resets the value of increment to 1
			increment = 1;
			// Searches for the corresponding letters of the word in the left
			// direction, while ensuring that no index out of bounds error
			// occurs
			while (increment < word.length()
					&& ((board[row][column - increment] == Character
							.toLowerCase(word
									.charAt(increment)))
					|| (board[row][column - increment] == Character
							.toUpperCase(word
									.charAt(increment)))))
			{
				increment++;
			}
			// Should the value of increment equal the length of the word,
			// returns 7 to indicate direction
			if (increment == word.length())
			{
				return 7;
			}
			// Resets the value of increment to 1
			increment = 1;
			// Searches for the corresponding letters of the word in the upper
			// left diagonal direction, while ensuring that no index out of
			// bounds error occurs
			while (increment < word.length()
					&& ((board[row - increment][column - increment] == Character
							.toLowerCase(word
									.charAt(increment)))
					|| (board[row - increment][column - increment] == Character
							.toUpperCase(word
									.charAt(increment)))))
			{
				increment++;
			}
			// Should the value of increment equal the length of the word,
			// returns 8 to indicate direction
			if (increment == word.length())
			{
				return 8;
			}
		}
		// Returns a 9 should the letter being verified not be the first letter
		// of the given word
		return 9;
	}
}