package com.simonpreece;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

public class Connect4AI extends AI {
    private final Game game;
    int gameInARow;
    private int numCols;
    private int numRows;

    public Connect4AI(double intelligencePercent, Game game) {
        super(intelligencePercent);
        this.game = game;
    }

    @Override
    protected int makeMove(Game game, Player player) {
        int move;
        try {
            this.numCols = (int) game.getClass().getMethod("getNumCols").invoke(game);
            this.numRows = (int) game.getClass().getMethod("getNumRows").invoke(game);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        gameInARow = ((ConnectX) game).inARow;

        move = respondToOpportunities(game, player, true);
        if (move > -1) { //moves are not zero indexed in the front end cos of humans.....
            return move + 1;
        }
        else {
            move = respondToThreat(game, player);
            if (move > -1) {
                return move + 1;
            }
            else {
                move = respondToOpportunities(game, player);
                if (move > -1) {
                    return move + 1;
                }
                else {
                    return randomMove();
                }
            }
        }
    }

    private int randomMove() {
        if(debugMode)System.out.println("generating random move");
        int move = 0;
        int maxRandom;
        maxRandom = numCols;
        boolean moveIsValid = false;
        while (!moveIsValid) {
            move = (int) (Math.random() * (maxRandom - 1 + 1) + 1);
            moveIsValid = game.isMoveValid(move, true);
        }
        return move;
    }

    private ArrayList<Integer[][]> detectOpportunitiesOrThreats(Game game, Player player, int inARow, boolean detectOpportunity) {
        //detect threats by looking for opponents lines
        ArrayList<Integer[][]> potentialRowList;
        potentialRowList = ((ConnectX) game).checkHorizontal(player, inARow, detectOpportunity);
        potentialRowList.addAll(((ConnectX) game).checkVertical(player, inARow, detectOpportunity));
        potentialRowList.addAll(((ConnectX) game).checkDiagonal_Negative(player, inARow, detectOpportunity));
        potentialRowList.addAll(((ConnectX) game).checkDiagonal_Positive(player, inARow, detectOpportunity));
        if(debugMode)System.out.printf("%d in a row %s found = %b\n", inARow, (detectOpportunity ? "opportunity" : "threat"), potentialRowList.size() > 0);
        return potentialRowList;
    }

    @Override
    protected int respondToThreat(Game game, Player player) {
        int checkInARow;
        ArrayList<Integer> inARowPossible;
        for (checkInARow = ((ConnectX) game).inARow - 1; checkInARow > 1; checkInARow--) {
            if(debugMode)System.out.printf("**checking for %d in a row threats**\n", checkInARow);
            ArrayList<Integer[][]> threatList = detectOpportunitiesOrThreats(game, player, checkInARow, false);
            if(debugMode)System.out.println("starting decision loop");
            if (threatList.size() > 0) {
                Collections.shuffle(threatList); //randomise order of threat checks so doesn't always start with horizontal lines at (0, 0)
                for (Integer[][] threat : threatList) {
                    if(debugMode)System.out.printf("Deciding for threat %s,%s %s,%s\n", threat[0][0]+1, threat[0][1]+1, threat[1][0]+1, threat[1][1]+1);

                    if (aiSpotsThreatOpportunity()) {
                        inARowPossible = isLinePossible(game, threat, checkInARow);
                        if (inARowPossible.size() > 0) {
                            if (decideToAct(checkInARow, inARowPossible.size())) {
                                //do something
                                Collections.shuffle(inARowPossible); //shuffle so there is no weighting towards moves closer to 0,0
                                if(debugMode)System.out.printf("decided to act on threat - placing counter in column %d\n", inARowPossible.get(0)+1);
                                return inARowPossible.get(0);
                            }

                        }
                    }
                }
            }
            else {
                if(debugMode)System.out.printf("no %d in a row threats\n", checkInARow);
            }
        }
        return -1;
    }


    @Override
    protected int respondToOpportunities(Game game, Player player) {
        return respondToOpportunities(game, player, false);
    }

    protected int respondToOpportunities(Game game, Player player, boolean checkForNMinusOne) {
        int checkInARow;
        ArrayList<Integer> inARowPossible;
        int checkInARowStart = (checkForNMinusOne ? ((ConnectX) game).inARow - 1 : ((ConnectX) game).inARow - 2);
        int checkInARowLimit = (checkForNMinusOne ? (((ConnectX) game).inARow - 2) : 1);
        for (checkInARow = checkInARowStart; checkInARow > checkInARowLimit; checkInARow--) {
            if(debugMode)System.out.printf("checking for %d in a row opportunities\n", checkInARow);
            ArrayList<Integer[][]> opportunityList = detectOpportunitiesOrThreats(game, player, checkInARow, true);
            if(debugMode)System.out.println("starting decision loop");
            if (opportunityList.size() > 0) {
                Collections.shuffle(opportunityList); //randomise order of threat checks so doesn't always start with horizontal lines at (0, 0)
                for (Integer[][] threat : opportunityList) {
                    if(debugMode)System.out.printf("Deciding for opportunity %s,%s %s,%s\n", threat[0][0]+1, threat[0][1]+1, threat[1][0]+1, threat[1][1]+1);
                    if (aiSpotsThreatOpportunity()) {
                        inARowPossible = isLinePossible(game, threat, checkInARow);
                        //return potential columns to here
                        if (inARowPossible.size() > 0) {
                            if (decideToAct(checkInARow, inARowPossible.size())) {
                                //do something
                                Collections.shuffle(inARowPossible); //shuffle so there is no weighting towards moves closer to 0,0
                                if(debugMode)System.out.printf("decided to act on opportunity - placing counter in column %d\n", inARowPossible.get(0)+1);
                                return inARowPossible.get(0);
                            }
                        }
                    }
                }
            }
            else {
                if(debugMode)System.out.printf("no %d in a row threats\n", checkInARow);
            }
        }
        return -1;
    }

    /**
     * Decide to act will always return true if an inARow-1 attack is found, otherwise it will get less likely
     *
     * @param checkInARow           - current level of inARow checks that have generated the question
     * @param numberOfPossibleMoves - number of moves possible (1 or 2) for the check in question.
     * @return True or False - whether have decided to respond
     */
    private boolean decideToAct(int checkInARow, int numberOfPossibleMoves) {
        int countersLeft = gameInARow - checkInARow;
        double responseChance;
        boolean twoMovesToWin = (numberOfPossibleMoves == 2 && gameInARow - checkInARow == 2);
        if (twoMovesToWin) {
            if(debugMode)System.out.println("2 moves to win detected");
            responseChance = intelligencePercent; //if there are a possible two moves to win for either the threat or opportunity it is up to the AI's 'intelligence' as to whether it spots this
        }
        else {
            responseChance = 1.0 / countersLeft;
        }
        double randomRoll = Math.random();
        boolean respond = (randomRoll < responseChance);
        if(debugMode)System.out.printf("responseChance = %.2f, random = %.2f  = decided to respond? %b\n", responseChance, randomRoll, respond);
        return respond;
    }

    /**
     * Checks if a potential threat is real (there are enough blank spaces in the line to get to the required game
     * inARow setting, and the next counter dropped would extend the row at either end.
     *
     * @param game        game to be checked
     * @param threat      xy to xy coordinates of potential threat to be assessed
     * @param checkInARow how many inARow are we checking for on this pass?
     * @return Arraylist<Integer> containing any columns that counters can be dropped to counter a threat or use an opportunity
     */
    private ArrayList<Integer> isLinePossible(Game game, Integer[][] threat, int checkInARow) {
        //determine gradient of line
        //int possibleSquares;
        int possSpacesBefore, possSpacesAfter;
        ArrayList<Integer[][]> possibleCoordinates = new ArrayList<>();
        ArrayList<Integer> confirmedColumnsToDrop = new ArrayList<>();
        //Vertical Lines
        if (threat[0][0].equals(threat[1][0])) {
            if(debugMode)System.out.printf("%s,%s : %s,%s is a vertical line\n", threat[0][0]+1, threat[0][1]+1, threat[1][0]+1, threat[1][1]+1);
            possSpacesAfter = checkVerticalBlanks(game, new Integer[]{threat[0][0], threat[0][1]});
            if(debugMode)System.out.printf("verticalBlanks = %d\n", possSpacesAfter);
            if (possSpacesAfter + checkInARow >= gameInARow) {
                if(debugMode)System.out.println("movePossible() determined vertical line IS REAL!!");
                confirmedColumnsToDrop.add(threat[0][0]);
            }
            else {
                if(debugMode)System.out.println("movePossible() determined vertical line not real");
            }
        }
        //Horizontal Lines
        else if (threat[0][1].equals(threat[1][1])) {
            if(debugMode)System.out.printf("%s,%s : %s,%s is a Horizontal line\n", threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1);
            possSpacesBefore = checkHorizontalBlanks(game, new Integer[]{threat[0][0], threat[0][1]}, true);
            if(debugMode)System.out.printf("%d squares found to the left of starting point %s,%s : %s,%s\n", possSpacesBefore, threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1);
            if (possSpacesBefore > 0) possibleCoordinates.add(new Integer[][]{{threat[0][0] - 1, threat[0][1]}});
            possSpacesAfter = checkHorizontalBlanks(game, new Integer[]{threat[1][0], threat[1][1]}, false);
            if(debugMode)System.out.printf("%d squares found to the left or right of starting point %s,%s : %s,%s\n", possSpacesAfter, threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1);
            if (possSpacesAfter > 0) possibleCoordinates.add(new Integer[][]{{threat[1][0] + 1, threat[1][1]}});
            if (possSpacesBefore + possSpacesAfter + checkInARow >= gameInARow) {
                confirmedColumnsToDrop = movePossible(possibleCoordinates);
                if (confirmedColumnsToDrop.size() > 0) {
                    if(debugMode)System.out.println("movePossible() determined horizontal line IS REAL!!");
                }
                else {
                    if(debugMode)System.out.println("movePossible() determined line not real");
                }
            }
        }
        //Negative-y diagonals (/)
        else if ((threat[1][0] - threat[0][0]) / (threat[1][1] - threat[0][1]) == -1) {
            if(debugMode)System.out.printf("%s,%s : %s,%s is a negative y diagonal line\n", threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1);
            possSpacesBefore = checkNegativeDiagonalBlanks(game, new Integer[]{threat[0][0], threat[0][1]}, false);
            if (possSpacesBefore > 0) possibleCoordinates.add(new Integer[][]{{threat[0][0] - 1, threat[0][1] + 1}});
            possSpacesAfter = checkNegativeDiagonalBlanks(game, new Integer[]{threat[1][0], threat[1][1]}, true);
            if (possSpacesAfter > 0) possibleCoordinates.add(new Integer[][]{{threat[1][0] + 1, threat[1][1] - 1}});
            if(debugMode)System.out.printf("%d squares found at either end of diagonal with of starting point %s,%s : %s,%s\n", possSpacesBefore + possSpacesAfter, threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1);
            if (possSpacesBefore + possSpacesAfter + checkInARow >= gameInARow) {
                confirmedColumnsToDrop = movePossible(possibleCoordinates);
                if (confirmedColumnsToDrop.size() > 0) {
                    if(debugMode)System.out.println("movePossible() determined diagonal line IS REAL!!");
                }
                else {
                    if(debugMode)System.out.println("movePossible() determined line not real");
                }
            }
        }
        //Positive-y diagonals (\)
        else if ((threat[1][0] - threat[0][0]) / (threat[1][1] - threat[0][1]) == 1) {
            if(debugMode)System.out.printf("%s,%s : %s,%s is a positive y diagonal line\n", threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1);
            possSpacesBefore = checkPositiveDiagonalBlanks(game, new Integer[]{threat[0][0], threat[0][1]}, true);
            if (possSpacesBefore > 0) possibleCoordinates.add(new Integer[][]{{threat[0][0] - 1, threat[0][1] - 1}});
            possSpacesAfter = checkPositiveDiagonalBlanks(game, new Integer[]{threat[1][0], threat[1][1]}, false);
            if (possSpacesAfter > 0) possibleCoordinates.add(new Integer[][]{{threat[1][0] + 1, threat[1][1] + 1}});
            if(debugMode)System.out.printf("%d squares found at either end of diagonal with of starting point %s,%s : %s,%s\n", possSpacesBefore + possSpacesAfter, threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1);
            if (possSpacesBefore + possSpacesAfter + checkInARow >= gameInARow) {
                confirmedColumnsToDrop = movePossible(possibleCoordinates);
                if (confirmedColumnsToDrop.size() > 0) {
                    if(debugMode)System.out.println("movePossible() determined diagonal line IS REAL!!");
                }
                else {
                    if(debugMode)System.out.println("movePossible() determined line not real");
                }
            }
        }
        return confirmedColumnsToDrop;
    }


    /**
     * Confirms if a move is possible, either to counter an attack or take advantage of an opportunity
     *
     * @param xyCoordinatesToCheck zero-indexed coordinates of square to check
     * @return ArrayList containing list of columns that contain legal moves to counter threat or take advantage of opportunity
     */
    private ArrayList<Integer> movePossible(ArrayList<Integer[][]> xyCoordinatesToCheck) {
        // remember isMoveValid and getNextEmptyRow inputs are NOT zero indexed
        boolean threatIsReal;
        ArrayList<Integer> confirmedColumns = new ArrayList<>();
        for (Integer[][] coordinate : xyCoordinatesToCheck) {
            if(debugMode)System.out.printf("movePossible received x:%d, y:%d\n", coordinate[0][0]+1, coordinate[0][1]+1);
            threatIsReal = ((game.isMoveValid(coordinate[0][0] + 1, false)) && ((ConnectX) game).getNextEmptyRow(coordinate[0][0] + 1) == coordinate[0][1]);
            if (threatIsReal) {
                confirmedColumns.add(coordinate[0][0]);
            }
        }
        return confirmedColumns;
    }


    protected int checkHorizontalBlanks(Game game, Integer[] startingPoint, boolean checkNegativeDirection) {
        int countersInARow = 0;
        int boardWidth = numCols;
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

    private int checkVerticalBlanks(Game game, Integer[] startingPoint) {
        int countersInARow = 0;
        int boardHeight = numRows;
        Board board = game.board;
        String counter = board.getValueAtPosition(startingPoint[0], startingPoint[1]);
        String valueAtPosition;
        int yIncrement = -1;
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

    private int checkNegativeDiagonalBlanks(Game game, Integer[] startingPoint, boolean checkNegativeDirection) {
        int countersInARow = 0;
        int boardWidth = numCols;
        int boardHeight = numRows;
        Board board = game.board;
        String counter = board.getValueAtPosition(startingPoint[0], startingPoint[1]);
        String valueAtPosition;
        int xIncrement = (checkNegativeDirection ? 1 : -1);
        int yIncrement = (checkNegativeDirection ? -1 : 1);
        int x = startingPoint[0] + xIncrement;
        int y = startingPoint[1] + yIncrement;
        while (y >= 0 && y < boardHeight && x >= 0 && x < boardWidth) {
            valueAtPosition = board.getValueAtPosition(x, y);
            if (valueAtPosition.equals(counter) || valueAtPosition.equals(" ")) {
                countersInARow++;
            }
            else {
                break;
            }
            x = +xIncrement;
            y += yIncrement;
        }
        return countersInARow;
    }

    private int checkPositiveDiagonalBlanks(Game game, Integer[] startingPoint, boolean checkNegativeDirection) {
        int countersInARow = 0;
        int boardWidth = numCols;
        int boardHeight = numRows;
        Board board = game.board;
        String counter = board.getValueAtPosition(startingPoint[0], startingPoint[1]);
        String valueAtPosition;
        int xIncrement = (checkNegativeDirection ? -1 : 1);
        int yIncrement = (checkNegativeDirection ? -1 : 1);
        int x = startingPoint[0] + xIncrement;
        int y = startingPoint[1] + yIncrement;
        while (y >= 0 && y < boardHeight && x >= 0 && x < boardWidth) {
            valueAtPosition = board.getValueAtPosition(x, y);
            if (valueAtPosition.equals(counter) || valueAtPosition.equals(" ")) {
                countersInARow++;
            }
            else {
                break;
            }
            x += xIncrement;
            y += yIncrement;
        }
        return countersInARow;
    }

}