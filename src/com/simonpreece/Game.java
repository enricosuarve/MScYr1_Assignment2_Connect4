package com.simonpreece;

import java.util.ArrayList;

public abstract class Game {
    protected Board board;

    protected abstract void setupGame();

    protected abstract void playGame(Board board, int firstPlayer);

    protected abstract boolean isMoveValid(int move, @SuppressWarnings("SameParameterValue") boolean playerIsHuman);

    public abstract boolean checkForWin(Player player);

    public void displayScoreboard(ArrayList<Player> players) {
        String playerScore;
        String spaces = "                        ";
        int spacesBefore, spacesAfter;
        System.out.println("          ==============================");
        System.out.println("          |    Scores on the doors     |");
        System.out.println("          ==============================");
        for (Player player : players) {
            playerScore = player.getName() + "  " + player.getWins();
            spacesBefore = Math.max(0, (28 - playerScore.length()) / 2);
            spacesAfter = Math.max(0, 28 - playerScore.length() - spacesBefore);
            System.out.printf("          |%s%s%s|\n",
                    spaces.substring(0, spacesBefore),
                    playerScore,
                    spaces.substring(0, spacesAfter));
        }
        System.out.println("          ==============================");
    }
}
