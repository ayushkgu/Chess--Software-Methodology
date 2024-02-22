package chess;

public class Chessboard {
    private ChessPiece[][] board;

    public Chessboard() {
        this.board = new ChessPiece[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        // Place white pieces
        board[0][0] = new Rook(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.a, 1);
        board[0][1] = new Knight(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.b, 1);
        board[0][2] = new Bishop(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.c, 1);
        board[0][3] = new Queen(ReturnPiece.PieceType.WQ, ReturnPiece.PieceFile.d, 1);
        board[0][4] = new King(ReturnPiece.PieceType.WK, ReturnPiece.PieceFile.e, 1);
        board[0][5] = new Bishop(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.f, 1);
        board[0][6] = new Knight(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.g, 1);
        board[0][7] = new Rook(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.h, 1);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(ReturnPiece.PieceType.WP, ReturnPiece.PieceFile.values()[i], 2);
        }

        // Place black pieces
        board[7][0] = new Rook(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.a, 8);
        board[7][1] = new Knight(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.b, 8);
        board[7][2] = new Bishop(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.c, 8);
        board[7][3] = new Queen(ReturnPiece.PieceType.BQ, ReturnPiece.PieceFile.d, 8);
        board[7][4] = new King(ReturnPiece.PieceType.BK, ReturnPiece.PieceFile.e, 8);
        board[7][5] = new Bishop(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.f, 8);
        board[7][6] = new Knight(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.g, 8);
        board[7][7] = new Rook(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.h, 8);
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(ReturnPiece.PieceType.BP, ReturnPiece.PieceFile.values()[i], 7);
        }
    }

    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    System.out.print(board[i][j].toString() + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }

    // Additional methods for moving pieces, checking for checkmate, etc. can be added here
}
