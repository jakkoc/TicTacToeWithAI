package tictactoe;

public class MediumAI implements Player {
    @Override
    public void move(Gameboard gameboard) {
        System.out.println("Making move level \"medium\"");
        var moveToMake = findWinningMove(gameboard);

        if(moveToMake != null) {
            gameboard.setCell(moveToMake, gameboard.turnOf());
        }
        else if((moveToMake = preventOpponentVictory(gameboard)) != null) {
            gameboard.setCell(moveToMake, gameboard.turnOf());
        }
        else {
            var possibleMoves = gameboard.getPossibleMoves();
            gameboard.setCell(possibleMoves.get((int)(Math.random() * possibleMoves.size())),gameboard.turnOf());
        }
        gameboard.displayGameboard();
    }


    /**
     * Looks for a winning move from the pool of possible moves
     * @param gameboard current state of the gameboard
     * @return move to make to win or null if there is no such move
     */
    private TicTacToe.Coordinates findWinningMove(Gameboard gameboard) {
        var possibleMoves = gameboard.getPossibleMoves();
        Gameboard clone;
        char character;

        for(var move : possibleMoves) {
            clone = gameboard.clone();
            character = clone.turnOf();
            clone.setCell(move, clone.turnOf());

            if(character == 'X' && clone.getGameState() == Gameboard.GameState.X_WINS || character == 'O' && clone.getGameState() == Gameboard.GameState.O_WINS) {
                return move;
            }
        }

        return null;
    }

    /**
     * Looks for a move that prevents opponent from winning
     * @param gameboard current state of the gameboard
     * @return move to make or null if opponent can't win in next turn
     */
    private TicTacToe.Coordinates preventOpponentVictory(Gameboard gameboard) {
        Gameboard clone;
        char character;

       for(var move : gameboard.getPossibleMoves()) {
           clone = gameboard.clone();
           character = gameboard.turnOf();
           clone.setCell(move, character == 'X' ? 'O' : 'X');

           if(character == 'X' && clone.getGameState() == Gameboard.GameState.O_WINS || character == 'O' && clone.getGameState() == Gameboard.GameState.X_WINS) {
               return move;
           }
       }



        return null;
    }
}
