package pieces;

public class boardCoordinates {
    public int rank, file;

    public boardCoordinates(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }


    public boolean isOnTheSameFile(boardCoordinates other) {
        return this.file == other.file;
    }

    public boolean isDiagonal(boardCoordinates other) {
        if (this.file == other.file || this.rank == other.rank) {
            return false;
        } else {
        return Math.abs(this.rank - other.rank) == Math.abs(this.file - other.file);
        }
    }

    public boolean isOnTheSameRank(boardCoordinates other) {
        return this.rank == other.rank;
    }
    
    public static boolean isPromotionValid(String promote) {
        return "NQBRP".contains(promote);
    }
    


    public void setRank(int rank) {
		this.rank = rank;
	}

	public void setFile(int file) {
		this.file = file;
	}
	public int getRank() {
		return this.rank;
	}
	public int getFile() {
		return this.file;
	}
}