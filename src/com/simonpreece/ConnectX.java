//todo highlight winning row in bold??
package com.simonpreece;

import java.awt.*;
import java.util.ArrayList;

public abstract class ConnectX extends Game {

    protected final ArrayList<Player> players = new ArrayList<>();
    protected final UI ui = new UI();
    protected final AI ai = new Connect4AI(.925, this);
    protected int inARow;
    protected ANSIColourList colours = new ANSIColourList();

    public ConnectX() {
    }

    public ConnectX(int inARow) {
        //inARow constructors defined in sub classes
    }

    @Override
    protected abstract void setupGame();

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
            displayScoreboard(players);
        }
        else {
            System.out.println("It's a draw - how disappointing...");
        }
        System.out.println("##################################################\n");
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

    @Override
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

    @SuppressWarnings("unused")//called via .getMethod("getNumRows").invoke(game) from Connect4AI
    public int getNumRows() {
        return board.getNumRows();
    }

    public AI getAIClass(){
        return ai;
    }
}