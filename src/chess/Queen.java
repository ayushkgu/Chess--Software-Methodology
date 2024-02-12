package chess;

public class Queen extends ChessPiece {

    public Queen(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        super(pieceType, pieceFile, pieceRank);
    }

    @Override
    public boolean isValidMove(String move) {
        char sourceFile = move.charAt(0);
        int sourceRank = Character.getNumericValue(move.charAt(1));
        char destFile = move.charAt(3);
        int destRank = Character.getNumericValue(move.charAt(4));
        // Check if the move is horizontal, vertical, or diagonal
        return sourceFile == destFile || sourceRank == destRank || Math.abs(destFile - sourceFile) == Math.abs(destRank - sourceRank);
    }
}
