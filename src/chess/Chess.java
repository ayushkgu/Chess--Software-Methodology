//Ayush Gupta & Yash Shah
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
        ReturnPlay returnPlay = new ReturnPlay();
        // Parse the move string to get the source and destination squares
        String[] moveParts = move.trim().split(" ");
        String sourceSquare = moveParts[0];
        String destSquare = moveParts[1];
    
        // Convert source and destination squares to file and rank
        ReturnPiece.PieceFile sourceFile = ReturnPiece.PieceFile.valueOf(sourceSquare.substring(0, 1));
        int sourceRank = Integer.parseInt(sourceSquare.substring(1));
        ReturnPiece.PieceFile destFile = ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1));
        int destRank = Integer.parseInt(destSquare.substring(1));
    
        // Get the piece at the source square
        ReturnPiece sourcePiece = getPieceAtSquare(sourceFile, sourceRank, returnPlay.piecesOnBoard);
    
        // Check if there is a piece at the source square
        if (sourcePiece == null) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }
    
        // Create a ChessPiece object corresponding to the source piece
        ChessPiece chessPiece = createChessPieceFromReturnPiece(sourcePiece);
    
        // Check if the move is valid for the piece according to its movement rules
        if (!chessPiece.isValidMove(sourceFile.name() + sourceRank + " " + destFile.name() + destRank)) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }
    
        // Check if the move puts the player's own king in check
        ArrayList<ReturnPiece> updatedBoard = new ArrayList<>(returnPlay.piecesOnBoard);
        applyMoveToBoard(sourcePiece, destFile, destRank, updatedBoard);
    
        if (isKingInCheck(updatedBoard, sourcePiece.pieceType)) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }
    
        // Update the board state
        applyMoveToBoard(sourcePiece, destFile, destRank, returnPlay.piecesOnBoard);
    
        // Check for special moves (e.g., castling, pawn promotion)
        if (isPawnPromotion(sourcePiece, destRank)) {
            // Perform pawn promotion
            performPawnPromotion(sourcePiece, ReturnPiece.PieceType.valueOf("WQ"), returnPlay.piecesOnBoard);
        }
    
        // Check for check, checkmate, or draw
        if (isCheckmate(returnPlay.piecesOnBoard, sourcePiece.pieceType)) {
            returnPlay.message = (sourcePiece.pieceType == ReturnPiece.PieceType.WK) ?
                    ReturnPlay.Message.CHECKMATE_WHITE_WINS : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
        } else if (isStalemate(returnPlay.piecesOnBoard, sourcePiece.pieceType)) {
            returnPlay.message = ReturnPlay.Message.STALEMATE;
        } else if (isKingInCheck(returnPlay.piecesOnBoard, sourcePiece.pieceType)) {
            returnPlay.message = ReturnPlay.Message.CHECK;
        }
    
        // Set the message based on the outcome of the move
        return returnPlay;
    }
    

    private static boolean isValidMove(ReturnPiece.PieceFile sourceFile, int sourceRank, ReturnPiece.PieceFile destFile, int destRank, ArrayList<ReturnPiece> piecesOnBoard) {
        // Get the piece at the source square
        ReturnPiece sourcePiece = getPieceAtSquare(sourceFile, sourceRank, piecesOnBoard);

        // Check if there is a piece at the source square
        if (sourcePiece == null) {
        return false;
        }

        // Create a ChessPiece object corresponding to the source piece
        ChessPiece chessPiece = createChessPieceFromReturnPiece(sourcePiece);

        // Check if the move is valid for the piece according to its movement rules
        if (chessPiece != null) {
        return chessPiece.isValidMove(sourceFile.name() + sourceRank + " " + destFile.name() + destRank);
        }

        return false;
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


    private static void applyMoveToBoard(ReturnPiece sourcePiece, ReturnPiece.PieceFile destFile, int destRank, ArrayList<ReturnPiece> piecesOnBoard) {
        // Update the piece's file and rank to the destination square
        sourcePiece.pieceFile = destFile;
        sourcePiece.pieceRank = destRank;
        // Update the board state with the modified piece
        for (ReturnPiece piece : piecesOnBoard) {
            if (piece.equals(sourcePiece)) {
                piece.pieceFile = destFile;
                piece.pieceRank = destRank;
                break;
            }
        }
    }

    private static boolean isKingInCheck(ArrayList<ReturnPiece> board, ReturnPiece.PieceType kingType) {
        // Find the king of the specified type on the board
        ReturnPiece king = null;
        for (ReturnPiece piece : board) {
            if (piece.pieceType == kingType) {
                king = piece;
                break;
            }
        }
        
        // If king is not found, return false
        if (king == null) {
            return false;
        }
        
        // Check if any opponent piece can attack the king
        for (ReturnPiece piece : board) {
            if (piece.pieceType != kingType) {
                ChessPiece opponentPiece = createChessPieceFromReturnPiece(piece);
                if (opponentPiece != null && opponentPiece.isValidMove(piece.pieceFile.name() + piece.pieceRank + " " + king.pieceFile.name() + king.pieceRank)) {
                    return true;
                }
            }
        }
        
        // If no opponent piece can attack the king, return false
        return false;
    }
    

    private static boolean isPawnPromotion(ReturnPiece sourcePiece, int destRank) {
        // Check if the source piece is a pawn and it reaches the last rank (8 for white, 1 for black)
        return (sourcePiece.pieceType == ReturnPiece.PieceType.WP && destRank == 8) ||
               (sourcePiece.pieceType == ReturnPiece.PieceType.BP && destRank == 1);
    }

    private static void performPawnPromotion(ReturnPiece sourcePiece, ReturnPiece.PieceType promotedPieceType, ArrayList<ReturnPiece> piecesOnBoard) {
        // Find the promoted pawn in the piecesOnBoard list
        for (ReturnPiece piece : piecesOnBoard) {
            if (piece.equals(sourcePiece)) {
                // Update the piece's type to the chosen promoted piece type
                piece.pieceType = promotedPieceType;
                // Exit the loop once the promoted pawn is found and updated
                break;
            }
        }
    }
    

    private static boolean isCheckmate(ArrayList<ReturnPiece> piecesOnBoard, ReturnPiece.PieceType kingPieceType) {
        // Get the king piece of the current player
        ReturnPiece kingPiece = getKingPiece(piecesOnBoard, kingPieceType);
    
        // Check if the king is in check
        if (!isKingInCheck(piecesOnBoard, kingPieceType)) {
            // The king is not in check, so it's not checkmate
            return false;
        }
    
        // Check if the king has any legal moves to escape check
        ReturnPiece.PieceFile kingFile = kingPiece.pieceFile;
        int kingRank = kingPiece.pieceRank;
    
        // Iterate through each possible destination square around the king
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue; // Skip the current square (king's position)
                }
                ReturnPiece.PieceFile destFile = ReturnPiece.PieceFile.values()[(kingFile.ordinal() + dx + 8) % 8];
                int destRank = kingRank + dy;
    
                // Check if the move is valid and doesn't put the king in check
                if (isValidMove(kingFile, kingRank, destFile, destRank, piecesOnBoard)
                        && !isKingInCheckAfterMove(piecesOnBoard, kingPiece, destFile, destRank)) {
                    // There is at least one legal move for the king, so it's not checkmate
                    return false;
                }
            }
        }
    
        // If no legal moves are found for the king, it's checkmate
        return true;
    }
    
    private static boolean isStalemate(ArrayList<ReturnPiece> piecesOnBoard, ReturnPiece.PieceType kingPieceType) {
        // Check if the current player's king is in check
        if (isKingInCheck(piecesOnBoard, kingPieceType)) {
            return false; // If the king is in check, it's not stalemate
        }
    
        // Iterate through each piece of the current player
        for (ReturnPiece piece : piecesOnBoard) {
            if (piece.pieceType != kingPieceType) {
                // Check if the piece has any legal moves
                ReturnPiece.PieceFile pieceFile = piece.pieceFile;
                int pieceRank = piece.pieceRank;
    
                for (ReturnPiece.PieceFile destFile : ReturnPiece.PieceFile.values()) {
                    for (int destRank = 1; destRank <= 8; destRank++) {
                        if (isValidMove(pieceFile, pieceRank, destFile, destRank, piecesOnBoard)) {
                            // There's at least one legal move for a non-king piece, so it's not stalemate
                            return false;
                        }
                    }
                }
            }
        }
    
        // If no legal moves are found for any piece (excluding the king), it's stalemate
        return true;
    }    

    private static ReturnPiece getKingPiece(ArrayList<ReturnPiece> piecesOnBoard, ReturnPiece.PieceType kingPieceType) {
        // Iterate through the pieces on the board to find the king piece of the specified type
        for (ReturnPiece piece : piecesOnBoard) {
            if (piece.pieceType == kingPieceType) {
                return piece;
            }
        }
        // If no king piece of the specified type is found, return null
        return null;
    }

    private static boolean isKingInCheckAfterMove(ArrayList<ReturnPiece> piecesOnBoard, ReturnPiece kingPiece, ReturnPiece.PieceFile destFile, int destRank) {
        // Simulate the move by updating the king's position
        ReturnPiece.PieceFile originalFile = kingPiece.pieceFile;
        int originalRank = kingPiece.pieceRank;
        kingPiece.pieceFile = destFile;
        kingPiece.pieceRank = destRank;
    
        // Check if the king is now in check
        boolean kingInCheck = isKingInCheck(piecesOnBoard, kingPiece.pieceType);
    
        // Restore the king's original position
        kingPiece.pieceFile = originalFile;
        kingPiece.pieceRank = originalRank;
    
        return kingInCheck;
    }
    

 /**
     * Initializes the game board with default pieces.
     *
     * @return An ArrayList containing the default pieces on the board.
     */
    private static ArrayList<ReturnPiece> initializeBoard() {
        ArrayList<ReturnPiece> piecesOnBoard = new ArrayList<>();

        // Add white pieces
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.a, 1));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.b, 1));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.c, 1));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WQ, ReturnPiece.PieceFile.d, 1));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WK, ReturnPiece.PieceFile.e, 1));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.f, 1));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.g, 1));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.h, 1));
        for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
            piecesOnBoard.add(createPiece(ReturnPiece.PieceType.WP, file, 2));
        }

        // Add black pieces
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.a, 8));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.b, 8));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.c, 8));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BQ, ReturnPiece.PieceFile.d, 8));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BK, ReturnPiece.PieceFile.e, 8));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.f, 8));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.g, 8));
        piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.h, 8));
        for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
            piecesOnBoard.add(createPiece(ReturnPiece.PieceType.BP, file, 7));
        }

        return piecesOnBoard;
    }

    /**
     * Creates a ReturnPiece object without directly invoking the constructor.
     *
     * @param type The type of the piece.
     * @param file The file of the piece.
     * @param rank The rank of the piece.
     * @return The created ReturnPiece object.
     */
    private static ReturnPiece createPiece(ReturnPiece.PieceType type, ReturnPiece.PieceFile file, int rank) {
        ReturnPiece piece = new ReturnPiece();
        piece.pieceType = type;
        piece.pieceFile = file;
        piece.pieceRank = rank;
        return piece;
    }

    /**
     * Resets the game state and initializes the board with default pieces.
     */
    public static void start() {
        // Initialize the game board
        ArrayList<ReturnPiece> piecesOnBoard = initializeBoard();

        // Create a new ReturnPlay instance
        ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = piecesOnBoard;

    }

}