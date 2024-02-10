package chess;

public abstract class ChessPiece {
    protected ReturnPiece.PieceType pieceType;
    protected ReturnPiece.PieceFile pieceFile;
    protected int pieceRank;

    // Constructor
    public ChessPiece(ReturnPiece.PieceType pieceType, ReturnPiece.PieceFile pieceFile, int pieceRank) {
        this.pieceType = pieceType;
        this.pieceFile = pieceFile;
        this.pieceRank = pieceRank;
    }

    // Abstract method for movement logic
    public abstract boolean isValidMove(String move);

    // Getters and Setters
    public ReturnPiece.PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(ReturnPiece.PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public ReturnPiece.PieceFile getPieceFile() {
        return pieceFile;
    }

    public void setPieceFile(ReturnPiece.PieceFile pieceFile) {
        this.pieceFile = pieceFile;
    }

    public int getPieceRank() {
        return pieceRank;
    }

    public void setPieceRank(int pieceRank) {
        this.pieceRank = pieceRank;
    }
}