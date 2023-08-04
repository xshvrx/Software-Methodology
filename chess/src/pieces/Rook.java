package pieces;

import chess.Board;

/**
 * @author Zaeem Zahid - zz446
 * @author Shiv Patel - sp1957
 */

public class Rook extends Piece {
    public Rook(String color, String name) {
        super(color, name);
        if (color.equals("white")) {
            this.name = "wR";
        } else {
            this.name = "bR";
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
     * A rook moves horizontally or vertically
     * so the current row and the new row must be the same
     * or the current column and the new column must be the same
     */

    @Override
    public boolean isValidMove(Board board, int currentRow, int currentCol, int newRow, int newCol) {
        if (currentRow == newRow || currentCol == newCol) {
            return true;
        } else {
            return false;
        }
    }
}
