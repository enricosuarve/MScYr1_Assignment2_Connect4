package com.snp39;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Helper class to request input from players and validate replies
 */
public class UI {
    private final BufferedReader input;

    public UI() {
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Basic request to user to provide a string.
     *
     * @param requestToUser - message to user prompting a reply.
     * @return String from user
     */
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

    /**
     * Basic request to user to provide a string, but provide a default value
     *
     * @param requestToUser - message to user prompting a reply.
     * @param defaultValue  - default value to display
     * @return String from user
     */
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

    /**
     * Request an integer value from to user, validate whether an integer was returned;
     * display an error and prompt again if not.
     *
     * @param requestToUser - message to user prompting a reply.
     * @return integer from user
     */
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

    /**
     * Request an integer value from to user, between two values validate whether a valid integer was returned;
     * display an error and prompt again if not.
     *
     * @param requestToUser - message to user prompting a reply.
     * @param min           - minimum value to be returned
     * @param max           - maximum value to be returned
     * @return integer from user
     */
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

    /**
     * Request a Y/N response from a user, validate whether a valid reply was returned;
     * display an error and prompt again if not.
     * Validates against the first letter in any reply and accepts upper or lowercase
     *
     * @param requestToUser - message to user prompting a reply.
     * @return - boolean true/false y/n response.
     */
    public boolean getUserYN(String requestToUser) {
        while (true) {
            String result = getUserInput(requestToUser).toLowerCase();
            char ynResponse = result.charAt(0);
            switch (ynResponse) {
                case 'y':
                    return true;
                case 'n':
                    return false;
                default:
                    Main.view.Display(String.format("You answered '%c', please answer (Y)es or (N)o\n", ynResponse));
            }
        }
    }

    /**
     * Provide the user a list of choices, generate a number for each and prompt for a integer response,
     * validates if the response id valid (integer within the correct range);
     * display an error and prompt again if not.
     *
     * @param requestToUser - message to user prompting a reply.
     * @param choices       - String array of possible choices
     * @return - integer index value denoting choice made
     */
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
