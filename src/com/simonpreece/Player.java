package com.simonpreece;

public class Player {
    private static int lastPlayerNumber = 0;
    private final int playerNumber;
    String name;
    char counter;

    public Player(String name, char counter) {
        this.name = name;
        this.counter = counter;
        playerNumber = ++lastPlayerNumber;
    }

    public Player() {
        //todo add check userNumber not already assigned
        this.playerNumber = ++lastPlayerNumber;
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for player " + playerNumber);
        counter = ui.getUserInput("Enter a name for player " + playerNumber).charAt(0);
    }

    public String getName() {
        return name;
    }
    public char getCounter(){
        return counter;
    }
    public int getPlayerNumber(){
        return playerNumber;
    }
}
