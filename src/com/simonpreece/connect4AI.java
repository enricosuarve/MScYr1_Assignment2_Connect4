package com.simonpreece;

public class connect4AI extends AI {

    @Override
    protected int makeMove(Game game) {
        int maxRandom = 7;//game.board.getNumCols(); //todo - enter working code to dynamically get the max number
        int move = 0;
        boolean moveIsValid = false;

        while (!moveIsValid) {
            move = (int) (Math.random() * (maxRandom - 1 + 1) + 1);
            moveIsValid = game.isMoveValid(move, true);
        }

        return move;
    }
}
