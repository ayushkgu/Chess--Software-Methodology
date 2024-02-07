public class boardCoordinates {
    public int rank, file;

    public boardCoordinates(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }


    public boolean isVerticalTo(boardCoordinates other) {
        return this.file == other.file;
    }

    public boolean isDiagonalTo(boardCoordinates other) {
        if (this.file == other.file || this.rank == other.rank) {
            return false;
        }
        return Math.abs(this.file - other.file) == Math.abs(this.rank - other.rank);
    }

    public boolean isHorizontalTo(boardCoordinates other) {
        return this.rank == other.rank;
    }
    
    public static boolean isValidPromotion(String promote) {
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