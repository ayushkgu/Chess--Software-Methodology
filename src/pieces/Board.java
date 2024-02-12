 import java.awt.Color;
 import chess.*;
 import pieces.*;
 
 public class Board {
    // class to represent a square on the board
     public class square {
         public boardCoordinates coordinates;
         public ChessPiece occupier;

         public square(int rank, int file, ChessPiece occupier) {
             this.coordinates = new boardCoordinates(rank, file);
             this.occupier = occupier;
         } 

         public ChessPiece getPiece() {
             return this.occupier;
         }
 

         public void setPiece(ChessPiece occupier) {
             this.occupier = occupier;
         }
 
         public boolean isOccupiedByPiece() {
            if(this.occupier == null)   {
                return false;
            } else{
                return true;
            }
         }
 
         public boardCoordinates getBoardCoordinates() {
             return this.coordinates;
         }
 
     }
 
     public static final int COUNT = 8;
 
     public square[][] board;

     public Board() {
        // class to reprsent the whole board
         this.board = new square[COUNT][COUNT];
         Color pieceColor = null;
 
         for (int curRank = 0; curRank < COUNT; curRank++) {
             for (int curFile = 0; curFile < COUNT; curFile++) {
                 ChessPiece newPiece = null;
                 if (curRank == 0 || curRank == 7) { // initialize row of non pawns
                     // TO DO
                 } else if (curRank == 1 || curRank == 6) { // initialize pawns
                   // TO DO
                 }
             }
        }
        }

     public void movePiece(int startRank, int startFile, int endRank, int endFile) {
         this.board[endRank][endFile].occupier = this.board[startRank][startFile].occupier;
         this.board[startRank][startFile].occupier = null;
     }

     
     public String toString() {
        // TO DO, PRINT OUT THE BOARD
     }
 }
