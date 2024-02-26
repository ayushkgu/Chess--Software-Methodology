package chess;

import java.util.ArrayList;

public class Pawn extends ChessPiece {

    public boolean hasMoved;

    public Pawn(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        super(pieceType, pieceFile, pieceRank);
        this.hasMoved = false;
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
        ReturnPiece destPiece = getPieceAtSquare(ReturnPiece.PieceFile.valueOf(String.valueOf(destFile)), destRank,
                pieces);

        ReturnPiece sourcePiece = getPieceAtSquare(ReturnPiece.PieceFile.valueOf(String.valueOf(sourceFile)),
                sourceRank,
                pieces);

        // System.out.println(sourcePiece.hasMoved);
        if (sourcePiece == null || sourcePiece.pieceType != pieceType) {
            return false;
        }

        // Check if the destination square is one square forward for white pawns
        if (pieceType == ReturnPiece.PieceType.WP &&
                pieceFile.name().charAt(0) == destFile && pieceRank + 1 == destRank && (destPiece == null)) {
            sourcePiece.hasMoved = true; // Set hasMoved to true after moving one square forward
            return true;
        }

        // Check if the destination square is two squares forward for white pawns (only
        // valid if pawn hasn't moved yet)
        if (pieceType == ReturnPiece.PieceType.WP && hasMoved == false &&
                pieceFile.name().charAt(0) == destFile && pieceRank + 2 == destRank && (destPiece == null)) {
            sourcePiece.hasMoved = true; // Set hasMoved to true after moving two squares forward

            return true;
        }

        // Check if the destination square is one square diagonally forward for
        // capturing for white pawns
        if (pieceType == ReturnPiece.PieceType.WP &&
                Math.abs(pieceFile.ordinal() - ReturnPiece.PieceFile.valueOf(String.valueOf(destFile)).ordinal()) == 1
                &&
                pieceRank + 1 == destRank && (destPiece != null)) {
            sourcePiece.hasMoved = true;

            return true;
        }

        // Check if the destination square is one square forward for black pawns
        if (pieceType == ReturnPiece.PieceType.BP &&
                pieceFile.name().charAt(0) == destFile && pieceRank - 1 == destRank && (destPiece == null)) {
            sourcePiece.hasMoved = true;

            return true;
        }

        // Check if the destination square is two squares forward for black pawns (only
        // valid if pawn hasn't moved yet)
        if (pieceType == ReturnPiece.PieceType.BP && hasMoved == false &&
                pieceFile.name().charAt(0) == destFile && pieceRank - 2 == destRank && (destPiece == null)) {
            sourcePiece.hasMoved = true;
            return true;
        }

        // Check if the destination square is one square diagonally forward for
        // capturing for black pawns
        if (pieceType == ReturnPiece.PieceType.BP &&
                Math.abs(pieceFile.ordinal() - ReturnPiece.PieceFile.valueOf(String.valueOf(destFile)).ordinal()) == 1
                &&
                pieceRank - 1 == destRank && (destPiece != null)) {
            sourcePiece.hasMoved = true;
            return true;
        }
        if (isEnPassant(move)) {
   
            pieces.remove(getPieceAtSquare(ReturnPiece.PieceFile.valueOf(String.valueOf(move.charAt(3))),
            Character.getNumericValue(move.charAt(1)),
                    pieces));
            return true;
        }
        return false;

    }

    private boolean isEnPassant(String move) {
        char sourceFile = move.charAt(0);
        int sourceRank = Character.getNumericValue(move.charAt(1));
        char destFile = move.charAt(3);
        int destRank = Character.getNumericValue(move.charAt(4));

        if (Math.abs(sourceFile - destFile) == 1) {
            if (getPieceAtSquare(ReturnPiece.PieceFile.valueOf(String.valueOf(destFile)), destRank,
                    pieces) == null) {

                if (getPieceAtSquare(ReturnPiece.PieceFile.valueOf(String.valueOf(destFile)),
                        sourceRank,
                        pieces) != null
                        && getPieceAtSquare(ReturnPiece.PieceFile.valueOf(String.valueOf(destFile)),
                                sourceRank,
                                pieces).pieceType != pieceType) {
                    String lastMove = Chess.movesHistory.get(Chess.movesHistory.size() - 1);
                    if (lastMove != null) {
                        if (lastMove.charAt(3) == destFile && Math.abs(Character.getNumericValue(lastMove.charAt(4))
                                - Character.getNumericValue(lastMove.charAt(1))) == 2) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

}