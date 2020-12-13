package com.simonpreece;

import java.lang.reflect.InvocationTargetException;

public class Connect4AI extends AI {
//todo implement detectThreats()
    //todo implement seekopportunities

    @Override
    protected int makeMove(Game game) {
        int maxRandom = 0;
        try {
            maxRandom = (int) game.getClass().getMethod("getNumCols").invoke(game);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //System.out.printf("maxRandom = %d\n",maxRandom);
        int move = 0;
        boolean moveIsValid = false;

        while (!moveIsValid) {
            move = (int) (Math.random() * (maxRandom - 1 + 1) + 1);
            moveIsValid = game.isMoveValid(move, true);
        }
        return move;
    }

    @Override
    protected int detectThreats(Game game) {
        //detect threats by looking for opponents lines

        return 0;
    }
}
