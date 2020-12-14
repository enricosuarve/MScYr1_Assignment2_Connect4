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
            for (checkInARow = ((MyConnectFour) game).inARow - 1; checkInARow > 1; checkInARow--) {
                System.out.printf("checking for %d in a row threats\n", checkInARow);
                ArrayList<Integer[][]> threatList = detectThreats(game, player, checkInARow);
                System.out.println("starting decision loop");
                if (threatList.size()>0){
                //todo - goes through in a predictable order - how to randomise?
                    for (Integer[][] threat : threatList) {
                    System.out.printf("Deciding for %s,%s %s,%s\n", threat[0][0], threat[0][1],threat[1][0],threat[1][1]);

                    if (aiSpotsThreatOpportunity()) {
                        if (movePossible(threat)) {
                            if (decideToAct(gameInARow, checkInARow)) {
                                //do something
                                moveMade = true;
                            }
                        }
                    }
                }}else{
                    System.out.printf("no %d in a row threats\n", checkInARow);
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
