package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class TicTacToe {
    private static final Scanner scanner = new Scanner(System.in);
    private final Gameboard board;
    private Player firstPlayer;
    private Player secondPlayer;

    private TicTacToe(Character[][] board) {
        this.board = new Gameboard(board);
    }

    public void play() {
       System.out.print("Input command: ");
       var command = scanner.nextLine();
       while(!arevalidGameSettings(command)) {
           if(command.equals("exit")) return;
           System.out.println("Bad parameters!");
           System.out.print("Input command: ");
           command = scanner.nextLine();
       }

       setPlayers(Level.valueOf(command.split(" ")[1].toUpperCase()),Level.valueOf(command.split(" ")[2].toUpperCase()));
       displayBoard();

       while(board.getGameState() == Gameboard.GameState.GAME_NOT_FINISHED) {
           firstPlayer.move(board);
           board.determineGameState();
           if(board.getGameState() == Gameboard.GameState.GAME_NOT_FINISHED) {
               secondPlayer.move(board);
               board.determineGameState();
           }
       }

       displayGameStatus();
       board.clearBoard();
       play();
    }

    private void setPlayers(Level firstPlayer, Level secondPlayer) {
        switch(firstPlayer) {
            case EASY:
                this.firstPlayer = new EasyAI();
                break;
            case MEDIUM:
                this.firstPlayer = new MediumAI();
                break;
            case HARD:
                this.firstPlayer = new HardAI();
                break;
            case USER:
                this.firstPlayer = new Human();
                break;
            default:
                throw new IllegalArgumentException("NOT IMPLEMENTED YET");
        }

        switch(secondPlayer) {
            case EASY:
                this.secondPlayer = new EasyAI();
                break;
            case MEDIUM:
                this.secondPlayer = new MediumAI();
                break;
            case HARD:
                this.secondPlayer = new HardAI();
                break;
            case USER:
                this.secondPlayer = new Human();
                break;
            default:
                throw new IllegalArgumentException("NOT IMPLEMENTED YET");
        }
    }

    private boolean arevalidGameSettings(String settings) {
        var splitted = settings.split(" ");
        return splitted.length == 3 && splitted[0].equals("start") && isValidPlayerSetting(splitted[1]) && isValidPlayerSetting(splitted[2]);
    }

    private boolean isValidPlayerSetting(String playerSetting) {
        return playerSetting.equals("user") || playerSetting.equals("easy") || playerSetting.equals("medium") || playerSetting.equals("hard");
    }


    /**
     * Enter the game starting status from keyboard
     * @return instance of initialized game
     */
    public static TicTacToe fromKeyboard() {
        System.out.print("Enter the cells: ");
        Character[][] board = parseBoardFromString(scanner.nextLine());

        return new TicTacToe(board);
    }

    /**
     * Creates empty board to start a game
     * @return empty gameboard
     */
    public static TicTacToe emptyBoard() {
        return new TicTacToe(parseBoardFromString("_________"));
    }

    public Gameboard getBoard() {
        return board;
    }

    private static Character[][] parseBoardFromString(String initialState) {
        if(initialState.length() != 9) throw new IllegalArgumentException(initialState);

        Character[][] board = new Character[3][3];

        for(int i = 0; i < 3 ; i++) {
            for(int j = 0; j < 3; j++) {
                board[i][j] = initialState.charAt(3 * i + j) == '_' ? ' ' : initialState.charAt(3 * i + j);
            }
        }

        return board;
    }

    /**
     * Displays current state of the board in standard input
     */
    public void displayBoard() {
        board.displayGameboard();
    }

    public void displayGameStatus() {
        board.determineGameState();
        System.out.println(board.getGameState());
    }

    /**
     * Supportive class representing cell coordinates
     */
    static class Coordinates {
        private final int row;
        private final int column;

        Coordinates(int row, int column) {
            this.row = row;
            this.column = column;
        }

        static Coordinates parseCoordinates(String input) {
            String[] splittedArray = input.split(" ");

            if(splittedArray.length != 2) throw new IllegalArgumentException(input);

            var row = Integer.parseInt(splittedArray[0]);
            var column = Integer.parseInt(splittedArray[1]);

            return new Coordinates(row, column);
        }

        boolean isValid() {
            return row > 0 && row < 4 && column > 0 && column < 4;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }
    }

    enum Level {
        EASY,
        MEDIUM,
        HARD,
        USER
    }

}

