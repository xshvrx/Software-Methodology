package pieces;

import chess.Board;

/**
 * @author Zaeem Zahid - zz446
 * @author Shiv Patel - sp1957
 */

public class Queen extends Piece {
    public Queen(String color, String name) {
        super(color, name);
        if (color.equals("white")) {
            this.name = "wQ";
        } else {
            this.name = "bQ";
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
     * A queen moves diagonally, horizontally, or vertically
     * so the difference between the current row and the new row
     * must be equal to the difference between the current column and the new column
     * or the current row and the new row must be the same
     * or the current column and the new column must be the same
     * if any of these conditions are true, then the queen can move to the new
     * position
     */

    @Override
    public boolean isValidMove(Board board, int currentRow, int currentCol, int newRow, int newCol) {
        if (Math.abs(currentCol - newCol) == Math.abs(currentRow - newRow)) {
            return true;
        } else if (currentRow == newRow || currentCol == newCol) {
            return true;
        } else {
            return false;
        }
    }
}
