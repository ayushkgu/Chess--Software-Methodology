package chess;

public class King extends ChessPiece {

    private boolean hasMoved; 

    public King(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        super(pieceType, pieceFile, pieceRank);
        this.hasMoved = false;
    }

    @Override
    public boolean isValidMove(String move) {
        char sourceFile = move.charAt(0);
        int sourceRank = Character.getNumericValue(move.charAt(1));
        char destFile = move.charAt(3);
        int destRank = Character.getNumericValue(move.charAt(4));

        // Check if the move is valid for a king (one square in any direction)
        return Math.abs(destFile - sourceFile) <= 1 && Math.abs(destRank - sourceRank) <= 1;
    }

    // Getter and setter for hasMoved field
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
