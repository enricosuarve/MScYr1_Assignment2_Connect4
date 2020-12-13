package com.simonpreece;

//todo make main callable from command line "N will be passed to the code as a command line argument and 2 < N < 7."
class Main {
    UI ui = new UI();

    public static void main(String[] args) {
        int inARow = 0;
        Main m = new Main();

        System.out.println("now with command line arguments");
        for (String argString : args) {
            System.out.println(argString + ":" + argString.substring(0, 2));
            if (argString.substring(0, 2).equals("n=")) {
                try {
                    inARow = Integer.parseInt(argString.substring(2).trim());
                } catch (Exception e) {

                }
            }
        }
        System.out.printf("inARow = %d in main()\n", inARow);
inARow = 3;
        if (inARow > 0) {
            new ConnectN(inARow);
        }
        else {
            switch (m.ui.getUserChoice("Pick a game to play", new String[]{"Connect4", "ConnectN"})) {
                case 1:
                    new MyConnectFour();
                    break;
                case 2:
                    new ConnectN();
                    break;
            }
        }
    }
}