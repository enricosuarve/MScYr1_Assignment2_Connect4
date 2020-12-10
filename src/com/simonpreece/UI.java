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
        String toReturn = "";
        try {
            while (toReturn.length() == 0) {
                toReturn = input.readLine().trim();
            }

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

    public boolean getUserYN(String requestToUser) {
        while (true) {
            String result = getUserInput(requestToUser);
            char ynResponse = result.charAt(0);
            switch (ynResponse) {
                case 'y':
                case 'Y':
                    return true;
                case 'n':
                case 'N':
                    return false;
                default:
                    System.out.printf("You answered '%c', please answer (Y)es or (N)o\n", ynResponse);
            }
        }
    }
}
