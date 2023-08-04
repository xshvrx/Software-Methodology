package pieces;

import chess.Board;

/**
 * @author Zaeem Zahid - zz446
 * @author Shiv Patel - sp1957
 */

public class Knight extends Piece {
    public Knight(String color, String name) {
        super(color, name);
        if (color.equals("white")) {
            this.name = "wN";
        } else {
            this.name = "bN";
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
     * A knight moves in an L shape
     * so the difference between the current row and the new row
     * must be 2 and the difference between the current column and the new column
     * must be 1 or vice versa
     */

    @Override
    public boolean isValidMove(Board board, int currentRow, int currentCol, int newRow, int newCol) {
        if (Math.abs(currentRow - newRow) == 2 && Math.abs(currentCol - newCol) == 1) {
            return true;
        } else if (Math.abs(currentRow - newRow) == 1 && Math.abs(currentCol - newCol) == 2) {
            return true;
        } else {
            return false;
        }
    }
}
