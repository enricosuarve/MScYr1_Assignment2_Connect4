package com.simonpreece;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.lang.Math.*;

public class Connect4AI extends AI {
//todo implement detectThreats()
    //todo implement seekopportunities

    public Connect4AI(double intelligencePercent) {
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

    private ArrayList<Integer[][]> detectThreats(Game game, Player player, int inARow) {
        //detect threats by looking for opponents lines
        //check for 3 in a row
        ArrayList<Integer[][]> threatList = new ArrayList<>();
        threatList = ((MyConnectFour) game).checkHorizontal(player, inARow, false);
        threatList.addAll(((MyConnectFour) game).checkVertical(player, inARow, false));
        threatList.addAll(((MyConnectFour) game).checkDiagonal_Negative(player, inARow, false));
        threatList.addAll(((MyConnectFour) game).checkDiagonal_Positive(player, inARow, false));
        System.out.printf("%d in a row found = %b\n", inARow, threatList.size() > 0);
        return threatList;
    }

    @Override
    protected void respondToThreat(Game game, Player player) {
        boolean moveMade = false;
        int gameInARow = ((MyConnectFour) game).inARow;
        int checkInARow;
        while (!moveMade) {
            for (checkInARow = ((MyConnectFour) game).inARow - 1; checkInARow > 1; checkInARow--) {
                ArrayList<Integer[][]> threatList = detectThreats(game, player, checkInARow);
                for (Integer[][] threat : threatList) {
                    if (aiSpotsThreatOpportunity()) {
                        if (movePossible(threat)) {
                            if (decideToAct(gameInARow, checkInARow)) {
                                //do something
                                moveMade = true;
                            }
                        }
                    }
                }
            }
        }


    }

    private boolean decideToAct(int gameInARow, int checkInARow) {
        return true;
    }

    private boolean movePossible(Integer[][] threat) {
        //determine gradient of line
        System.out.println("movePossible received threat: "+ threat.toString());
        return true;
    }
}
