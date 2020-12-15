package com.simonpreece;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

public class Connect4AI extends AI {
    int gameInARow;
    //todo implement detectThreats()
    //todo implement seekopportunities
//todo check for winning move first
    private int numCols;
    private int numRows;
    private Game game;
    private boolean aiMadeMove = false;

    public Connect4AI(double intelligencePercent, Game game) {
        super(intelligencePercent);
        this.game = game;


    }

    @Override
    protected int makeMove(Game game, Player player) {
        int move = -1;

        try {
            this.numCols = (int) game.getClass().getMethod("getNumCols").invoke(game);
            this.numRows = (int) game.getClass().getMethod("getNumRows").invoke(game);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        gameInARow = ((MyConnectFour) game).inARow;

        move = respondToThreat(game, player);
        if (move > -1) {
            return move + 1; //moves are not zero indexed in the front end cos of humans.....
        }
        else {
            return randomMove();
        }
    }

    private int randomMove() {
        System.out.println("generating random move");
        //todo - turn the below into a random method
        int move = 0;
        int maxRandom = 0;
        maxRandom = numCols;
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
        ArrayList<Integer[][]> threatList;
        threatList = ((MyConnectFour) game).checkHorizontal(player, inARow, false);
        threatList.addAll(((MyConnectFour) game).checkVertical(player, inARow, false));
        threatList.addAll(((MyConnectFour) game).checkDiagonal_Negative(player, inARow, false));
        threatList.addAll(((MyConnectFour) game).checkDiagonal_Positive(player, inARow, false));
        System.out.printf("%d in a row found = %b\n", inARow, threatList.size() > 0);
        return threatList;
    }

    @Override
    protected int respondToThreat(Game game, Player player) {
        boolean moveMade = false;
        int checkInARow;
        int possibleMove = -1;
        for (checkInARow = ((MyConnectFour) game).inARow - 1; checkInARow > 1; checkInARow--) {
            System.out.printf("checking for %d in a row threats\n", checkInARow);
            ArrayList<Integer[][]> threatList = detectThreats(game, player, checkInARow);
            System.out.println("starting decision loop");
            if (threatList.size() > 0) {
                Collections.shuffle(threatList); //randomise order of threat checks so doesn't always start with horizontal lines at (0, 0)
                for (Integer[][] threat : threatList) {
                    System.out.printf("Deciding for %s,%s %s,%s\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);

                    if (aiSpotsThreatOpportunity()) {
                        possibleMove = linePossible(game, player, threat, checkInARow);
                        if (possibleMove > -1) {
                            if (decideToAct(checkInARow)) {
                                //do something
                                System.out.printf("decided to act - placing counter in column %d\n", possibleMove + 1);
                                return possibleMove;
                            }
                            else {
                                possibleMove = -1; //reset as 'decided' to ignore.
                            }
                        }
                    }
                }
            }
            else {
                System.out.printf("no %d in a row threats\n", checkInARow);
            }
        }
        return possibleMove;
    }

    /**
     * decide to act will always return true if an inARow-1 attack is found, otherwise it will get less likely
     *
     * @param checkInARow - current level of inARow checks that have generated the question
     * @return True or False - whether have decided to respond
     */
    private boolean decideToAct(int checkInARow) {
        int countersLeft = gameInARow - checkInARow;
        double responseChance = 1 / countersLeft;
        double randomRoll = Math.random();
        boolean respond = (randomRoll < responseChance);
        System.out.printf("responseChance = %f.2, random = %f.2  = decided to respond? %b\n", responseChance, randomRoll, respond);
        return respond;
    }

    /**
     * Checks if a potential threat is real (there are enough blank spaces in the line to get to the required game
     * inARow setting, and the next counter dropped would extend the row at either end.
     *
     * @param game
     * @param player
     * @param threat
     * @param checkInARow
     * @return -1 if there is no next move possible (i.e. the next slot in a column would not block or extend a line
     * otherwise returns a zero indexed column reference of a potential counter attack/move.
     */
    private int linePossible(Game game, Player player, Integer[][] threat, int checkInARow) {
        //determine gradient of line
        int possibleSquares;
        int PossibleMove = -1;
        if (threat[0][0].equals(threat[1][0])) {
            System.out.printf("%s,%s : %s,%s is a vertical line\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
            possibleSquares = checkVerticalBlanks(game, new Integer[]{threat[0][0], threat[0][1]}, true);
            System.out.printf("verticablanks = %d\n", possibleSquares);
            if (possibleSquares + checkInARow >= gameInARow) {
                System.out.println("movePossible() determined vertical THREAT IS REAL!!");
                return threat[0][0];
            }
            else {
                System.out.println("movePossible() determined vertical threat not real");
            }
        }
        else if (threat[0][1].equals(threat[1][1])) {
            System.out.printf("%s,%s : %s,%s is a Horizontal line\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
            possibleSquares = checkHorizontalBlanks(game, new Integer[]{threat[0][0], threat[0][1]}, true);
            System.out.printf("%d squares found to the left of starting point %s,%s : %s,%s\n", possibleSquares, threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
            possibleSquares += checkHorizontalBlanks(game, new Integer[]{threat[1][0], threat[1][1]}, false);
            System.out.printf("%d squares found to the left or right of starting point %s,%s : %s,%s\n", possibleSquares, threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
            if (possibleSquares + checkInARow >= gameInARow) {
                PossibleMove = movePossible(new Integer[][]{{threat[0][0] - 1, threat[0][1]}, {threat[1][0] + 1, threat[1][1]}});
                if (PossibleMove > -1) {
                    System.out.println("movePossible() determined horizontal THREAT IS REAL!!");
                    return PossibleMove;
                }
                else {
                    System.out.println("movePossible() determined threat not real");
                }
            }
        }
        else if ((threat[1][0] - threat[0][0]) / (threat[1][1] - threat[0][1]) == -1) {
            System.out.printf("%s,%s : %s,%s is a negative y diagonal line\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
        }
        else if ((threat[1][0] - threat[0][0]) / (threat[1][1] - threat[0][1]) == 1) {
            System.out.printf("%s,%s : %s,%s is a positive y diagonal line\n", threat[0][0], threat[0][1], threat[1][0], threat[1][1]);
        }
        return PossibleMove;
    }


    /**
     * Confirms if a move is possible, either to counter an attack or take advantage of an opportunity, randomises which
     * end to check first so that responses are not weighted towards the 0,0 end of a line.
     *
     * @param xyCoordinatesToCheck zero-indexed coordinates of square to check
     * @return -1 if no valid attacks, otherwise returns the zero indexed column number of any potential next move.
     */
    private int movePossible(Integer[][] xyCoordinatesToCheck) {
        // remember isMoveValid and getNextEmptyRow inputs are NOT zero indexed
        boolean threatIsReal = false;
        ArrayList<Integer[]> coordinates = new ArrayList<>();
        coordinates.add(xyCoordinatesToCheck[0]);
        coordinates.add(xyCoordinatesToCheck[1]);
        Collections.shuffle(coordinates);
        for (Integer[] coordinate : coordinates) {
            System.out.printf("movePossible received x:%d, y:%d\n", coordinate[0] + 1, coordinate[1] + 1);
            threatIsReal = ((game.isMoveValid(coordinate[0] + 1, false)) && ((MyConnectFour) game).getNextEmptyRow(coordinate[0] + 1) == coordinate[1]);
            if (threatIsReal) {
                return coordinate[0];
            }
        }
        return -1;
    }


    protected int checkHorizontalBlanks(Game game, Integer[] startingPoint, boolean checkNegativeDirection) {
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
            for (; x >= 0 && x < boardWidth; x += xIncrement) {
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

    private int checkVerticalBlanks(Game game, Integer[] startingPoint, boolean checkNegativeDirection) {
        int countersInARow = 0;
        int boardWidth = numCols;
        int boardHeight = numRows;
        Board board = game.board;
        String counter = board.getValueAtPosition(startingPoint[0], startingPoint[1]);
        String valueAtPosition;
        int yIncrement = (checkNegativeDirection ? -1 : 1);
        int x = startingPoint[0];
        int y = startingPoint[1] + yIncrement;
        if (y >= 0 && x < numRows) {
            for (; y >= 0 && y < boardHeight; y += yIncrement) {
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