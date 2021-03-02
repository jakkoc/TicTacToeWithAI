package tictactoe;

import java.util.Scanner;

public class Human implements Player {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prompts user to make a move on the board and checks it's validity
     */
    public void move(Gameboard gameboard) {
        String input;
        TicTacToe.Coordinates coordinates;
        System.out.print("Enter the coordinates: ");

        if(!madeOfDigits(input = scanner.nextLine())) {
            System.out.println("You should enter numbers!");
            move(gameboard);
            return;
        }

        coordinates = TicTacToe.Coordinates.parseCoordinates(input);

        if(!coordinates.isValid()) {
            System.out.println("Coordinates should be from 1 to 3!");
            move(gameboard);
            return;
        }

        if(gameboard.isOccupied(coordinates)) {
            System.out.println("This cell is occupied! Choose another one!");
            move(gameboard);
            return;
        }

        gameboard.setCell(coordinates, gameboard.turnOf());
        gameboard.displayGameboard();
    }

    /**
     * Checks whether input given by parameter is made only of digits
     * @param line - input to be checked
     * @return whether input is made only of digits
     */
    private boolean madeOfDigits(String line) {
        String[] splittedArray = line.split(" ");

        for(String splitted : splittedArray) {
            for(int i = 0; i < splitted.length(); i++) {
                if (splitted.charAt(i) < '0' || splitted.charAt(i) > '9') return false;
            }
        }

        return true;
    }
}
