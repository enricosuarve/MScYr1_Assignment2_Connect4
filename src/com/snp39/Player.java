package com.snp39;

public abstract class Player {
    protected static int lastPlayerNumber = 0;
    protected int playerNumber;
    protected String name;
    String counter;
    private int wins = 0;

    public Player(String name, String counter) {
        this.name = name;
        this.counter = counter;
        playerNumber = ++lastPlayerNumber;
    }

    public Player() {
        this.playerNumber = ++lastPlayerNumber;
    }

    public static void resetPlayerNumbers() {
        lastPlayerNumber = 0;
    }

    public String getName() {
        return name;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
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

    public abstract int getMoveFromPlayer(String format, Game game);
}
