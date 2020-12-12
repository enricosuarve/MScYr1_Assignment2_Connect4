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

    @Override
    public int getMoveFromPlayer() {
        return 0;
    }


    @Override
    public int getMoveFromPlayer(String requestToUser, Game game) {
        System.out.println(requestToUser);
        try {
            //todo - can I do this without casting??
            this.ai = (AI) game.getClass().getMethod("getAIClass").invoke(game);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return ai.makeMove(game);
    }
}
