package pieces;

import chess.Board;

/**
 * @author Zaeem Zahid - zz446
 * @author Shiv Patel - sp1957
 */

public class Bishop extends Piece {
    public Bishop(String color, String name) {
        super(color, name);
        if (color.equals("white")) {
            this.name = "wB";
        } else {
            this.name = "bB";
        }
    }

    /**
     * @author Zaeem Zahid
     * @author Shiv Patel
     * @param currentRow
     * @param currentCol
     * @param newRow
     * @param newCol
     * @return boolean
     */

    /*
     * A bishop moves diagonally
     * so the difference between the current row and the new row
     * must be equal to the difference between the current column and the new column
     * if the difference is equal, then the bishop can move to the new position
     */

    @Override
    public boolean isValidMove(Board board, int currentRow, int currentCol, int newRow, int newCol) {
        if (Math.abs(currentCol - newCol) == Math.abs(currentRow - newRow)) {
            return true;
        }
        return false;
    }

}
