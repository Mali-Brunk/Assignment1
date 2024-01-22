// Mali Brunk
// CS 145
// Assignment 1
// Word Search Generator

// try to place at a random coordinate 
// first trying diagonal NE SW, diagonal NW SE, then vertical, then horizontal
// if can't replace, return false, failed attempt counter++
// if it reaches 100, inform user word cannot be placed

// asssumes input file format is one word per line

import java.io.*;
import java.util.*;

public class MBAssignment1 {
    public static final String ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int MAX_SEARCHES = 100;
    public static void main(String[] args) throws FileNotFoundException, IOException {
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
                    showSolution(copyGrid);
                    break;
                case 'q':
                    System.out.println("Thanks for playing!");
                    isDone = !isDone;
            }
        } while (!isDone);
    }
    public static void printIntro() {
        System.out.println("Welcome to my word search generator!");
        System.out.println("Generate your very own word search puzzle!");
        System.out.println();
    }

    public static void menuOptions() {
        System.out.println("Please select an option:");
        System.out.println("C to Create a new word search");
        System.out.println("I to Import a word list");
        System.out.println("O to Output your word search to a file");
        System.out.println("P to Print out your word search");
        System.out.println("S to Show the solution to your word search");
        System.out.println("Q to Quit the program");
    }

    public static File takeInputFile(Scanner input) throws FileNotFoundException {
        System.out.println("Please enter a filename to read from: ");
        File inputFile = new File(input.next());

        while (!inputFile.exists()) {
            System.out.println("File not found. Please try again.");
            inputFile = new File(input.next());
        }
        return inputFile;
    }

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

    public static char[][] generate(String[] wordArray) {
        Random rand = new Random();
        int longestWord = 0;
        boolean foundPlace;

        for (int i = 0; i < wordArray.length; i++) {
            if(wordArray[i].length() > longestWord) {
                longestWord = wordArray[i].length();
            }
        }

        int gridSize = longestWord + 5;
        char[][] grid = new char[gridSize][gridSize];

        for (String word : wordArray) {
            for (int failCount = 0; failCount < MAX_SEARCHES; failCount++) {
                int row = rand.nextInt(gridSize);
                int col = rand.nextInt(gridSize);
                foundPlace = placeWord(word, grid, gridSize, row, col);

                if (foundPlace) {
                    break;
                } else if (failCount + 1 == MAX_SEARCHES) {
                    System.out.println("Maximum attempts reached. The word " + word.toLowerCase() + " could not be placed.");
                }
            }
        }
        return grid;
    }

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

    public static char[][] saveGridCopy(char[][] grid) {
        char[][] copyGrid = new char[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                copyGrid[i][j] = grid[i][j];
            }
        }
        return copyGrid;
    }

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
    public static void showSolution(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    grid[i][j] = 'X';
                }
            }
        }
        System.out.println();
        for (int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[1].length; j++) {
                System.out.print(grid[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}