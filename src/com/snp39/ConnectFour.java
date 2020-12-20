package com.snp39;

public class ConnectFour extends ConnectX {

    public ConnectFour() {
        boolean playAgain = true;
        setupGame();
        int firstPlayer = 1;
        while (playAgain) {
            playGame(board, firstPlayer);
            playAgain = ui.getUserYN("Play again?");
            firstPlayer = Math.max(++firstPlayer % (players.size()+1),1);
        }
        Player.resetPlayerNumbers();
    }

    @SuppressWarnings("unused")  //It is used by the Main() in testing
    public ConnectFour(int inARow) {
     if (inARow == 4 ){
         new ConnectFour();

     }else{
         new ConnectN(inARow);
     }
    }

    @Override
    protected void setupGame() {
        // ASCII art created using tool at http://www.patorjk.com/software/taag/#p=display&f=Stop&t=o (Colossal font)
        String logo = String.format("\n\n%s .d8888b.   .d88888b.  888b    888 888b    888 8888888888 .d8888b. 88888888888 %s d8888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        logo += String.format("%sd88P  Y88b d88P\" \"Y88b 8888b   888 8888b   888 888       d88P  Y88b    888   %s  d8P888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        logo += String.format("%s888    888 888     888 88888b  888 88888b  888 888       888    888    888  %s  d8P 888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        logo += String.format("%s888        888     888 888Y88b 888 888Y88b 888 8888888   888           888  %s d8P  888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        logo += String.format("%s888        888     888 888 Y88b888 888 Y88b888 888       888           888 %s d88   888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        logo += String.format("%s888    888 888     888 888  Y88888 888  Y88888 888       888    888    888 %s 8888888888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        logo += String.format("%sY88b  d88P Y88b. .d88P 888   Y8888 888   Y8888 888       Y88b  d88P    888       %s 888 %s\n", colours.BoldWhite, colours.Red, colours.BoldWhite);
        logo += String.format("%s \"Y8888P\"   \"Y88888P\"  888    Y888 888    Y888 8888888888 \"Y8888P\"     888       %s 888 %s\n\n", colours.BoldWhite, colours.Red, colours.White);
        Main.view.Display(logo);
        Main.view.Display("Welcome to Connect 4");
        //noinspection SpellCheckingInspection
        Main.view.Display(String.format("There are 2 players %sred%s and %syellow%s\n", colours.Red, colours.White, colours.Yellow, colours.White));
        Main.view.Display(String.format("%sPlayer 1 is Red%s,%s Player 2 is Yellow%s\n", colours.Red, colours.White, colours.Yellow, colours.White));
        Main.view.Display("To play the game type in the number of the column you want to drop your counter in");
        Main.view.Display("A player wins by connecting 4 counters in a row - vertically, horizontally or diagonally");
        Main.view.Display("");
        inARow = 4;
        board = new Board(6, 7);
        players.add(new HumanPlayer((colours.osSupportsAnsiColours ? "@" : "R")));
        if (ui.getUserYN("Do you wish to play against the computer? (Y/N)")) {
            players.add(new ComputerPlayer());
        }
        else {
            players.add(new HumanPlayer((colours.osSupportsAnsiColours ? "@" : "Y")));
        }
        for (Player player : players) {
            if (player.getPlayerNumber() == 1) {
                player.setCounter(colours.Red + player.counter + colours.White);
            }
            else {
                player.setCounter(colours.Yellow + player.counter + colours.White);
            }
        }
    }


}