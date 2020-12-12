package com.simonpreece;

import java.util.ArrayList;

class Main {
    UI ui = new UI();

    public static void main(String[] args) {
        Main m = new Main();
        switch (m.ui.getUserChoice(new String[]{"Connect4", "ConnectN"})) {
            case 1:
                new MyConnectFour();
                break;
            case 2:
                new ConnectN();
                break;
        }
    }
}