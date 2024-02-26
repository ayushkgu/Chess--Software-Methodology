package chess;

import java.util.ArrayList;

public class King extends ChessPiece {

    public King(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        super(pieceType, pieceFile, pieceRank);
    }

    ReturnPlay returnPlay = Chess.returnPlay;
    ArrayList<ReturnPiece> pieces = returnPlay.piecesOnBoard;

    public static ReturnPiece getPieceAtSquare(ReturnPiece.PieceFile file, int rank,
            ArrayList<ReturnPiece> piecesOnBoard) {
        // Iterate through the pieces on the board to find the one at the specified
        // square
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
        char sourceFile = move.charAt(0);
        int sourceRank = Character.getNumericValue(move.charAt(1));
        char destFile = move.charAt(3);
        int destRank = Character.getNumericValue(move.charAt(4));
        ReturnPiece sourcePiece = getPieceAtSquare(ReturnPiece.PieceFile.valueOf(String.valueOf(sourceFile)),
                sourceRank,
                pieces);
        // Check if the move is valid for a king (one square in any direction)
        if (Math.abs(destFile - sourceFile) <= 1 && Math.abs(destRank - sourceRank) <= 1) {
            sourcePiece.hasMoved = true;
            return true;
        }

        // Check for castling
        if (sourcePiece.hasMoved == true) {
            return false;
        }
        if (sourcePiece.hasMoved == false && destRank == sourceRank && Math.abs(destFile - sourceFile) == 2) {
            // Check if there are pieces in the way
            int rookFile = (destFile > sourceFile) ? 7 : 0;
            char intermediateFile = (destFile > sourceFile) ? (char) (destFile - 1) : (char) (destFile + 1);
            ReturnPiece intermediatePiece = getPieceAtSquare(
                    ReturnPiece.PieceFile.valueOf(String.valueOf(intermediateFile)), destRank,
                    Chess.returnPlay.piecesOnBoard);
            ReturnPiece rooks = getPieceAtSquare(ReturnPiece.PieceFile.valueOf(String.valueOf((char) ('a' + rookFile))),
                    destRank, Chess.returnPlay.piecesOnBoard);
            if (intermediatePiece != null || rooks == null || rooks.hasMoved) {
                return false;
            }
            return true; // Valid castling move
        }

        return false; // Invalid move
    }

}