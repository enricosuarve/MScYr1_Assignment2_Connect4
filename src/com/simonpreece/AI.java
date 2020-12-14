package com.simonpreece;

import java.lang.Math;

public abstract class AI {
    double intelligencePercent;

    public AI(double intelligencePercent) {
        this.intelligencePercent = intelligencePercent;
    }

    protected abstract int makeMove(Game game); //may need to expand out int to an array for other board games

    protected abstract void respondToThreat(Game game, Player player);

    protected boolean aiSpotsThreatOpportunity() {
        boolean threatSpotted = Math.random() < intelligencePercent;
        System.out.println("AI spotted threat?" + threatSpotted);
        return Math.random() < intelligencePercent;
    }
}
