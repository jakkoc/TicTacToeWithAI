package tictactoe;

public class EasyAI implements Player {
    public void move(Gameboard gameboard) {
        System.out.println("Making move level \"easy\"");
        var possibleMoves = gameboard.getPossibleMoves();
        var chosenMove = possibleMoves.get((int)(Math.random() * possibleMoves.size()));
        gameboard.setCell(chosenMove, gameboard.turnOf());
        gameboard.displayGameboard();
    }
}
