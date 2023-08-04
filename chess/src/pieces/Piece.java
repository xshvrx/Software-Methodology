package pieces;

import chess.Board;

/**
 * @author Zaeem Zahid - zz446
 * @author Shiv Patel - sp1957
 */

/*
 * Abstract Piece class that all pieces inherit from
 * Contains the color, name, and location of the piece
 * Has abstract methods which implementation will be different for each piece
 */

public abstract class Piece {
    public String color;
    public String name;
    private boolean firstMove = true;
    private int numberOfMoves = 0;

    public Piece(String color, String name) {
        this.color = color;
        this.name = name;
    }

    /**
     * @author Zaeem Zahid
     * @author Shiv Patel
     * @return String
     */
    public String getColor() {
        return color;
    }

    /**
     * @author Zaeem Zahid
     * @author Shiv Patel
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @author Zaeem Zahid
     * @author Shiv Patel
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @author Zaeem Zahid
     * @author Shiv Patel
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @author Zaeem Zahid
     * @author Shiv Patel
     * @return String
     */
    public String getInverse() {
        if (color.equals("white")) {
            return "black";
        } else {
            return "white";
        }
    }

    /**
     * @author Zaeem Zahid
     * @author Shiv Patel
     * @return boolean
     */
    public boolean isFirstMove() {
        return this.firstMove;
    }

    /**
     * @author Zaeem Zahid
     * @author Shiv Patel
     * @param firstMove
     */
    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public int getNumberOfMoves() {
        return this.numberOfMoves;
    }

    public void incrementNumberOfMoves() {
        this.numberOfMoves++;
    }

    public abstract boolean isValidMove(Board board, int currentRow, int currentCol, int newRow, int newCol);

}