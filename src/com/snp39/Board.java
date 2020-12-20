package com.snp39;

import java.util.Arrays;

/**
 * Basic board class for a grid board game - has the ability to store counters within a 2 dimensional string array
 * and return either values at a position or a string representation of the entire board.
 */
public class Board {
    private final int numRows;
    private final int numCols;
    private String[][] boardData;

    /**
     * Create a default (6 x 7) board if no size given; populates all 'squares' with a space character initially.
     */
    public Board() {

        numCols = 7;
        numRows = 6;
        initialiseBoard();
    }

    /**
     * Create a custom board with the dimensions given; populates all 'squares' with a space character initially.
     *
     * @param numRows - number of rows wide to make the board.
     * @param numCols - number of columns high to make the board.
     */
    public Board(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        initialiseBoard();
    }

    protected void initialiseBoard() {
        boardData = new String[numRows][numCols];
        for (String[] boardDatum : boardData) {
            Arrays.fill(boardDatum, " ");
        }
    }

    /**
     * Returns the value at the specified single square on the board.
     *
     * @param x - column reference to return.
     * @param y - row reference to return.
     * @return - return the string value stored at that square - will return a space character if no counter there.
     */
    public String getValueAtPosition(int x, int y) {
        return boardData[y][x];
    }

    /**
     * Place a String value into the specified single square on the board - effectively this is plavcing a counter.
     *
     * @param x     - column reference of square to set
     * @param y     - row reference of square to set
     * @param value - String to place in the square
     */
    public void setValueAtPosition(int x, int y, String value) {
        boardData[y][x] = value;
    }

    /**
     * Returns the contents of the board as a single String with carriage returns to mark the end of a row
     * and pipe symbols to mark columns.
     *
     * @return - String representation of board.
     */
    public String toString() {
        StringBuilder Output = new StringBuilder();
        int x, y;
        for (y = 0; y < boardData.length; y++) {
            Output.append("   ");
            for (x = 0; x < boardData[0].length; x++) {
                Output.append(String.format("¦ %s ", boardData[y][x]));
            }
            Output.append("¦\n");
        }
        Output.append("  ");
        for (x = 1; x <= boardData[0].length; x++) {
            Output.append("   ").append(x);
        }
        Output.append('\n');
        return Output.toString();
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

}


