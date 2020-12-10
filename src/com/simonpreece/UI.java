package com.simonpreece;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UI {
    private BufferedReader input;

    public UI() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getUserInput(String requestToUser) {
        String toReturn = null;
        try {
            toReturn = input.readLine();
        } catch (Exception e) {
            System.out.printf("Exception '%s' occured whilst trying to getUserInput()", e.toString());
        }
        return toReturn;
    }

}
