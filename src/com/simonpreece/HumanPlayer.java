package com.simonpreece;

public class HumanPlayer extends Player {
    public HumanPlayer() {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for player " + playerNumber);
        counter = ui.getUserInput("Enter a counter symbol for player " + playerNumber,"@").substring(0,1);
    }

    public HumanPlayer(String counter) {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for player " + playerNumber);
        this.counter = ui.getUserInput("Enter a counter symbol for player " + playerNumber,counter).substring(0,1);
    }

    @Override
    public int getMoveFromPlayer() {
        return 0;
    }

    @Override
    public int getMoveFromPlayer(String requestToUser, Game game) {
        UI ui = new UI();
        while (true) {
            int move = ui.getUserInteger(requestToUser);
            if (game.isMoveValid(move, true)) {
                return move;
            }
        }
    }
}
