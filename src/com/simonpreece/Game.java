package com.simonpreece;

public abstract class Game {

    protected abstract void setupGame() ;

    protected abstract void playGame(Board board);
    protected abstract boolean isMoveValid(int move);
}
