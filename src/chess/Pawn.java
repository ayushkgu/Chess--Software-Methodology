package chess;

public class Pawn extends ChessPiece {

    private boolean hasMoved;

    public Pawn(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        super(pieceType, pieceFile, pieceRank);
        this.hasMoved = false; 
    }
d
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

    // Additional method to check for en passant move
    public boolean isEnPassantMove(String move) {
        char sourceFile = move.charAt(0);
        int sourceRank = Character.getNumericValue(move.charAt(1));
        char destFile = move.charAt(3);
        int destRank = Character.getNumericValue(move.charAt(4));
    
        // Check if the move is a two-step advance
        if (Math.abs(pieceRank - destRank) == 2) {
            // Check if the destination rank is the same as the starting rank
            if ((pieceFile == ReturnPiece.PieceFile.a && destFile == 'b') ||
                (pieceFile == ReturnPiece.PieceFile.h && destFile == 'g')) {
                return true;
            }
        }
    
        return false;
    }
    
    // Method to validate the new piece type for promotion
    private boolean isValidPieceType(String newPieceType) {
        // Check if the new piece type is one of the valid options: R, N, B, or Q
        return newPieceType.equals("R") || newPieceType.equals("N") || newPieceType.equals("B") || newPieceType.equals("Q");
    }

    public void promotePawn(String newPieceType) {
        // Validate the new piece type
        if (!isValidPieceType(newPieceType)) {
            System.out.println("Invalid piece type for promotion.");
            return;
        }
    
        // Logic for pawn promotion based on the specific rules
        switch (newPieceType) {
            case "R":
                pieceType = pieceType == ReturnPiece.PieceType.WP ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR;
                break;
            case "N":
                pieceType = pieceType == ReturnPiece.PieceType.WP ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN;
                break;
            case "B":
                pieceType = pieceType == ReturnPiece.PieceType.WP ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB;
                break;
            case "Q":
                pieceType = pieceType == ReturnPiece.PieceType.WP ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ;
                break;
            default: // Default to queen if no promotion piece is indicated
                pieceType = pieceType == ReturnPiece.PieceType.WP ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ;
                break;
        }
    }

    // Getter and setter for hasMoved field
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}