package com.snp39;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UI {
    private final BufferedReader input;

    public UI() {
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getUserInput(String requestToUser) {

        Main.view.Display(requestToUser);
        String toReturn = "";
        try {
            while (toReturn.length() == 0) {
                toReturn = input.readLine().trim();
            }

        } catch (Exception e) {
            Main.view.Display(String.format("Exception '%s' occurred whilst trying to getUserInput()", e.toString()));
        }
        return toReturn;
    }

    public String getUserInput(String requestToUser, String defaultValue) {
        Main.view.Display(String.format(requestToUser + " default=[%s]", defaultValue));
        String toReturn = "";
        try {
            toReturn = input.readLine().trim();
            if (toReturn.length() == 0) {
                toReturn = defaultValue;
            }
        } catch (Exception e) {
            Main.view.Display(String.format("Exception '%s' occurred whilst trying to getUserInput()", e));
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
                Main.view.Display("You do not appear to have entered a valid number - please try again");
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
                    Main.view.Display(String.format("You entered '%d', which is outside the range (%d-%d) - please try again\n", returnInteger, min, max));
                }
            } catch (Exception e) {
                Main.view.Display("You do not appear to have entered a valid number - please try again");
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
                    Main.view.Display(String.format("You answered '%c', please answer (Y)es or (N)o\n", ynResponse));
            }
        }
    }

    public int getUserChoice(String requestToUser, String[] choices) {
        int userResponse;
        int i;
        while (true) {
            i = 1;
            Main.view.Display(String.format("%s\nPlease select from one of the choices below: (1 to %d)\n", requestToUser, choices.length));
            for (String choice : choices) {
                Main.view.Display(String.format("  %d: %s", i, choice));
                i++;
            }
            userResponse = getUserInteger("");
            if (userResponse < 1 || userResponse > choices.length) {
                Main.view.Display(String.format("You answered '%d' which is outside the range above.", userResponse));
            }
            else {
                return userResponse;
            }
        }
    }
}
