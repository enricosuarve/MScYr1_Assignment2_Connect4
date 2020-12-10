package com.simonpreece;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
Requirement 3
Submit a revised version of our code which compiles and runs to provide a working version of the game Connect4.

More specifically, provide an altered reworking of our code that a human marker can use to play a complete game of Connect4 against the computer.

You may wish (but are not obliged) to start by commenting out large parts of our code and altering our printBoard() method. This approach has the advantage of giving you a relatively manageable starting point in the debugging process.

You may also wish to tackle our placeCounter() method next.
 */


public class MyConnectFour {

    private BufferedReader input;
    private Board board;
    private ArrayList<Player> players = new ArrayList<>();
    private UI ui = new UI();

    public MyConnectFour() {
        UI ui = new UI();
        setupGame();
        playGame(board);
    }

    private void setupGame() {
        System.out.println("\033[0;97m .d8888b.   .d88888b.  888b    888 888b    888 8888888888 .d8888b. 88888888888 \033[0;31m d8888 \033[0;97m");
        System.out.println("d88P  Y88b d88P\" \"Y88b 8888b   888 8888b   888 888       d88P  Y88b    888   \033[0;31m  d8P888 \033[0;97m");
        System.out.println("888    888 888     888 88888b  888 88888b  888 888       888    888    888  \033[0;31m  d8P 888 \033[0;97m");
        System.out.println("888        888     888 888Y88b 888 888Y88b 888 8888888   888           888  \033[0;31m d8P  888 \033[0;97m");
        System.out.println("888        888     888 888 Y88b888 888 Y88b888 888       888           888 \033[0;31m d88   888 \033[0;97m");
        System.out.println("888    888 888     888 888  Y88888 888  Y88888 888       888    888    888 \033[0;31m 8888888888 \033[0;97m");
        System.out.println("Y88b  d88P Y88b. .d88P 888   Y8888 888   Y8888 888       Y88b  d88P    888       \033[0;31m 888 \033[0;97m");
        System.out.println(" \"Y8888P\"   \"Y88888P\"  888    Y888 888    Y888 8888888888 \"Y8888P\"     888       \033[0;31m 888 \033[0;57m");
        System.out.println("");
        System.out.println("Welcome to Connect 4");
        System.out.println("There are 2 players red and yellow");
        System.out.println("Player 1 is Red, Player 2 is Yellow");
        System.out.println("To play the game type in the number of the column you want to drop you counter in");
        System.out.println("A player wins by connecting 4 counters in a row - vertically, horizontally or diagonally");
        System.out.println("");
        board = new Board(6, 7);
        players.add(new Player());
        players.add(new Player());
        System.out.println("\nStarting game....\n");
    }

    private int getNextEmptyRow(int colNum) {
        int nextEmptyRow = -1;
        for (int y = 0; y < board.getNumRows(); y++) {
            if (board.getValueAtPosition(colNum - 1, y) == ' ') {
                nextEmptyRow = y;
            }
        }
        return nextEmptyRow;
    }

    private void playGame(Board board) {
        int move;
        board.printBoard();
        Player currentPlayer = null;
        boolean win = false;
        while (!win) {
            for (Player player : players) {
                currentPlayer = player;
                move = getMoveFromUser(String.format("Player %d - enter a column to drop a counter", currentPlayer.getPlayerNumber()));
                System.out.println("move was: " + move);
                placeCounter(currentPlayer, move);
                board.printBoard();
                if (checkHorizontalWin(currentPlayer) || checkVerticalWin(currentPlayer)) {
                    win = true;
                    break;
                }
            }
        }
        System.out.printf("Player %d '%s' has Won!!!\n", currentPlayer.getPlayerNumber(), currentPlayer.getName());
    }

    private void placeCounter(Player player, int move) {
        boolean placed = false;
        for (int i = board.getNumRows() - 1; i >= 0; i--) {
            if (!placed) {
                if (board.getValueAtPosition(move - 1, i) == ' ') {
                    board.setValueAtPosition(move - 1, i, player.getCounter());
                    placed = true;
                }
            }
        }
    }

    private boolean checkHorizontalWin(Player player) {
        //todo - see if can merge check horizontal and vertical
        int countersInARow = 0;
        char counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                if (board.getValueAtPosition(x, y) == counter) {
                    countersInARow++;
                    if (countersInARow >= 4) {
                        return true;
                    }
                }
                else {
                    countersInARow = 0;
                }
            }
            countersInARow = 0;
        }
        return false;
    }

    private boolean checkVerticalWin(Player player) {
        int countersInARow = 0;
        char counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();

        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                if (board.getValueAtPosition(x, y) == counter) {
                    countersInARow++;
                    if (countersInARow >= 4) {
                        return true;
                    }
                }
                else {
                    countersInARow = 0;
                }
            }
            countersInARow = 0;
        }
        return false;
    }

    private int getMoveFromUser(String requestToUser) {
        int move = ui.getUserInteger(requestToUser);
        while (true) {
            if (move > board.getNumCols() || move < 1) {
                System.out.printf("You entered '%d', which is outside the number of columns in the game - please try again\n", move);
            }
            else if (getNextEmptyRow(move) == -1) {
                System.out.printf("Column '%d' is already full - please try again\n", move);
            }
            else {
                return move;
            }
            move = ui.getUserInteger(requestToUser);
        }
    }

    private String getUserInput() {
        String toReturn = null;
        try {
            toReturn = input.readLine();
        } catch (Exception e) {

        }
        return toReturn;
    }

}




