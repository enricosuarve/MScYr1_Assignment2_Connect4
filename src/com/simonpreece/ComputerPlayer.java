package com.simonpreece;

public class ComputerPlayer extends Player {

    public ComputerPlayer() {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for computer player " + playerNumber, "Computer");
        counter = ui.getUserInput("Enter a counter symbol for computer player " + playerNumber, "C").charAt(0);
    }
    @Override
    public int getMoveFromPlayer() {
        return 0;
    }

    @Override
    public int getMoveFromPlayer(Board board, Game game) {
        return 0;
    }
}
