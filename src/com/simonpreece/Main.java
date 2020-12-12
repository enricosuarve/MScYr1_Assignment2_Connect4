package com.simonpreece;
//todo make main callable from command line "N will be passed to the code as a command line argument and 2 < N < 7."
class Main {
    UI ui = new UI();

    public static void main(String[] args) {
        Main m = new Main();
        switch (m.ui.getUserChoice("Pick a game to play",new String[]{"Connect4", "ConnectN"})) {
            case 1:
                new MyConnectFour();
                break;
            case 2:
                new ConnectN();
                break;
        }
    }
}