package com.simonpreece;

class Main {
    UI ui = new UI();

    public static void main(String[] args) {
        int inARow = 0;
        Main m = new Main();

        for (String argString : args) {
            System.out.println(argString + ":" + argString.substring(0, 2));
            if (argString.startsWith("n=")) {
                try {
                    inARow = Integer.parseInt(argString.substring(2).trim());
                } catch (Exception e) {
                    //do nothing - just ignore and prompt for a value in the game as normal
                }
            }
        }
        if (inARow > 0) {
            new ConnectN(inARow);
        }
        else {
            boolean playAgain = true;
            while (playAgain) {
                switch (m.ui.getUserChoice("Pick a game to play", new String[]{"Connect4", "ConnectN","Exit", "Connect5"})) {
                    case 1:
                        new MyConnectFour();
                        break;
                    case 2:
                        new ConnectN();
                        break;
                    case 3:
                        playAgain = false;
                        break;
                    case 4: //todo remove this option before publishing
                        new ConnectN(5);
                        break;
                }
            }
        }
        System.out.println("\nGoodbye....\n\n");
    }
}