package chess;

public class Pawn extends ChessPiece {

    private boolean hasMoved;

    public Pawn(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        super(pieceType, pieceFile, pieceRank);
        this.hasMoved = false; 
    }

    @Override
    public boolean isValidMove(String move) {
        char sourceFile = move.charAt(0);
        int sourceRank = Character.getNumericValue(move.charAt(1));
        char destFile = move.charAt(3);
        int destRank = Character.getNumericValue(move.charAt(4));

        // Check if the destination square is one square forward
        if (pieceFile.name().charAt(0) == destFile && pieceRank + 1 == destRank) {
            return true;
        }

        // Check if the destination square is two squares forward (only valid if pawn hasn't moved yet)
        if (!hasMoved && pieceFile.name().charAt(0) == destFile && pieceRank + 2 == destRank) {
            return true;
        }

        // Check if the destination square is one square diagonally forward for capturing
        if (Math.abs(pieceFile.ordinal() - ReturnPiece.PieceFile.valueOf(String.valueOf(destFile)).ordinal()) == 1 &&
                pieceRank + 1 == destRank) {
            return true;
        }

        return false;
    }

    // Getter and setter for hasMoved field
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}