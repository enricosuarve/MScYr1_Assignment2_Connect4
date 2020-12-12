package com.simonpreece;

import java.lang.reflect.InvocationTargetException;

public class Connect4AI extends AI {

    @Override
    protected int makeMove(Game game) {
        int maxRandom = 7;
        /*int maxRandom = 0;
        try {
            maxRandom = (int) game.board.getClass().getMethod("getNumCols").invoke(game.board);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }*/
        //game.board.getNumCols(); //todo - enter working code to dynamically get the max number
        int move = 0;
        boolean moveIsValid = false;

        while (!moveIsValid) {
            move = (int) (Math.random() * (maxRandom - 1 + 1) + 1);
            moveIsValid = game.isMoveValid(move, true);
        }

        return move;
    }
}
