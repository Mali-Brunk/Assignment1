// Mali Brunk
// CS 145
// Assignment 1
// Word Search Generator

// try to place at a random coordinate 
// first trying diagonal NE SW, diagonal NW SE, then vertical, then horizontal
// if can't replace, return false, failed attempt counter++
// if it reaches 100, inform user word cannot be placed

import java.util.*;

public class MBAssignment1 {
    public static final String ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int MAX_SEARCHES = 100;
    public final String[] directions = {"VERTICAL", "HORIZONTAL", "POSITIVE_LINEAR", "NEGATIVE_LINEAR"};
    public static void main(String[] args) {
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
                    grid = generate(input);
                    copyGrid = saveGridCopy(grid);
                    hasRun = true;
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
        System.out.println("P to Print out your word search");
        System.out.println("S to Show the solution to your word search");
        System.out.println("Q to Quit the program");
    }

    public static char[][] generate(Scanner input) {
        Random rand = new Random();
        int longestWord = 0;
        boolean foundPlace;

        System.out.println("How many words would you like to enter?");
        int length = input.nextInt();
        String[] wordArray = new String[length]; // try multidimensional array

        for (int i = 0; i < wordArray.length; i++) {
            System.out.println("Enter a word: ");
            wordArray[i] = input.next().toUpperCase();
            
            if(wordArray[i].length() > longestWord) {
                longestWord = wordArray[i].length();
            }
            
        }
        System.out.println(Arrays.toString(wordArray));

        int gridSize = longestWord + 5;
        char[][] grid = new char[gridSize][gridSize];

        for (String word : wordArray) {
            for (int failCount = 0; failCount < MAX_SEARCHES; failCount++) {
                int row = rand.nextInt(gridSize);
                int col = rand.nextInt(gridSize);
                foundPlace = placeWord(word, grid, gridSize, row, col);

                if (foundPlace) {
                    break;
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
        // while (failCount < MAX_SEARCHES) {

        // }
        // System.out.println("Maximum attempts reached. " + word.toLowerCase() + " could not be placed.");
        // try catch array out of bounds and continue

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

    public static void print(char[][] grid) {
        fill(grid);

        for (int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[1].length; j++) {
                System.out.print(grid[i][j] + "  ");
            }
            System.out.println();
        }
    }
    public static void showSolution(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    grid[i][j] = 'X';
                }
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[1].length; j++) {
                System.out.print(grid[i][j] + "  ");
            }
            System.out.println();
        }
    }
}
