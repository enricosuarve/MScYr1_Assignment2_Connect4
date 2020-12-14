package com.simonpreece;

import java.lang.reflect.InvocationTargetException;

public class ComputerPlayer extends Player {

    AI ai;

    public ComputerPlayer() {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for computer player " + playerNumber, "Computer");
        counter = ui.getUserInput("Enter a counter symbol for computer player " + playerNumber, "C").substring(0, 1);
    }
    public ComputerPlayer(String name) {
        super();
        UI ui = new UI();
        this.name = ui.getUserInput("Enter a name for computer player " + playerNumber, name);
        counter = ui.getUserInput("Enter a counter symbol for computer player " + playerNumber, "C").substring(0, 1);
    }

    public ComputerPlayer(String name, String counter) {
        super();
        UI ui = new UI();
        this.name = ui.getUserInput("Enter a name for computer player " + playerNumber, name);
        this.counter = ui.getUserInput("Enter a counter symbol for computer player " + playerNumber, counter).substring(0, 1);
    }

    @Override
    public int getMoveFromPlayer(String requestToUser, Game game) {
        System.out.println(requestToUser);
        try {
            //todo - can I do this without casting??
            this.ai = (AI) game.getClass().getMethod("getAIClass").invoke(game);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        ai.detectThreats(game, this);
        return ai.makeMove(game);
    }
}
