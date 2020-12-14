package com.simonpreece;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.lang.Math.*;

public class Connect4AI extends AI {
//todo implement detectThreats()
    //todo implement seekopportunities

    public Connect4AI(double intelligencePercent){
        super(intelligencePercent);
    }

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
    protected void detectThreats(Game game, Player player) {
        //detect threats by looking for opponents lines
        //check for 3 in a row
        for (int i = ((MyConnectFour) game).inARow - 1; i > 1; i--) {
            ArrayList<Integer[][]> threatList = new ArrayList<>();
            threatList = ((MyConnectFour) game).checkHorizontal(player, i, false);
            threatList.addAll(((MyConnectFour) game).checkVertical(player, i, false));
            threatList.addAll(((MyConnectFour) game).checkDiagonal_Negative(player, i, false));
            threatList.addAll(((MyConnectFour) game).checkDiagonal_Positive(player, i, false));
            System.out.printf("%d in a row found = %b\n", i, threatList.size() > 0);
            respondToThreat(threatList, i);

        }
    }

    private void respondToThreat(ArrayList<Integer[][]> threatList, int inARow) {
        for (Integer[][] threat:threatList){
            if(aiSpotsThreatOpportunity()){

            }
        }

    }
}
