package com.simonpreece;

import java.lang.Math;

public abstract class AI {
   double intelligencePercent;
   protected abstract int makeMove(Game game); //may need to expand out int to an array for other board games
   protected abstract void respondToThreat(Game game, Player player);

   public AI(double intelligencePercent){
      this.intelligencePercent = intelligencePercent;
   }

   protected boolean aiSpotsThreatOpportunity(){
      return Math.random() < intelligencePercent;
   }
}
