package com.simonpreece;

//todo make main callable from command line "N will be passed to the code as a command line argument and 2 < N < 7."
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
        System.out.printf("inARow = %d in main()\n", inARow);
        if (inARow > 0) {
            new ConnectN(inARow);
        }
        else {
            boolean playAgain = true;
            while (playAgain) {
                switch (m.ui.getUserChoice("Pick a game to play", new String[]{"Connect4", "ConnectN", "Exit"})) {
                    case 1:
                        new MyConnectFour();
                        break;
                    case 2:
                        new ConnectN();
                        break;
                    case 3:
                        playAgain = false;

                        break;
                }
            }
        }
        System.out.println("\nGoodbye....\n\n");
    }
}