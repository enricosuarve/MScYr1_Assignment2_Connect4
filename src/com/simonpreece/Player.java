package com.simonpreece;

//todo check that counter is not already assigned
public abstract class Player {
    protected static int lastPlayerNumber = 0;
    protected int playerNumber;
    String name;
    char counter;
    private int wins = 0;

    public Player(String name, char counter) {
        this.name = name;
        this.counter = counter;
        playerNumber = ++lastPlayerNumber;
    }

    public Player() {
        this.playerNumber = ++lastPlayerNumber;
    }

    public String getName() {
        return name;
    }

    public char getCounter() {
        return counter;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void addWin() {
        wins++;
    }

    public int getWins() {
        return wins;
    }
}
