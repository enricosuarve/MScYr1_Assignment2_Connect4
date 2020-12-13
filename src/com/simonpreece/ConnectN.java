package com.simonpreece;

public class ConnectN extends MyConnectFour {
    public ConnectN() {
        boolean playAgain = true;
        setupGame();
        while (playAgain) {
            playGame(board);
            playAgain = super.ui.getUserYN("Play again?");
            //todo - swap player order??
        }
        System.out.println("\nGoodbye...\n");
    }

    public ConnectN(int inARow) {
        //todo below does not execute as calls the superclass constructor first which calls playgame
//        this.inARow = inARow;
//        System.out.printf("this.inARow (%d) = inARow (%d)\n",this.inARow, inARow);
        System.out.printf("inARow = (%d) in ConnectN constructor\n", inARow);

        boolean playAgain = true;
        setupGame();
        while (playAgain) {
            playGame(board);
            playAgain = super.ui.getUserYN("Play again?");
            //todo - swap player order??
        }
        System.out.println("\nGoodbye...\n");
    }

    @Override
    protected void setupGame() {
        // ASCII art created using tool at http://www.patorjk.com/software/taag/#p=display&f=Stop&t=o (Colossal font)
        System.out.println("\033[0;97m .d8888b.   .d88888b.  888b    888 888b    888 8888888888 .d8888b. 88888888888 \033[0;34m888b    888\033[0;97m");
        System.out.println("d88P  Y88b d88P\" \"Y88b 8888b   888 8888b   888 888       d88P  Y88b    888     \033[0;34m8888b   888\033[0;97m");
        System.out.println("888    888 888     888 88888b  888 88888b  888 888       888    888    888     \033[0;34m88888b  888\033[0;97m");
        System.out.println("888        888     888 888Y88b 888 888Y88b 888 8888888   888           888     \033[0;34m888Y88b 888\033[0;97m");
        System.out.println("888        888     888 888 Y88b888 888 Y88b888 888       888           888     \033[0;34m888 Y88b888\033[0;97m");
        System.out.println("888    888 888     888 888  Y88888 888  Y88888 888       888    888    888     \033[0;34m888  Y88888\033[0;97m");
        System.out.println("Y88b  d88P Y88b. .d88P 888   Y8888 888   Y8888 888       Y88b  d88P    888     \033[0;34m888   Y8888\033[0;97m");
        System.out.println(" \"Y8888P\"   \"Y88888P\"  888    Y888 888    Y888 8888888888 \"Y8888P\"     888     \033[0;34m888    Y888\033[0;57m");
        System.out.println();
        System.out.println("Welcome to Connect N");
        System.out.println("Play against 2 Computer Players");
        System.out.println("Player 1 (you) is Red, Player 2 is Yellow, Player 3 is Blue\n");
        System.out.println("To play the game type in the number of the column you want to drop you counter in");
        System.out.println("A player wins by connecting (n) counters in a row - vertically, horizontally or diagonally");
        System.out.println();
        if (inARow == 0) {
            inARow = ui.getUserInteger("How many counters in a row to win? (3-6)", 3, 6);
        }
        else {
            System.out.printf("%d in a row specified in command line\n", inARow);
        }
        board = new Board(6, 7);
        players.add(new HumanPlayer());
        players.add(new ComputerPlayer("Computer 1"));
        players.add(new ComputerPlayer("Computer 2"));
        for (Player player : players) {
            switch (player.getPlayerNumber()) {
                case 1:
                    player.setCounter("\033[0;31m" + player.counter + "\033[0;57m");
                    break;
                case 2:
                    player.setCounter("\033[0;33m" + player.counter + "\033[0;57m");
                    break;
                case 3:
                    player.setCounter("\033[0;34m" + player.counter + "\033[0;57m");
                    break;

            }
        }
    }

}
