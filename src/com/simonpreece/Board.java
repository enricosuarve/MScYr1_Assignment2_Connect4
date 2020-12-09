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
    private int numRows;
    private int numCols;
    private char[][] boardData;

    public Board() {
        // create default board if no size given
        numCols = 7;
        numRows = 6;
        boardData = new char[numCols][numRows];
        initialiseBoard();
    }

    public Board(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        initialiseBoard();
    }

    private void initialiseBoard() {
        boardData = new char[numCols][numRows];
        for (char[] boardDatum : boardData) {
            Arrays.fill(boardDatum, ' ');
        }
    }

    public char getValueAtPosition(int x, int y) {
        return boardData[y][x];
    }

    public void setValueAtPosition(int x, int y, char value) {
        boardData[y][x] = value;
    }

    public void printBoard() {
        int x, y;
        for (y = 0; y < boardData.length; y++) {
            System.out.print(" ");
            for (x = 0; x < boardData[y].length; x++) {
                System.out.printf("| %c ", boardData[y][x]);
            }
            System.out.println("|");
        }
        for (x = 1; x <= boardData[0].length; x++) {
            System.out.print("   " + x);
        }
        System.out.print('\n');
    }
}


