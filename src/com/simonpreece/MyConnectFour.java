//todo highlight winning row in bold??
package com.simonpreece;

import java.awt.Toolkit;
import java.util.ArrayList;

/*
Requirement 3
Submit a revised version of our code which compiles and runs to provide a working version of the game Connect4.

More specifically, provide an altered reworking of our code that a human marker can use to play a complete game of Connect4 against the computer.

You may wish (but are not obliged) to start by commenting out large parts of our code and altering our printBoard() method. This approach has the advantage of giving you a relatively manageable starting point in the debugging process.

You may also wish to tackle our placeCounter() method next.
 */


public class MyConnectFour extends Game implements HasComputerPlayer {

    private final ArrayList<Player> players = new ArrayList<>();
    private final UI ui = new UI();
    private final AI ai = new Connect4AI();
    protected Board board;

    public MyConnectFour() {
        boolean playAgain = true;
        setupGame();
        while (playAgain) {
            playGame(board);
            playAgain = ui.getUserYN("Play again?");
            //todo - swap player order??
        }
        System.out.println("\nGoodbye...\n");
    }

    @Override
    protected void setupGame() {
        // ASCII art created using tool at http://www.patorjk.com/software/taag/#p=display&f=Stop&t=o (Colossal font)
        System.out.println("\033[0;97m .d8888b.   .d88888b.  888b    888 888b    888 8888888888 .d8888b. 88888888888 \033[0;31m d8888 \033[0;97m");
        System.out.println("d88P  Y88b d88P\" \"Y88b 8888b   888 8888b   888 888       d88P  Y88b    888   \033[0;31m  d8P888 \033[0;97m");
        System.out.println("888    888 888     888 88888b  888 88888b  888 888       888    888    888  \033[0;31m  d8P 888 \033[0;97m");
        System.out.println("888        888     888 888Y88b 888 888Y88b 888 8888888   888           888  \033[0;31m d8P  888 \033[0;97m");
        System.out.println("888        888     888 888 Y88b888 888 Y88b888 888       888           888 \033[0;31m d88   888 \033[0;97m");
        System.out.println("888    888 888     888 888  Y88888 888  Y88888 888       888    888    888 \033[0;31m 8888888888 \033[0;97m");
        System.out.println("Y88b  d88P Y88b. .d88P 888   Y8888 888   Y8888 888       Y88b  d88P    888       \033[0;31m 888 \033[0;97m");
        System.out.println(" \"Y8888P\"   \"Y88888P\"  888    Y888 888    Y888 8888888888 \"Y8888P\"     888       \033[0;31m 888 \033[0;57m");
        System.out.println();
        System.out.println("Welcome to Connect 4");
        System.out.println("There are 2 players red and yellow");
        System.out.println("Player 1 is Red, Player 2 is Yellow\n");
        System.out.println("To play the game type in the number of the column you want to drop you counter in");
        System.out.println("A player wins by connecting 4 counters in a row - vertically, horizontally or diagonally");
        System.out.println();
        board = new Board(6, 7);
        players.add(new HumanPlayer());
        if (ui.getUserYN("Do you wish to play against the computer? (Y/N)")) {
            players.add(new ComputerPlayer());
        }
        else {
            players.add(new HumanPlayer());
        }
        for (Player player : players) {
            if (player.getPlayerNumber() == 1) {
                player.setCounter("\033[0;31m" + player.counter + "\033[0;57m");
            }
            else {
                player.setCounter("\033[0;33m" + player.counter + "\033[0;57m");
            }
        }
    }

