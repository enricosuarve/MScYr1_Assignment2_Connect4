package com.snp39;

public class HumanPlayer extends Player {

    /**
     * Create a new human player - prompt for both name and counter.
     *
     * Default counter = First letter of any name given.
     */
    public HumanPlayer() {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for player " + playerNumber);
        counter = ui.getUserInput("Enter a counter symbol for player " + playerNumber, "@").substring(0, 1);
    }

    /**
     * Create a new human player - prompt for both name and counter,
     * but use the first letter in the String given as a default.
     *
     * @param counter - Default counter
     */

    public HumanPlayer(String counter) {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for player " + playerNumber);
        this.counter = ui.getUserInput("Enter a counter symbol for player " + playerNumber, counter).substring(0, 1);
    }

    /**
     * Make a request to a human player to make a move
     *
     * @param requestToUser - String prompt to user
     * @param game          - game being played
     * @return - column to place a counter.
     */
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
