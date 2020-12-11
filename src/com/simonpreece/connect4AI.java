package com.simonpreece;


import java.util.Random;

public class connect4AI extends AI {

 Random rand = new Random();

    @Override
    protected int makeMove() {
        return 4;
    }
    //code to do make decisions based on the rules of connect4
}