    @Override
    protected void playGame(Board board) {
        int move;
        int numTurns = 0;
        int maxTurns = board.getNumCols() * board.getNumRows();
        String moveRequestToUser;

        System.out.println("\nStarting game....\n");
        board.initialiseBoard();
        board.printBoard();
        Player currentPlayer = null;
        boolean win = false;
        while (!win && numTurns < maxTurns) {
            for (Player player : players) {
                currentPlayer = player;
                if (currentPlayer.getClass().getSimpleName().equals("HumanPlayer")) {
                    moveRequestToUser = String.format("Player %d: %s - enter a column to drop a counter", currentPlayer.getPlayerNumber(), currentPlayer.getName());
                }
                else {
                    moveRequestToUser = String.format("Computer Player %d is moving", currentPlayer.getPlayerNumber());
                }
                move = currentPlayer.getMoveFromPlayer(moveRequestToUser, this);
                placeCounter(currentPlayer, move);
                board.printBoard();
                if (checkForWin(currentPlayer)) {
                    win = true;
                    break;
                }
                if (++numTurns == maxTurns) {
                    break;
                }
                System.out.printf("%s dropped a counter in column %d", currentPlayer.getName(), move);
            }
        }
        System.out.println("\n##################################################");
        if (win) {
            currentPlayer.addWin();
            Toolkit.getDefaultToolkit().beep();
            System.out.printf("           Player %d '%s' has Won!!!\n\n", currentPlayer.getPlayerNumber(), currentPlayer.getName());
            displayScoreboard();
        }
        else {
            System.out.println("It's a draw - how disappointing...");
        }
        System.out.println("##################################################\n");
    }

    private void displayScoreboard() {
        String playerScore;
        String spaces = "                        ";
        int spacesBefore, spacesAfter;
        System.out.println("          ==============================");
        System.out.println("          |    Scores on the doors     |");
        System.out.println("          ==============================");
        for (Player player : players) {
            playerScore = player.getName() + "  " + player.getWins();
            spacesBefore = (28 - playerScore.length()) / 2; //todo stop this erroring if goes into a minus value
            spacesAfter = 28 - playerScore.length() - spacesBefore;
            System.out.printf("          |%s%s%s|\n",
                    spaces.substring(0, spacesBefore),
                    playerScore,
                    spaces.substring(0, spacesAfter));
        }
        System.out.println("          ==============================");
    }

    @Override
    public boolean isMoveValid(int move, boolean playerIsHuman) {
        if (move > board.getNumCols() || move < 1) {
            if (playerIsHuman) {
                Toolkit.getDefaultToolkit().beep();
                System.out.printf("You entered '%d', which is outside the number of columns in the game - please try again\n", move);
            }
            return false;
        }
        else if (getNextEmptyRow(move) == -1) {
            if (playerIsHuman) {
                System.out.printf("Column '%d' is already full - please try again\n", move);
            }
            return false;
        }
        return true;
    }

    public boolean checkForWin(Player player) {
        return checkHorizontalWin(player) || checkVerticalWin(player) ||
                checkDiagonalWin_Positive(player) || checkDiagonalWin_Negative(player);
    }

    private void placeCounter(Player player, int move) {
        boolean placed = false;
        for (int i = board.getNumRows() - 1; i >= 0; i--) {
            if (!placed) {
                if (board.getValueAtPosition(move - 1, i).equals(" ")) {
                    board.setValueAtPosition(move - 1, i, player.getCounter());
                    placed = true;
                }
            }
        }
    }

    private boolean checkHorizontalWin(Player player) {
        //todo - see if can merge check horizontal and vertical
        int countersInARow = 0;
        String counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                if (board.getValueAtPosition(x, y).equals(counter)) {
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
        String counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();

        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                if (board.getValueAtPosition(x, y).equals(counter)) {
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

    private boolean checkDiagonalWin_Positive(Player player) {
        int countersInARow = 0;
        String counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        for (int x = 0; x < boardWidth - 3; x++) {
            for (int y = 0; y < boardHeight - 3; y++) {
                for (int i = 0; i < 4; i++) {
                    if (board.getValueAtPosition(x + i, y + i).equals(counter)) {
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
            countersInARow = 0;
        }
        return false;
    }

    private boolean checkDiagonalWin_Negative(Player player) {
        int countersInARow = 0;
        String counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        for (int x = 0; x < boardWidth - 3; x++) {
            for (int y = 3; y < boardHeight; y++) {
                for (int i = 0; i < 4; i++) {
                    if (board.getValueAtPosition(x + i, y - i).equals(counter)) {
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
            countersInARow = 0;
        }
        return false;
    }

    private int getNextEmptyRow(int colNum) {
        int nextEmptyRow = -1;
        for (int y = 0; y < board.getNumRows(); y++) {
            if (board.getValueAtPosition(colNum - 1, y).equals(" ")) {
                nextEmptyRow = y;
            }
        }
        return nextEmptyRow;
    }

    @Override
    public AI getAIClass() {
        return ai;
    }
}




