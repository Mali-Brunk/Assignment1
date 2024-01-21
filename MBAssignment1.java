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
    public static void main(String[] args) {
        char[][] grid = new char[100][100];
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
                    showSolution(grid);
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
        int longestWord = 0;
        System.out.println("How many words would you like to enter?");
        int length = input.nextInt();
        String[] wordArray = new String[length]; // try multidimensional array

        for (int i = 0; i < wordArray.length; i++) {
            System.out.println("Enter a word: ");
            wordArray[i] = input.next();
            
            if(wordArray[i].length() > longestWord) {
                longestWord = wordArray[i].length();
            }
            
        }
        System.out.println(Arrays.toString(wordArray));

        char[][] grid = new char[longestWord + 5][longestWord + 5];

        for (String word : wordArray) {
            placeWord(word, grid);
        }
        return grid;
    }

    public static void placeWord(String word, char[][] grid) {
        char[] wordToChar = word.toCharArray();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[1].length; j++) {
                grid[i][j] = 'X';
            }
        }

        
    }
    public static void print(char[][] grid) {
            for (int i = 0; i < grid.length; i++) {
                for(int j = 0; j < grid[1].length; j++) {
                    System.out.printf(" %c ", grid[i][j]);
                }
                System.out.println();
            }
    }
    public static void showSolution(char[][] grid) {
        System.out.println("test");
    }
}
