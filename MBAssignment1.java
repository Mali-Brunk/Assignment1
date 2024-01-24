// Mali Brunk
// CS 145
// Assignment 1
// Word Search Generator
// 8 directions and file I/O extra credit
// asssumes input file format is one word per line
// included one sample text file

import java.io.*;
import java.util.*;

public class MBAssignment1 {
    // initialize String with all letters in alphabet and max searches constant
    public static final String ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int MAX_SEARCHES = 100;

    // The menu! Restrict some menu options until program has run once
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // initialize two grid 2D arrays
        char[][] grid = new char[100][100];
        char[][] copyGrid = new char[100][100];
        boolean isDone = false;
        boolean hasRun = false;
        Scanner input = new Scanner(System.in);
        printIntro();
        
        do {
            menuOptions();
            char command = input.next().toLowerCase().charAt(0);

            switch (command) {
                case 'c':
                    String[] userWords = getUserWords(input);
                    grid = generate(userWords);
                    copyGrid = saveGridCopy(grid); 
                    // creates a copy of the grid before fill to use with solution
                    fill(grid);
                    hasRun = true;
                    break;
                case 'i':
                    File inputFile = takeInputFile(input);
                    String[] fileWords = getWordsFromFile(inputFile);
                    grid = generate(fileWords);
                    copyGrid = saveGridCopy(grid);
                    fill(grid);
                    hasRun = true;
                    break;
                case 'o':
                    if (!hasRun) {
                        System.out.println("Please generate a word search first");
                        break;
                    }
                    printToFile(grid, copyGrid);
                    break;
                case 'p':
                    if (!hasRun) {
                        System.out.println("Please generate a word search first");
                        break;
                    }
                    print(grid);
                    break;
                case 's':
                    if (!hasRun) {
                        System.out.println("Please generate a word search first");
                        break;
                    }
                    showSolution(copyGrid); // fills grid copy empty spaces with X and prints
                    break;
                case 'q':
                    System.out.println("Thanks for playing!");
                    isDone = !isDone;
                default: 
                    System.out.println("Enter a valid command");
            }
        } while (!isDone);
    }

    // prints intro to program
    public static void printIntro() {
        System.out.println("Welcome to my word search generator!");
        System.out.println("Generate your very own word search puzzle!");
        System.out.println();
    }

    // displays menu options
    public static void menuOptions() {
        System.out.println("Please select an option:");
        System.out.println("C to Create a new word search");
        System.out.println("I to Import a word list");
        System.out.println("O to Output your word search to a file");
        System.out.println("P to Print out your word search");
        System.out.println("S to Show the solution to your word search");
        System.out.println("Q to Quit the program");
    }

    // takes user input file, throws FileNotFoundException if not a valid file
    public static File takeInputFile(Scanner input) throws FileNotFoundException {
        System.out.println("Please enter a filename to read from: ");
        File inputFile = new File(input.next());

        while (!inputFile.exists()) {
            System.out.println("File not found. Please try again.");
            inputFile = new File(input.next());
        }
        return inputFile;
    }

    // adds words from file with an ArrayList, then converts to and returns an array
    public static String[] getWordsFromFile(File inputFile) throws IOException {
        List<String> stringList = new ArrayList<String>();
        Scanner inputFileScan = new Scanner(inputFile);
        
        while (inputFileScan.hasNextLine()) {
            stringList.add(inputFileScan.nextLine().toUpperCase());
        }

        String[] fileWords = new String[stringList.size()];

        for (int i = 0; i < stringList.size(); i++) {
            fileWords[i] = stringList.get(i);
        }

        return fileWords;
    }

    // prompts user for how many words they want to enter
    // creates a word array of that length, stores words, returns
    public static String[] getUserWords(Scanner input) {
        System.out.println("How many words would you like to enter?");
        int length = input.nextInt();
        String[] wordArray = new String[length]; 

        for (int i = 0; i < wordArray.length; i++) {
            System.out.println("Enter a word: ");
            wordArray[i] = input.next().toUpperCase();   
        }
        return wordArray;
    }

    // takes an array of words, creates a grid, attempts to place words, returns a grid
    public static char[][] generate(String[] wordArray) {
        Random rand = new Random();
        int longestWord = 0;
        boolean foundPlace;

        for (int i = 0; i < wordArray.length; i++) {
            if(wordArray[i].length() > longestWord) {
                longestWord = wordArray[i].length();
            }
        }
        // determines length of the longest word and makes grid based on that + 5
        int gridSize = longestWord + 5;
        char[][] grid = new char[gridSize][gridSize];
        // for each word, tries to place a word a maximum of 100 times
        for (String word : wordArray) {
            for (int failCount = 0; failCount < MAX_SEARCHES; failCount++) {
                int row = rand.nextInt(gridSize);
                int col = rand.nextInt(gridSize);
                // generates a random coordinate to try to place the word
                foundPlace = placeWord(word, grid, gridSize, row, col);
                
                if (foundPlace) {
                    break;
                } else if (failCount + 1 == MAX_SEARCHES) {
                    System.out.println("Maximum attempts reached. The word " + 
                    word.toLowerCase() + " could not be placed.");
                }
            }
        }
        return grid;
    }

    // checks if all coordinates where word would be placed are empty, returns false if not
    // otherwise adds word to grid and returns true
    public static boolean placeWord(String word, char[][] grid, int gridSize, int row, int col) {
        int direction = validDirection(word, grid, row, col);
        
        switch (direction) {
            case 1: // vertical
                for (int i = 0; i < word.length(); i++) {
                    if (grid[row + i][col] != 0) {
                        return false;
                    }
                }
                for (int i = 0; i < word.length(); i++) {
                    grid[row + i][col] = word.charAt(i);
                }
                return true;
            case 2: // backwards vertical
                for (int i = 0; i < word.length(); i++) {
                    if (grid[row - i][col] != 0) {
                        return false;
                    }
                }

                for (int i = 0; i < word.length(); i++) {
                    grid[row - i][col] = word.charAt(i);
                }
                return true;
            case 3: // horizontal
                for (int i = 0; i < word.length(); i++) {
                    if (grid[row][col + i] != 0) {
                        return false;
                    }
                }
                for (int i = 0; i < word.length(); i++) {
                    grid[row][col + i] = word.charAt(i);
                }
                return true;
            case 4: // backwards horizontal
                for (int i = 0; i < word.length(); i++) {
                    if (grid[row][col - i] != 0) {
                        return false;
                    }
                }
                for (int i = 0; i < word.length(); i++) {
                    grid[row][col - i] = word.charAt(i);
                }
                return true;
            case 5: // diagonal /
                for (int i = 0; i < word.length(); i++) {
                    if (grid[row + i][col + i] != 0) {
                        return false;
                    }
                }
                for (int i = 0; i < word.length(); i++) {
                    grid[row + i][col + i] = word.charAt(i);
                }
                return true;
            case 6: // backwards diagonal /
                for (int i = 0; i < word.length(); i++) {
                    if (grid[row + i][col - i] != 0) {
                        return false;
                    }
                }
                for (int i = 0; i < word.length(); i++) {
                    grid[row + i][col - i] = word.charAt(i);
                }
                return true;
            case 7: // diagonal \
                for (int i = 0; i < word.length(); i++) {
                    if (grid[row - i][col + i] != 0) {
                        return false;
                    }
                }
                for (int i = 0; i < word.length(); i++) {
                    grid[row - i][col + i] = word.charAt(i);
                }
                return true;
            case 8: // backwards diagonal \
                for (int i = 0; i < word.length(); i++) {
                    if (grid[row - i][col - i] != 0) {
                        return false;
                    }
                }
                for ( int i = 0; i < word.length(); i++) {
                    grid[row - i][col - i] = word.charAt(i);
                }
                return true;
        }
        return false;
    }

    // checks if placing the whole word at a random coordinate in a random direction will result in going out of bounds
    public static int validDirection(String word, char[][] grid, int row, int col) {
        Random rand = new Random();
        int randDirection = rand.nextInt(8);
        
        switch (randDirection) {
            case 1: // vertical
                if (row + word.length() < grid.length) {
                    return 1;
                }
            case 2: // backwards vertical
                if (row - word.length() > 0) {
                    return 2;
                }
            case 3: // horizontal
                if (col + word.length() < grid.length) {
                    return 3;
                }
            case 4: // backwards horizontal
                if (col - word.length() > 0) {
                    return 4;
                }
            case 5: // diagonal /
                if ((row + word.length() < grid.length) && (col + word.length() < grid.length)) {
                    return 5;
                }
            case 6: // backwards diagonal /
                if ((row + word.length() < grid.length) && (col - word.length() > 0)) {
                    return 6;
                }
            case 7: // diagonal \
                if ((row - word.length() > 0) && (col + word.length() < grid.length)) {
                    return 7;
                }
            case 8: // backwards diagonal \
                if ((row - word.length() > 0) && (col - word.length() > 0)) {
                    return 8;
                }
            default:
                return 0;
            }
    }
    // saves a copy of the grid with placed words and no fill, returns copy
    public static char[][] saveGridCopy(char[][] grid) {
        char[][] copyGrid = new char[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                copyGrid[i][j] = grid[i][j];
            }
        }
        return copyGrid;
    }
    // takes a random index of ALL_CHARS constant, adds letters to empty spaces in grid
    public static void fill(char[][] grid) {
        Random rand = new Random();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    grid[i][j] = ALL_CHARS.charAt(rand.nextInt(ALL_CHARS.length()));
                }
            }
        }
    }

    // prints filled word search to console
    public static void print(char[][] grid) {
        System.out.println();
        for (int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[1].length; j++) {
                System.out.print(grid[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    // fills empty spaces with letter X
    public static void showSolution(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    grid[i][j] = 'X';
                }
            }
        }
        print(grid);
    }

    // adaptation of print and showSolution methods for file output, throws FileNotFoundException
    public static void printToFile(char[][] grid, char[][] copyGrid) throws FileNotFoundException {
        System.out.println("This word search will be printed to WordSearch.txt");
        File outputFile = new File("WordSearch.txt");
        PrintStream outputFileWrite = new PrintStream(outputFile);

        outputFileWrite.println("Your word search:");
        for (int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[1].length; j++) {
                outputFileWrite.print(grid[i][j] + "  ");
            }
            outputFileWrite.println();
        }
        outputFileWrite.println();

        outputFileWrite.println("Solution:");
        for (int i = 0; i < copyGrid.length; i++) {
            for (int j = 0; j < copyGrid.length; j++) {
                if (copyGrid[i][j] == 0) {
                    copyGrid[i][j] = 'X';
                }
            }
        }
        System.out.println();
        for (int i = 0; i < copyGrid.length; i++) {
            for(int j = 0; j < copyGrid[1].length; j++) {
                outputFileWrite.print(copyGrid[i][j] + "  ");
            }
            outputFileWrite.println();
        }
    }
}