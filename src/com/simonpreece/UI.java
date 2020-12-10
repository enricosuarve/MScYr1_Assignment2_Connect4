package com.simonpreece;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UI {
    private BufferedReader input;

    public UI() {
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getUserInput(String requestToUser) {
        System.out.println(requestToUser);
        String toReturn = null;
        try {
            toReturn = input.readLine().trim();
        } catch (Exception e) {
            System.out.printf("Exception '%s' occurred whilst trying to getUserInput()", e.toString());
        }
        return toReturn;
    }

    public int getUserInteger(String requestToUser) {
        String result = getUserInput(requestToUser);
        int returnInteger;
        while (true) {
            try {
                returnInteger = Integer.parseInt(result);
                return returnInteger;
            } catch (Exception e) {
                System.out.println("You do not appear to have entered a valid number - please try again");
                result = getUserInput(requestToUser);
            }
        }
    }
}
