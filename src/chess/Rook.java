package chess;

import java.util.ArrayList;

public class Rook extends ChessPiece {

    private boolean hasMoved;

    public Rook(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        super(pieceType, pieceFile, pieceRank);
        this.hasMoved = false;
    }


    ReturnPlay returnPlay = Chess.returnPlay;
    ArrayList<ReturnPiece> pieces = returnPlay.piecesOnBoard;

    public static ReturnPiece getPieceAtSquare(ReturnPiece.PieceFile file, int rank,
            ArrayList<ReturnPiece> piecesOnBoard) {

        for (ReturnPiece piece : piecesOnBoard) {
            if (piece.pieceFile == file && piece.pieceRank == rank) {
                return piece;
            }
        }
        // If no piece is found at the specified square, return null
        return null;
    }


    @Override
    public boolean isValidMove(String move) {
        char sourceFileChar = move.charAt(0);
        int sourceRank = Character.getNumericValue(move.charAt(1));
        char destFileChar = move.charAt(3);
        int destRank = Character.getNumericValue(move.charAt(4));
    
        // Convert char file to ReturnPiece.PieceFile enum
        ReturnPiece.PieceFile sourceFile = ReturnPiece.PieceFile.valueOf(String.valueOf(sourceFileChar));
        ReturnPiece.PieceFile destFile = ReturnPiece.PieceFile.valueOf(String.valueOf(destFileChar));
    
        // Check if the move is vertical or horizontal
        boolean isVertical = sourceFile == destFile;
        boolean isHorizontal = sourceRank == destRank;
    
        if (!isVertical && !isHorizontal) {
            return false; // If the move is neither vertical nor horizontal, it's invalid for a rook
        }
    
        // Check if all squares on the path are unoccupied, excluding the destination square
        if (isVertical) {
            int start = Math.min(sourceRank, destRank) + 1;
            int end = Math.max(sourceRank, destRank);
            for (int rank = start; rank < end; rank++) {
                if (getPieceAtSquare(sourceFile, rank, pieces) != null) return false;
            }
        } else { // Horizontal
            int start = Math.min(sourceFile.ordinal(), destFile.ordinal()) + 1;
            int end = Math.max(sourceFile.ordinal(), destFile.ordinal());
            for (int i = start; i < end; i++) {
                if (getPieceAtSquare(ReturnPiece.PieceFile.values()[i], sourceRank, pieces) != null) return false;
            }
        }
    
        // If all checks pass, mark the rook as having moved and return true
        this.hasMoved = true;
        return true;
    }
    

    // Getter and setter for hasMoved field
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
