package com.snp39;

import java.lang.Math;

public abstract class AI {
    double intelligencePercent;
    protected final boolean debugMode = false;

    public AI(double intelligencePercent) {
        this.intelligencePercent = intelligencePercent;
    }

    protected abstract int makeMove(Game game, Player player); //may need to expand out int to an array for other board games

    protected abstract int respondToThreat(Game game, Player player);

    protected boolean aiSpotsThreatOpportunity() {
        boolean threatSpotted = Math.random() < intelligencePercent;
        if(debugMode)Main.view.Display("AI spotted line?" + threatSpotted);
        return threatSpotted; // Math.random() < intelligencePercent;
    }

    protected abstract int respondToOpportunities(Game game, Player player);
}
