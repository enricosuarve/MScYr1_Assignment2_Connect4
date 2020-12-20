package com.snp39;

import java.awt.*;
import java.util.ArrayList;

/**
 * Main abstract Class for Connect4 games
 */
public abstract class ConnectX extends Game {

    protected final ArrayList<Player> players = new ArrayList<>();
    protected final UI ui = new UI();
    protected final AI ai = new Connect4AI(.925, this);
    protected int inARow;
    protected ANSIColourList colours = new ANSIColourList();


    public ConnectX() {
    }

    @Override
    protected abstract void setupGame();

    /**
     * Play the game Connect(x) using the board passed. All game decisioning starts here and
     * if one player wins details and scoreboard are shown.
     *
     * @param board       - Board object containing all counters and their positions.
     * @param firstPlayer - Number of Player who starts this game.
     */
    @Override
    protected void playGame(Board board, int firstPlayer) {
        int move;
        int numTurns = 0;
        int maxTurns = board.getNumCols() * board.getNumRows();
        String moveRequestToUser;
        ArrayList<Integer[][]> winningMove = new ArrayList<>();

        Main.view.Display("\nStarting game....\n");
        board.initialiseBoard();
        Main.view.Display(board.toString());
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
                    Main.view.Display(board.toString());
                    winningMove = checkForWinningLine(currentPlayer);
                    if (winningMove.size() > 0) {
                        win = true;
                        break;
                    }
                    if (++numTurns == maxTurns) {
                        break;
                    }
                    Main.view.Display(String.format("%s dropped a counter in column %d\n", currentPlayer.getName(), move));
                    firstTurn = false;
                }
            }
        }
        Main.view.Display("\n##################################################");
        if (win) {
            currentPlayer.addWin();
            Toolkit.getDefaultToolkit().beep();
            Main.view.Display(String.format("           Player %d '%s' has Won!!!\n", currentPlayer.getPlayerNumber(), currentPlayer.getName()));
            Main.view.Display(String.format("        (Winning move = %d,%d to %d,%d)\n\n", winningMove.get(0)[0][0] + 1, 6 - winningMove.get(0)[0][1], winningMove.get(0)[1][0] + 1, 6 - winningMove.get(0)[1][1]));
            displayScoreboard(players);
        }
        else {
            Main.view.Display("It's a draw - how disappointing...");
        }
        Main.view.Display("##################################################\n");
    }


    @Override
    public boolean isMoveValid(int move, boolean playerIsHuman) {
        if (move > board.getNumCols() || move < 1) {
            if (playerIsHuman) {
                Toolkit.getDefaultToolkit().beep();
                Main.view.Display(String.format("You entered '%d', which is outside the number of columns in the game - please try again\n", move));
            }
            return false;
        }
        else if (getNextEmptyRow(move) == -1) {
            if (playerIsHuman) {
                Main.view.Display(String.format("Column '%d' is already full - please try again\n", move));
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean checkForWin(Player player) {
        return checkForWinningLine(player).size() > 0;
    }

    public ArrayList<Integer[][]> checkForWinningLine(Player player) {
        ArrayList<Integer[][]> winningCounters = checkHorizontal(player, inARow, true);
        if (winningCounters.size() > 0) {
            return winningCounters;
        }
        else {
            winningCounters = checkVertical(player, inARow, true);
            if (winningCounters.size() > 0) {
                return winningCounters;
            }
            else {
                winningCounters = checkDiagonal_Positive(player, inARow, true);
                if (winningCounters.size() > 0) {
                    return winningCounters;
                }
                else {
                    winningCounters = checkDiagonal_Negative(player, inARow, true);
                    if (winningCounters.size() > 0) {
                        return winningCounters;
                    }
                }
            }
        }
        return winningCounters;
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

    /**
     * Check the Board for counters in a row horizontally for either the player specified or other players
     *
     * @param player             - player initiating the checks.
     * @param inARow             - number of counters in a row to look for.
     * @param checkForThisPlayer - if true will look for the players counter in a row,
     *                           if false will look for all other opposing players lines.
     * @return - x/y, x/y coordinates of any lines matching the description.
     */
    protected ArrayList<Integer[][]> checkHorizontal(Player player, int inARow, boolean checkForThisPlayer) {
        int countersInARow = 0;
        ArrayList<Integer[][]> lineCoordinates = new ArrayList<>();
        String counter;
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        String valueAtPosition;
        for (Player checkThisPlayer : players) {
            if ((checkForThisPlayer && checkThisPlayer.getName().equals(player.getName())) || (!checkForThisPlayer && !checkThisPlayer.getName().equals(player.getName()))) {
                counter = checkThisPlayer.getCounter();
                for (int y = 0; y < boardHeight; y++) {
                    for (int x = 0; x < boardWidth; x++) {
                        valueAtPosition = board.getValueAtPosition(x, y);
                        if (valueAtPosition.equals(counter)) {
                            countersInARow++;
                            if (countersInARow >= inARow) {
                                lineCoordinates.add(new Integer[][]{{x - (inARow - 1), y}, {x, y}});
                            }
                        }
                        else {
                            countersInARow = 0;
                        }
                    }
                    countersInARow = 0;
                }
            }
        }
        return lineCoordinates;
    }

    /**
     * Check the Board for counters in a line vertically for either the player specified or other players
     *
     * @param player             - player initiating the checks.
     * @param inARow             - number of counters in a row to look for.
     * @param checkForThisPlayer - if true will look for the players counter in a row,
     *                           if false will look for all other opposing players lines.
     * @return - x/y, x/y coordinates of any lines matching the description.
     */
    protected ArrayList<Integer[][]> checkVertical(Player player, int inARow, boolean checkForThisPlayer) {
        int countersInARow = 0;
        ArrayList<Integer[][]> lineCoordinates = new ArrayList<>();
        String counter;
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        String valueAtPosition;
        for (Player checkThisPlayer : players) {
            if ((checkForThisPlayer && checkThisPlayer.getName().equals(player.getName())) || (!checkForThisPlayer && !checkThisPlayer.getName().equals(player.getName()))) {
                counter = checkThisPlayer.getCounter();

                for (int x = 0; x < boardWidth; x++) {
                    for (int y = 0; y < boardHeight; y++) {
                        valueAtPosition = board.getValueAtPosition(x, y);
                        if (valueAtPosition.equals(counter)) {
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
            }
        }
        return lineCoordinates;
    }

    /**
     * Check the Board for counters in a Positive diagonal row (\) for either the player specified or other players
     *
     * @param player             - player initiating the checks.
     * @param inARow             - umber of counters in a row to look for.
     * @param checkForThisPlayer - if true will look for the players counter in a row,
     *                           if false will look for all other opposing players lines.
     * @return - x/y, x/y coordinates of any lines matching the description.
     */
    protected ArrayList<Integer[][]> checkDiagonal_Positive(Player player, int inARow, boolean checkForThisPlayer) {
        int countersInARow = 0;
        ArrayList<Integer[][]> lineCoordinates = new ArrayList<>();
        String counter;
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        String valueAtPosition;
        for (Player checkThisPlayer : players) {
            if ((checkForThisPlayer && checkThisPlayer.getName().equals(player.getName())) || (!checkForThisPlayer && !checkThisPlayer.getName().equals(player.getName()))) {
                counter = checkThisPlayer.getCounter();

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
            }
        }
        return lineCoordinates;
    }

    /**
     * Check the Board for counters in a Negative diagonal row (/) for either the player specified or other players
     *
     * @param player             - player initiating the checks.
     * @param inARow             - umber of counters in a row to look for.
     * @param checkForThisPlayer - if true will look for the players counter in a row,
     *                           if false will look for all other opposing players lines.
     * @return - x/y, x/y coordinates of any lines matching the description.
     */
    protected ArrayList<Integer[][]> checkDiagonal_Negative(Player player, int inARow, boolean checkForThisPlayer) {
        int countersInARow = 0;
        ArrayList<Integer[][]> lineCoordinates = new ArrayList<>();
        String counter;
        int boardWidth = board.getNumCols();
        int boardHeight = board.getNumRows();
        String valueAtPosition;

        for (Player checkThisPlayer : players) {
            if ((checkForThisPlayer && checkThisPlayer.getName().equals(player.getName())) || (!checkForThisPlayer && !checkThisPlayer.getName().equals(player.getName()))) {
                counter = checkThisPlayer.getCounter();

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
            }
        }
        return lineCoordinates;
    }

    /**
     * Get the next empty slot in a column.
     *
     * @param colNum - zero indexed column to check.
     * @return - zero indexed row indicating next empty slot (-1) if column is full.
     */
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

    /**
     * Get the correct AI subclass to use with this game
     * (different AIs may be required for different varients as the rules change).
     *
     * @return AI subclass for a computer player to use
     */
    public AI getAIClass() {
        return ai;
    }
}