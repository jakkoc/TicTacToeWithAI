package tictactoe;

public class HardAI implements Player {
    @Override
    public void move(Gameboard gameboard) {
        System.out.println("Making move level \"hard\"");
        gameboard.setCell(chooseBestMove(gameboard), gameboard.turnOf());
        gameboard.displayGameboard();
    }


    private TicTacToe.Coordinates chooseBestMove(Gameboard gameboard) {
        Gameboard futureGameboard;
        var possibleMoves = gameboard.getPossibleMoves();
        int bestResult;
        int currentResult;
        TicTacToe.Coordinates bestMove = null;


        if(gameboard.turnOf() == 'X') {
            bestResult = -2;

            for(var move : possibleMoves) {
                futureGameboard = gameboard.clone();
                futureGameboard.setCell(move, 'X');
                currentResult = minimax(futureGameboard, false);

                if(currentResult > bestResult) {
                    bestMove = move;
                    bestResult = currentResult;
                }
            }
        }
        else {
            bestResult = 2;

            for(var move : possibleMoves) {
                futureGameboard = gameboard.clone();
                futureGameboard.setCell(move, 'O');
                currentResult = minimax(futureGameboard, true);

                if(currentResult < bestResult) {
                    bestMove= move;
                    bestResult = currentResult;
                }
            }
        }

        return bestMove;
    }

    private int minimax(Gameboard gameboard, boolean maximizingPlayer) {
        if(gameboard.getGameState() != Gameboard.GameState.GAME_NOT_FINISHED) {
            return gameValue(gameboard);
        }

        Gameboard futureGameboard;
        var possibleMoves = gameboard.getPossibleMoves();
        int bestResult = -2;

        if(maximizingPlayer) {
            for(var move : possibleMoves) {
                futureGameboard = gameboard.clone();
                futureGameboard.setCell(move, 'X');
                bestResult = Math.max(bestResult, minimax(futureGameboard, false));
            }
        }
        else {
            bestResult = 2;
            for(var move: possibleMoves) {
                futureGameboard = gameboard.clone();
                futureGameboard.setCell(move,'O');
                bestResult = Math.min(bestResult, minimax(futureGameboard, true));
            }
        }

        return bestResult;
    }

    /**
     * This method is to be called only on a board that has a finished game
     * @param gameboard game state to be evaluated
     * @return heuristic value of the move
     */
    private int gameValue(Gameboard gameboard) {
        if(gameboard.getGameState() == Gameboard.GameState.X_WINS) return 1;
        else if(gameboard.getGameState() == Gameboard.GameState.DRAW) return 0;
        else return - 1;
    }
}
