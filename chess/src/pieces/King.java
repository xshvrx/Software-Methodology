package pieces;

import chess.Board;

/**
 * @author Zaeem Zahid - zz446
 * @author Shiv Patel - sp1957
 */

public class King extends Piece {
    public King(String color, String name) {
        super(color, name);
        if (color.equals("white")) {
            this.name = "wK";
        } else {
            this.name = "bK";
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
     * A king moves one square in any direction
     * so the difference between the current row and the new row
     * must be less than or equal to 1
     * and the difference between the current column and the new column
     * must be less than or equal to 1
     * if the difference is less than or equal to 1, then the king can move to the
     * new position
     */

    @Override
    public boolean isValidMove(Board board, int currentRow, int currentCol, int newRow, int newCol) {
        int diffRow = Math.abs(currentRow - newRow);
        int diffCol = Math.abs(currentCol - newCol);

        if (diffRow <= 1 && diffCol <= 1) {
            if (board.getPiece(newRow, newCol) == null) {
                // Case: No piece present
                return true;
            } else if (board.getPiece(newRow, newCol).getColor().equals(this.getColor())) {
                // Case: User's piece already present
                return false;
            } else {
                // Case: Opponent's piece present
                return true;
            }
        } else if (diffRow == 0 && diffCol == 2 && this.isFirstMove()) {
            if (board.isPathBlocked(currentRow, currentCol, newRow, newCol) == false) {
                Integer rookCol = currentCol < newRow ? 7 : 0;
                Piece rook = board.getPiece(currentRow, rookCol);
                if (rook != null && rook.isFirstMove()) {
                    return true;
                }
            }

        }

        return false;
    }
}