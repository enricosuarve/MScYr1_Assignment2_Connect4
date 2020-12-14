package com.simonpreece;

public abstract class AI {
   int intelligencePercent;
   protected abstract int makeMove(Game game); //may need to expand out int to an array for other board games
   protected abstract void detectThreats(Game game, Player player);
}
