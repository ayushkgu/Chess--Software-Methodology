package chess;

import java.util.ArrayList;

class ReturnPiece {
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank;  // 1..8
	public String toString() {
		return ""+pieceFile+pieceRank+":"+pieceType;
	}
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece)other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {ILLEGAL_MOVE, DRAW, 
				  RESIGN_BLACK_WINS, RESIGN_WHITE_WINS, 
				  CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS, 
				  STALEMATE};
	
	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {
	
	enum Player { white, black }
	
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {
		// Split the move string into source and destination squares
		String[] moveParts = move.split(" ");
		String sourceSquare = moveParts[0];
		String destSquare = moveParts[1];
		
		// Parse the source and destination squares to extract file and rank information
		ReturnPiece.PieceFile sourceFile = ReturnPiece.PieceFile.valueOf(sourceSquare.substring(0, 1));
		int sourceRank = Integer.parseInt(sourceSquare.substring(1));
		ReturnPiece.PieceFile destFile = ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1));
		int destRank = Integer.parseInt(destSquare.substring(1));
		
		// Get the piece on the source square
		ReturnPiece sourcePiece = getPieceAtSquare(sourceFile, sourceRank);
		
		// Check if there is a piece on the source square
		if (sourcePiece == null) {
			return new ReturnPlay(); // Return an illegal move
		}
		
		// Check if the piece belongs to the player whose turn it is (for simplicity, let's assume white always starts)
		Player currentPlayer = Player.white;
		if (sourcePiece.pieceType.name().charAt(0) == 'B') {
			currentPlayer = Player.black;
		}
		if (currentPlayer != Player.white) {
			return new ReturnPlay(); // Return an illegal move
		}
		
		// Check if the move is valid for the piece according to its movement rules
		if (!isValidMove(sourceFile, sourceRank, destFile, destRank)) {
			return new ReturnPlay(); // Return an illegal move
		}
		
		// Placeholder return to make the compiler happy for now
		return null;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
	}







	private static boolean isValidMove(ReturnPiece.PieceFile sourceFile, int sourceRank, ReturnPiece.PieceFile destFile, int destRank) {
		// Get the piece at the source square
		ReturnPiece sourcePiece = getPieceAtSquare(sourceFile, sourceRank);
	
		// Check if there is a piece at the source square
		if (sourcePiece == null) {
			return false;
		}
	
		// Validate the move using the piece's isValidMove method
		ChessPiece chessPiece = createChessPieceFromReturnPiece(sourcePiece);
		if (chessPiece != null) {
			return chessPiece.isValidMove(sourceFile.name() + sourceRank + " " + destFile.name() + destRank);
		}
	
		return false;
	}
	
	private static ChessPiece createChessPieceFromReturnPiece(ReturnPiece returnPiece) {
		// Convert ReturnPiece to corresponding ChessPiece instance
		ReturnPiece.PieceType pieceType = returnPiece.pieceType;
		ReturnPiece.PieceFile pieceFile = returnPiece.pieceFile;
		int pieceRank = returnPiece.pieceRank;
	
		switch (pieceType) {
			case WP:
				return new Pawn(pieceType, pieceFile, pieceRank);
			case WR:
				return new Rook(pieceType, pieceFile, pieceRank);
			case WN:
				return new Knight(pieceType, pieceFile, pieceRank);
			case WB:
				return new Bishop(pieceType, pieceFile, pieceRank);
			case WQ:
				return new Queen(pieceType, pieceFile, pieceRank);
			case WK:
				return new King(pieceType, pieceFile, pieceRank);
			case BP:
				return new Pawn(pieceType, pieceFile, pieceRank);
			case BR:
				return new Rook(pieceType, pieceFile, pieceRank);
			case BN:
				return new Knight(pieceType, pieceFile, pieceRank);
			case BB:
				return new Bishop(pieceType, pieceFile, pieceRank);
			case BQ:
				return new Queen(pieceType, pieceFile, pieceRank);
			case BK:
				return new King(pieceType, pieceFile, pieceRank);
			default:
				return null;
		}
	}
	private static ReturnPiece getPieceAtSquare(ReturnPiece.PieceFile file, int rank, ArrayList<ReturnPiece> piecesOnBoard) {
		// Iterate through the pieces on the board to find the one at the specified square
		for (ReturnPiece piece : piecesOnBoard) {
			if (piece.pieceFile == file && piece.pieceRank == rank) {
				return piece;
			}
		}
		// If no piece is found at the specified square, return null
		return null;
	}
	
	
	
}
