package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pieces.*;

/**
 * @author Zaeem Zahid - zz446
 * @author Shiv Patel - sp1957
 */

/*
 * Main class that runs the game
 * Contains the main method
 */
public class Chess {
    public static Board chessBoard;

    /**
     * Main method
     * 
     * @author Zaeem Zahid
     * @author Shiv Patel
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Board board = new Board();
        board.setup();
        board.printBoard();

        Boolean isWhiteTurn = true;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println();

            System.out.print(isWhiteTurn ? "White's move: " : "Black's move: ");
            String input = reader.readLine().trim().toLowerCase();

            // length below 5 is invalid input
            if (input.length() < 5) {
                System.out.println("Illegal move, try again");
                continue;
            } else if (input.equals("resign")) {
                System.out.println(isWhiteTurn ? "Black wins" : "White wins");
                break;
            } else if (input.substring(input.length() - 5).equals("draw?")) {
                System.out.println("draw");
                break;
            } else {
                // Parse input of FileRank FileRank format
                String[] inputArray2 = input.split(" ");
                String[] from = inputArray2[0].split("");
                String[] to = inputArray2[1].split("");

                int fromRow = 8 - Integer.parseInt(from[1]);
                int fromCol = from[0].charAt(0) - 'a';

                int toRow = 8 - Integer.parseInt(to[1]);
                int toCol = to[0].charAt(0) - 'a';

                // Check if the piece is of the correct color
                Piece piece = board.getPiece(fromRow, fromCol);
                Piece targetPiece = board.getPiece(toRow, toCol);

                // Check if the piece is present
                if (!board.isPiecePresent(fromRow, fromCol)) {
                    System.out.println("Illegal move, try again");
                    continue;
                }

                // if the target box is same as the current box, then you can't move the piece
                // there
                if (fromRow == toRow && fromCol == toCol) {
                    System.out.println("Illegal move, try again");
                    continue;
                }

                // Check if the target box is valid
                if (!isValidInput(fromRow, fromCol, toRow, toCol)) {
                    System.out.println("Illegal move, try again");
                    continue;
                }

                // if there is a piece on target box and if it is same color as the piece on
                // current box, then you can't move the piece there
                if (board.isPiecePresent(toRow, toCol)
                        && targetPiece.getColor().equals(piece.getColor())
                        && !piece.getName().endsWith("K")) {
                    System.out.println("Illegal move, try again");
                    continue;
                }

                if (isWhiteTurn ? !piece.getColor().equals("white") : !piece.getColor().equals("black")) {
                    System.out.println("Illegal move, try again");
                    continue;
                }

                // Check if path is blocked
                if (board.isPathBlocked(fromRow, fromCol, toRow, toCol)) {
                    System.out.println("Illegal move, try again");
                    continue;
                }

                // Check if the move is valid
                System.out.println("From " + fromRow + " " + fromCol + " to " + toRow + " " + toCol + "");
                if (!piece.isValidMove(board, fromRow, fromCol, toRow, toCol)) {
                    System.out.println("Illegal move, try again");
                    continue;
                }

                if (board.isCastling(fromRow, fromCol, toRow, toCol)) {
                    board.castle(fromRow, fromCol, toRow, toCol);
                } else if (board.performingEnPassant(fromRow, fromCol, toRow, toCol)) {
                    board.enPassant(fromRow, fromCol, toRow, toCol);
                } else {
                    board.movePiece(fromRow, fromCol, toRow, toCol);
                }
                
                String turn = isWhiteTurn ? "white" : "black";
                String opColor = !isWhiteTurn ? "white" : "black";
                if (board.isCheckmate(opColor)){
                    System.out.println("Checkmate : " + turn +"s wins!");
                    break;
                }
                if (board.isInCheck(turn)){
                    System.out.println("Illegal move, try again");
                    board.movePiece(toRow,toCol,fromRow,fromCol);
                    continue;
                }

                if (board.getPiece(toRow, toCol) instanceof Pawn && (toRow == 0 || toRow == 7)) {
                    String promote = input.substring(input.length() - 1).toUpperCase();
                    if (!promote.equals("Q") && !promote.equals("R") && !promote.equals("N") && !promote.equals("B")) {
                        promote = "Q";
                    }

                    switch (promote) {
                        case "Q":
                            board.setPiece(toRow, toCol, new Queen(piece.getColor(), piece.getName()));
                            break;
                        case "R":
                            board.setPiece(toRow, toCol, new Rook(piece.getColor(), piece.getName()));
                            break;
                        case "N":
                            board.setPiece(toRow, toCol, new Knight(piece.getColor(), piece.getName()));
                            break;
                        case "B":
                            board.setPiece(toRow, toCol, new Bishop(piece.getColor(), piece.getName()));
                            break;
                    }
                }
            }

            System.out.println();

            board.printBoard();
            isWhiteTurn = !isWhiteTurn;

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
    public static boolean isValidInput(int currentRow, int currentCol, int newRow, int newCol) {
        return (currentRow >= 0 && currentRow <= 7) && (currentCol >= 0 && currentCol <= 7)
                && (newRow >= 0 && newRow <= 7) && (newCol >= 0 && newCol <= 7);

    }
}
