package com.simonpreece;

/*
Board Class
    Parameters:
        dimensions - dimensions of board
    checkWin()
    placeCounter()
    printBoard()

 */

public class Board {
    private int numRows;
    private int numCols;
    private char[][] boardData;

    public Board() {
        // create default board if no size given
        numCols = 7;
        numRows = 6;
        boardData = new char[numCols][numRows];
    }

    public Board(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        boardData = new char[numCols][numRows];
    }

    public char getValueAtPosition(int x, int y) {
        return boardData[x][y];
    }

    public void setValueAtPosition(int x, int y, char value) {
        boardData[x][y] = value;
    }

}
