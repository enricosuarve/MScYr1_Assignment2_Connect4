package com.simonpreece;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.lang.Math.*;
import java.util.Collections;

public class Connect4AI extends AI {
    int gameInARow;
    //todo implement detectThreats()
    //todo implement seekopportunities
//todo check for winning move first
    private int numCols;
    private int numRows;
    private Game game;

    public Connect4AI(double intelligencePercent, Game game) {
        super(intelligencePercent);
        this.game = game;


    }

    @Override
    protected int makeMove(Game game, Player player) {
        try {
            this.numCols = (int) game.getClass().getMethod("getNumCols").invoke(game);
            this.numRows = (int) game.getClass().getMethod("getNumRows").invoke(game);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        gameInARow = ((MyConnectFour) game).inARow;

        respondToThreat(game, player);

        //todo - turn the below into a random method
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
        int checkInARow;
        for (checkInARow = ((MyConnectFour) game).inARow - 1; checkInARow > 1; checkInARow--) {
            System.out.printf("checking for %d in a row threats\n", checkInARow);
            ArrayList<Integer[][]> threatList = detectThreats(game, player, checkInARow);
            System.out.println("starting decision loop");
            if (threatList.size() > 0) {
                Collections.shuffle(threatList);
                for (Integer[][] threat : threatList) {
                    System.out.printf("Deciding for %s,%s %s,%s\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);

                    if (aiSpotsThreatOpportunity()) {
                        if (movePossible(game, player, threat, checkInARow)) {
                            if (decideToAct(gameInARow, checkInARow)) {
                                //do something
                                System.out.println("decided to act (TBD)");
                                moveMade = true;
                            }
                        }
                    }
                }
            }
            else {
                System.out.printf("no %d in a row threats\n", checkInARow);
            }
        }


    }

    private boolean decideToAct(int gameInARow, int checkInARow) {
        return true;
    }

    private boolean movePossible(Game game, Player player, Integer[][] threat, int checkInARow) {
        //determine gradient of line
        if (threat[0][0] == threat[1][0]) {
            System.out.printf("%s,%s : %s,%s is a vertical line\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
        }
        else if (threat[0][1] == threat[1][1]) {
            System.out.printf("%s,%s : %s,%s is a Horizontal line\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
            Integer[][] nextMoveCordinates = new Integer[][]{{threat[0][0], threat[0][1] - 1}, {threat[1][0], threat[1][1] + 1}};
            int availableBlanks = checkHorizontalBlanks(game, new Integer[]{threat[0][0], threat[0][1]}, true);
            System.out.printf("%d blanks found to the left of starting point %s,%s : %s,%s\n", availableBlanks, threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
            availableBlanks += checkHorizontalBlanks(game, new Integer[]{threat[1][0], threat[1][1]}, false);
            System.out.printf("%d blanks found to the left or right of starting point %s,%s : %s,%s\n", availableBlanks, threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
            if (availableBlanks + checkInARow >= gameInARow) {
                if (((MyConnectFour) game).getNextEmptyRow(threat[0][0]) == threat[0][1]) {
                    System.out.println("THREAT IS REAL!!");
                }
                else {
                    System.out.println("no real threat");
                }
            }
        }
        else if ((threat[1][0] - threat[0][0]) / (threat[1][1] - threat[0][1]) == -1) {
            System.out.printf("%s,%s : %s,%s is a negative y diagonal line\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
        }
        else if ((threat[1][0] - threat[0][0]) / (threat[1][1] - threat[0][1]) == 1) {
            System.out.printf("%s,%s : %s,%s is a positive y diagonal line\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
        }
        return true;
    }

    protected int checkHorizontalBlanks(Game game, Integer[] startingPoint, boolean checkNegativeDirection) {
        //todo - see if can merge check horizontal and vertical
        int countersInARow = 0;
        int boardWidth = numCols;
        int boardHeight = numRows;
        Board board = game.board;
        String counter = board.getValueAtPosition(startingPoint[0], startingPoint[1]);
        String valueAtPosition;
        int xIncrement = (checkNegativeDirection ? -1 : 1);
        int x = startingPoint[0] + xIncrement;
        int y = startingPoint[1];
        if (x >= 0 && x < numCols) {
            for (; x >= 0 && x < boardWidth; x = x + xIncrement) {
                valueAtPosition = board.getValueAtPosition(x, y);
                if (valueAtPosition.equals(counter) || valueAtPosition.equals(" ")) {
                    countersInARow++;
                }
                else {
                    break;
                }
            }
        }
        return countersInARow;
    }

}
