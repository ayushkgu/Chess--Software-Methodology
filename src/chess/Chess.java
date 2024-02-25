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
    static Player currentPlayer = Player.white; // Track current player's turn
    static boolean enPassant = false; // Flag to track en passant moves
    static boolean boardInitialized = false;
    static ReturnPlay returnPlay = new ReturnPlay();

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
        // Initialize the board if it hasn't been initialized yet
        if (!boardInitialized) {
            returnPlay.piecesOnBoard = initializeBoard();
            boardInitialized = true;
        }
    
        // Check if it's the correct player's turn
        if ((currentPlayer == Player.white && isBlackPiece(move)) ||
            (currentPlayer == Player.black && isWhitePiece(move))) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }
    
        // Parse the move string to get the source and destination squares
        String[] moveParts = move.trim().split(" ");
        String sourceSquare = moveParts[0];
        String destSquare = moveParts[1];
        boolean isDrawRequested = moveParts.length > 2 && moveParts[2].equals("draw?");
    
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
    
        /*
        // Check for en passant move
        if (isEnPassantMove(sourceSquare, destSquare)) {
            if (canEnPassant(returnPlay.piecesOnBoard, sourceSquare, destSquare)) {
                performEnPassant(returnPlay.piecesOnBoard, sourceSquare, destSquare);
                enPassant = false;
                switchTurn(); // Switch to the next player's turn
                return returnPlay;
            } else {
                returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
                return returnPlay;
            }
        } else {
            enPassant = false;
        }
    
        // Check for castling move
        if (isCastlingMove(sourceSquare, destSquare)) {
            if (canCastle(returnPlay.piecesOnBoard, sourceSquare, destSquare)) {
                performCastling(returnPlay.piecesOnBoard, sourceSquare, destSquare);
                switchTurn(); // Switch to the next player's turn
                return returnPlay;
            } else {
                returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
                return returnPlay;
            }
        }
        */
    
        // Create a ChessPiece object corresponding to the source piece
        ChessPiece chessPiece = createChessPieceFromReturnPiece(sourcePiece);
    
        // Check if the move is valid for the piece according to its movement rules
        if (!chessPiece.isValidMove(sourceFile.name() + sourceRank + " " + destFile.name() + destRank)) {
            returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
            return returnPlay;
        }
    
        //  THIS SEEMS TO ALWAYS GIVE ILLEGAL_MOVE
        // // Check if the move puts the player's own king in check
        // ArrayList<ReturnPiece> updatedBoard = new ArrayList<>(returnPlay.piecesOnBoard);
        // applyMoveToBoard(sourcePiece, destFile, destRank, updatedBoard);
    
        // if (isKingInCheck(updatedBoard, sourcePiece.pieceType)) {
        //     returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
        //     return returnPlay;
        // }
    
        // Update the board state
        applyMoveToBoard(sourcePiece, destFile, destRank, returnPlay.piecesOnBoard);
    
        /*
        // Check for special moves (e.g., en passant, castling, pawn promotion)
        if (isPawnPromotion(sourcePiece, destRank)) {
            // Perform pawn promotion
            performPawnPromotion(sourcePiece, ReturnPiece.PieceType.valueOf("WQ"), returnPlay.piecesOnBoard);
        }
    
        // If the move is valid and doesn't result in checkmate, check for draw
        if (!isDrawRequested && !isCheckmate(returnPlay.piecesOnBoard, sourcePiece.pieceType)) {
            // Check for draw if not already requested and not in checkmate
            if (isDraw(returnPlay.piecesOnBoard, sourcePiece.pieceType)) {
                returnPlay.message = ReturnPlay.Message.DRAW;
                return returnPlay;
            }
        }
    
        // If the move includes a draw request, handle it
        if (isDrawRequested) {
            returnPlay.message = ReturnPlay.Message.DRAW;
            return returnPlay;
        }
    
        // Check for check, checkmate, or stalemate
        if (isCheckmate(returnPlay.piecesOnBoard, sourcePiece.pieceType)) {
            returnPlay.message = (sourcePiece.pieceType.equals(ReturnPiece.PieceType.WK)) ?
                    ReturnPlay.Message.CHECKMATE_WHITE_WINS : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
        } else if (isStalemate(returnPlay.piecesOnBoard, sourcePiece.pieceType)) {
            returnPlay.message = ReturnPlay.Message.STALEMATE;
        } else if (isKingInCheck(returnPlay.piecesOnBoard, sourcePiece.pieceType)) {
            returnPlay.message = ReturnPlay.Message.CHECK;
        }
        */


        // Switch to the next player's turn
        switchTurn();
    
        // Set the message based on the outcome of the move
        return returnPlay;
    }

    //Method to switch the turn to the next player
    private static void switchTurn() {
        currentPlayer = (currentPlayer == Player.white) ? Player.black : Player.white;
    }

    private static boolean isWhitePiece(String move) {
        String[] moveParts = move.trim().split(" ");
        String sourceSquare = moveParts[0];
        ReturnPiece.PieceFile sourceFile = ReturnPiece.PieceFile.valueOf(sourceSquare.substring(0, 1));
        ReturnPiece piece = getPieceAtSquare(sourceFile, Integer.parseInt(sourceSquare.substring(1)), returnPlay.piecesOnBoard);
        return piece != null && piece.pieceType.name().startsWith("W");
    }

    private static boolean isBlackPiece(String move) {
        String[] moveParts = move.trim().split(" ");
        String sourceSquare = moveParts[0];
        ReturnPiece.PieceFile sourceFile = ReturnPiece.PieceFile.valueOf(sourceSquare.substring(0, 1));
        ReturnPiece piece = getPieceAtSquare(sourceFile, Integer.parseInt(sourceSquare.substring(1)), returnPlay.piecesOnBoard);
        return piece != null && piece.pieceType.name().startsWith("B");
    }





    /**
     * Resets the game state and initializes the board with default pieces.
     */
    public static void start() {
        // Initialize the game board
        ArrayList<ReturnPiece> piecesOnBoard = initializeBoard();

        // Create a new ReturnPlay instance
        returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = piecesOnBoard;

        // Reset current player to white at the start of the game
        currentPlayer = Player.white;
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
            if (piece.pieceType.equals(kingType)) {
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
        return (sourcePiece.pieceType.equals(ReturnPiece.PieceType.WP) && destRank == 8) ||
               (sourcePiece.pieceType.equals(ReturnPiece.PieceType.BP) && destRank == 1);
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
            if (piece.pieceType.equals(kingPieceType)) {
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


    private static ReturnPiece createPiece(ReturnPiece.PieceType type, ReturnPiece.PieceFile file, int rank) {
        ReturnPiece piece = new ReturnPiece();
        piece.pieceType = type;
        piece.pieceFile = file;
        piece.pieceRank = rank;
        return piece;
    }

    private static boolean isCastlingMove(String sourceSquare, String destSquare) {
        // Check if the move represents a castling move
        return sourceSquare.equalsIgnoreCase("e1") && destSquare.equalsIgnoreCase("g1") ||
               sourceSquare.equalsIgnoreCase("e8") && destSquare.equalsIgnoreCase("g8") ||
               sourceSquare.equalsIgnoreCase("e1") && destSquare.equalsIgnoreCase("c1") ||
               sourceSquare.equalsIgnoreCase("e8") && destSquare.equalsIgnoreCase("c8");
    }
    
    private static boolean canCastle(ArrayList<ReturnPiece> piecesOnBoard, String sourceSquare, String destSquare) {
        // Get the king piece involved in castling
        ReturnPiece kingPiece = getPieceAtSquare(ReturnPiece.PieceFile.valueOf(sourceSquare.substring(0, 1)),
                                                  Integer.parseInt(sourceSquare.substring(1)), piecesOnBoard);
        // Get the rook piece involved in castling
        ReturnPiece rookPiece = getPieceAtSquare(ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1)),
                                                  Integer.parseInt(destSquare.substring(1)), piecesOnBoard);
    
        // Verify if the king and rook pieces exist and are of the correct types
        if (kingPiece == null || rookPiece == null ||
                kingPiece.pieceType != ReturnPiece.PieceType.WK && kingPiece.pieceType != ReturnPiece.PieceType.BK ||
                rookPiece.pieceType != ReturnPiece.PieceType.WR && rookPiece.pieceType != ReturnPiece.PieceType.BR) {
            return false;
        }
    
        // Check if the king and rook have not moved yet
        if (kingPiece.pieceType == ReturnPiece.PieceType.WK && rookPiece.pieceType == ReturnPiece.PieceType.WR) {
            if (!isKingSideCastlingAllowed(piecesOnBoard, kingPiece)) {
                return false;
            }
        } else if (kingPiece.pieceType == ReturnPiece.PieceType.BK && rookPiece.pieceType == ReturnPiece.PieceType.BR) {
            if (!isKingSideCastlingAllowed(piecesOnBoard, kingPiece)) {
                return false;
            }
        } else {
            if (!isQueenSideCastlingAllowed(piecesOnBoard, kingPiece)) {
                return false;
            }
        }
    
        // Check if there are no pieces between the king and the rook
        ReturnPiece.PieceFile kingFile = kingPiece.pieceFile;
        ReturnPiece.PieceFile rookFile = rookPiece.pieceFile;
        int kingRank = kingPiece.pieceRank;
        int rookRank = rookPiece.pieceRank;
    
        if (kingFile.ordinal() < rookFile.ordinal()) {
            // King side castling
            for (int fileIndex = kingFile.ordinal() + 1; fileIndex < rookFile.ordinal(); fileIndex++) {
                if (getPieceAtSquare(ReturnPiece.PieceFile.values()[fileIndex], kingRank, piecesOnBoard) != null) {
                    return false;
                }
            }
        } else {
            // Queen side castling
            for (int fileIndex = rookFile.ordinal() + 1; fileIndex < kingFile.ordinal(); fileIndex++) {
                if (getPieceAtSquare(ReturnPiece.PieceFile.values()[fileIndex], kingRank, piecesOnBoard) != null) {
                    return false;
                }
            }
        }
    
        // Verify if the king is not in check and does not pass through or end up on a square attacked by an enemy piece
        if (isKingInCheck(piecesOnBoard, kingPiece.pieceType)) {
            return false;
        }
    
        return true;
    }    
    

    private static void performCastling(ArrayList<ReturnPiece> piecesOnBoard, String sourceSquare, String destSquare) {
        // Move the king and rook pieces to their new positions
        ReturnPiece kingPiece = getPieceAtSquare(ReturnPiece.PieceFile.valueOf(sourceSquare.substring(0, 1)),
                                                  Integer.parseInt(sourceSquare.substring(1)), piecesOnBoard);
        ReturnPiece rookPiece = getPieceAtSquare(ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1)),
                                                  Integer.parseInt(destSquare.substring(1)), piecesOnBoard);
    
        ReturnPiece.PieceFile kingFile = kingPiece.pieceFile;
        int kingRank = kingPiece.pieceRank;
    
        if (kingFile.ordinal() < ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1)).ordinal()) {
            // King side castling
            kingPiece.pieceFile = ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1));
            rookPiece.pieceFile = previousFile(ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1)));
        } else {
            // Queen side castling
            kingPiece.pieceFile = ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1));
            rookPiece.pieceFile = nextFile(ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1)));
        }
    
        kingPiece.pieceRank = Integer.parseInt(destSquare.substring(1));
        rookPiece.pieceRank = kingRank;
    
        // Update the board state with the modified pieces
        for (ReturnPiece piece : piecesOnBoard) {
            if (piece.equals(kingPiece) || piece.equals(rookPiece)) {
                piece.pieceFile = (piece.equals(kingPiece)) ? kingPiece.pieceFile : rookPiece.pieceFile;
                piece.pieceRank = (piece.equals(kingPiece)) ? kingPiece.pieceRank : rookPiece.pieceRank;
            }
        }
    }
    

    private static boolean isKingSideCastlingAllowed(ArrayList<ReturnPiece> piecesOnBoard, ReturnPiece kingPiece) {
        // Get the corresponding rook piece for king side castling
        ReturnPiece rookPiece = getPieceAtSquare(ReturnPiece.PieceFile.h, kingPiece.pieceRank, piecesOnBoard);
        
        // Check if the rook piece exists and has not moved
        if (rookPiece == null || rookPiece.pieceType != ReturnPiece.PieceType.WR && rookPiece.pieceType != ReturnPiece.PieceType.BR) {
            return false;
        }
    
        // Check if there are no pieces between the king and the rook
        for (int fileIndex = kingPiece.pieceFile.ordinal() + 1; fileIndex < rookPiece.pieceFile.ordinal(); fileIndex++) {
            if (getPieceAtSquare(ReturnPiece.PieceFile.values()[fileIndex], kingPiece.pieceRank, piecesOnBoard) != null) {
                return false;
            }
        }
    
        // Check if the squares the king passes through are not attacked by enemy pieces
        if (isSquareAttacked(piecesOnBoard, ReturnPiece.PieceFile.f, kingPiece.pieceRank, kingPiece)) {
            return false;
        }
        if (isSquareAttacked(piecesOnBoard, ReturnPiece.PieceFile.g, kingPiece.pieceRank, kingPiece)) {
            return false;
        }
    
        return true;
    }

    private static boolean isQueenSideCastlingAllowed(ArrayList<ReturnPiece> piecesOnBoard, ReturnPiece kingPiece) {
        // Get the corresponding rook piece for queen side castling
        ReturnPiece rookPiece = getPieceAtSquare(ReturnPiece.PieceFile.a, kingPiece.pieceRank, piecesOnBoard);
        
        // Check if the rook piece exists and has not moved
        if (rookPiece == null || rookPiece.pieceType != ReturnPiece.PieceType.WR && rookPiece.pieceType != ReturnPiece.PieceType.BR) {
            return false;
        }
    
        // Check if there are no pieces between the king and the rook
        for (int fileIndex = rookPiece.pieceFile.ordinal() + 1; fileIndex < kingPiece.pieceFile.ordinal(); fileIndex++) {
            if (getPieceAtSquare(ReturnPiece.PieceFile.values()[fileIndex], kingPiece.pieceRank, piecesOnBoard) != null) {
                return false;
            }
        }
    
        // Check if the squares the king passes through are not attacked by enemy pieces
        if (isSquareAttacked(piecesOnBoard, ReturnPiece.PieceFile.d, kingPiece.pieceRank, kingPiece)) {
            return false;
        }
        if (isSquareAttacked(piecesOnBoard, ReturnPiece.PieceFile.c, kingPiece.pieceRank, kingPiece)) {
            return false;
        }
        if (isSquareAttacked(piecesOnBoard, ReturnPiece.PieceFile.b, kingPiece.pieceRank, kingPiece)) {
            return false;
        }
    
        return true;
    }

    private static boolean isSquareAttacked(ArrayList<ReturnPiece> piecesOnBoard, ReturnPiece.PieceFile file, int rank, ReturnPiece kingPiece) {
        // Iterate through each piece on the board
        for (ReturnPiece piece : piecesOnBoard) {
            // Check if the piece belongs to the opponent
            if (piece.pieceType != kingPiece.pieceType) {
                // Create a ChessPiece object corresponding to the opponent's piece
                ChessPiece opponentPiece = createChessPieceFromReturnPiece(piece);
                // Check if the opponent's piece can attack the specified square
                if (opponentPiece.isValidMove(piece.pieceFile.name() + piece.pieceRank + " " + file.name() + rank)) {
                    return true;
                }
            }
        }
        // If no opponent piece can attack the square, return false
        return false;
    }

    private static ReturnPiece.PieceFile previousFile(ReturnPiece.PieceFile file) {
        switch (file) {
            case b: return ReturnPiece.PieceFile.a;
            case c: return ReturnPiece.PieceFile.b;
            case d: return ReturnPiece.PieceFile.c;
            case e: return ReturnPiece.PieceFile.d;
            case f: return ReturnPiece.PieceFile.e;
            case g: return ReturnPiece.PieceFile.f;
            case h: return ReturnPiece.PieceFile.g;
            default: return null; // If file is 'a', there's no previous file
        }
    }
    
    private static ReturnPiece.PieceFile nextFile(ReturnPiece.PieceFile file) {
        switch (file) {
            case a: return ReturnPiece.PieceFile.b;
            case b: return ReturnPiece.PieceFile.c;
            case c: return ReturnPiece.PieceFile.d;
            case d: return ReturnPiece.PieceFile.e;
            case e: return ReturnPiece.PieceFile.f;
            case f: return ReturnPiece.PieceFile.g;
            case g: return ReturnPiece.PieceFile.h;
            default: return null; // If file is 'h', there's no next file
        }
    }


      // Method to check if the move is an en passant move
      private static boolean isEnPassantMove(String sourceSquare, String destSquare) {
        ReturnPiece.PieceFile sourceFile = ReturnPiece.PieceFile.valueOf(sourceSquare.substring(0, 1));
        int sourceRank = Integer.parseInt(sourceSquare.substring(1));
        ReturnPiece.PieceFile destFile = ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1));
        int destRank = Integer.parseInt(destSquare.substring(1));

        // Check if the move is a diagonal pawn move of length 1, indicating en passant
        return Math.abs(destFile.ordinal() - sourceFile.ordinal()) == 1 &&
               Math.abs(destRank - sourceRank) == 1;
    }

    // Method to check if en passant conditions are met
    private static boolean canEnPassant(ArrayList<ReturnPiece> piecesOnBoard, String sourceSquare, String destSquare) {
        // Check if the en passant flag is set and the target square is empty
        ReturnPiece targetPiece = getPieceAtSquare(ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1)),
                                                    Integer.parseInt(destSquare.substring(1)), piecesOnBoard);
        return enPassant && targetPiece == null;
    }

    // Method to perform en passant
    private static void performEnPassant(ArrayList<ReturnPiece> piecesOnBoard, String sourceSquare, String destSquare) {
        // Get the target pawn's square
        ReturnPiece.PieceFile targetFile = ReturnPiece.PieceFile.valueOf(destSquare.substring(0, 1));
        int targetRank = Integer.parseInt(sourceSquare.substring(1)); // Target pawn's rank
        
        // Find the target pawn's position
        ReturnPiece targetPawn = getPieceAtSquare(targetFile, targetRank, piecesOnBoard);
        
        // Remove the target pawn from the board
        piecesOnBoard.remove(targetPawn);
    }

    //Method to handle a player's resignation
    public static ReturnPlay resign(Player player) {
        ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.message = (player == Player.white) ? ReturnPlay.Message.RESIGN_BLACK_WINS : ReturnPlay.Message.RESIGN_WHITE_WINS;
        return returnPlay;
    }

    //Method to handle a draw
    private static boolean isDraw(ArrayList<ReturnPiece> piecesOnBoard, ReturnPiece.PieceType kingPieceType) {
        // Check for draw conditions such as stalemate, threefold repetition, etc.
        if (isStalemate(piecesOnBoard, kingPieceType)) {
            return true;
        }
        return false;
    }

    // Method to handle a draw request
    public static ReturnPlay drawRequest() {
        ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = initializeBoard();
        returnPlay.message = ReturnPlay.Message.DRAW;
        return returnPlay;
    }

    // Method to handle accepting a draw request
    public static ReturnPlay acceptDraw() {
        ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = initializeBoard();
        returnPlay.message = ReturnPlay.Message.DRAW;
        return returnPlay;
    }

}