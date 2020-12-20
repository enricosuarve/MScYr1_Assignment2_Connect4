package com.snp39;

import java.util.ArrayList;

public abstract class Game implements HasComputerPlayer {
    protected Board board;

    protected abstract void setupGame();

    protected abstract void playGame(Board board, int firstPlayer);

    protected abstract boolean isMoveValid(int move, @SuppressWarnings("SameParameterValue") boolean playerIsHuman);

    @SuppressWarnings("unused") // Leaving intact as still seems like something most games would want to implement
    public abstract boolean checkForWin(Player player);

    public abstract AI getAIClass();

    public void displayScoreboard(ArrayList<Player> players) {
        String playerScore;
        String spaces = "                        ";
        int spacesBefore, spacesAfter;
        Main.view.Display("          ==============================");
        Main.view.Display("          |    Scores on the doors     |");
        Main.view.Display("          ==============================");
        for (Player player : players) {
            playerScore = player.getName() + "  " + player.getWins();
            spacesBefore = Math.max(0, (28 - playerScore.length()) / 2);
            spacesAfter = Math.max(0, 28 - playerScore.length() - spacesBefore);
            Main.view.Display(String.format("          |%s%s%s|",
                    spaces.substring(0, spacesBefore),
                    playerScore,
                    spaces.substring(0, spacesAfter)));
        }
        Main.view.Display("          ==============================");
    }

}