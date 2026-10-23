package Tree;

import Board.Board;
import java.util.ArrayList;

public class TreeNode {
    Board board;
    String enPassantSquareString;
    ArrayList<String> whitePieces;
    ArrayList<String> blackPieces;
    boolean a1Castling;
    boolean h1Castling;
    boolean a8Castling;
    boolean h8Castling;
    int moveCount100;
    boolean whiteMove;
    String hashedPosition;
    String parentHash;

    public TreeNode(Board board, String enPassantSquareString, ArrayList<String> whitePieces, ArrayList<String> blackPieces, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling, int moveCount100, boolean whiteMove, String hashedPosition, String parentHash) {
        this.board = board;
        this.enPassantSquareString = enPassantSquareString;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.a1Castling = a1Castling;
        this.h1Castling = h1Castling;
        this.a8Castling = a8Castling;
        this.h8Castling = h8Castling;
        this.moveCount100 = moveCount100;
        this.whiteMove = whiteMove;
        this.hashedPosition = hashedPosition;
        this.parentHash = parentHash;
    }
    public Board getBoard() {
        return this.board;
    }
    public String getEnPassantSquareString() {
        return this.enPassantSquareString;
    }
    public ArrayList<String> getWhitePieces() {return this.whitePieces; }
    public ArrayList<String> getBlackPieces() {return this.blackPieces; }
    public boolean getA1Castling() { return this.a1Castling; }
    public boolean getH1Castling() { return this.h1Castling; }
    public boolean getA8Castling() { return this.a8Castling; }
    public boolean getH8Castling() { return this.h8Castling; }
    public int getMoveCount100() { return this.moveCount100; }
    public boolean getWhiteMove() { return this.whiteMove; }
    public String getHashedPosition() { return this.hashedPosition; }
    public String getParentHash() { return this.parentHash; }

    public void setBoard(Board board) { this.board = board; }
    public void setEnPassantSquareString(String enPassantSquareString) {  this.enPassantSquareString = enPassantSquareString; }
    public void setWhitePieces(ArrayList<String> whitePieces) { this.whitePieces = whitePieces; }
    public void setBlackPieces(ArrayList<String> blackPieces) { this.blackPieces = blackPieces; }
    public void setA1Castling(boolean a1Castling) { this.a1Castling = a1Castling; }
    public void setH1Castling(boolean h1Castling) { this.h1Castling = h1Castling; }
    public void setA8Castling(boolean a8Castling) { this.a8Castling = a8Castling; }
    public void setH8Castling(boolean h8Castling) { this.h8Castling = h8Castling; }
    public void setMoveCount100(int moveCount100) { this.moveCount100 = moveCount100; }
    public void setWhiteMove(boolean whiteMove) { this.whiteMove = whiteMove; }
    public void setHashedPosition(String hashedPosition) { this.hashedPosition = hashedPosition; }
    public void setParentHash(String parentHash) { this.parentHash = parentHash; }

}
