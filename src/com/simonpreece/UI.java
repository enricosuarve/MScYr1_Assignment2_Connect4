package com.simonpreece;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UI {
    private final BufferedReader input;

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

    public String getUserInput(String requestToUser, String defaultValue) {
        System.out.printf(requestToUser + " default=[%s]", defaultValue);
        String toReturn = "";
        try {
            toReturn = input.readLine().trim();
            if (toReturn.length() == 0) {
                toReturn = defaultValue;
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

    public int getUserInteger(String requestToUser, int min, int max) {
        String result;
        int returnInteger;
        while (true) {
            result = getUserInput(requestToUser);
            try {
                returnInteger = Integer.parseInt(result);
                if (returnInteger >= min && returnInteger <= max) {
                    return returnInteger;
                }
                else {
                    System.out.printf("You entered '%d', which is outside the range (%d-%d) - please try again\n", returnInteger, min, max);
                }
            } catch (Exception e) {
                System.out.println("You do not appear to have entered a valid number - please try again");
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

    public int getUserChoice(String requestToUser, String[] choices) {
        int userResponse;
        int i;
        while (true) {
            i = 1;
            System.out.printf("%s\nPlease select from one of the choices below: (1 to %d)\n", requestToUser, choices.length);
            for (String choice : choices) {
                System.out.printf("  %d: %s\n", i, choice);
                i++;
            }
            userResponse = getUserInteger("");
            if (userResponse < 1 || userResponse > choices.length) {
                System.out.printf("You answered '%d' which is outside the range above.", userResponse);
            }
            else {
                return userResponse;
            }
        }
    }
}
