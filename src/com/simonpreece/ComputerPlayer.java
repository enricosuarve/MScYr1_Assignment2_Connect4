package com.simonpreece;

public class ComputerPlayer extends Player {

    AI ai;

    public ComputerPlayer() {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for computer player " + playerNumber, "Computer");
        counter = ui.getUserInput("Enter a counter symbol for computer player " + playerNumber, "C").substring(0,1);
    }

    @Override
    public int getMoveFromPlayer() {
        return 0;
    }


    @Override
    public int getMoveFromPlayer(String requestToUser, Game game) {
        System.out.println(requestToUser);
        return ai.makeMove(game);
    }

    public int getMoveFromAIPlayer(String requestToUser, Game game, AI ai) {
        this.ai = ai;
        return getMoveFromPlayer(requestToUser, game);
    }
}
