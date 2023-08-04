package pieces;

import chess.Board;

/**
 * @author Zaeem Zahid - zz446
 * @author Shiv Patel - sp1957
 */

public class Pawn extends Piece {
    public Pawn(String color, String name) {
        super(color, name);
        if (color.equals("white")) {
            this.name = "wP";
        } else {
            this.name = "bP";
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
     * A pawn moves forward one space
     * if it has not moved yet, it can move forward two spaces
     */
    @Override
    public boolean isValidMove(Board board, int currentRow, int currentCol, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - currentRow);
        int colDiff = Math.abs(newCol - currentCol);
        boolean performingEnPassant = board.performingEnPassant(currentRow, currentCol, newRow, newCol);

        if (rowDiff == 1 && colDiff == 0) {
            return true;
        } else if (rowDiff == 2 && colDiff == 0) {
            if (this.isFirstMove()) {
                return true;
            }
        } else if (rowDiff == 1 && colDiff == 1) {
            if (board.getPiece(newRow, newCol) != null) {
                if (board.getPiece(newRow, newCol).getColor().equals(this.getInverse())) {
                    return true;
                }
            } else if (performingEnPassant) {
                return true;
            }
        }

        return false;
    }
}