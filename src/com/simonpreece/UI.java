package com.simonpreece;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UI {
    private BufferedReader input;

    public UI() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    }
}