class Gameboard implements Cloneable {
    private final Character[][] board;
    private GameState gameState;

    public Gameboard(Character[][] initialState) {
        board = initialState;
        gameState = GameState.GAME_NOT_FINISHED;
    }

    public void displayGameboard() {
        displayDottedLine();
        for(var row : board) {
            System.out.print("| ");
            for(var character : row) {
                if(character.equals('_')) System.out.print("  ");
                else System.out.print(character + " ");
            }
            System.out.println("|");
        }
        displayDottedLine();
    }

    private void displayDottedLine() {
        System.out.println("---------");
    }

    public void clearBoard() {
        for (Character[] characters : board) {
            Arrays.fill(characters, ' ');
        }
    }

    public void setCell(TicTacToe.Coordinates coordinates, char value) {
        board[coordinates.getRow() - 1][coordinates.getColumn() - 1] = value;
        determineGameState();
    }

    public Character getCell(TicTacToe.Coordinates coordinates) {
        return board[coordinates.getRow() - 1][coordinates.getColumn() - 1];
    }

    public Character[][] getBoard() {
        return board;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public Gameboard clone() {
        Character[][] clonedBoard = new Character[board.length][];

        for(int i = 0; i < board.length; i++) {
            clonedBoard[i] = new Character[board[i].length];

            System.arraycopy(board[i], 0, clonedBoard[i], 0, board[i].length);
        }

        return new Gameboard(clonedBoard);
    }

    public ArrayList<TicTacToe.Coordinates> getPossibleMoves() {
        var possibleMoves = new ArrayList<TicTacToe.Coordinates>();

        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j].equals(' ')) possibleMoves.add(new TicTacToe.Coordinates(i + 1, j + 1));
            }
        }

        return possibleMoves;
    }

    public boolean isOccupied(TicTacToe.Coordinates coordinates) {
        return !board[coordinates.getRow() - 1][coordinates.getColumn() - 1].equals(' ');
    }

    /**
     * Counts number of occurrences of specific character in the board
     * @param character - character, occurrences of which are to be counted
     * @return number of occurrences of given character in the board
     */
    public int countCharacter(char character) {
        int result = 0;

        for(var row : board) {
            for(var cell : row) {
                if(cell.equals(character)) result++;
            }
        }

        return result;
    }

    public char turnOf() {
        return countCharacter('X') - countCharacter('O') < 1 ? 'X' : 'O';
    }

    /**
     * Determines state of the game depending on the board status
     */
    public void determineGameState() {
        checkRows(board);
        checkColumns();
        checkDiagonals();
        if(gameState == GameState.GAME_NOT_FINISHED && countCharacter('X') + countCharacter('O') == 9) {
            gameState = GameState.DRAW;
        }
    }

    private void checkRows(Character[][] board) {
        for(var row : board) {
            if (Arrays.stream(row).allMatch(cell -> cell.equals('X'))) {
                gameState = GameState.X_WINS;
                return;
            }
            else if(Arrays.stream(row).allMatch(cell -> cell.equals('O'))) {
                gameState = GameState.O_WINS;
                return;
            }
        }
    }

    private void checkColumns() {
        checkRows(rotateBoard());
    }

    private void checkDiagonals() {
        if(board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            if(board[0][0].equals('X')) {
                gameState = GameState.X_WINS;
            }
            else if(board[0][0].equals('O')) {
                gameState = GameState.O_WINS;
            }
        }
        else if(board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            if(board[1][1].equals('X')) {
                gameState = GameState.X_WINS;
            }
            else if(board[1][1].equals('O')) {
                gameState = GameState.O_WINS;
            }
        }
    }

    private Character[][] rotateBoard() {
        var rotatedBoard = new Character[3][3];

        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                rotatedBoard[i][j] = board[j][i];
            }
        }

        return rotatedBoard;
    }

    /**
     * Enum representing possible states of the game
     */
    enum GameState {
        GAME_NOT_FINISHED,
        DRAW,
        X_WINS,
        O_WINS;

        @Override
        public String toString() {
            String[] splitted = name().split("_");

            StringBuilder result = new StringBuilder();

            result.append(splitted[0].charAt(0)).append(splitted[0].substring(1).toLowerCase(Locale.ROOT)).append(" ");

            for(int i = 1; i < splitted.length; i++) {
                result.append(splitted[i].toLowerCase(Locale.ROOT)).append(" ");
            }

            return result.toString();
        }
    }
}