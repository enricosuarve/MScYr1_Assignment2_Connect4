package com.simonpreece;

public class HumanPlayer extends Player {
    public HumanPlayer() {
        super();
        UI ui = new UI();
        name = ui.getUserInput("Enter a name for player " + playerNumber);
        counter = ui.getUserInput("Enter a counter symbol for player " + playerNumber).charAt(0);
    }

    @Override
    public int getMoveFromPlayer() {
        return 0;
    }

    @Override
    public int getMoveFromPlayer(Board board, Game game) {
        UI ui = new UI();
        int move = ui.getUserInteger(String.format("Player %d - enter a column to drop a counter",playerNumber));
        if(game.isMoveValid(move)){
            //do something
        }
 /*       while (true) {
            if (move > board.getNumCols() || move < 1) {
                System.out.printf("You entered '%d', which is outside the number of columns in the game - please try again\n", move);
            }
            else if (getNextEmptyRow(move) == -1) {
                System.out.printf("Column '%d' is already full - please try again\n", move);
            }
            else {
                return move;
            }
            move = ui.getUserInteger(requestToUser);
        }
*/
    return 1;}
}
