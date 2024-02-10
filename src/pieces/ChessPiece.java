package pieces;

import java.util.*;

public abstract class ChessPiece {
    protected int rank;
    protected int file;
    protected boolean isWhite;

    public ChessPiece(int rank, int file, boolean isWhite) {
        this.rank = rank;
        this.file = file;
        this.isWhite = isWhite;
    }

    public abstract boolean isValidMove(int toRank, int toFile); 

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }
}
