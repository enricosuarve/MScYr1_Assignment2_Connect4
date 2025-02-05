package com.snp39;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;

public class Connect4AI extends AI {
    private final Game game;
    int gameInARow;
    private int numCols;
    private int numRows;

    /**
     * Instantiate a connect 4 AI
     *
     * @param intelligencePercent - double to be used for intelligence (0.0-1.0), used to determine the
     *                            percentage change of spotting attacks etc.
     * @param game                - instance of game to be analyzed by AI.
     */
    public Connect4AI(double intelligencePercent, Game game) {
        super(intelligencePercent);
        this.game = game;
    }

    /**
     * Entry point for a request to the AI to make a move, AI will decide whether to counter
     * a threat or make a move of its own.
     *
     * @param game   - instance of game being played.
     * @param player - Computer player making the move.
     * @return - zero-indexed column computer play has decided to drop a counter in.
     */
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
        if (debugMode) Main.view.Display("generating random move");
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

    /**
     * Analyze the game in play and list all threats or opportunities of a given length as x/y, x/y co-ordinates
     *
     * @param game              - game to be analyzed;
     * @param player            - player deciding for
     * @param inARow            - number of counters in a row to check for
     * @param detectOpportunity - if true look for rows for the player (opportunities)
     *                          if false look for other players rows (threats)
     * @return list of potential lines as x/y, x/y co-ordinates
     */
    private ArrayList<Integer[][]> detectOpportunitiesOrThreats(Game game, Player player, int inARow, boolean detectOpportunity) {
        //detect threats by looking for opponents lines
        ArrayList<Integer[][]> potentialRowList;
        potentialRowList = ((ConnectX) game).checkHorizontal(player, inARow, detectOpportunity);
        potentialRowList.addAll(((ConnectX) game).checkVertical(player, inARow, detectOpportunity));
        potentialRowList.addAll(((ConnectX) game).checkDiagonal_Negative(player, inARow, detectOpportunity));
        potentialRowList.addAll(((ConnectX) game).checkDiagonal_Positive(player, inARow, detectOpportunity));
        if (debugMode)
            Main.view.Display(String.format("%d in a row %s found = %b\n", inARow, (detectOpportunity ? "opportunity" : "threat"), potentialRowList.size() > 0));
        return potentialRowList;
    }

    /**
     * Check for threatening lines to the computer player, verify if the computer has 'spotted' the threat and whether
     * the line is actually possible (i.e. will the next counter dropped in a column join up with it?)
     * If a valid threat is found, decide whether to counter.
     *
     * @param game   - game to be analyzed.
     * @param player - computer player.
     * @return - column to make a move (-1) if no move made.
     */
    @Override
    protected int respondToThreat(Game game, Player player) {
        int checkInARow;
        ArrayList<Integer> inARowPossible;
        for (checkInARow = ((ConnectX) game).inARow - 1; checkInARow > 1; checkInARow--) {
            if (debugMode) Main.view.Display(String.format("**checking for %d in a row threats**\n", checkInARow));
            ArrayList<Integer[][]> threatList = detectOpportunitiesOrThreats(game, player, checkInARow, false);
            if (debugMode) Main.view.Display("starting decision loop");
            if (threatList.size() > 0) {
                Collections.shuffle(threatList); //randomise order of threat checks so doesn't always start with horizontal lines at (0, 0)
                for (Integer[][] threat : threatList) {
                    if (debugMode)
                        Main.view.Display(String.format("Deciding for threat %s,%s %s,%s\n", threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));

                    if (aiSpotsThreatOpportunity()) {
                        inARowPossible = isLinePossible(game, threat, checkInARow);
                        if (inARowPossible.size() > 0) {
                            if (decideToAct(checkInARow, inARowPossible.size())) {
                                //do something
                                Collections.shuffle(inARowPossible); //shuffle so there is no weighting towards moves closer to 0,0
                                if (debugMode)
                                    Main.view.Display(String.format("decided to act on threat - placing counter in column %d\n", inARowPossible.get(0) + 1));
                                return inARowPossible.get(0);
                            }

                        }
                    }
                }
            }
            else {
                if (debugMode) Main.view.Display(String.format("no %d in a row threats\n", checkInARow));
            }
        }
        return -1;
    }

    @Override
    protected int respondToOpportunities(Game game, Player player) {
        return respondToOpportunities(game, player, false);
    }

