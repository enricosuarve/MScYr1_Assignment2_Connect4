package com.simonpreece;

public abstract class Game {
    protected Board board;

    protected abstract void setupGame();

    protected abstract void playGame(Board board);

    protected abstract boolean isMoveValid(int move, @SuppressWarnings("SameParameterValue") boolean playerIsHuman);
}
