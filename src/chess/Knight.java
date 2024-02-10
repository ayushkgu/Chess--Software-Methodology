package chess;

public class Knight extends ChessPiece {

    private boolean hasMoved; 

    public Knight(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        super(pieceType, pieceFile, pieceRank);
        this.hasMoved = false;
    }

    @Override
    public boolean isValidMove(String move) {
        char sourceFile = move.charAt(0);
        int sourceRank = Character.getNumericValue(move.charAt(1));
        char destFile = move.charAt(3);
        int destRank = Character.getNumericValue(move.charAt(4));

        // Check if the move is a valid knight move (L-shape)
        int fileDiff = Math.abs(sourceFile - destFile);
        int rankDiff = Math.abs(sourceRank - destRank);
        return (fileDiff == 2 && rankDiff == 1) || (fileDiff == 1 && rankDiff == 2);
    }

    // Getter and setter for hasMoved field
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
