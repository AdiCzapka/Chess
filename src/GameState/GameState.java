package GameState;
import Board.*;
import java.util.*;


public class GameState {
    Board board;
    boolean playerIsWhite;
    String enPassantSquareString;
    ArrayList<String> whitePieces;
    ArrayList<String> blackPieces;
    StringBuilder stringBuilder;
    boolean a1Castling;
    boolean h1Castling;
    boolean a8Castling;
    boolean h8Castling;
    int moveCount100;
    ArrayList<Hash> hashTable;
    StringBuilder gameNotation;
    int turnCounter;
    boolean whiteWin = false;
    boolean blackWin = false;
    boolean stalemate = false;
    boolean moveRule50 = false;
    boolean repetitionDraw = false;
    String originSquareString = "";
    String destinationSquareString = "";
    char pieceSymbol = ' ';
    boolean whiteMove = true;
    boolean captureMade = false;


    public GameState(Board board, boolean playerIsWhite, String enPassantSquareString, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling, int moveCount100, ArrayList<Hash> hashTable, StringBuilder gameNotation, int turnCounter) {
        this.board = board;
        this.playerIsWhite = playerIsWhite;
        this.enPassantSquareString = enPassantSquareString;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.stringBuilder = stringBuilder;
        this.a1Castling = a1Castling;
        this.h1Castling = h1Castling;
        this.a8Castling = a8Castling;
        this.h8Castling = h8Castling;
        this.moveCount100 = moveCount100;
        this.hashTable = hashTable;
        this.gameNotation = gameNotation;
        this.turnCounter = turnCounter;
    }

    public Board getBoard() {
        return this.board;
    }
    public boolean getPlayerIsWhite() {
        return this.playerIsWhite;
    }
    public String getEnPassantSquareString() {
        return this.enPassantSquareString;
    }
    public ArrayList<String> getWhitePieces() {return this.whitePieces; }
    public ArrayList<String> getBlackPieces() {return this.blackPieces; }
    public StringBuilder getStringBuilder() {return this.stringBuilder; }
    public boolean getA1Castling() { return this.a1Castling; }
    public boolean getH1Castling() { return this.h1Castling; }
    public boolean getA8Castling() { return this.a8Castling; }
    public boolean getH8Castling() { return this.h8Castling; }
    public int getMoveCount100() { return this.moveCount100; }
    public ArrayList<Hash> getHashTable() { return this.hashTable; }
    public StringBuilder getGameNotation() { return this.gameNotation; }
    public int getTurnCounter() { return this.turnCounter; }
    public boolean getWhiteWin() { return this.whiteWin; }
    public boolean getBlackWin() { return this.blackWin; }
    public boolean getStalemate() { return this.stalemate; }
    public boolean getMoveRule50() { return this.moveRule50; }
    public boolean getRepetitionDraw() { return this.repetitionDraw; }
    public String getOriginSquareString() { return this.originSquareString; }
    public String getDestinationSquareString() { return this.destinationSquareString; }
    public char getPieceSymbol() { return this.pieceSymbol; }
    public boolean getWhiteMove() { return this.whiteMove; }
    public boolean getCaptureMade() { return this.captureMade; }

    public void setBoard(Board board) { this.board = board; }
    public void setPlayerIsWhite(boolean playerIsWhite) { this.playerIsWhite = playerIsWhite; }
    public void setEnPassantSquareString(String enPassantSquareString) {  this.enPassantSquareString = enPassantSquareString; }
    public void setWhitePieces(ArrayList<String> whitePieces) { this.whitePieces = whitePieces; }
    public void setBlackPieces(ArrayList<String> blackPieces) { this.blackPieces = blackPieces; }
    public void setStringBuilder(StringBuilder stringBuilder) { this.stringBuilder = stringBuilder; }
    public void setA1Castling(boolean a1Castling) { this.a1Castling = a1Castling; }
    public void setH1Castling(boolean h1Castling) { this.h1Castling = h1Castling; }
    public void setA8Castling(boolean a8Castling) { this.a8Castling = a8Castling; }
    public void setH8Castling(boolean h8Castling) { this.h8Castling = h8Castling; }
    public void setMoveCount100(int moveCount100) { this.moveCount100 = moveCount100; }
    public void setHashTable(ArrayList<Hash> hashTable) { this.hashTable = hashTable; }
    public void setGameNotation(StringBuilder gameNotation) { this.gameNotation = gameNotation; }
    public void setTurnCounter(int turnCounter) { this.turnCounter = turnCounter; }
    public void setWhiteWin(boolean whiteWin) { this.whiteWin = whiteWin; }
    public void setBlackWin(boolean blackWin) { this.blackWin = blackWin; }
    public void setStalemate(boolean stalemate) { this.stalemate = stalemate; }
    public void setMoveRule50(boolean moveRule50) { this.moveRule50 = moveRule50; }
    public void setRepetitionDraw(boolean repetitionDraw) { this.repetitionDraw = repetitionDraw; }
    public void setOriginSquareString(String originSquareString) { this.originSquareString = originSquareString; }
    public void setDestinationSquareString(String destinationSquareString) { this.destinationSquareString = destinationSquareString; }
    public void setPieceSymbol(char pieceSymbol) { this.pieceSymbol = pieceSymbol; }
    public void setWhiteMove(boolean whiteMove) { this.whiteMove = whiteMove; }
    public void setCaptureMade(boolean captureMade) { this.captureMade = captureMade; }
}
