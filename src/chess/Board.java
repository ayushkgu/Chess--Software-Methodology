// package chess;
// import java.awt.Color;
 
//  public class Board {
//      // Inner class to represent a square on the board
//      public class Square {
//          public BoardCoordinates coordinates;
//          public ChessPiece occupier;
 
//          public Square(int rank, int file, ChessPiece occupier) {
//              this.coordinates = new BoardCoordinates(rank, file);
//              this.occupier = occupier;
//          }
 
//          public ChessPiece getPiece() {
//              return this.occupier;
//          }
 
//          public void setPiece(ChessPiece occupier) {
//              this.occupier = occupier;
//          }
 
//          public boolean isOccupiedByPiece() {
//              return this.occupier != null;
//          }
 
//          public BoardCoordinates getBoardCoordinates() {
//              return this.coordinates;
//          }
//      }
 
//      public static final int COUNT = 8;
 
//      public Square[][] board;
 
//      public Board() {
//          // Initialize the board
//          this.board = new Square[COUNT][COUNT];
 
//          // Initialize each square on the board
//          for (int rank = 0; rank < COUNT; rank++) {
//              for (int file = 0; file < COUNT; file++) {
//                  this.board[rank][file] = new Square(rank, file, null);
//              }
//          }
 
//          // Initialize non-pawn pieces
//          initializeNonPawnPieces();
 
//          // Initialize pawns
//          initializePawns();
//      }
 
//      private void initializeNonPawnPieces() {
//          // Initialize white pieces
//          this.board[0][0].occupier = new Rook(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.a, 0);
//          this.board[0][1].occupier = new Knight(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.b, 0);
//          this.board[0][2].occupier = new Bishop(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.c, 0);
//          this.board[0][3].occupier = new Queen(ReturnPiece.PieceType.WQ, ReturnPiece.PieceFile.d, 0);
//          this.board[0][4].occupier = new King(ReturnPiece.PieceType.WK, ReturnPiece.PieceFile.e, 0);
//          this.board[0][5].occupier = new Bishop(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.f, 0);
//          this.board[0][6].occupier = new Knight(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.g, 0);
//          this.board[0][7].occupier = new Rook(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.h, 0);
 
//          // Initialize black pieces
//          this.board[7][0].occupier = new Rook(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.a, 7);
//          this.board[7][1].occupier = new Knight(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.b, 7);
//          this.board[7][2].occupier = new Bishop(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.c, 7);
//          this.board[7][3].occupier = new Queen(ReturnPiece.PieceType.BQ, ReturnPiece.PieceFile.d, 7);
//          this.board[7][4].occupier = new King(ReturnPiece.PieceType.BK, ReturnPiece.PieceFile.e, 7);
//          this.board[7][5].occupier = new Bishop(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.f, 7);
//          this.board[7][6].occupier = new Knight(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.g, 7);
//          this.board[7][7].occupier = new Rook(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.h, 7);
//      }
 
//      private void initializePawns() {
//          // Initialize white pawns
//          for (int file = 0; file < COUNT; file++) {
//              this.board[1][file].occupier = new Pawn(ReturnPiece.PieceType.WP, ReturnPiece.PieceFile.values()[file], 1);
//          }
 
//          // Initialize black pawns
//          for (int file = 0; file < COUNT; file++) {
//              this.board[6][file].occupier = new Pawn(ReturnPiece.PieceType.BP, ReturnPiece.PieceFile.values()[file], 6);
//          }
//      }
 
//      public void movePiece(int startRank, int startFile, int endRank, int endFile) {
//          // Move a piece from the start square to the end square
//          this.board[endRank][endFile].occupier = this.board[startRank][startFile].occupier;
//          this.board[startRank][startFile].occupier = null;
//      }
 
//      public String toString() {
//         StringBuilder sb = new StringBuilder();
//         for (int rank = COUNT - 1; rank >= 0; rank--) {
//             for (int file = 0; file < COUNT; file++) {
//                 if (this.board[rank][file].isOccupiedByPiece()) {
//                     sb.append(this.board[rank][file].occupier.getPieceType().name().charAt(0));
//                     sb.append(this.board[rank][file].occupier.getPieceType().name().charAt(1));
//                     sb.append(" ");
//                 } else {
//                     if ((rank + file) % 2 == 0) {
//                         sb.append("## ");
//                     } else {
//                         sb.append("   ");
//                     }
//                 }
//             }
//             sb.append(rank + 1); 
//             sb.append("\n");
//         }

//         sb.append(" a  b  c  d  e  f  g  h\n");
//         return sb.toString();
//     }
    
//  }
 