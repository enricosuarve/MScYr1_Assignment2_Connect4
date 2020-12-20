package com.snp39;

public class ComputerPlayer extends Player {

    AI ai;

    /**
     * Create a new computer player - prompt for both name and counter
     * Default name = "Computer"
     * Default counter = "C"
     */
    public ComputerPlayer() {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for computer player " + playerNumber, "Computer");
        counter = ui.getUserInput("Enter a counter symbol for computer player " + playerNumber, "C").substring(0, 1);
    }

    /**
     * Create a new computer player - prompt for both name and counter but use provided String as a default name
     * Default counter = "C"
     *
     * @param name - default name to use
     */
    public ComputerPlayer(String name) {
        super();
        UI ui = new UI();
        this.name = ui.getUserInput("Enter a name for computer player " + playerNumber, name);
        counter = ui.getUserInput("Enter a counter symbol for computer player " + playerNumber, "C").substring(0, 1);
    }

    /**
     * Create a new computer player - prompt for both name and counter and use provided Strings as a
     * default name and counter
     *
     * @param name - default name
     * @param counter - default counter
     */
    public ComputerPlayer(String name, String counter) {
        super();
        UI ui = new UI();
        this.name = ui.getUserInput("Enter a name for computer player " + playerNumber, name);
        this.counter = ui.getUserInput("Enter a counter symbol for computer player " + playerNumber, counter).substring(0, 1);
    }

    /**
     * Prompt player for a move
     *
     * @param requestToUser - String to be used in the request to the user
     * @param game - game object for the game in play
     * @return - value denoting column to make a move
     */
    @Override
    public int getMoveFromPlayer(String requestToUser, Game game) {
        Main.view.Display(requestToUser);
        this.ai = game.getAIClass() ;
        return ai.makeMove(game, this);
    }

}