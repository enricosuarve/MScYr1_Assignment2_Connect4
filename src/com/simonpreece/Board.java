package com.simonpreece;

/*
Board Class
    Parameters:
        dimensions - dimensions of board
    checkWin()
    placeCounter()
    printBoard()

 */

import java.util.Arrays;

public class Board {
    private final int numRows;
    private final int numCols;
    private String[][] boardData;

    public Board() {
        // create default board if no size given
        numCols = 7;
        numRows = 6;
        initialiseBoard();
    }

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

    public String getValueAtPosition(int x, int y) {
        return boardData[y][x];
    }

    public void setValueAtPosition(int x, int y, String value) {
        boardData[y][x] = value;
    }

    public void printBoard() {
        int x, y;
        for (y = 0; y < boardData.length; y++) {
            System.out.print(" ");
            for (x = 0; x < boardData[0].length; x++) {
                System.out.printf("| %s ", boardData[y][x]);
            }
            System.out.println("|");
        }
        for (x = 1; x <= boardData[0].length; x++) {
            System.out.print("   " + x);
        }
        System.out.print('\n');
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumCols(int row) {
        return numCols;
    }
}


