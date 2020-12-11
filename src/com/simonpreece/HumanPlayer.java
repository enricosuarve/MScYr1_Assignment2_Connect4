package com.simonpreece;

public class HumanPlayer extends Player {
    public HumanPlayer() {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for player " + playerNumber);
        counter = ui.getUserInput("Enter a counter symbol for player " + playerNumber).charAt(0);
    }
}
