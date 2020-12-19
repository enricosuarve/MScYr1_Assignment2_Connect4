//todo implement a command line help function with parameter info
package com.simonpreece;

import java.util.Locale;

class Main {
    protected static View view = new ConsoleView();
    UI ui = new UI();

    public static void main(String[] args) {
        int inARow = 0;
        Main m = new Main();
        String helpMsg = "";

        for (String argString : args) {
            if (argString.toLowerCase().startsWith("-n=")) {
                try {
                    inARow = Integer.parseInt(argString.substring(3).trim());
                } catch (Exception e) {
                    //do nothing - just ignore and prompt for a value in the game as normal
                }
            }
            if (argString.equals("-?") || argString.equals("-h") || argString.equals("-help") || argString.equals("--help")) {
                helpMsg += " Usage: Connect4 [options]\n\n";
                helpMsg += "  where options include:\n\n";
                helpMsg += "    -n=<value(3-6)>    Play ConnectN and set the number of counters in a row\n";
                helpMsg += "                  required to win between 3 & 6.\n\n";
                helpMsg += "    -? -h -help --help\n";
                helpMsg += "                  print this help message to the output stream.";
            }
        }

        if (helpMsg.length() > 0) {
            Main.view.Display(helpMsg);
        }
        else if (inARow > 0) {
            new ConnectN(inARow);
            Main.view.Display("\nGoodbye....\n\n");
        }
        else {
            boolean playAgain = true;
            while (playAgain) {
                switch (m.ui.getUserChoice("Pick a game to play", new String[]{"Connect4", "ConnectN", "Exit"})) {
                    case 1:
                        new ConnectFour();
                        break;
                    case 2:
                        new ConnectN();
                        break;
                    case 3:
                        playAgain = false;
                        break;
                }
            }
            Main.view.Display("\nGoodbye....\n\n");
        }

    }
}