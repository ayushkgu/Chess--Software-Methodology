//Ayush Gupta & Yash Shah
package chess;

import java.util.ArrayList;

class ReturnPiece {
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
    int pieceRank; // 1..8
    boolean hasMoved = false; 
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

        // Check if the move is a resignation
        if (move.trim().equalsIgnoreCase("resign")) {
            ReturnPlay.Message resignMessage = (currentPlayer == Player.white) ? ReturnPlay.Message.RESIGN_BLACK_WINS : ReturnPlay.Message.RESIGN_WHITE_WINS;
            returnPlay.message = resignMessage;
            return returnPlay;
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
            returnPlay.message = ReturnPlay.Message.DRAW;
            return returnPlay;
        }

        // Get the piece at the destination square
        ReturnPiece destPiece = getPieceAtSquare(destFile, destRank, returnPlay.piecesOnBoard);

        // Check if there is a piece at the destination square
        if (destPiece != null) {
            if ((sourcePiece.pieceType.name().startsWith("W") && destPiece.pieceType.name().startsWith("W")) ||
                (sourcePiece.pieceType.name().startsWith("B") && destPiece.pieceType.name().startsWith("B"))) {
                returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
                return returnPlay;
            }
        }
    
        // Create a ChessPiece object corresponding to the source piece
        ChessPiece chessPiece = createChessPieceFromReturnPiece(sourcePiece);
    
        // Check if the move is valid for the piece according to its movement rules 
if (!chessPiece.isValidMove(sourceSquare + " " + destSquare)) {
    returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
    return returnPlay;
}
    
        // Update the board state
        applyMoveToBoard(sourcePiece, destFile, destRank, returnPlay.piecesOnBoard);

        // Check for check
        if (isInCheck(currentPlayer, returnPlay.piecesOnBoard)) {
            returnPlay.message = ReturnPlay.Message.CHECK;
            // Check for checkmate
            if (isCheckmate(currentPlayer, returnPlay.piecesOnBoard)) {
                returnPlay.message = (currentPlayer == Player.white) ? ReturnPlay.Message.CHECKMATE_BLACK_WINS : ReturnPlay.Message.CHECKMATE_WHITE_WINS;
            }
        }

        // Check for draw request after the move is executed
        if (isDrawRequested) {
            returnPlay.message = ReturnPlay.Message.DRAW;
        } else {
            switchTurn();
        }
    
        // Set the message based on the outcome of the move
        return returnPlay;
    }

    

      /**
     * Resets the game state and initializes the board with default pieces.
     */
    public static void start() {
        // Initialize the game board
        ArrayList<ReturnPiece> piecesOnBoard = initializeBoard();
        PlayChess.printBoard(piecesOnBoard);
        // Create a new ReturnPlay instance
        returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = piecesOnBoard;

        // Reset current player to white at the start of the game
        currentPlayer = Player.white;
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
    
    private static boolean isInCheck(Player player, ArrayList<ReturnPiece> piecesOnBoard) {
        // Find the king of the specified player
        ReturnPiece king = findKing(player, piecesOnBoard);
    
        // Check if any opponent's piece can attack the king's position
        Player opponent = (player == Player.white) ? Player.black : Player.white;
        for (ReturnPiece piece : piecesOnBoard) {
            if (piece.pieceType.name().charAt(0) == (opponent == Player.white ? 'B' : 'W')) {
                ChessPiece chessPiece = createChessPieceFromReturnPiece(piece);
                // Construct the move string
                String move = piece.pieceFile.name() + piece.pieceRank + " " + king.pieceFile.name() + king.pieceRank;
                // Check if the opponent's piece can attack the king's position
                if (chessPiece.isValidMove(move)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    private static boolean isCheckmate(Player player, ArrayList<ReturnPiece> piecesOnBoard) {
        // Check if the player is in check
        if (!isInCheck(player, piecesOnBoard)) {
            return false;
        }
    
        // Iterate through all the player's pieces to find a legal move to get out of check
        for (ReturnPiece piece : piecesOnBoard) {
            if (piece.pieceType.name().charAt(0) == (player == Player.white ? 'W' : 'B')) {
                for (int rank = 1; rank <= 8; rank++) {
                    for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
                        if (isValidMove(piece.pieceFile, piece.pieceRank, file, rank, piecesOnBoard)) {
                            // If a legal move is found, the player is not in checkmate
                            return false;
                        }
                    }
                }
            }
        }
        // If no legal move is found, it's checkmate
        return true;
    }
    
    private static ReturnPiece findKing(Player player, ArrayList<ReturnPiece> piecesOnBoard) {
        String kingSymbol = (player == Player.white) ? "WK" : "BK";
        for (ReturnPiece piece : piecesOnBoard) {
            if (piece.pieceType.name().equals(kingSymbol)) {
                return piece;
            }
        }
        return null; // This should not happen in a valid game
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

}