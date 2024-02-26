package chess;

import java.util.ArrayList;

public class Bishop extends ChessPiece {

    public Bishop(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        super(pieceType, pieceFile, pieceRank);
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

        ReturnPiece.PieceFile sourceFile = ReturnPiece.PieceFile.valueOf(String.valueOf(sourceFileChar));
        ReturnPiece.PieceFile destFile = ReturnPiece.PieceFile.valueOf(String.valueOf(destFileChar));

    
        // Check if the move is diagonal
        boolean isDiagonal = Math.abs(destFile.ordinal() - sourceFile.ordinal()) == Math.abs(destRank - sourceRank);
        if (!isDiagonal) {
            return false; 
        }
    
        // Check if all squares on the diagonal path are unoccupied, excluding the destination square
        int fileDirection = destFile.ordinal() > sourceFile.ordinal() ? 1 : -1;
        int rankDirection = destRank > sourceRank ? 1 : -1;
        int distance = Math.abs(destFile.ordinal() - sourceFile.ordinal());
        for (int i = 1; i < distance; i++) {
            ReturnPiece.PieceFile currentFile = ReturnPiece.PieceFile.values()[sourceFile.ordinal() + (i * fileDirection)];
            int currentRank = sourceRank + (i * rankDirection);
            if (getPieceAtSquare(currentFile, currentRank, pieces) != null) return false;
        }
    
        // If all checks pass, return true
        return true;
    }
    
}