    /**
     * Check for possible lines for the computer player, verify if the computer has 'spotted' the line and whether
     * the line is actually possible (i.e. will the next counter dropped in a column join up with it?)
     * If a valid opportunity is found, decide whether to use.
     *
     * @param game   - game to be analyzed.
     * @param player - computer player.
     * @return - column to make a move (-1) if no move made.
     */
    protected int respondToOpportunities(Game game, Player player, boolean checkForNMinusOne) {
        int checkInARow;
        ArrayList<Integer> inARowPossible;
        int checkInARowStart = (checkForNMinusOne ? ((ConnectX) game).inARow - 1 : ((ConnectX) game).inARow - 2);
        int checkInARowLimit = (checkForNMinusOne ? (((ConnectX) game).inARow - 2) : 1);
        for (checkInARow = checkInARowStart; checkInARow > checkInARowLimit; checkInARow--) {
            if (debugMode) Main.view.Display(String.format("checking for %d in a row opportunities\n", checkInARow));
            ArrayList<Integer[][]> opportunityList = detectOpportunitiesOrThreats(game, player, checkInARow, true);
            if (debugMode) Main.view.Display("starting decision loop");
            if (opportunityList.size() > 0) {
                Collections.shuffle(opportunityList); //randomise order of threat checks so doesn't always start with horizontal lines at (0, 0)
                for (Integer[][] threat : opportunityList) {
                    if (debugMode)
                        Main.view.Display(String.format("Deciding for opportunity %s,%s %s,%s\n", threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));
                    if (aiSpotsThreatOpportunity()) {
                        inARowPossible = isLinePossible(game, threat, checkInARow);
                        //return potential columns to here
                        if (inARowPossible.size() > 0) {
                            if (decideToAct(checkInARow, inARowPossible.size())) {
                                //do something
                                Collections.shuffle(inARowPossible); //shuffle so there is no weighting towards moves closer to 0,0
                                if (debugMode)
                                    Main.view.Display(String.format("decided to act on opportunity - placing counter in column %d\n", inARowPossible.get(0) + 1));
                                return inARowPossible.get(0);
                            }
                        }
                    }
                }
            }
            else {
                if (debugMode) Main.view.Display(String.format("no %d in a row threats\n", checkInARow));
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
            if (debugMode) Main.view.Display("2 moves to win detected");
            responseChance = intelligencePercent; //if there are a possible two moves to win for either the threat or opportunity it is up to the AI's 'intelligence' as to whether it spots this
        }
        else {
            responseChance = 1.0 / countersLeft;
        }
        double randomRoll = Math.random();
        boolean respond = (randomRoll < responseChance);
        if (debugMode)
            Main.view.Display(String.format("responseChance = %.2f, random = %.2f  = decided to respond? %b\n", responseChance, randomRoll, respond));
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
            if (debugMode)
                Main.view.Display(String.format("%s,%s : %s,%s is a vertical line\n", threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));
            possSpacesAfter = checkVerticalBlanks(game, new Integer[]{threat[0][0], threat[0][1]});
            if (debugMode) Main.view.Display(String.format("verticalBlanks = %d\n", possSpacesAfter));
            if (possSpacesAfter + checkInARow >= gameInARow) {
                if (debugMode) Main.view.Display("movePossible() determined vertical line IS REAL!!");
                confirmedColumnsToDrop.add(threat[0][0]);
            }
            else {
                if (debugMode) Main.view.Display("movePossible() determined vertical line not real");
            }
        }
        //Horizontal Lines
        else if (threat[0][1].equals(threat[1][1])) {
            if (debugMode)
                Main.view.Display(String.format("%s,%s : %s,%s is a Horizontal line\n", threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));
            possSpacesBefore = checkHorizontalBlanks(game, new Integer[]{threat[0][0], threat[0][1]}, true);
            if (debugMode)
                Main.view.Display(String.format("%d squares found to the left of starting point %s,%s : %s,%s\n", possSpacesBefore, threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));
            if (possSpacesBefore > 0) possibleCoordinates.add(new Integer[][]{{threat[0][0] - 1, threat[0][1]}});
            possSpacesAfter = checkHorizontalBlanks(game, new Integer[]{threat[1][0], threat[1][1]}, false);
            if (debugMode)
                Main.view.Display(String.format("%d squares found to the left or right of starting point %s,%s : %s,%s\n", possSpacesAfter, threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));
            if (possSpacesAfter > 0) possibleCoordinates.add(new Integer[][]{{threat[1][0] + 1, threat[1][1]}});
            if (possSpacesBefore + possSpacesAfter + checkInARow >= gameInARow) {
                confirmedColumnsToDrop = isMovePossible(possibleCoordinates);
                if (confirmedColumnsToDrop.size() > 0) {
                    if (debugMode) Main.view.Display("movePossible() determined horizontal line IS REAL!!");
                }
                else {
                    if (debugMode) Main.view.Display("movePossible() determined line not real");
                }
            }
        }
        //Negative-y diagonals (/)
        else if ((threat[1][0] - threat[0][0]) / (threat[1][1] - threat[0][1]) == -1) {
            if (debugMode)
                Main.view.Display(String.format("%s,%s : %s,%s is a negative y diagonal line\n", threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));
            possSpacesBefore = checkNegativeDiagonalBlanks(game, new Integer[]{threat[0][0], threat[0][1]}, false);
            if (possSpacesBefore > 0) possibleCoordinates.add(new Integer[][]{{threat[0][0] - 1, threat[0][1] + 1}});
            possSpacesAfter = checkNegativeDiagonalBlanks(game, new Integer[]{threat[1][0], threat[1][1]}, true);
            if (possSpacesAfter > 0) possibleCoordinates.add(new Integer[][]{{threat[1][0] + 1, threat[1][1] - 1}});
            if (debugMode)
                Main.view.Display(String.format("%d squares found at either end of diagonal with of starting point %s,%s : %s,%s\n", possSpacesBefore + possSpacesAfter, threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));
            if (possSpacesBefore + possSpacesAfter + checkInARow >= gameInARow) {
                confirmedColumnsToDrop = isMovePossible(possibleCoordinates);
                if (confirmedColumnsToDrop.size() > 0) {
                    if (debugMode) Main.view.Display("movePossible() determined diagonal line IS REAL!!");
                }
                else {
                    if (debugMode) Main.view.Display("movePossible() determined line not real");
                }
            }
        }
        //Positive-y diagonals (\)
        else if ((threat[1][0] - threat[0][0]) / (threat[1][1] - threat[0][1]) == 1) {
            if (debugMode)
                Main.view.Display(String.format("%s,%s : %s,%s is a positive y diagonal line\n", threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));
            possSpacesBefore = checkPositiveDiagonalBlanks(game, new Integer[]{threat[0][0], threat[0][1]}, true);
            if (possSpacesBefore > 0) possibleCoordinates.add(new Integer[][]{{threat[0][0] - 1, threat[0][1] - 1}});
            possSpacesAfter = checkPositiveDiagonalBlanks(game, new Integer[]{threat[1][0], threat[1][1]}, false);
            if (possSpacesAfter > 0) possibleCoordinates.add(new Integer[][]{{threat[1][0] + 1, threat[1][1] + 1}});
            if (debugMode)
                Main.view.Display(String.format("%d squares found at either end of diagonal with of starting point %s,%s : %s,%s\n", possSpacesBefore + possSpacesAfter, threat[0][0] + 1, threat[0][1] + 1, threat[1][0] + 1, threat[1][1] + 1));
            if (possSpacesBefore + possSpacesAfter + checkInARow >= gameInARow) {
                confirmedColumnsToDrop = isMovePossible(possibleCoordinates);
                if (confirmedColumnsToDrop.size() > 0) {
                    if (debugMode) Main.view.Display("movePossible() determined diagonal line IS REAL!!");
                }
                else {
                    if (debugMode) Main.view.Display("movePossible() determined line not real");
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
    private ArrayList<Integer> isMovePossible(ArrayList<Integer[][]> xyCoordinatesToCheck) {
        // remember isMoveValid and getNextEmptyRow inputs are NOT zero indexed
        boolean threatIsReal;
        ArrayList<Integer> confirmedColumns = new ArrayList<>();
        for (Integer[][] coordinate : xyCoordinatesToCheck) {
            if (debugMode)
                Main.view.Display(String.format("movePossible received x:%d, y:%d\n", coordinate[0][0] + 1, coordinate[0][1] + 1));
            threatIsReal = ((game.isMoveValid(coordinate[0][0] + 1, false)) && ((ConnectX) game).getNextEmptyRow(coordinate[0][0] + 1) == coordinate[0][1]);
            if (threatIsReal) {
                confirmedColumns.add(coordinate[0][0]);
            }
        }
        return confirmedColumns;
    }

    /**
     * Check for empty squares horizontally before or after a starting point
     * (if there are no spaces the line may not be possible).
     *
     * @param game                   - game being analysed.
     * @param startingPoint          - x/y co-ordinate.
     * @param checkNegativeDirection if true check towards 0,0 if false check in the other direction
     * @return number of blank spaces immediately before or after found
     */
    private int checkHorizontalBlanks(Game game, Integer[] startingPoint, boolean checkNegativeDirection) {
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

    /**
     * Check for empty squares vertically above a starting point
     * (if there are no spaces the line may not be possible).
     *
     * @param game          - game being analysed.
     * @param startingPoint - x/y co-ordinate.
     * @return number of blank spaces immediately above found
     */
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

    /**
     * Check for empty squares on a negative diagonal (/) before or after a starting point
     * (if there are no spaces the line may not be possible).
     *
     * @param game                   - game being analysed.
     * @param startingPoint          - x/y co-ordinate.
     * @param checkNegativeDirection if true check towards 0,0 if false check in the other direction
     * @return number of blank spaces immediately before or after found
     */
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

    /**
     * Check for empty squares on a positive diagonal (\) before or after a starting point
     * (if there are no spaces the line may not be possible).
     *
     * @param game                   - game being analysed.
     * @param startingPoint          - x/y co-ordinate.
     * @param checkNegativeDirection if true check towards 0,0 if false check in the other direction
     * @return number of blank spaces immediately before or after found
     */
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