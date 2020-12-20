package com.snp39;

import java.lang.Math;

/**
 * Abstract class for a simple game 'Artificial Intelligence'
 */
public abstract class AI {
    protected final boolean debugMode = false; //used during development to check the logic behind the AI decisioning - should be false in all production versions
    double intelligencePercent;

    public AI(double intelligencePercent) {
        this.intelligencePercent = intelligencePercent;
    }

    protected abstract int makeMove(Game game, Player player); //may need to expand out int to an array for other board games

    protected abstract int respondToThreat(Game game, Player player);

    protected boolean aiSpotsThreatOpportunity() {
        boolean threatSpotted = Math.random() < intelligencePercent;
        if (debugMode) Main.view.Display("AI spotted line?" + threatSpotted);
        return threatSpotted;
    }

    protected abstract int respondToOpportunities(Game game, Player player);
}
