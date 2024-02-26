package chess;

import java.util.ArrayList;

public class Queen extends ChessPiece {

    public Queen(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
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

        boolean isValidPath = sourceFile == destFile || sourceRank == destRank ||
                Math.abs(destFile.ordinal() - sourceFile.ordinal()) == Math.abs(destRank - sourceRank);

        if (!isValidPath)
            return false;

        if (sourceFile == destFile) {
            int start = Math.min(sourceRank, destRank) + 1;
            int end = Math.max(sourceRank, destRank);
            for (int rank = start; rank < end; rank++) {
                if (getPieceAtSquare(sourceFile, rank, pieces) != null)
                    return false;
            }
        } else if (sourceRank == destRank) {
            // Horizontal movement
            int start = Math.min(sourceFile.ordinal(), destFile.ordinal()) + 1;
            int end = Math.max(sourceFile.ordinal(), destFile.ordinal());
            for (int i = start; i < end; i++) {
                if (getPieceAtSquare(ReturnPiece.PieceFile.values()[i], sourceRank, pieces) != null)
                    return false;
            }
        } else {
            int fileDirection;
            if (destFile.ordinal() > sourceFile.ordinal()) {
                fileDirection = 1;
            } else {
                fileDirection = -1;
            };
            int rankDirection = destRank > sourceRank ? 1 : -1;
            int distance = Math.abs(destFile.ordinal() - sourceFile.ordinal());
            for (int i = 1; i < distance; i++) {
                ReturnPiece.PieceFile currentFile = ReturnPiece.PieceFile.values()[sourceFile.ordinal()+ (i * fileDirection)];
                int currentRank = sourceRank + (i * rankDirection);
                if (getPieceAtSquare(currentFile, currentRank, pieces) != null)
                    return false;
            }
        }

        return true;
    }

}
