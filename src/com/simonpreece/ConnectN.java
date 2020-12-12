package com.simonpreece;

public class ConnectN extends MyConnectFour{
    public ConnectN() {
        boolean playAgain = true;
        setupGame();
        while (playAgain) {
            playGame(board);
            playAgain = super.ui.getUserYN("Play again?");
            //todo - swap player order??
        }
        System.out.println("\nGoodbye...\n");
    }
}
