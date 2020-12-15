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

    protected final ArrayList<Player> players = new ArrayList<>();
    protected final UI ui = new UI();
    private final AI ai = new Connect4AI(.925, this);
    protected int inARow;
    protected ANSIColourList colours = new ANSIColourList();

    public MyConnectFour() {
        boolean playAgain = true;
        setupGame();
        int firstPlayer = 1;
        while (playAgain) {
            playGame(board, firstPlayer);
            playAgain = ui.getUserYN("Play again?");
            firstPlayer = Math.max(++firstPlayer % (players.size()+1),1);
        }
        Player.resetPlayerNumbers();
    }

//    public MyConnectFour(int inARow) {
//        this.inARow = inARow;
//        //to do sort out repeat code in constructors
//        boolean playAgain = true;
//        setupGame();
//        while (playAgain) {
//            playGame(board);
//            playAgain = ui.getUserYN("Play again?");
//            //to do - swap player order??
//        }
//        Player.resetPlayerNumbers();
//    }

    @Override
    protected void setupGame() {
        // ASCII art created using tool at http://www.patorjk.com/software/taag/#p=display&f=Stop&t=o (Colossal font)
        System.out.printf("\n\n%s .d8888b.   .d88888b.  888b    888 888b    888 8888888888 .d8888b. 88888888888 %s d8888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        System.out.printf("%sd88P  Y88b d88P\" \"Y88b 8888b   888 8888b   888 888       d88P  Y88b    888   %s  d8P888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        System.out.printf("%s888    888 888     888 88888b  888 88888b  888 888       888    888    888  %s  d8P 888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        System.out.printf("%s888        888     888 888Y88b 888 888Y88b 888 8888888   888           888  %s d8P  888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        System.out.printf("%s888        888     888 888 Y88b888 888 Y88b888 888       888           888 %s d88   888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        System.out.printf("%s888    888 888     888 888  Y88888 888  Y88888 888       888    888    888 %s 8888888888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        System.out.printf("%sY88b  d88P Y88b. .d88P 888   Y8888 888   Y8888 888       Y88b  d88P    888       %s 888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        System.out.printf("%s \"Y8888P\"   \"Y88888P\"  888    Y888 888    Y888 8888888888 \"Y8888P\"     888       %s 888 %s\n", colours.BoldWhite, colours.Red, colours.White);
        System.out.println();
        System.out.println("Welcome to Connect 4");
        System.out.printf("There are 2 players %sred%s and %syellow%s\n", colours.Red, colours.White, colours.Yellow, colours.White);
        System.out.printf("%sPlayer 1 is Red%s,%s Player 2 is Yellow%s\n", colours.Red, colours.White, colours.Yellow, colours.White);
        System.out.println("To play the game type in the number of the column you want to drop your counter in");
        System.out.println("A player wins by connecting 4 counters in a row - vertically, horizontally or diagonally");
        System.out.println();
        inARow = 4;
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
                player.setCounter(colours.Red + player.counter + colours.White);
            }
            else {
                player.setCounter(colours.Yellow + player.counter + colours.White);
            }
        }
    }

    @Override
    protected void playGame(Board board, int firstPlayer) {
        int move;
        int numTurns = 0;
        int maxTurns = board.getNumCols() * board.getNumRows();
        String moveRequestToUser;

        System.out.println("\nStarting game....\n");
        board.initialiseBoard();
        board.printBoard();
        Player currentPlayer = null;
        boolean win = false;
        boolean firstTurn = true;
        while (!win && numTurns < maxTurns) {
            for (Player player : players) {
                if (!(firstTurn && player.getPlayerNumber() != firstPlayer)) {
                    //skip go to allow players to take turns at going first

                    currentPlayer = player;
                    if (currentPlayer.getClass().getSimpleName().equals("HumanPlayer")) {
                        moveRequestToUser = String.format("Player %d: %s - enter a column to drop a counter", currentPlayer.getPlayerNumber(), currentPlayer.getName());
                        move = currentPlayer.getMoveFromPlayer(moveRequestToUser, this);
                    }
                    else {
                        moveRequestToUser = String.format("Computer Player %d is moving", currentPlayer.getPlayerNumber());
                        move = currentPlayer.getMoveFromPlayer(moveRequestToUser, this);
                    }
                    placeCounter(currentPlayer, move);
                    board.printBoard();
                    if (checkForWin(currentPlayer)) {
                        win = true;
                        break;
                    }
                    if (++numTurns == maxTurns) {
                        break;
                    }
                    System.out.printf("%s dropped a counter in column %d\n", currentPlayer.getName(), move);
                    firstTurn = false;
                }
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
            spacesBefore = Math.max(0, (28 - playerScore.length()) / 2);
            spacesAfter = Math.max(0, 28 - playerScore.length() - spacesBefore);
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

    private boolean checkHorizontalWinOLD(Player player) {
        //todo - see if can merge check horizontal and vertical
        int countersInARow = 0;
        String counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                if (board.getValueAtPosition(x, y).equals(counter)) {
                    countersInARow++;
                    if (countersInARow >= inARow) {
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

    private boolean checkHorizontalWin(Player player) {
        return !(checkHorizontal(player, inARow, true).size() == 0);
    }

    protected ArrayList<Integer[][]> checkHorizontal(Player player, int inARow, boolean checkForThisPlayer) {
        //todo - see if can merge check horizontal and vertical
        int countersInARow = 0;
        ArrayList<Integer[][]> lineCoordinates = new ArrayList<>();
        String counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        String valueAtPosition;
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                valueAtPosition = board.getValueAtPosition(x, y);
                if ((checkForThisPlayer ?
                        valueAtPosition.equals(counter) :
                        !valueAtPosition.equals(counter) && !valueAtPosition.equals(" "))) {
                    countersInARow++;
                    if (countersInARow >= inARow) {
                        lineCoordinates.add(new Integer[][]{{x - (inARow - 1), y}, {x, y}});
                        //return lineCoordinates;
                    }
                }
                else {
                    countersInARow = 0;
                }
            }
            countersInARow = 0;
        }
        return lineCoordinates;
    }

    private boolean checkVerticalWin(Player player) {
        return !(checkVertical(player, inARow, true).size() == 0);
    }

    protected ArrayList<Integer[][]> checkVertical(Player player, int inARow, boolean checkForThisPlayer) {
        int countersInARow = 0;
        ArrayList<Integer[][]> lineCoordinates = new ArrayList<>();
        String counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        String valueAtPosition;

        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                valueAtPosition = board.getValueAtPosition(x, y);
                if ((checkForThisPlayer ?
                        valueAtPosition.equals(counter) :
                        !valueAtPosition.equals(counter) && !valueAtPosition.equals(" "))) {
                    countersInARow++;
                    if (countersInARow >= inARow) {
                        lineCoordinates.add(new Integer[][]{{x, y - (inARow - 1)}, {x, y}});
                    }
                }
                else {
                    countersInARow = 0;
                }
            }
            countersInARow = 0;
        }
        return lineCoordinates;
    }

    private boolean checkDiagonalWin_Positive(Player player) {
        return !(checkDiagonal_Positive(player, inARow, true).size() == 0);
    }

    protected ArrayList<Integer[][]> checkDiagonal_Positive(Player player, int inARow, boolean checkForThisPlayer) {
        int countersInARow = 0;
        ArrayList<Integer[][]> lineCoordinates = new ArrayList<>();
        String counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        String valueAtPosition;
        for (int x = 0; x < boardWidth - (inARow - 1); x++) {
            for (int y = 0; y < boardHeight - (inARow - 1); y++) {
                for (int i = 0; i < inARow; i++) {
                    valueAtPosition = board.getValueAtPosition(x + i, y + i);
                    if ((checkForThisPlayer ?
                            valueAtPosition.equals(counter) :
                            !valueAtPosition.equals(counter) && !valueAtPosition.equals(" "))) {
                        countersInARow++;
                        if (countersInARow >= inARow) {
                            lineCoordinates.add(new Integer[][]{{x, y}, {x + i, y + i}});
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
        return lineCoordinates;
    }

    private boolean checkDiagonalWin_Negative(Player player) {
        return !(checkDiagonal_Negative(player, inARow, true).size() == 0);
    }

    protected ArrayList<Integer[][]> checkDiagonal_Negative(Player player, int inARow, boolean checkForThisPlayer) {
        int countersInARow = 0;
        ArrayList<Integer[][]> lineCoordinates = new ArrayList<>();
        String counter = player.getCounter();
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        String valueAtPosition;
        for (int x = 0; x < boardWidth - (inARow - 1); x++) {
            for (int y = inARow - 1; y < boardHeight; y++) {
                for (int i = 0; i < inARow; i++) {
                    valueAtPosition = board.getValueAtPosition(x + i, y - i);
                    if ((checkForThisPlayer ?
                            valueAtPosition.equals(counter) :
                            !valueAtPosition.equals(counter) && !valueAtPosition.equals(" "))) {
                        countersInARow++;
                        if (countersInARow >= inARow) {
                            lineCoordinates.add(new Integer[][]{{x, y}, {x + i, y - i}});
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
        return lineCoordinates;
    }

    int getNextEmptyRow(int colNum) {
        int nextEmptyRow = -1;
        for (int y = 0; y < board.getNumRows(); y++) {
            if (board.getValueAtPosition(colNum - 1, y).equals(" ")) {
                nextEmptyRow = y;
            }
        }
        return nextEmptyRow;
    }

    @SuppressWarnings("unused")//called via .getMethod("getNumCols").invoke(game) from Connect4AI
    public int getNumCols() {
        return board.getNumCols();
    }

    public int getNumRows() {
        return board.getNumRows();
    }

    @Override
    public AI getAIClass() {
        return ai;
    }
}