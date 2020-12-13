package com.simonpreece;

public class ConnectN extends MyConnectFour {
    public ConnectN() {
//        boolean playAgain = true;
//        setupGame();
//        while (playAgain) {
//            playGame(board);
//            playAgain = super.ui.getUserYN("Play again?");
//            //todo - swap player order??
//        }
    }

    public ConnectN(int inARow) {
        super(inARow);
    }

    @Override
    protected void setupGame() {
        // ASCII art created using tool at http://www.patorjk.com/software/taag/#p=display&f=Stop&t=o (Colossal font)
        System.out.printf("\n\n%s .d8888b.   .d88888b.  888b    888 888b    888 8888888888 .d8888b. 88888888888 %s888b    888%s\n", colours.BoldWhite, colours.Blue, colours.BoldWhite);
        System.out.printf("%sd88P  Y88b d88P\" \"Y88b 8888b   888 8888b   888 888       d88P  Y88b    888     %s8888b   888%s\n", colours.BoldWhite, colours.Blue, colours.BoldWhite);
        System.out.printf("%s888    888 888     888 88888b  888 88888b  888 888       888    888    888     %s88888b  888%s\n", colours.BoldWhite, colours.Blue, colours.BoldWhite);
        System.out.printf("%s888        888     888 888Y88b 888 888Y88b 888 8888888   888           888     %s888Y88b 888%s\n", colours.BoldWhite, colours.Blue, colours.BoldWhite);
        System.out.printf("%s888        888     888 888 Y88b888 888 Y88b888 888       888           888     %s888 Y88b888%s\n", colours.BoldWhite, colours.Blue, colours.BoldWhite);
        System.out.printf("%s888    888 888     888 888  Y88888 888  Y88888 888       888    888    888     %s888  Y88888%s\n", colours.BoldWhite, colours.Blue, colours.BoldWhite);
        System.out.printf("%sY88b  d88P Y88b. .d88P 888   Y8888 888   Y8888 888       Y88b  d88P    888     %s888   Y8888%s\n", colours.BoldWhite, colours.Blue, colours.BoldWhite);
        System.out.printf("%s \"Y8888P\"   \"Y88888P\"  888    Y888 888    Y888 8888888888 \"Y8888P\"     888     %s888    Y888%s\n", colours.BoldWhite, colours.Blue, colours.White);
        System.out.println();
        System.out.println("Welcome to Connect N");
        System.out.println("Play against 2 Computer Players");
        System.out.printf("%sPlayer 1 (you) is Red%s, %sPlayer 2 is Yellow%s, %sPlayer 3 is Blue%s\n", colours.Red, colours.White, colours.Yellow, colours.White, colours.Blue, colours.White);
        System.out.println("To play the game type in the number of the column you want to drop your counter in");
        System.out.println("A player wins by connecting (n) counters in a row - vertically, horizontally or diagonally");
        if (inARow >= 3 && inARow <= 6) {
            System.out.printf("You specified %d in a row to win in the command line\n", inARow);
        }
        else {
            if (inARow != 0) {
                System.out.printf("\nYou specified %d in a row to win in the command line, however this is outside the min/max for this game", inARow);
            }
            inARow = ui.getUserInteger("\nHow many counters in a row to win? (3-6)\n", 3, 6);
        }
        System.out.println();
        board = new Board(6, 7);
        players.add(new HumanPlayer((colours.osSupportsAnsiColours ? "@" : "R")));
        players.add(new ComputerPlayer("Computer 1", (colours.osSupportsAnsiColours ? "@" : "Y")));
        players.add(new ComputerPlayer("Computer 2", (colours.osSupportsAnsiColours ? "@" : "B")));
        for (Player player : players) {
            switch (player.getPlayerNumber()) {
                case 1:
                    player.setCounter(colours.Red + player.counter + colours.White);
                    break;
                case 2:
                    player.setCounter(colours.Yellow + player.counter + colours.White);
                    break;
                case 3:
                    player.setCounter(colours.Blue + player.counter + colours.White);
                    break;

            }
        }
    }

}
