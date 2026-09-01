import Board.*;
import java.util.*;
import java.io.*;
import Pieces.*;
import GameState.*;
import MachineLearning.*;

import javax.print.DocFlavor;


public class Main {
    public static void main(String[] args) {
//        System.out.println("Welcome to chess. 1. Play against bot. 2. Bot plays against itself. 3. Train Model");
//        Scanner input = new Scanner(System.in);
//        int choice = input.nextInt();
//        if (choice == 1) Start();
//        else if (choice == 2) SelfStart();
//        else if (choice == 3) TrainModel();
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(rand.nextBoolean());
        }
    }

    public static void Start() {
        System.out.println("Welcome to Chess");
        //Set up the board, input, player colours, etc
        Board board = new Board();
        Scanner input = new Scanner(System.in);
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder gameNotation = new StringBuilder();
        int turnCounter = 0;
        ArrayList<Hash> hashTable = new ArrayList<>();
        int moveCount100 = 0;
        board.printBoard();
        System.out.println("White or Black");
        String choice = input.nextLine();
        boolean playerIsWhite = choice.equalsIgnoreCase("White");
        String enPassantSquare = null;
        ArrayList<String> whitePieces = new ArrayList<>();
        whitePieces.add("a1"); whitePieces.add("b1"); whitePieces.add("c1"); whitePieces.add("d1"); whitePieces.add("e1"); whitePieces.add("f1"); whitePieces.add("g1"); whitePieces.add("h1");
        whitePieces.add("a2"); whitePieces.add("b2"); whitePieces.add("c2"); whitePieces.add("d2"); whitePieces.add("e2"); whitePieces.add("f2"); whitePieces.add("g2"); whitePieces.add("h2");
        ArrayList<String> blackPieces = new ArrayList<>();
        blackPieces.add("a7"); blackPieces.add("b7"); blackPieces.add("c7"); blackPieces.add("d7"); blackPieces.add("e7"); blackPieces.add("f7"); blackPieces.add("g7"); blackPieces.add("h7");
        blackPieces.add("a8"); blackPieces.add("b8"); blackPieces.add("c8"); blackPieces.add("d8"); blackPieces.add("e8"); blackPieces.add("f8"); blackPieces.add("g8"); blackPieces.add("h8");
        boolean a1Castling = true;
        boolean h1Castling = true;
        boolean a8Castling = true;
        boolean h8Castling = true;
        //Start game loop
        PlayGame(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
    }
    public static void PlayGame(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling, int moveCount100, ArrayList<Hash> hashTable, StringBuilder gameNotation, int turnCounter) {
        //Determine if plaeyr or computer goes first
        if  (playerIsWhite) {
            PlayerMove(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
        }
        else ComputerMove(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
    }
    public static void PlayerMove(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare,  ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder,  boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling, int moveCount100, ArrayList<Hash> hashTable, StringBuilder gameNotation, int turnCounter) {
        stringBuilder.setLength(0);
        //Let player pick piece to move
        board.printBoard();
        boolean validSquareOnBoard = false;
        boolean pieceIsUsers = false;
        Square toBeMoved = null;
        String userMove;
        ArrayList<String> playerMoves = new ArrayList<>();
        String pieceToMoveString = "";
        boolean pickedValidMove = false;
        do {
            //Check square belongs on board, then check that the piece belongs to player
            do {
                do {
                    //Inner loop checks that the square is on the board
                    System.out.println("Piece to move: "); //Enter something like 'e4'
                    pieceToMoveString = input.nextLine();
                    //Convert 'e4' into a 2d array coordinate
                    toBeMoved = StringToSquare(pieceToMoveString);
                    if ((toBeMoved.getRow() < 8) && (toBeMoved.getCol() < 8) && (toBeMoved.getRow() >= 0) && (toBeMoved.getCol() >= 0))
                        validSquareOnBoard = true;
                } while (!validSquareOnBoard);
                //Outer loop checks user picks their own respective piece
                if (playerIsWhite && Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())))
                    pieceIsUsers = true;
                else if (!playerIsWhite && Character.isLowerCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())))
                    pieceIsUsers = true;
            } while (!pieceIsUsers);


            //By now the user has selected their own piece to move, now we need to get a list of all the moves that piece can make
            //Create a temporary board to not allow changes to be made to the actual board
            Board tempBoard = CopyBoard(board);
            switch (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())) {
                case ('P'):
                case ('p'):
                    playerMoves = PawnMove(tempBoard, toBeMoved, whitePieces, blackPieces, stringBuilder, enPassantSquare);
                    break;
                case ('R'):
                case ('r'):
                    playerMoves = RookMove(tempBoard, toBeMoved, whitePieces, blackPieces, stringBuilder);
                    break;
                case ('B'):
                case ('b'):
                    playerMoves = BishopMove(tempBoard, toBeMoved, whitePieces, blackPieces, stringBuilder);
                    break;
                case ('N'):
                case ('n'):
                    playerMoves = KnightMove(tempBoard, toBeMoved, whitePieces, blackPieces, stringBuilder);
                    break;
                case ('Q'):
                case ('q'):
                    playerMoves = QueenMove(tempBoard, toBeMoved, whitePieces, blackPieces, stringBuilder);
                    break;
                case ('K'):
                case ('k'):
                    playerMoves = KingMove(tempBoard, toBeMoved, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling);
                    break;
            }
            //Once we get the moves the piece can make, ask the user to pick one of the moves provided
            //Print out all the moves for the user
            stringBuilder.setLength(0);
            stringBuilder.append("Moves available: ");
            for (String move : playerMoves) stringBuilder.append(move).append(" ");
            System.out.println(stringBuilder.toString());
            //Get user to pick move
            userMove = input.nextLine();
            //If user didn't pick one of the available moves, make them pick a square again to select a piece to move
            for (String move : playerMoves) {
                if (userMove.equalsIgnoreCase(move)) pickedValidMove = true;
            }
        } while (!pickedValidMove);

        //Check if castling picked
        if (userMove.equals("O-O-O") || userMove.equals("O-O")) {
            PlayerCastling(input, board, playerIsWhite, enPassantSquare,  whitePieces, blackPieces, stringBuilder,  a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, userMove, gameNotation, turnCounter);
        }


        //Make the move and update whitePieces/blackPieces with capture
        Square moveSquare = StringToSquare(userMove);
        boolean captureMade = false;
        //If white piece captured
        if (Character.isUpperCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
            whitePieces.remove(userMove);
            captureMade = true;
        }
        //If black piece captured
        else if (Character.isLowerCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
            blackPieces.remove(userMove);
            captureMade = true;
        }



        //Update enPassantSquare
        if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'P' && toBeMoved.getRow() == 1 && moveSquare.getRow() == 3) enPassantSquare = CoordinateToString(2, toBeMoved.getCol(), stringBuilder);
        else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'p' && toBeMoved.getRow() == 6 && moveSquare.getRow() == 4) enPassantSquare = CoordinateToString(5, toBeMoved.getCol(), stringBuilder);
        else enPassantSquare = null;

        //Update castling rights
        if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'K') { a1Castling = false; h1Castling = false; }
        else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'k') { a8Castling = false; h8Castling = false; }
        else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 0) a1Castling = false;
        else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 7) h1Castling = false;
        else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 0) a8Castling = false;
        else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 7) h8Castling = false;
        else if (userMove.equals("a1")) a1Castling = false;
        else if (userMove.equals("h1")) h1Castling = false;
        else if (userMove.equals("a8")) a8Castling = false;
        else if (userMove.equals("h8")) h8Castling = false;


        //Make the move. Update board & whitePieces + blackPieces
        char pieceSymbol = board.getPiece(toBeMoved.getRow(), toBeMoved.getCol());
        board.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
        board.setPiece(moveSquare.getRow(), moveSquare.getCol(), pieceSymbol);

        //Add to chess notation
        //If computer is white
        char newPieceSymbol = board.getPiece(moveSquare.getRow(), moveSquare.getCol());
        switch (newPieceSymbol) {
            case 'P':
            case 'p':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + pieceToMoveString + userMove + " ");
                else gameNotation.append(pieceToMoveString + userMove + " ");
                break;
            case 'B':
            case 'b':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "B" + pieceToMoveString + userMove + " ");
                else gameNotation.append("B" + pieceToMoveString + userMove + " ");
                break;
            case 'N':
            case 'n':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "N" + pieceToMoveString + userMove + " ");
                else gameNotation.append("N" + pieceToMoveString + userMove + " ");
                break;
            case 'R':
            case 'r':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "R" + pieceToMoveString + userMove + " ");
                else gameNotation.append("R" + pieceToMoveString + userMove + " ");
                break;
            case 'Q':
            case 'q':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "Q" + pieceToMoveString + userMove + " ");
                else gameNotation.append("Q" + pieceToMoveString + userMove + " ");
                break;
            case 'K':
            case 'k':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "K" + pieceToMoveString + userMove + " ");
                else gameNotation.append("K" + pieceToMoveString + userMove + " ");
                break;
        }

        int pieceIndex = 0;
        if (playerIsWhite) {
            for (String piece : whitePieces) {
                if (piece.equals(pieceToMoveString)) break;
                else pieceIndex++;
            }
            whitePieces.remove(pieceIndex);
            whitePieces.add(userMove);
        }
        else {
            for (String piece : blackPieces) {
                if (piece.equals(pieceToMoveString)) break;
                else pieceIndex++;
            }
            blackPieces.remove(pieceIndex);
            blackPieces.add(userMove);
        }
        //Handle Promotion
        boolean correctSymbol = false;
        if ((pieceSymbol == 'P' && moveSquare.getRow() == 7) || (pieceSymbol == 'p' && moveSquare.getRow() == 0)) {
            System.out.println("Pick a piece to promote your pawn to: Queen (Q), Rook (R), Bishop (B), Knight(N)");
            char newPiece;
            do {
                newPiece = Character.toUpperCase(input.nextLine().charAt(0));
                if (newPiece == 'Q' || newPiece == 'R' || newPiece == 'B' || newPiece == 'N') correctSymbol = true;
            } while (!correctSymbol);
            if (playerIsWhite) board.setPiece(moveSquare.getRow(), moveSquare.getCol(), newPiece);
            else board.setPiece(moveSquare.getRow(), moveSquare.getCol(), Character.toLowerCase(newPiece));
        }
        //Check opponent has legal moves. If not, checkmate/stalemate
        ArrayList<ArrayList<String>> opponentMoves = AllMoves(board, !playerIsWhite, whitePieces, blackPieces, stringBuilder, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling);
        boolean movesAvailable = false;
        for (ArrayList<String> moveList : opponentMoves) {
            if (moveList.size() > 1) {
                movesAvailable = true;
                break;
            }
        }
        //50 move rule
        moveCount100++;
        if (captureMade) moveCount100 = 0;
        else if (pieceSymbol == 'p' || pieceSymbol == 'P') moveCount100 = 0;
        if (moveCount100 >= 100) {
            System.out.println("Draw by 50 move rule");
            gameNotation.append("1/2-1/2");
            System.out.println(gameNotation);
            System.exit(0);
        }

        //Check 3-fold repetition
        hashTable = HashPosition(board, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling, !playerIsWhite, stringBuilder, hashTable);
        //If moves are available, pass onto computer to make their move
        if (movesAvailable) ComputerMove(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
        else {
            boolean checkmate = CheckIfCheckmate(board, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (checkmate) {
                System.out.println("Player Wins!");
                gameNotation.delete(gameNotation.length() - 1, gameNotation.length());
                gameNotation.append("# ");
                if (playerIsWhite) gameNotation.append("1-0");
                else gameNotation.append("0-1");
                System.out.println(gameNotation);
                System.exit(0);
            }
            else {
                System.out.println("Stalemate!");
                gameNotation.append("1/2-1/2");
                System.out.println(gameNotation);
                System.exit(0);
            }
        }
    }
    public static ArrayList<String> KingMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling) {
        stringBuilder.setLength(0);
        //Get all the theoretical moves that the king can make
        ArrayList<String> PseudoMoves = KingAttacks(board, SquareToString(toBeMoved, stringBuilder), stringBuilder);
        ArrayList<String> finalMoves = new ArrayList<>();
        boolean pieceIsWhite = (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())));
        //For each one, check if it leads to the king being in check
        //Simulate the move
        Board tempBoard;
        for (String move : PseudoMoves) {
            //Create copy of board to pass into function
            tempBoard = CopyBoard(board);
            Square moveSquare = StringToSquare(move);
            ArrayList<String> newBlackPieces = new ArrayList<>();
            ArrayList<String> newWhitePieces = new ArrayList<>();
            //Do move on temporary board
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'K');
            else tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'k');
            //Create list of updated white pieces/ black pieces. It should be the same, but include the moved piece
            if (pieceIsWhite) {
                newWhitePieces = CreateNewPieceList(tempBoard, whitePieces, moveSquare, stringBuilder);
                newBlackPieces.addAll(blackPieces);
            }
            else {
                newBlackPieces = CreateNewPieceList(tempBoard, blackPieces, moveSquare, stringBuilder);
                newWhitePieces.addAll(whitePieces);
            }
            boolean isCheck = CheckIfCheck(tempBoard, newWhitePieces, newBlackPieces, moveSquare, stringBuilder);
            if (!isCheck) finalMoves.add(move);

        }
        //Onto castling (uh oh)
        ArrayList<String> CastlingMoves = CastlingCheck(board, whitePieces, blackPieces, toBeMoved, a1Castling, h1Castling, a8Castling, h8Castling, stringBuilder);
        //Once we have the castling legal moves, add them to the king move list
        finalMoves.addAll(CastlingMoves);
        return finalMoves;
    }
    public static ArrayList<String> PawnMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder, String enPassantSquare) {
        stringBuilder.setLength(0);
        ArrayList<String> PseudoMoves = new ArrayList<>();
        ArrayList<String> finalMoves = new ArrayList<>();
        Board tempBoard;
        boolean pieceIsWhite = false;
        //Figure out if piece is white or black
        if(Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()))) pieceIsWhite = true;
        //White pieces move up, black pieces go down
        int offset = (pieceIsWhite ? 1 : -1);
        //Pawns can move up 1, 2 if on the 2nd row or 7th, not if obstructed, and capture diagonally 1

        //Check space above/below is clear
        if (board.getPiece(toBeMoved.getRow() + offset, toBeMoved.getCol()) == '.') {
            PseudoMoves.add(CoordinateToString(toBeMoved.getRow() + offset, toBeMoved.getCol(), stringBuilder));
            //Additional check for possible 2nd row double move
            if (pieceIsWhite) {
                if (toBeMoved.getRow() == 1 && board.getPiece(toBeMoved.getRow() + offset * 2, toBeMoved.getCol()) == '.') PseudoMoves.add(CoordinateToString(toBeMoved.getRow() + offset * 2, toBeMoved.getCol(), stringBuilder));
            }
            else {
                if (toBeMoved.getRow() == 6 && board.getPiece(toBeMoved.getRow() + offset*2, toBeMoved.getCol()) == '.') PseudoMoves.add(CoordinateToString(toBeMoved.getRow() + offset*2, toBeMoved.getCol(), stringBuilder));
            }
        }
        //Captures (Pawn Attacks basically does it, but it returns attacks on empty squares, and before I figure out what I can cut I'll repeat code)

        Integer[][] attackOffsets = new Integer[2][2];
        //Generate moves depending on colour
        if (pieceIsWhite) {attackOffsets[0][0] = 1; attackOffsets[0][1] = -1; attackOffsets[1][0] = 1; attackOffsets[1][1] = 1;}
        else {attackOffsets[0][0] = -1; attackOffsets[0][1] = -1; attackOffsets[1][0] = -1; attackOffsets[1][1] = 1;}
        //Create the attacking squares for each offset
        for (Integer[] attackOffset : attackOffsets) {
            //Get new square coordinates
            int newRow = toBeMoved.getRow() + attackOffset[0];
            int newCol = toBeMoved.getCol() + attackOffset[1];
            //Check square in bounds
            if  (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                //Check the attacking square is not occupied by friendly piece (not white piece, white piece or black piece, black piece)
                if (!(Character.isUpperCase(board.getPiece(newRow, newCol)) && pieceIsWhite) && !(Character.isLowerCase(board.getPiece(newRow, newCol)) && !pieceIsWhite) && board.getPiece(newRow, newCol) != '.') {
                    //Mismatch colours
                    PseudoMoves.add(CoordinateToString(newRow, newCol, stringBuilder));
                }
                //Check for en passant
                if (CoordinateToString(newRow, newCol, stringBuilder).equals(enPassantSquare)) PseudoMoves.add(CoordinateToString(newRow, newCol, stringBuilder));
            }
        }
        //Now that we have all the possible moves, check if any leave king in check
        for (String move : PseudoMoves) {
            ArrayList<String> newWhitePieces = new ArrayList<>();
            ArrayList<String> newBlackPieces = new ArrayList<>();
            tempBoard = CopyBoard(board);
            Square moveSquare = StringToSquare(move);
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'P');
            else  tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'p');
            if (pieceIsWhite) {
                newWhitePieces = CreateNewPieceList(tempBoard, whitePieces, moveSquare, stringBuilder);
                newBlackPieces.addAll(blackPieces);
            }
            else {
                newBlackPieces = CreateNewPieceList(tempBoard, blackPieces, moveSquare, stringBuilder);
                newWhitePieces.addAll(whitePieces);
            }
            boolean IsCheck = CheckIfCheck(tempBoard, newWhitePieces, newBlackPieces, moveSquare, stringBuilder);
            if (!IsCheck) finalMoves.add(move);
        }
        return finalMoves;
    }
    public static ArrayList<String> KnightMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Get all theoretical moves that knight can make
        ArrayList<String> pseudoMoves = KnightAttacks(board, SquareToString(toBeMoved, stringBuilder), stringBuilder);
        ArrayList<String> finalMoves = new ArrayList<>();
        boolean pieceIsWhite = (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())));
        //Simulate move
        Board tempBoard;
        for (String move : pseudoMoves) {
            tempBoard = CopyBoard(board);
            ArrayList<String> newBlackPieces = new ArrayList<>();
            ArrayList<String> newWhitePieces = new ArrayList<>();
            Square moveSquare = StringToSquare(move);
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'N');
            else tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'n');
            if (pieceIsWhite) {
                newWhitePieces = CreateNewPieceList(tempBoard, whitePieces, moveSquare, stringBuilder);
                newBlackPieces.addAll(blackPieces);
            }
            else {
                newBlackPieces = CreateNewPieceList(tempBoard, blackPieces, moveSquare, stringBuilder);
                newWhitePieces.addAll(whitePieces);
            }
            boolean isCheck = CheckIfCheck(tempBoard, newWhitePieces, newBlackPieces, moveSquare, stringBuilder);
            if (!isCheck) finalMoves.add(move);
        }
        return finalMoves;
    }
    public static ArrayList<String> RookMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Get theoretical moves
        ArrayList<String> pseudoMoves = RookAttacks(board, SquareToString(toBeMoved, stringBuilder), stringBuilder);
        ArrayList<String> finalMoves = new ArrayList<>();
        boolean pieceIsWhite = (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())));
        //Simulate move
        Board tempBoard;
        for (String move : pseudoMoves) {
            ArrayList<String> newBlackPieces = new ArrayList<>();
            ArrayList<String> newWhitePieces = new ArrayList<>();
            tempBoard = CopyBoard(board);
            Square moveSquare = StringToSquare(move);
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'R');
            else tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'r');
            if (pieceIsWhite) {
                newWhitePieces = CreateNewPieceList(tempBoard, whitePieces, moveSquare, stringBuilder);
                newBlackPieces.addAll(blackPieces);
            }
            else {
                newBlackPieces = CreateNewPieceList(tempBoard, blackPieces, moveSquare, stringBuilder);
                newWhitePieces.addAll(whitePieces);
            }
            boolean isCheck = CheckIfCheck(tempBoard, newWhitePieces, newBlackPieces, moveSquare, stringBuilder);
            if (!isCheck) finalMoves.add(move);
        }
        return finalMoves;
    }
    public static ArrayList<String> BishopMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Get theoretical moves
        ArrayList<String> pseudoMoves = BishopAttacks(board, SquareToString(toBeMoved, stringBuilder), stringBuilder);
        ArrayList<String> finalMoves = new ArrayList<>();
        boolean pieceIsWhite = (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())));
        //Simulate move
        Board tempBoard;
        for (String move : pseudoMoves) {
            tempBoard = CopyBoard(board);
            ArrayList<String> newBlackPieces = new ArrayList<>();
            ArrayList<String> newWhitePieces = new ArrayList<>();
            Square moveSquare = StringToSquare(move);
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'B');
            else tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'b');
            if (pieceIsWhite) {
                newWhitePieces = CreateNewPieceList(tempBoard, whitePieces, moveSquare, stringBuilder);
                newBlackPieces.addAll(blackPieces);
            }
            else {
                newBlackPieces = CreateNewPieceList(tempBoard, blackPieces, moveSquare, stringBuilder);
                newWhitePieces.addAll(whitePieces);
            }
            boolean isCheck = CheckIfCheck(tempBoard, newWhitePieces, newBlackPieces, moveSquare, stringBuilder);
            if (!isCheck) finalMoves.add(move);
        }
        return finalMoves;
    }
    public static ArrayList<String> QueenMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Queen moves like a rook and bishop
        ArrayList<String> rookMoves = RookMove(board, toBeMoved, whitePieces, blackPieces, stringBuilder);
        ArrayList<String> bishopMoves = BishopMove(board, toBeMoved, whitePieces, blackPieces, stringBuilder);
        ArrayList<String> finalMoves =  new ArrayList<>();
        finalMoves.addAll(rookMoves);
        finalMoves.addAll(bishopMoves);
        return finalMoves;
    }
    public static boolean CheckIfCheck(Board tempBoard,  ArrayList<String> whitePieces, ArrayList<String> blackPieces, Square destinationSquare, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Determine attacking colour
        boolean blackAttack = false;
        ArrayList<String> pieceAttacks;
        ArrayList<String> allAttacks = new ArrayList<>();
        //If white piece is the one that got moved, black is attacking, and vice versa
        if (Character.isUpperCase(tempBoard.getPiece(destinationSquare.getRow(), destinationSquare.getCol()))) blackAttack = true;
        if (blackAttack) {
            //For each black piece, get its attacks, and if any are attacking the king then it's check
            for (String piece : blackPieces) {
                //Each piece is represesnted as coordinate: "e7". Take each coordinate, convert to board position, check board position
                //for piece type (represented by letter) and get its attacks
                Square attackSquare =  StringToSquare(piece);
                switch (tempBoard.getPiece(attackSquare.getRow(), attackSquare.getCol())) {
                    case ('p'):
                        pieceAttacks = PawnAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('r'):
                        pieceAttacks = RookAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('b'):
                        pieceAttacks = BishopAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('n'):
                        pieceAttacks = KnightAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('q'):
                        pieceAttacks = QueenAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('k'):
                        pieceAttacks = KingAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;

                }

            }
            //Once we have all the attacks, check that none are attacking the king square
            //Find king
            String kingSquare = null;
            for (String piece : whitePieces) {
                Square pieceSquare =  StringToSquare(piece);
                if (tempBoard.getPiece(pieceSquare.getRow(), pieceSquare.getCol()) == 'K') {
                    kingSquare = piece;
                }
            }
            for (String attack : allAttacks) {
                if (attack.equals(kingSquare)) return true;
            }
            return false;

        }
        else {
            //For each white piece, do the same
            for (String piece : whitePieces) {
                Square attackSquare =  StringToSquare(piece);
                switch (tempBoard.getPiece(attackSquare.getRow(), attackSquare.getCol())) {
                    case ('P'):
                        pieceAttacks = PawnAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('R'):
                        pieceAttacks = RookAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('B'):
                        pieceAttacks = BishopAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('N'):
                        pieceAttacks = KnightAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('Q'):
                        pieceAttacks = QueenAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('K'):
                        pieceAttacks = KingAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;

                }

            }
            String kingSquare = null;
            for (String piece : blackPieces) {
                Square pieceSquare =  StringToSquare(piece);
                if (tempBoard.getPiece(pieceSquare.getRow(), pieceSquare.getCol()) == 'k') {
                    kingSquare = piece;
                }
            }
            for (String attack : allAttacks) {
                if (attack.equals(kingSquare)) return true;
            }
            return false;
        }
    }
    public static ArrayList<String> PawnAttacks(Board board, String piece, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Create list of attacks
        ArrayList<String> attacks = new ArrayList<>();
        //Get square of piece
        Square pieceSquare = StringToSquare(piece);
        //Check if piece is white or black
        boolean pieceIsWhite = false;
        Integer[][] attackOffsets = new Integer[2][2];
        //Generate moves depending on colour
        if (Character.isUpperCase(board.getPiece(pieceSquare.getRow(), pieceSquare.getCol()))) pieceIsWhite = true;
        if (pieceIsWhite) {attackOffsets[0][0] = 1; attackOffsets[0][1] = -1; attackOffsets[1][0] = 1; attackOffsets[1][1] = 1;}
        else {attackOffsets[0][0] = -1; attackOffsets[0][1] = -1; attackOffsets[1][0] = -1; attackOffsets[1][1] = 1;}
        //Create the attacking squares for each offset
        for (Integer[] offset : attackOffsets) {
            //Get new square coordinates
            int newRow = pieceSquare.getRow() + offset[0];
            int newCol = pieceSquare.getCol() + offset[1];

            //Check square in bounds
            if  (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {

                //If in bounds, check if attacking an empty square. If so, add as attack
                if (board.getPiece(newRow, newCol) == '.') attacks.add(CoordinateToString(newRow, newCol, stringBuilder));

                //Else, check the attacking square is not occupied by friendly piece (not white piece, white piece or black piece, black piece)
                else if (!(Character.isUpperCase(board.getPiece(newRow, newCol)) && pieceIsWhite) && !(Character.isLowerCase(board.getPiece(newRow, newCol)) && !pieceIsWhite)) {
                    //Mismatch colours
                    attacks.add(CoordinateToString(newRow, newCol, stringBuilder));
                }
            }
        }
        return attacks;
    }
    public static ArrayList<String> RookAttacks(Board board, String piece, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Create list of attacks
        ArrayList<String> attacks = new ArrayList<>();
        //Get square of piece
        Square pieceSquare = StringToSquare(piece);
        //Check if piece is white or black
        boolean pieceIsWhite = false;
        //The 4 directions of attack - Up, Down, Left, Right
        Integer[][] attackOffsets = {{1,0}, {-1, 0}, {0, -1} , {0, 1}};
        //Find piece colour
        if (Character.isUpperCase(board.getPiece(pieceSquare.getRow(), pieceSquare.getCol()))) pieceIsWhite = true;
        //Boolean to indicate search for a given direction is finished
        boolean finished;
        //For each direction, add each move along the line until the line reaches a friendly piece or the first enemy piece
        for (Integer[] direction : attackOffsets) {
            //Reset original position
            finished = false;
            int newRow = pieceSquare.getRow();
            int newCol = pieceSquare.getCol();
            do {
                //iterate over direction
                newRow = newRow + direction[0];
                newCol = newCol + direction[1];
                //Check within bounds of board. If not, direction is finished
                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    //If empty square, add it to attacks and keep iterating
                    if (board.getPiece(newRow, newCol) == '.') attacks.add(CoordinateToString(newRow, newCol, stringBuilder));
                        //If the piece on the board is white and piece is white, finish without adding the move
                    else if (Character.isUpperCase(board.getPiece(newRow, newCol)) && pieceIsWhite) finished = true;
                        //Same for black
                    else if (Character.isLowerCase(board.getPiece(newRow, newCol)) && !pieceIsWhite)  finished = true;
                        //Last case is a mismatch. Add the square as an attack and finish
                    else {
                        attacks.add(CoordinateToString(newRow, newCol, stringBuilder));
                        finished = true;
                    }
                }
                else finished = true;
            } while (!finished);
        }
        return attacks;
    }
    public static ArrayList<String> BishopAttacks(Board board, String piece, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Create list of attacks
        ArrayList<String> attacks = new ArrayList<>();
        //Get square of piece
        Square pieceSquare = StringToSquare(piece);
        //Check if piece is white or black
        boolean pieceIsWhite = false;
        //The 4 directions of attack - Up Right, Down Left, Up Left, Down Right
        Integer[][] attackOffsets = {{1,1}, {-1, -1}, {1, -1} , {-1, 1}};
        //Find piece colour
        if (Character.isUpperCase(board.getPiece(pieceSquare.getRow(), pieceSquare.getCol()))) pieceIsWhite = true;
        //Boolean to indicate search for a given direction is finished
        boolean finished;
        //For each direction, add each move along the line until the line reaches a friendly piece or the first enemy piece
        for (Integer[] direction : attackOffsets) {
            //Reset original position
            finished = false;
            int newRow = pieceSquare.getRow();
            int newCol = pieceSquare.getCol();
            do {
                //iterate over direction
                newRow = newRow + direction[0];
                newCol = newCol + direction[1];
                //Check within bounds of board. If not, direction is finished
                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    //If empty square, add it to attacks and keep iterating
                    if (board.getPiece(newRow, newCol) == '.') attacks.add(CoordinateToString(newRow, newCol, stringBuilder));
                        //If the piece on the board is white and piece is white, finish without adding the move
                    else if (Character.isUpperCase(board.getPiece(newRow, newCol)) && pieceIsWhite) finished = true;
                        //Same for black
                    else if (Character.isLowerCase(board.getPiece(newRow, newCol)) && !pieceIsWhite)  finished = true;
                        //Last case is a mismatch. Add the square as an attack and finish
                    else {
                        attacks.add(CoordinateToString(newRow, newCol, stringBuilder));
                        finished = true;
                    }
                }
                else finished = true;
            } while (!finished);
        }
        return attacks;
    }
    public static ArrayList<String> QueenAttacks(Board board, String piece, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Queen attacks like a rook and bishop. Get rook and bishop attacks, add both to 1 list
        ArrayList<String> rookAttacks = RookAttacks(board, piece, stringBuilder);
        ArrayList<String> bishopAttacks = BishopAttacks(board, piece, stringBuilder);
        ArrayList<String> attacks = new ArrayList<>();
        attacks.addAll(rookAttacks);
        attacks.addAll(bishopAttacks);
        return attacks;
    }
    public static ArrayList<String> KnightAttacks(Board board, String piece, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Create list of attacks
        ArrayList<String> attacks = new ArrayList<>();
        //Get square of piece
        Square pieceSquare = StringToSquare(piece);
        //Check if piece is white or black
        boolean pieceIsWhite = false;
        //The 4 directions of attack - Up, Down, Left, Right
        Integer[][] attackOffsets = {{2,-1}, {2, 1}, {-2, -1} , {-2, 1}, {1,2}, {-1, 2}, {1, -2} , {-1, -2}};
        //Find piece colour
        if (Character.isUpperCase(board.getPiece(pieceSquare.getRow(), pieceSquare.getCol()))) pieceIsWhite = true;
        //Go over every move
        for (Integer[] direction : attackOffsets) {
            //Get new square
            int newRow = pieceSquare.getRow() + direction[0];
            int newCol = pieceSquare.getCol() + direction[1];
            //Check within bounds of board
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                //If empty square, add it to attacks
                if (board.getPiece(newRow, newCol) == '.') attacks.add(CoordinateToString(newRow, newCol, stringBuilder));
                //If the piece on the board is white and piece is white, not an attack
                else if (Character.isUpperCase(board.getPiece(newRow, newCol)) && pieceIsWhite);
                //Same for black
                else if (Character.isLowerCase(board.getPiece(newRow, newCol)) && !pieceIsWhite);
                //Last case is a mismatch. Add the square as an attack and finish
                else {
                    attacks.add(CoordinateToString(newRow, newCol, stringBuilder));
                }
            }
        }
        return attacks;
    }
    public static ArrayList<String> KingAttacks(Board board, String piece, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Create list of attacks
        ArrayList<String> attacks = new ArrayList<>();
        //Get square of piece
        Square pieceSquare = StringToSquare(piece);
        //Check if piece is white or black
        boolean pieceIsWhite = false;
        //The 4 directions of attack - Up, Down, Left, Right
        Integer[][] attackOffsets = {{1,-1}, {1, 0}, {1, 1} , {0, -1}, {0,1}, {-1, -1}, {-1, 0} , {-1, 1}};
        //Find piece colour
        if (Character.isUpperCase(board.getPiece(pieceSquare.getRow(), pieceSquare.getCol()))) pieceIsWhite = true;
        //Go over every move
        for (Integer[] direction : attackOffsets) {
            //Get new square
            int newRow = pieceSquare.getRow() + direction[0];
            int newCol = pieceSquare.getCol() + direction[1];
            //Check within bounds of board
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                //If empty square, add it to attacks
                if (board.getPiece(newRow, newCol) == '.') attacks.add(CoordinateToString(newRow, newCol, stringBuilder));
                    //If the piece on the board is white and piece is white, not an attack
                else if (Character.isUpperCase(board.getPiece(newRow, newCol)) && pieceIsWhite);
                    //Same for black
                else if (Character.isLowerCase(board.getPiece(newRow, newCol)) && !pieceIsWhite);
                    //Last case is a mismatch. Add the square as an attack and finish
                else {
                    attacks.add(CoordinateToString(newRow, newCol, stringBuilder));
                }
            }
        }
        return attacks;
    }
    public static ArrayList<String> CastlingCheck(Board board, ArrayList<String> whitePieces,  ArrayList<String> blackPieces, Square toBeMoved, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        ArrayList<String> castling =  new ArrayList<>();
        ArrayList<String> newWhitePieces = new ArrayList<>();
        ArrayList<String> newBlackPieces = new ArrayList<>();
        //For castling, king cannot be in check, king or rooks haven't moved, doesn't pass through check and squares between are empty
        //Check if white or black king
        boolean pieceIsWhite = false;
        boolean check;
        boolean biggerCheck = false;
        if (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()))) pieceIsWhite = true;
        //For white king
        if (pieceIsWhite) {
            //Check if king is currently in check
            boolean kingInCheck = CheckIfCheck(board, whitePieces, blackPieces, toBeMoved, stringBuilder);
            if (!kingInCheck) {
                //Check if left rook has moved
                if (a1Castling) {
                    //For each square
                    String[] squares = {"d1", "c1", "b1"};
                    for (String square : squares) {
                        Square moveSquare = StringToSquare(square);
                        Board tempBoard = CopyBoard(board);
                        //Check it's empty
                        if (board.getPiece(moveSquare.getRow(), moveSquare.getCol()) == '.') {
                            //Simulate the move and check if king would be in check
                            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
                            tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'K');
                            newWhitePieces = CreateNewPieceList(tempBoard, whitePieces, moveSquare, stringBuilder);
                            check = CheckIfCheck(tempBoard, newWhitePieces, blackPieces, toBeMoved, stringBuilder);
                            if (check) biggerCheck  = true;
                        }
                        else biggerCheck = true;
                    }
                    if (!biggerCheck) castling.add("O-O-O");
                }
                biggerCheck = false;
                //Check if right rook has moved
                if (h1Castling) {
                    //For each square
                    String[] squares = {"f1", "g1"};
                    for (String square : squares) {
                        Square moveSquare = StringToSquare(square);
                        Board tempBoard = CopyBoard(board);
                        //Check it's empty
                        if (board.getPiece(moveSquare.getRow(), moveSquare.getCol()) == '.') {
                            //Simulate the move and check if king would be in check
                            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
                            tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'K');
                            newWhitePieces = CreateNewPieceList(tempBoard, whitePieces, moveSquare, stringBuilder);
                            check = CheckIfCheck(tempBoard, newWhitePieces, blackPieces, toBeMoved, stringBuilder);
                            if (check) biggerCheck  = true;
                        }
                        else biggerCheck = true;
                    }
                    if (!biggerCheck) castling.add("O-O");
                }
            }

        }
        else {
            //Check if king is currently in check
            boolean kingInCheck = CheckIfCheck(board, whitePieces, blackPieces, toBeMoved, stringBuilder);
            if (!kingInCheck) {
                //Check if left rook has moved
                if (a8Castling) {
                    //For each square
                    String[] squares = {"d8", "c8", "b8"};
                    for (String square : squares) {
                        Square moveSquare = StringToSquare(square);
                        Board tempBoard = CopyBoard(board);
                        //Check it's empty
                        if (board.getPiece(moveSquare.getRow(), moveSquare.getCol()) == '.') {
                            //Simulate the move and check if king would be in check
                            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
                            tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'k');
                            newBlackPieces = CreateNewPieceList(tempBoard, blackPieces, moveSquare, stringBuilder);
                            check = CheckIfCheck(tempBoard, whitePieces, newBlackPieces, toBeMoved, stringBuilder);
                            if (check) biggerCheck  = true;
                        }
                        else biggerCheck = true;
                    }
                    if (!biggerCheck) castling.add("O-O-O");
                }
                biggerCheck = false;
                //Check if right rook has moved
                if (h8Castling) {
                    //For each square
                    String[] squares = {"f8", "g8"};
                    for (String square : squares) {
                        Square moveSquare = StringToSquare(square);
                        Board tempBoard = CopyBoard(board);
                        //Check it's empty
                        if (board.getPiece(moveSquare.getRow(), moveSquare.getCol()) == '.') {
                            //Simulate the move and check if king would be in check
                            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
                            tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'k');
                            newBlackPieces = CreateNewPieceList(tempBoard, blackPieces, moveSquare, stringBuilder);
                            check = CheckIfCheck(tempBoard, whitePieces, newBlackPieces, toBeMoved, stringBuilder);
                            if (check) biggerCheck = true;
                        }
                        else biggerCheck = true;
                    }
                    if (!biggerCheck) castling.add("O-O");
                }
            }
        }
        return castling;
    }
    public static ArrayList<ArrayList<String>> AllMoves(Board board, boolean whiteMove, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder, String enPassantSquare, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling) {
        stringBuilder.setLength(0);
        //Get all the moves
        ArrayList<ArrayList<String>> moves = new ArrayList<>();
        ArrayList<String> pieceMoves;
        //If white moves next, get all the moves for the white pieces
        if (whiteMove) {
            for (String piece : whitePieces) {
                ArrayList<String> newMoves= new ArrayList<>();
                newMoves.add(piece);
                switch (board.getPiece(StringToSquare(piece).getRow(), StringToSquare(piece).getCol())) {
                    case 'P':
                        pieceMoves = (PawnMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder, enPassantSquare));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'R':
                        pieceMoves = (RookMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'B':
                        pieceMoves = (BishopMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'N':
                        pieceMoves = (KnightMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'Q':
                        pieceMoves = (QueenMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'K':
                        pieceMoves = (KingMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;

                }

            }
        }
        else {
            for (String piece : blackPieces) {
                ArrayList<String> newMoves= new ArrayList<>();
                newMoves.add(piece);
                switch (board.getPiece(StringToSquare(piece).getRow(), StringToSquare(piece).getCol())) {
                    case 'p':
                        pieceMoves = (PawnMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder, enPassantSquare));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'r':
                        pieceMoves = (RookMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'b':
                        pieceMoves = (BishopMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'n':
                        pieceMoves = (KnightMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'q':
                        pieceMoves = (QueenMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;
                    case 'k':
                        pieceMoves = (KingMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling));
                        newMoves.addAll(pieceMoves);
                        moves.add(newMoves);
                        break;

                }
            }
        }
        return moves;
    }
    public static void PlayerCastling(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare,  ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder,  boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling, int moveCount100, ArrayList<Hash> hashTable, String userMove, StringBuilder gameNotation, int turnCounter) {
        //If player picked to castle
        //fakeDestinationSquare used later for checking check
        Square fakeDestionationSquare;
        ArrayList<String> newWhitePieces = new ArrayList<>();
        ArrayList<String> newBlackPieces = new ArrayList<>();
        //If player plays white
        if (playerIsWhite) {
            //Copy black pieces
            newBlackPieces.addAll(blackPieces);
            a1Castling = false;
            h1Castling = false;
            boolean kingsideCaslting = false;
            //Determine castling side
            if (userMove.equals("O-O")) kingsideCaslting = true;
            int pieceIndex = 0;
            //If player castling short
            if (kingsideCaslting) {
                //Find add in all the pieces to newWhitePieces apart from king and short rook
                for (String piece : whitePieces) {
                    if (!piece.equals("e1") && !piece.equals("h1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                    pieceIndex++;
                }
                //Perform the castling on the board
                board.setPiece(0, 7, '.');
                board.setPiece(0, 6, 'K');
                board.setPiece(0, 5, 'R');
                board.setPiece(0, 4, '.');
                //Add in the new piece locations for king and short rook into newWHitePieces
                newWhitePieces.add("f1");
                newWhitePieces.add("g1");
                fakeDestionationSquare = new Square(0,6);
            }
            //If castling long
            else {
                //Same thing. FInd all pieces apart from king and long rook, add them, perform move and add new ones
                for (String piece : whitePieces) {
                    if (!piece.equals("e1") && !piece.equals("a1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                    pieceIndex++;
                }
                board.setPiece(0, 4, '.');
                board.setPiece(0, 2, 'K');
                board.setPiece(0, 3, 'R');
                board.setPiece(0, 1, '.');
                board.setPiece(0, 0, '.');
                newWhitePieces.add("c1");
                newWhitePieces.add("d1");
                fakeDestionationSquare = new Square(0,2);
            }
        }
        else {
            //Same thing for when player is playing black
            newWhitePieces.addAll(whitePieces);
            a8Castling = false;
            h8Castling = false;
            boolean kingsideCaslting = false;
            if (userMove.equals("O-O")) kingsideCaslting = true;
            int pieceIndex = 0;
            if (kingsideCaslting) {
                for (String piece : blackPieces) {
                    if (!piece.equals("e8") && !piece.equals("h8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                    pieceIndex++;
                }
                board.setPiece(7, 7, '.');
                board.setPiece(7, 6, 'k');
                board.setPiece(7, 5, 'r');
                board.setPiece(7, 4, '.');
                newBlackPieces.add("f8");
                newBlackPieces.add("g8");
                fakeDestionationSquare = new Square(7,6);
            } else {
                for (String piece : blackPieces) {
                    if (!piece.equals("a8") && !piece.equals("e8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                    pieceIndex++;
                }
                board.setPiece(7, 4, '.');
                board.setPiece(7, 2, 'k');
                board.setPiece(7, 3, 'r');
                board.setPiece(7, 1, '.');
                board.setPiece(7, 0, '.');
                newBlackPieces.add("c8");
                newBlackPieces.add("d8");
                fakeDestionationSquare = new Square(7,2);
            }
        }

        //Add to chess notation
        //If player is white
        if (playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + userMove);
        else gameNotation.append(userMove);
        System.out.println("Computer played " + userMove);

        //Check opponent has legal moves. If not, checkmate/stalemate
        ArrayList<ArrayList<String>> opponentMoves = AllMoves(board, !playerIsWhite, newWhitePieces, newBlackPieces, stringBuilder, null, a1Castling, h1Castling, a8Castling, h8Castling);
        boolean movesAvailable = false;
        for (ArrayList<String> moveList : opponentMoves) {
            if (moveList.size() > 1) {
                movesAvailable = true;
                break;
            }
        }
        //50 move rule
        moveCount100++;
        if (moveCount100 >= 100) {
            System.out.println("Draw by 50 move rule");
            gameNotation.append("1/2-1/2");
            System.out.println(gameNotation);
            System.exit(0);
        }

        //Check 3-fold repetition
        hashTable = HashPosition(board, null, a1Castling, h1Castling, a8Castling, h8Castling, !playerIsWhite, stringBuilder, hashTable);
        //If moves are available, pass onto computer to make their move
        if (movesAvailable) ComputerMove(input, board, playerIsWhite, null, newWhitePieces, newBlackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
        else {
            boolean checkmate = CheckIfCheckmate(board, newWhitePieces, newBlackPieces, fakeDestionationSquare, stringBuilder);
            if (checkmate) {
                System.out.println("Player Wins!");
                gameNotation.delete(gameNotation.length() - 1, gameNotation.length());
                gameNotation.append("# ");
                if (playerIsWhite) gameNotation.append("1-0");
                else gameNotation.append("0-1");
                System.out.println(gameNotation);
                System.exit(0);
            }
            else {
                System.out.println("Stalemate!");
                gameNotation.append("1/2-1/2");
                System.out.println(gameNotation);
                System.exit(0);
            }
        }
    }
    public static void ComputerCastling(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare,  ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder,  boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling, int moveCount100, ArrayList<Hash> hashTable, String computerMove, StringBuilder gameNotation, int turnCounter) {
        Square fakeDestionationSquare;
        ArrayList<String> newWhitePieces = new ArrayList<>();
        ArrayList<String> newBlackPieces = new ArrayList<>();
        if (!playerIsWhite) {
            newBlackPieces.addAll(blackPieces);
            a1Castling = false;
            h1Castling = false;
            boolean kingsideCaslting = false;
            if (computerMove.equals("O-O")) kingsideCaslting = true;
            int pieceIndex = 0;
            if (kingsideCaslting) {
                for (String piece : whitePieces) {
                    if (!piece.equals("e1") && !piece.equals("h1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                    pieceIndex++;
                }
                board.setPiece(0, 7, '.');
                board.setPiece(0, 6, 'K');
                board.setPiece(0, 5, 'R');
                board.setPiece(0, 4, '.');
                newWhitePieces.add("f1");
                newWhitePieces.add("g1");
                fakeDestionationSquare = new Square(0,6);
            } else {
                for (String piece : whitePieces) {
                    if (!piece.equals("e1") && !piece.equals("a1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                    pieceIndex++;
                }
                board.setPiece(0, 4, '.');
                board.setPiece(0, 2, 'K');
                board.setPiece(0, 3, 'R');
                board.setPiece(0, 1, '.');
                board.setPiece(0, 0, '.');
                newWhitePieces.add("c1");
                newWhitePieces.add("d1");
                fakeDestionationSquare = new Square(0,2);
            }
        }
        else {
            newWhitePieces.addAll(whitePieces);
            a8Castling = false;
            h8Castling = false;
            boolean kingsideCaslting = false;
            if (computerMove.equals("O-O")) kingsideCaslting = true;
            int pieceIndex = 0;
            if (kingsideCaslting) {
                for (String piece : blackPieces) {
                    if (!piece.equals("e8") && !piece.equals("h8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                    pieceIndex++;
                }
                board.setPiece(7, 7, '.');
                board.setPiece(7, 6, 'k');
                board.setPiece(7, 5, 'r');
                board.setPiece(7, 4, '.');
                newBlackPieces.add("f8");
                newBlackPieces.add("g8");
                fakeDestionationSquare = new Square(7,6);
            } else {
                for (String piece : blackPieces) {
                    if (!piece.equals("a8") && !piece.equals("e8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                    pieceIndex++;
                }
                board.setPiece(7, 4, '.');
                board.setPiece(7, 2, 'k');
                board.setPiece(7, 3, 'r');
                board.setPiece(7, 1, '.');
                board.setPiece(7, 0, '.');
                newBlackPieces.add("c8");
                newBlackPieces.add("d8");
                fakeDestionationSquare = new Square(7,2);
            }
        }
        //Add to chess notation
        //If computer is white
        if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + computerMove);
        else gameNotation.append(computerMove);
        System.out.println("Computer played " + computerMove);

        //Check opponent has legal moves. If not, checkmate/stalemate
        ArrayList<ArrayList<String>> opponentMoves = AllMoves(board, playerIsWhite, newWhitePieces, newBlackPieces, stringBuilder, null, a1Castling, h1Castling, a8Castling, h8Castling);
        boolean movesAvailable = false;
        for (ArrayList<String> moveList : opponentMoves) {
            if (moveList.size() > 1) {
                movesAvailable = true;
                break;
            }
        }
        //50 move rule
        moveCount100++;
        if (moveCount100 >= 100) {
            System.out.println("Draw by 50 move rule");
            gameNotation.append("1/2-1/2");
            System.out.println(gameNotation);
            System.exit(0);
        }

        //Check 3-fold repetition
        hashTable = HashPosition(board, null, a1Castling, h1Castling, a8Castling, h8Castling, playerIsWhite, stringBuilder, hashTable);
        //If moves are available, pass onto computer to make their move
        if (movesAvailable) PlayerMove(input, board, playerIsWhite, null, newWhitePieces, newBlackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
        else {
            boolean checkmate = CheckIfCheckmate(board, whitePieces, blackPieces, fakeDestionationSquare, stringBuilder);
            if (checkmate) {
                System.out.println("Computer Wins!");
                gameNotation.delete(gameNotation.length() - 1, gameNotation.length());
                gameNotation.append("# ");
                if (!playerIsWhite) gameNotation.append("1-0");
                else gameNotation.append("0-1");
                System.out.println(gameNotation);
                System.exit(0);
            }
            else {
                System.out.println("Stalemate.");
                gameNotation.append("1/2-1/2");
                System.out.println(gameNotation);
                System.exit(0);
            }
        }
    }
    public static void ComputerMove(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling, int moveCount100, ArrayList<Hash> hashTable, StringBuilder gameNotation, int turnCounter) {
        stringBuilder.setLength(0);
        //Computer makes random moves. Get all the moves possible
        ArrayList<ArrayList<String>> allMoves =  AllMoves(board, !playerIsWhite, whitePieces, blackPieces, stringBuilder, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling);
        //Get all move number (to be able to make rng moves)
        int pieceIndex = 0;
        int moveCount = 0;
        //Add length of list (-1 as 1st entry denotes origin square) to the count
        for (ArrayList<String> moveList : allMoves) if (moveList.size() >= 2) moveCount += moveList.size() - 1;
        Random random = new Random();
        int randomMove = random.nextInt(moveCount) + 1;
        //Now just cycle through each list, finding out what the move is
        String originSquare = null;
        String destinationSquare = null;
        int listNumber = 0;
        //For each list, subtract length from the count of random move. If it becomes negative, the move is in that list
        for (ArrayList<String> moveList : allMoves) {
            if ((moveList.size() - 1) >= randomMove) break;
            else {
                randomMove -= moveList.size() - 1;
                listNumber++;
            }
        }
        //So the random move picked is in the 'listNumber' list, and is the nth move where n = randomMove + 1
        if (allMoves.get(listNumber).get(randomMove).equals("O-O") || allMoves.get(listNumber).get(randomMove).equals("O-O-O")) {
            String computerMove = allMoves.get(listNumber).get(randomMove);
            ComputerCastling(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, computerMove, gameNotation, turnCounter);
        }
        originSquare = allMoves.get(listNumber).getFirst();
        destinationSquare = allMoves.get(listNumber).get(randomMove);
        Square toBeMoved = StringToSquare(originSquare);
        Square moveSquare = StringToSquare(destinationSquare);
        boolean captureMade = false;
        //If white piece captured
        if (Character.isUpperCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
            whitePieces.remove(destinationSquare);
            captureMade = true;
        }
        //If black piece captured
        else if (Character.isLowerCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
            blackPieces.remove(destinationSquare);
            captureMade = true;
        }
        //Update enPassantSquare
        if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'P' && toBeMoved.getRow() == 1 && moveSquare.getRow() == 3) enPassantSquare = CoordinateToString(2, toBeMoved.getCol(), stringBuilder);
        else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'p' && toBeMoved.getRow() == 6 && moveSquare.getRow() == 4) enPassantSquare = CoordinateToString(5, toBeMoved.getCol(), stringBuilder);
        else enPassantSquare = null;
        //Update castling rights
        if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'K') { a1Castling = false; h1Castling = false; }
        else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'k') { a8Castling = false; h8Castling = false; }
        else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 0) a1Castling = false;
        else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 7) h1Castling = false;
        else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 0) a8Castling = false;
        else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 7) h8Castling = false;
        else if (destinationSquare.equals("a1")) a1Castling = false;
        else if (destinationSquare.equals("h1")) h1Castling = false;
        else if (destinationSquare.equals("a8")) a8Castling = false;
        else if (destinationSquare.equals("h8")) h8Castling = false;
        //Update whitePieces + blackPieces
        char pieceSymbol = board.getPiece(toBeMoved.getRow(), toBeMoved.getCol());
        board.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
        board.setPiece(moveSquare.getRow(), moveSquare.getCol(), pieceSymbol);

        //Add to chess notation
        //If computer is white
        char newPieceSymbol = board.getPiece(moveSquare.getRow(), moveSquare.getCol());
        switch (newPieceSymbol) {
            case 'P':
            case 'p':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + originSquare + destinationSquare + " ");
                else gameNotation.append(originSquare + destinationSquare + " ");
                break;
            case 'B':
            case 'b':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "B" + originSquare + destinationSquare + " ");
                else gameNotation.append("B" + originSquare + destinationSquare + " ");
                break;
            case 'N':
            case 'n':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "N" + originSquare + destinationSquare + " ");
                else gameNotation.append("N" + originSquare + destinationSquare + " ");
                break;
            case 'R':
            case 'r':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "R" + originSquare + destinationSquare + " ");
                else gameNotation.append("R" + originSquare + destinationSquare + " ");
                break;
            case 'Q':
            case 'q':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "Q" + originSquare + destinationSquare + " ");
                else gameNotation.append("Q" + originSquare + destinationSquare + " ");
                break;
            case 'K':
            case 'k':
                if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "K" + originSquare + destinationSquare + " ");
                else gameNotation.append("K" + originSquare + destinationSquare + " ");
                break;
        }

        int pieceCount = 0;
        if (playerIsWhite) {
            for (String piece : blackPieces) {
                if (piece.equals(originSquare)) break;
                else pieceCount++;
            }
            blackPieces.remove(pieceCount);
            blackPieces.add(destinationSquare);
        }
        else {
            for (String piece : whitePieces) {
                if (piece.equals(originSquare)) break;
                else pieceCount++;
            }
            whitePieces.remove(pieceCount);
            whitePieces.add(destinationSquare);
        }

        //Handle Promotion
        if ((pieceSymbol == 'P' && moveSquare.getRow() == 7) || (pieceSymbol == 'p' && moveSquare.getRow() == 0)) {
            char[] promotionOptions = {'Q', 'R', 'B', 'N'};
            int randomPromotion = random.nextInt(promotionOptions.length);
            char newPiece = promotionOptions[randomPromotion];
            if (!playerIsWhite) board.setPiece(moveSquare.getRow(), moveSquare.getCol(), newPiece);
            else board.setPiece(moveSquare.getRow(), moveSquare.getCol(), Character.toLowerCase(newPiece));
            gameNotation.delete(gameNotation.length() - 1, gameNotation.length());
            gameNotation.append("=" + newPiece + " ");

        }
        //Check opponent has legal moves. If not, checkmate/stalemate
        ArrayList<ArrayList<String>> opponentMoves = AllMoves(board, playerIsWhite, whitePieces, blackPieces, stringBuilder, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling);
        boolean movesAvailable = false;
        for (ArrayList<String> moveList : opponentMoves) {
            if (moveList.size() > 1) {
                movesAvailable = true;
                break;
            }
        }
        //Say what move was played
        System.out.println("Computer played " + originSquare + " to " + destinationSquare);
        //50 move rule
        moveCount100++;
        if (captureMade) moveCount100 = 0;
        else if (pieceSymbol == ('p' | 'P')) moveCount100 = 0;
        if (moveCount100 >= 100) {
            System.out.println("Draw by 50 move rule");
            System.exit(0);
        }

        //Check 3-fold repetition
        hashTable = HashPosition(board, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling, playerIsWhite, stringBuilder, hashTable);
        //If moves are available, pass onto player to make their move
        if (movesAvailable) PlayerMove(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
        else {
            boolean checkmate = CheckIfCheckmate(board, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (checkmate) {
                System.out.println("Computer Wins!");
                gameNotation.delete(gameNotation.length() - 1, gameNotation.length());
                gameNotation.append("# ");
                if (!playerIsWhite) gameNotation.append("1-0");
                else gameNotation.append("0-1");
                System.out.println(gameNotation);
                System.exit(0);
            }
            else {
                System.out.println("Stalemate! Lol skill issue");
                gameNotation.append("1/2-1/2");
                System.out.println(gameNotation);
                System.exit(0);
            }
        }
    }
    public static ArrayList<Hash> HashPosition(Board board, String enPassantSquare, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling, boolean whiteMovesNext, StringBuilder stringBuilder, ArrayList<Hash> hashTable) {
        stringBuilder.setLength(0);
        //Get the board position currently
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                stringBuilder.append(board.getPiece(row, col));
            }
        }
        stringBuilder.append(Objects.requireNonNullElse(enPassantSquare, "null"));
        if (a1Castling) stringBuilder.append("1");
        else stringBuilder.append("0");
        if (h1Castling) stringBuilder.append("1");
        else stringBuilder.append("0");
        if (a8Castling) stringBuilder.append("1");
        else stringBuilder.append("0");
        if (h8Castling) stringBuilder.append("1");
        else stringBuilder.append("0");
        if (whiteMovesNext) stringBuilder.append("1");
        else stringBuilder.append("0");
        String hash = stringBuilder.toString();
        boolean reachedThrice = false;
        boolean exists = false;
        for (Hash singleHash : hashTable) {
            if (singleHash.getHashedPosition().equals(hash)) {
                exists = true;
                singleHash.increment();
                if (singleHash.getCount() >= 3) {
                    System.out.println("Draw by threefold repetition");
                    System.exit(0);
                }
                break;
            }
        }
        if (!exists) {
            Hash newHash = new Hash(hash, 1);
            hashTable.add(newHash);
        }
        return hashTable;
    }
    public static Board CopyBoard(Board board) {
        Board newBoard = new Board();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                newBoard.setPiece(row, col, board.getPiece(row, col));
            }
        }
        return  newBoard;
    }
    public static ArrayList<String> CreateNewPieceList (Board board, ArrayList<String> pieces, Square moveSquare, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Create a list of the updated pieces to be passed temporarily into functions for move gathering, etc
        ArrayList<String> newPieces = new ArrayList<>();
        //If a piece exists on the new board where the list shows, keep it in the new list. Otherwise, don't add it. Then add the piece that has moved on the moveSquare
        for (String piece : pieces) {
            Square pieceSquare = StringToSquare(piece);
            //Add all the unchanged pieces
            if (board.getPiece(pieceSquare.getRow(), pieceSquare.getCol()) != '.') newPieces.add(piece);
        }
        //Add the new piece
        newPieces.add(SquareToString(moveSquare, stringBuilder));
        return newPieces;
    }
    public static String CoordinateToString(int row, int col, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Variables to store the letter and number of the square
        String letter = Character.toString(col + 97);
        String number = Character.toString(row + 49);
        //Write square to string builder
        stringBuilder.append(letter);
        stringBuilder.append(number);
        return stringBuilder.toString();
    }
    public static Square StringToSquare(String coordinate) {
        //row is number, column is letter
        int row = Integer.parseInt(coordinate.substring(1,2)) - 1;
        char letter = coordinate.charAt(0);
        int col = letter - 'a';
        return new Square(row, col);
    }
    public static String SquareToString(Square square, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        int row = square.getRow();
        int col = square.getCol();
        //letter is column
        String letter = Character.toString(col + 97);
        //number is the row
        String number = Character.toString(row + 49);
        stringBuilder.append(letter);
        stringBuilder.append(number);
        return stringBuilder.toString();
    }
    public static boolean CheckIfCheckmate(Board tempBoard,  ArrayList<String> whitePieces, ArrayList<String> blackPieces, Square destinationSquare, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        //Determine attacking colour
        boolean blackAttack = true;
        ArrayList<String> pieceAttacks;
        ArrayList<String> allAttacks = new ArrayList<>();
        //If white piece is the one that got moved, white is attacking
        if (Character.isUpperCase(tempBoard.getPiece(destinationSquare.getRow(), destinationSquare.getCol()))) blackAttack = false;
        if (blackAttack) {
            //For each black piece, get its attacks, and if any are attacking the king then it's check
            for (String piece : blackPieces) {
                //Each piece is represesnted as coordinate: "e7". Take each coordinate, convert to board position, check board position
                //for piece type (represented by letter) and get its attacks
                Square attackSquare =  StringToSquare(piece);
                switch (tempBoard.getPiece(attackSquare.getRow(), attackSquare.getCol())) {
                    case ('p'):
                        pieceAttacks = PawnAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('r'):
                        pieceAttacks = RookAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('b'):
                        pieceAttacks = BishopAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('n'):
                        pieceAttacks = KnightAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('q'):
                        pieceAttacks = QueenAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('k'):
                        pieceAttacks = KingAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;

                }

            }
            //Once we have all the attacks, check that none are attacking the king square
            //Find king
            String kingSquare = null;
            for (String piece : whitePieces) {
                Square pieceSquare =  StringToSquare(piece);
                if (tempBoard.getPiece(pieceSquare.getRow(), pieceSquare.getCol()) == 'K') {
                    kingSquare = piece;
                }
            }
            for (String attack : allAttacks) {
                if (attack.equals(kingSquare)) return true;
            }
            return false;

        }
        else {
            //For each white piece, do the same
            for (String piece : whitePieces) {
                Square attackSquare =  StringToSquare(piece);
                switch (tempBoard.getPiece(attackSquare.getRow(), attackSquare.getCol())) {
                    case ('P'):
                        pieceAttacks = PawnAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('R'):
                        pieceAttacks = RookAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('B'):
                        pieceAttacks = BishopAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('N'):
                        pieceAttacks = KnightAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('Q'):
                        pieceAttacks = QueenAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;
                    case ('K'):
                        pieceAttacks = KingAttacks(tempBoard, piece, stringBuilder);
                        allAttacks.addAll(pieceAttacks);
                        break;

                }

            }
            String kingSquare = null;
            for (String piece : blackPieces) {
                Square pieceSquare =  StringToSquare(piece);
                if (tempBoard.getPiece(pieceSquare.getRow(), pieceSquare.getCol()) == 'k') {
                    kingSquare = piece;
                }
            }
            for (String attack : allAttacks) {
                if (attack.equals(kingSquare)) return true;
            }
            return false;
        }
    }
    public static GameState SelfHashPosition(GameState gameState) {
        //Get required variables
        Board board = gameState.getBoard();
        StringBuilder stringBuilder = gameState.getStringBuilder();
        stringBuilder.setLength(0); //Reset stringBuilder
        String enPassantSquare = gameState.getEnPassantSquare();
        boolean a1Castling = gameState.getA1Castling();
        boolean h1Castling = gameState.getH1Castling();
        boolean a8Castling = gameState.getA8Castling();
        boolean h8Castling = gameState.getH8Castling();
        boolean whiteMovesNext = gameState.getPlayerIsWhite();
        ArrayList<Hash> hashTable = gameState.getHashTable();
        StringBuilder gameNotation = gameState.getGameNotation();

        //Get the board position currently
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                stringBuilder.append(board.getPiece(row, col));
            }
        }
        stringBuilder.append(Objects.requireNonNullElse(enPassantSquare, "null"));
        if (a1Castling) stringBuilder.append("1");
        else stringBuilder.append("0");
        if (h1Castling) stringBuilder.append("1");
        else stringBuilder.append("0");
        if (a8Castling) stringBuilder.append("1");
        else stringBuilder.append("0");
        if (h8Castling) stringBuilder.append("1");
        else stringBuilder.append("0");
        if (whiteMovesNext) stringBuilder.append("1");
        else stringBuilder.append("0");
        String hash = stringBuilder.toString();
        boolean exists = false;
        for (Hash singleHash : hashTable) {
            if (singleHash.getHashedPosition().equals(hash)) {
                exists = true;
                singleHash.increment();
                if (singleHash.getCount() >= 3) {
                    gameNotation.append("1/2-1/2");
                    gameState.setRepetitionDraw(true);
                }
                break;
            }
        }
        if (!exists) {
            Hash newHash = new Hash(hash, 1);
            hashTable.add(newHash);
        }
        return gameState;
    }
    public static void SelfStart() {
        while (true) {
            //Create all the things to store in GameState
            Board board = new Board();
            boolean playerIsWhite = false;
            String enPassantSquare = null;
            ArrayList<String> whitePieces = new ArrayList<>();
            whitePieces.add("a1");whitePieces.add("b1");whitePieces.add("c1");whitePieces.add("d1");whitePieces.add("e1");whitePieces.add("f1");whitePieces.add("g1");whitePieces.add("h1");whitePieces.add("a2");whitePieces.add("b2");whitePieces.add("c2");whitePieces.add("d2");whitePieces.add("e2");whitePieces.add("f2");whitePieces.add("g2");whitePieces.add("h2");
            ArrayList<String> blackPieces = new ArrayList<>();
            blackPieces.add("a7");blackPieces.add("b7");blackPieces.add("c7");blackPieces.add("d7");blackPieces.add("e7");blackPieces.add("f7");blackPieces.add("g7");blackPieces.add("h7");blackPieces.add("a8");blackPieces.add("b8");blackPieces.add("c8");blackPieces.add("d8");blackPieces.add("e8");blackPieces.add("f8");blackPieces.add("g8");blackPieces.add("h8");
            StringBuilder stringBuilder = new StringBuilder();
            boolean a1Castling = true;
            boolean h1Castling = true;
            boolean a8Castling = true;
            boolean h8Castling = true;
            int moveCount100 = 0;
            ArrayList<Hash> hashTable = new ArrayList<>();
            StringBuilder gameNotation = new StringBuilder();
            int turnCounter = 0;
            GameState gameState = new GameState(board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
            boolean finished = false;
            //Run the game loop until checkmate (done this way to avoid my other implementation which ran into stack overflow)
            while (!finished) {
                //Play a move
                gameState = SelfMove(gameState);
                //Check for draw and checkmate
                if (gameState.getWhiteWin()) {System.out.println("White wins!"); System.out.println(gameState.getGameNotation()); finished = true;}
                else if (gameState.getBlackWin()) {System.out.println("Black wins!"); System.out.println(gameState.getGameNotation()); finished = true;}
                else if (gameState.getStalemate()) {System.out.println("Stalemate!"); System.out.println(gameState.getGameNotation()); finished = true;}
                else if (gameState.getMoveRule50()) {System.out.println("Draw by 50 move rule!"); System.out.println(gameState.getGameNotation()); finished = true;}
                else if (gameState.getRepetitionDraw()) {System.out.println("Draw by repetition!"); System.out.println(gameState.getGameNotation()); finished = true;}
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("GameCollection.txt", true))) {
                writer.write(gameState.getGameNotation().toString());
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Error writing");
            }

        }
    }
    public static GameState SelfMove(GameState gameState) {
        //Increment turn counter
        gameState.setTurnCounter(gameState.getTurnCounter() + 1);
        //Get all the required variables
        Board board = gameState.getBoard();
        boolean playerIsWhite = gameState.getPlayerIsWhite();
        String enPassantSquare = gameState.getEnPassantSquare();
        ArrayList<String> whitePieces = gameState.getWhitePieces();
        ArrayList<String> blackPieces = gameState.getBlackPieces();
        StringBuilder stringBuilder = gameState.getStringBuilder();
        stringBuilder.setLength(0); //(Reset string builder)
        boolean a1Castling = gameState.getA1Castling();
        boolean h1Castling = gameState.getH1Castling();
        boolean a8Castling = gameState.getA8Castling();
        boolean h8Castling = gameState.getH8Castling();
        int moveCount100 = gameState.getMoveCount100();
        ArrayList<Hash> hashTable = gameState.getHashTable();
        StringBuilder gameNotation = gameState.getGameNotation();
        boolean captureMade = false;
        char pieceSymbol = '0';
        int turnCounter = gameState.getTurnCounter();
        Square moveSquare;

        //Computer makes random moves. Get all the moves possible
        ArrayList<ArrayList<String>> allMoves =  AllMoves(board, !playerIsWhite, whitePieces, blackPieces, stringBuilder, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling);
        //Get all move number (to be able to make rng moves)
        int moveCount = 0;
        //Add length of list (-1 as 1st entry denotes origin square) to the count
        for (ArrayList<String> moveList : allMoves) if (moveList.size() >= 2) moveCount += moveList.size() - 1;
        Random random = new Random();
        int randomMove = random.nextInt(moveCount) + 1;
        //Now just cycle through each list, finding out what the move is
        String originSquare = null;
        String destinationSquare = null;
        int listNumber = 0;
        //For each list, subtract length from the count of random move. If it becomes negative, the move is in that list
        for (ArrayList<String> moveList : allMoves) {
            if ((moveList.size() - 1) >= randomMove) break;
            else {
                randomMove -= moveList.size() - 1;
                listNumber++;
            }
        }
        //If computer picked castling
        if (allMoves.get(listNumber).get(randomMove).equals("O-O") || allMoves.get(listNumber).get(randomMove).equals("O-O-O")) {
            String computerMove = allMoves.get(listNumber).get(randomMove);
            ArrayList<String> newWhitePieces = new ArrayList<>();
            ArrayList<String> newBlackPieces = new ArrayList<>();
            if (!playerIsWhite) {
                newBlackPieces.addAll(blackPieces);
                a1Castling = false;
                h1Castling = false;
                boolean kingsideCaslting = false;
                if (computerMove.equals("O-O")) kingsideCaslting = true;
                int pieceIndex = 0;
                if (kingsideCaslting) {
                    for (String piece : whitePieces) {
                        if (!piece.equals("e1") && !piece.equals("h1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                        pieceIndex++;
                    }
                    board.setPiece(0, 7, '.');
                    board.setPiece(0, 6, 'K');
                    board.setPiece(0, 5, 'R');
                    board.setPiece(0, 4, '.');
                    newWhitePieces.add("f1");
                    newWhitePieces.add("g1");
                    moveSquare = new Square(0,6);
                } else {
                    for (String piece : whitePieces) {
                        if (!piece.equals("e1") && !piece.equals("a1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                        pieceIndex++;
                    }
                    board.setPiece(0, 4, '.');
                    board.setPiece(0, 2, 'K');
                    board.setPiece(0, 3, 'R');
                    board.setPiece(0, 1, '.');
                    board.setPiece(0, 0, '.');
                    newWhitePieces.add("c1");
                    newWhitePieces.add("d1");
                    moveSquare = new Square(0,2);
                }
            }
            else {
                newWhitePieces.addAll(whitePieces);
                a8Castling = false;
                h8Castling = false;
                boolean kingsideCaslting = false;
                if (computerMove.equals("O-O")) kingsideCaslting = true;
                int pieceIndex = 0;
                if (kingsideCaslting) {
                    for (String piece : blackPieces) {
                        if (!piece.equals("e8") && !piece.equals("h8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                        pieceIndex++;
                    }
                    board.setPiece(7, 7, '.');
                    board.setPiece(7, 6, 'k');
                    board.setPiece(7, 5, 'r');
                    board.setPiece(7, 4, '.');
                    newBlackPieces.add("f8");
                    newBlackPieces.add("g8");
                    moveSquare = new Square(7,6);
                } else {
                    for (String piece : blackPieces) {
                        if (!piece.equals("a8") && !piece.equals("e8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                        pieceIndex++;
                    }
                    board.setPiece(7, 4, '.');
                    board.setPiece(7, 2, 'k');
                    board.setPiece(7, 3, 'r');
                    board.setPiece(7, 1, '.');
                    board.setPiece(7, 0, '.');
                    newBlackPieces.add("c8");
                    newBlackPieces.add("d8");
                    moveSquare = new Square(7,2);
                }
            }
            //Add to chess notation
            if (!playerIsWhite) gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + computerMove);
            else gameNotation.append(computerMove);
            whitePieces = newWhitePieces;
            blackPieces = newBlackPieces;
            enPassantSquare = null;
        }
        //If non castling move picked
        else {
            originSquare = allMoves.get(listNumber).getFirst();
            destinationSquare = allMoves.get(listNumber).get(randomMove);
            Square toBeMoved = StringToSquare(originSquare);
            moveSquare = StringToSquare(destinationSquare);

            //If white piece captured
            if (Character.isUpperCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
                whitePieces.remove(destinationSquare);
                captureMade = true;
            }
            //If black piece captured
            else if (Character.isLowerCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
                blackPieces.remove(destinationSquare);
                captureMade = true;
            }
            //Update enPassantSquare
            if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'P' && toBeMoved.getRow() == 1 && moveSquare.getRow() == 3)
                enPassantSquare = CoordinateToString(2, toBeMoved.getCol(), stringBuilder);
            else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'p' && toBeMoved.getRow() == 6 && moveSquare.getRow() == 4)
                enPassantSquare = CoordinateToString(5, toBeMoved.getCol(), stringBuilder);
            else enPassantSquare = null;
            //Update castling rights
            if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'K') {
                a1Castling = false;
                h1Castling = false;
            } else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'k') {
                a8Castling = false;
                h8Castling = false;
            } else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 0) a1Castling = false;
            else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 7) h1Castling = false;
            else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 0) a8Castling = false;
            else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 7) h8Castling = false;
            else if (destinationSquare.equals("a1")) a1Castling = false;
            else if (destinationSquare.equals("h1")) h1Castling = false;
            else if (destinationSquare.equals("a8")) a8Castling = false;
            else if (destinationSquare.equals("h8")) h8Castling = false;
            //Make move
            pieceSymbol = board.getPiece(toBeMoved.getRow(), toBeMoved.getCol());
            board.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            board.setPiece(moveSquare.getRow(), moveSquare.getCol(), pieceSymbol);
            //Add to chess notation
            char newPieceSymbol = board.getPiece(moveSquare.getRow(), moveSquare.getCol());
            switch (newPieceSymbol) {
                case 'P':
                case 'p':
                    if (!playerIsWhite)
                        gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + originSquare + destinationSquare + " ");
                    else gameNotation.append(originSquare + destinationSquare + " ");
                    break;
                case 'B':
                case 'b':
                    if (!playerIsWhite)
                        gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "B" + originSquare + destinationSquare + " ");
                    else gameNotation.append("B" + originSquare + destinationSquare + " ");
                    break;
                case 'N':
                case 'n':
                    if (!playerIsWhite)
                        gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "N" + originSquare + destinationSquare + " ");
                    else gameNotation.append("N" + originSquare + destinationSquare + " ");
                    break;
                case 'R':
                case 'r':
                    if (!playerIsWhite)
                        gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "R" + originSquare + destinationSquare + " ");
                    else gameNotation.append("R" + originSquare + destinationSquare + " ");
                    break;
                case 'Q':
                case 'q':
                    if (!playerIsWhite)
                        gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "Q" + originSquare + destinationSquare + " ");
                    else gameNotation.append("Q" + originSquare + destinationSquare + " ");
                    break;
                case 'K':
                case 'k':
                    if (!playerIsWhite)
                        gameNotation.append(String.valueOf(((turnCounter - 1) / 2) + 1) + ": " + "K" + originSquare + destinationSquare + " ");
                    else gameNotation.append("K" + originSquare + destinationSquare + " ");
                    break;
            }
            //Update whitePieces + blackPieces
            int pieceCount = 0;
            if (playerIsWhite) {
                for (String piece : blackPieces) {
                    if (piece.equals(originSquare)) break;
                    else pieceCount++;
                }
                blackPieces.remove(pieceCount);
                blackPieces.add(destinationSquare);
            } else {
                for (String piece : whitePieces) {
                    if (piece.equals(originSquare)) break;
                    else pieceCount++;
                }
                whitePieces.remove(pieceCount);
                whitePieces.add(destinationSquare);
            }
            //Handle Promotion
            if ((pieceSymbol == 'P' && moveSquare.getRow() == 7) || (pieceSymbol == 'p' && moveSquare.getRow() == 0)) {
                char[] promotionOptions = {'Q', 'R', 'B', 'N'};
                int randomPromotion = random.nextInt(promotionOptions.length);
                char newPiece = promotionOptions[randomPromotion];
                if (!playerIsWhite) board.setPiece(moveSquare.getRow(), moveSquare.getCol(), newPiece);
                else board.setPiece(moveSquare.getRow(), moveSquare.getCol(), Character.toLowerCase(newPiece));
                gameNotation.delete(gameNotation.length() - 1, gameNotation.length());
                gameNotation.append("=" + newPiece + " ");

            }

        }
        //Check opponent has legal moves. If not, checkmate/stalemate
        ArrayList<ArrayList<String>> opponentMoves = AllMoves(board, playerIsWhite, whitePieces, blackPieces, stringBuilder, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling);
        boolean movesAvailable = false;
        for (ArrayList<String> moveList : opponentMoves) {
            if (moveList.size() > 1) {
                movesAvailable = true;
                break;
            }
        }
        //Reassign needed variables in gameState (those that don't change automatically)
        gameState.setEnPassantSquare(enPassantSquare);
        gameState.setA1Castling(a1Castling);
        gameState.setH1Castling(h1Castling);
        gameState.setA8Castling(a8Castling);
        gameState.setH8Castling(h8Castling);


        //50 move rule
        moveCount100++;
        if (!allMoves.get(listNumber).get(randomMove).equals("O-O") || !allMoves.get(listNumber).get(randomMove).equals("O-O-O")) {
            if (captureMade) moveCount100 = 0;
            else if (pieceSymbol == ('p' | 'P')) moveCount100 = 0;
        }
        if (moveCount100 >= 100) {
            gameNotation.append("1/2-1/2");
            gameState.setMoveRule50(true);
            return gameState;
        }
        gameState.setMoveCount100(moveCount100);

        //Check 3-fold repetition
        gameState = SelfHashPosition(gameState);
        //Done after as SelfHashPosition still uses old playerIsWhite to determine which colour goes next
        gameState.setPlayerIsWhite(!playerIsWhite);
        //If moves are available, pass onto player to make their move
        if (movesAvailable) return gameState;
        else {
            boolean checkmate = CheckIfCheckmate(board, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (checkmate) {
                gameNotation.delete(gameNotation.length() - 1, gameNotation.length());
                gameNotation.append("# ");
                if (!playerIsWhite) {gameNotation.append("1-0"); gameState.setWhiteWin(true);}
                else {gameNotation.append("0-1"); gameState.setBlackWin(true);}
            }
            else {
                gameNotation.append("1/2-1/2");
                gameState.setStalemate(true);
            }
            return gameState;
        }
    }

    public static void TrainModel() {
        while (true) {
            //Initially just testing an idea. Hence model has 3 parameters, and a depth of 1
            //Instantiate models with temp values (real values will be read from files)
            PlayingModel bestModel = new PlayingModel(0, 0, 0);
            PlayingModel currentModel = new PlayingModel(0, 0, 0);
            //Read in the best model so far from file
            try (BufferedReader bestReader = new BufferedReader(new FileReader("BestModel.txt"))) {
                String line;
                while ((line = bestReader.readLine()) != null) {
                    String[] splitLine = line.split(" ");
                    switch (splitLine[0]) {
                        case "QueenActivity":
                            bestModel.setQueenActivity(Double.parseDouble(splitLine[1]));
                            break;
                        case "RookActivity":
                            bestModel.setRookActivity(Double.parseDouble(splitLine[1]));
                            break;
                        case "PointsSurroundingKing":
                            bestModel.setPointsSurroundingKing(Double.parseDouble(splitLine[1]));
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading best");
            }
            //Read in the current model being worked on
            try (BufferedReader currentReader = new BufferedReader(new FileReader("CurrentModel.txt"))) {
                String line;
                while ((line = currentReader.readLine()) != null) {
                    String[] splitLine = line.split(" ");
                    switch (splitLine[0]) {
                        case "QueenActivity":
                            currentModel.setQueenActivity(Double.parseDouble(splitLine[1]));
                            break;
                        case "RookActivity":
                            currentModel.setRookActivity(Double.parseDouble(splitLine[1]));
                            break;
                        case "PointsSurroundingKing":
                            currentModel.setPointsSurroundingKing(Double.parseDouble(splitLine[1]));
                            break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading current");
            }
            //During training, current model plays best many times, and average win/loss reward. If positive, update best as current (WIP solution)
            //Reward formula = 100 * (0.9 ^ (Turns taken / 2)), negative for loss. Draw = 0 reward. (Idk if this works at all but it's a start)
            //For now random move is best model
            int noOfGamesToDetBetterModel = 10000; //Models play 10000 games, if one has a positive reward it (in theory) is better
            int reward = 0;
            //Set up game
            //Play half of the games as white
            for (int i = 0; i < noOfGamesToDetBetterModel / 2; i++) {
                //Set up variables for GameState
                Board board = new Board();
                boolean whiteTurn = true;
                String enPassantSquare = null;
                ArrayList<String> whitePieces = new ArrayList<>();
                whitePieces.add("a1");
                whitePieces.add("b1");
                whitePieces.add("c1");
                whitePieces.add("d1");
                whitePieces.add("e1");
                whitePieces.add("f1");
                whitePieces.add("g1");
                whitePieces.add("h1");
                whitePieces.add("a2");
                whitePieces.add("b2");
                whitePieces.add("c2");
                whitePieces.add("d2");
                whitePieces.add("e2");
                whitePieces.add("f2");
                whitePieces.add("g2");
                whitePieces.add("h2");
                ArrayList<String> blackPieces = new ArrayList<>();
                blackPieces.add("a7");
                blackPieces.add("b7");
                blackPieces.add("c7");
                blackPieces.add("d7");
                blackPieces.add("e7");
                blackPieces.add("f7");
                blackPieces.add("g7");
                blackPieces.add("h7");
                blackPieces.add("a8");
                blackPieces.add("b8");
                blackPieces.add("c8");
                blackPieces.add("d8");
                blackPieces.add("e8");
                blackPieces.add("f8");
                blackPieces.add("g8");
                blackPieces.add("h8");
                StringBuilder stringBuilder = new StringBuilder();
                boolean a1Castling = true;
                boolean h1Castling = true;
                boolean a8Castling = true;
                boolean h8Castling = true;
                int moveCount100 = 0;
                ArrayList<Hash> hashTable = new ArrayList<>();
                StringBuilder gameNotation = new StringBuilder();
                int turnCounter = 0;
                GameState gameState = new GameState(board, whiteTurn, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
                boolean finished = false;
                //Run the game loop until checkmate (done this way to avoid my other implementation which ran into stack overflow)
                while (!finished) {
                    //Determine if white moves next (model) or black (random)
                    if (gameState.getPlayerIsWhite()) gameState = ModelMove(gameState, currentModel);
                    else gameState = SelfMove(gameState);
                    //Check for draw and checkmate
                    if (gameState.getWhiteWin()) {
                        System.out.println("White (model) wins!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                        //Update reward. There will be number errors, but hopefully small enough to not impact the program too much
                        //With enough games, the little errors should cancel out (if model wins and loses equally) or be stacked on one side (if model wins / loses more than the other)
                        //which wouldn't change the overall outcome of the model being better or worse
                        reward += 100 * Math.pow(0.9, Math.round((gameState.getTurnCounter() + 1) / 2));
                    } else if (gameState.getBlackWin()) {
                        System.out.println("Black (random) wins!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                        reward -= 100 * Math.pow(0.9, Math.round((gameState.getTurnCounter() + 1) / 2));
                    } else if (gameState.getStalemate()) {
                        System.out.println("Stalemate!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                    } else if (gameState.getMoveRule50()) {
                        System.out.println("Draw by 50 move rule!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                    } else if (gameState.getRepetitionDraw()) {
                        System.out.println("Draw by repetition!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                    }
                }
            }
            //Now play half as black
            for (int i = 0; i < noOfGamesToDetBetterModel / 2; i++) {
                //Set up variables for GameState
                Board board = new Board();
                boolean whiteTurn = true;
                String enPassantSquare = null;
                ArrayList<String> whitePieces = new ArrayList<>();
                whitePieces.add("a1");
                whitePieces.add("b1");
                whitePieces.add("c1");
                whitePieces.add("d1");
                whitePieces.add("e1");
                whitePieces.add("f1");
                whitePieces.add("g1");
                whitePieces.add("h1");
                whitePieces.add("a2");
                whitePieces.add("b2");
                whitePieces.add("c2");
                whitePieces.add("d2");
                whitePieces.add("e2");
                whitePieces.add("f2");
                whitePieces.add("g2");
                whitePieces.add("h2");
                ArrayList<String> blackPieces = new ArrayList<>();
                blackPieces.add("a7");
                blackPieces.add("b7");
                blackPieces.add("c7");
                blackPieces.add("d7");
                blackPieces.add("e7");
                blackPieces.add("f7");
                blackPieces.add("g7");
                blackPieces.add("h7");
                blackPieces.add("a8");
                blackPieces.add("b8");
                blackPieces.add("c8");
                blackPieces.add("d8");
                blackPieces.add("e8");
                blackPieces.add("f8");
                blackPieces.add("g8");
                blackPieces.add("h8");
                StringBuilder stringBuilder = new StringBuilder();
                boolean a1Castling = true;
                boolean h1Castling = true;
                boolean a8Castling = true;
                boolean h8Castling = true;
                int moveCount100 = 0;
                ArrayList<Hash> hashTable = new ArrayList<>();
                StringBuilder gameNotation = new StringBuilder();
                int turnCounter = 0;
                GameState gameState = new GameState(board, whiteTurn, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling, moveCount100, hashTable, gameNotation, turnCounter);
                boolean finished = false;
                //Run the game loop until checkmate (done this way to avoid my other implementation which ran into stack overflow)
                while (!finished) {
                    //Determine if white moves next (model) or black (random)
                    if (gameState.getPlayerIsWhite()) gameState = SelfMove(gameState);
                    else gameState = ModelMove(gameState, currentModel);
                    //Check for draw and checkmate
                    if (gameState.getWhiteWin()) {
                        System.out.println("White (model) wins!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                        //Update reward. There will be number errors, but hopefully small enough to not impact the program too much
                        //With enough games, the little errors should cancel out (if model wins and loses equally) or be stacked on one side (if model wins / loses more than the other)
                        //which wouldn't change the overall outcome of the model being better or worse
                        reward += 100 * Math.pow(0.9, Math.round((gameState.getTurnCounter() + 1) / 2));
                    } else if (gameState.getBlackWin()) {
                        System.out.println("Black (random) wins!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                        reward -= 100 * Math.pow(0.9, Math.round((gameState.getTurnCounter() + 1) / 2));
                    } else if (gameState.getStalemate()) {
                        System.out.println("Stalemate!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                    } else if (gameState.getMoveRule50()) {
                        System.out.println("Draw by 50 move rule!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                    } else if (gameState.getRepetitionDraw()) {
                        System.out.println("Draw by repetition!");
                        System.out.println(gameState.getGameNotation());
                        finished = true;
                    }
                }
            }
            //Now that the model has played 10000 games against the previous best implementation, we check if it has a positive reward (and is therefore better)
            if (reward > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                try (BufferedReader currentReader = new BufferedReader(new FileReader("CurrentModel.txt"))) {
                    String line;
                    while ((line = currentReader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                } catch (IOException e) {
                    System.err.println("Error reading current when trying to write to best");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                try (BufferedWriter bestWriter = new BufferedWriter(new FileWriter("BestModel.txt"))) {
                    bestWriter.write(stringBuilder.toString());
                } catch (IOException e) {
                    System.out.println("Error writing");
                }
            }
            //Now we need to change the current model slightly to play against the best model again
            //For now, pick random field to change by a random double +-
            Random random = new Random();
            int parameterChangeIndex = random.nextInt(3);
            double randomChangeValue = random.nextDouble();
            int multiplier = 1;
            if (random.nextBoolean()) {
                multiplier = -1;
            }
            switch (parameterChangeIndex) {
                case 0:
                    currentModel.setQueenActivity(currentModel.getQueenActivity() + (multiplier * randomChangeValue));
                    break;
                case 1:
                    currentModel.setRookActivity(currentModel.getRookActivity() + (multiplier * randomChangeValue));
                    break;
                case 2:
                    currentModel.setPointsSurroundingKing(currentModel.getPointsSurroundingKing() + (multiplier * randomChangeValue));
                    break;
                default:
                    System.out.println("Invalid parameter");
            }
            //Now we save the changes of the current parameter to the file
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("QueenActivity " + currentModel.getQueenActivity() + "\n");
            stringBuilder.append("RookActivity " + currentModel.getRookActivity() + "\n");
            stringBuilder.append("PointsSurroundingKing " + currentModel.getPointsSurroundingKing());
            try (BufferedWriter currentWriter = new BufferedWriter(new FileWriter("CurrentModel.txt"))) {
                currentWriter.write(stringBuilder.toString());
            } catch (IOException e) {
                System.out.println("Error writing to current");
            }
        }
    }

    public static GameState ModelMove(GameState gameState, PlayingModel model) {
        //Increment turn counter
        gameState.setTurnCounter(gameState.getTurnCounter() + 1);
        //Get all the required variables
        Board board = gameState.getBoard();
        boolean modelIsWhite = gameState.getPlayerIsWhite();
        String enPassantSquare = gameState.getEnPassantSquare();
        ArrayList<String> whitePieces = gameState.getWhitePieces();
        ArrayList<String> blackPieces = gameState.getBlackPieces();
        StringBuilder stringBuilder = gameState.getStringBuilder();
        stringBuilder.setLength(0); //(Reset string builder)
        boolean a1Castling = gameState.getA1Castling();
        boolean h1Castling = gameState.getH1Castling();
        boolean a8Castling = gameState.getA8Castling();
        boolean h8Castling = gameState.getH8Castling();
        int moveCount100 = gameState.getMoveCount100();
        ArrayList<Hash> hashTable = gameState.getHashTable();
        StringBuilder gameNotation = gameState.getGameNotation();
        boolean captureMade = false;
        char pieceSymbol = '0';
        int turnCounter = gameState.getTurnCounter();
        Square moveSquare;
        Random random = new Random();
        ArrayList<ModelEvaluation> positionValues = new ArrayList<>();
        //Model needs to evaluate all next possible positions using the evaluation metrics, and pick position with highest score
        //To get all possible next positions, we first get all the moves possibe
        ArrayList<ArrayList<String>> allMoves =  AllMoves(board, !modelIsWhite, whitePieces, blackPieces, stringBuilder, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling);
        //Now, for each possible move, we need to create a new board, simulate that move, and work out the corresponding model value
        for (ArrayList<String> moveList : allMoves) {
            //Get where piece is coming from and what piece
            Square originSquare = StringToSquare(moveList.get(0));
            char tempPieceSymbol = board.getPiece(originSquare.getRow(), originSquare.getCol());
            for (int i = 1; i < moveList.size(); i++) {
                Board tempBoard = CopyBoard(board);
                //If computer picked castling
                if (moveList.get(i).equals("O-O") || moveList.get(i).equals("O-O-O")) {
                    String computerMove = moveList.get(i);
                    ArrayList<String> newWhitePieces = new ArrayList<>();
                    ArrayList<String> newBlackPieces = new ArrayList<>();
                    if (modelIsWhite) {
                        newBlackPieces.addAll(blackPieces);
                        boolean tempA1Castling = false;
                        boolean tempH1Castling = false;
                        boolean tempKingsideCaslting = false;
                        if (computerMove.equals("O-O")) tempKingsideCaslting = true;
                        int pieceIndex = 0;
                        if (tempKingsideCaslting) {
                            for (String piece : whitePieces) {
                                if (!piece.equals("e1") && !piece.equals("h1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                                pieceIndex++;
                            }
                            tempBoard.setPiece(0, 7, '.');
                            tempBoard.setPiece(0, 6, 'K');
                            tempBoard.setPiece(0, 5, 'R');
                            tempBoard.setPiece(0, 4, '.');
                            newWhitePieces.add("f1");
                            newWhitePieces.add("g1");
                        } else {
                            for (String piece : whitePieces) {
                                if (!piece.equals("e1") && !piece.equals("a1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                                pieceIndex++;
                            }
                            tempBoard.setPiece(0, 4, '.');
                            tempBoard.setPiece(0, 2, 'K');
                            tempBoard.setPiece(0, 3, 'R');
                            tempBoard.setPiece(0, 1, '.');
                            tempBoard.setPiece(0, 0, '.');
                            newWhitePieces.add("c1");
                            newWhitePieces.add("d1");
                        }
                    }
                    else {
                        newWhitePieces.addAll(whitePieces);
                        boolean tempA8Castling = false;
                        boolean tempH8Castling = false;
                        boolean tempKingsideCaslting = false;
                        if (computerMove.equals("O-O")) tempKingsideCaslting = true;
                        int pieceIndex = 0;
                        if (tempKingsideCaslting) {
                            for (String piece : blackPieces) {
                                if (!piece.equals("e8") && !piece.equals("h8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                                pieceIndex++;
                            }
                            tempBoard.setPiece(7, 7, '.');
                            tempBoard.setPiece(7, 6, 'k');
                            tempBoard.setPiece(7, 5, 'r');
                            tempBoard.setPiece(7, 4, '.');
                            newBlackPieces.add("f8");
                            newBlackPieces.add("g8");
                        } else {
                            for (String piece : blackPieces) {
                                if (!piece.equals("a8") && !piece.equals("e8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                                pieceIndex++;
                            }
                            tempBoard.setPiece(7, 4, '.');
                            tempBoard.setPiece(7, 2, 'k');
                            tempBoard.setPiece(7, 3, 'r');
                            tempBoard.setPiece(7, 1, '.');
                            tempBoard.setPiece(7, 0, '.');
                            newBlackPieces.add("c8");
                            newBlackPieces.add("d8");
                        }
                    }
                    CalculateScoreOfPosition(tempBoard, modelIsWhite, newWhitePieces, newBlackPieces, positionValues, moveList.getFirst(), moveList.get(i), 'X', model);
                }
                //If non castling move picked
                else {
                    ArrayList<String> newWhitePieces = new ArrayList<>();
                    ArrayList<String> newBlackPieces = new ArrayList<>();
                    newWhitePieces.addAll(whitePieces);
                    newBlackPieces.addAll(blackPieces);
                    String originSquareString = moveList.get(0);
                    String destinationSquareString = moveList.get(i);
                    moveSquare = StringToSquare(destinationSquareString);

                    //If white piece captured
                    if (Character.isUpperCase(tempBoard.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
                        newWhitePieces.remove(destinationSquareString);
                        captureMade = true;
                    }
                    //If black piece captured
                    else if (Character.isLowerCase(tempBoard.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
                        newBlackPieces.remove(destinationSquareString);
                        captureMade = true;
                    }
                    //Update enPassantSquare
                    String tempEnPassantSquare = enPassantSquare;
                    if (tempBoard.getPiece(originSquare.getRow(), originSquare.getCol()) == 'P' && originSquare.getRow() == 1 && moveSquare.getRow() == 3)
                        tempEnPassantSquare = CoordinateToString(2, originSquare.getCol(), stringBuilder);
                    else if (tempBoard.getPiece(originSquare.getRow(), originSquare.getCol()) == 'p' && originSquare.getRow() == 6 && moveSquare.getRow() == 4)
                        tempEnPassantSquare = CoordinateToString(5, originSquare.getCol(), stringBuilder);
                    else tempEnPassantSquare = null;
                    //Update castling rights
                    boolean tempA1Castling = a1Castling;
                    boolean tempH1Castling = h1Castling;
                    boolean tempA8Castling = a8Castling;
                    boolean tempH8Castling = h8Castling;
                    if (tempBoard.getPiece(originSquare.getRow(), originSquare.getCol()) == 'K') {
                        tempA1Castling = false;
                        tempH1Castling = false;
                    } else if (board.getPiece(originSquare.getRow(), originSquare.getCol()) == 'k') {
                        tempA8Castling = false;
                        tempH8Castling = false;
                    } else if (originSquare.getRow() == 0 && originSquare.getCol() == 0) tempA1Castling = false;
                    else if (originSquare.getRow() == 0 && originSquare.getCol() == 7) tempH1Castling = false;
                    else if (originSquare.getRow() == 7 && originSquare.getCol() == 0) tempA8Castling = false;
                    else if (originSquare.getRow() == 7 && originSquare.getCol() == 7) tempH8Castling = false;
                    else if (destinationSquareString.equals("a1")) tempA1Castling = false;
                    else if (destinationSquareString.equals("h1")) tempH1Castling = false;
                    else if (destinationSquareString.equals("a8")) tempA8Castling = false;
                    else if (destinationSquareString.equals("h8")) tempH8Castling = false;
                    //Make move
                    tempPieceSymbol = tempBoard.getPiece(originSquare.getRow(), originSquare.getCol());
                    tempBoard.setPiece(originSquare.getRow(), originSquare.getCol(), '.');
                    tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), tempPieceSymbol);
                    //Update newWhitePieces + newBlackPieces
                    int pieceCount = 0;
                    if (!modelIsWhite) {
                        for (String piece : newBlackPieces) {
                            if (piece.equals(originSquareString)) break;
                            else pieceCount++;
                        }
                        newBlackPieces.remove(pieceCount);
                        newBlackPieces.add(destinationSquareString);
                    } else {
                        for (String piece : newWhitePieces) {
                            if (piece.equals(originSquareString)) break;
                            else pieceCount++;
                        }
                        whitePieces.remove(pieceCount);
                        whitePieces.add(destinationSquareString);
                    }
                    //Handle Promotion
                    if ((pieceSymbol == 'P' && moveSquare.getRow() == 7) || (pieceSymbol == 'p' && moveSquare.getRow() == 0)) {
                        char[] promotionOptions = {'Q', 'R', 'B', 'N'};
                        for (char piece : promotionOptions) {
                            Board tempTempBoard = CopyBoard(tempBoard);
                            char newPiece = piece;
                            if (modelIsWhite) tempTempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), newPiece);
                            else tempTempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), Character.toLowerCase(newPiece));
                            CalculateScoreOfPosition(tempTempBoard, modelIsWhite, newWhitePieces, newBlackPieces, positionValues, moveList.getFirst(), moveList.get(i), piece, model);
                        }
                    }
                    else CalculateScoreOfPosition(tempBoard, modelIsWhite, newWhitePieces, newBlackPieces, positionValues, moveList.getFirst(), moveList.get(i), 'X', model);
                }
            }
        }


        //By now the positionValues list has all the next moves, and their associated cost
        //Model needs to pick a move to play based on the evaluation. Each move has probability of being picked equal to score^2 / Sum of all (possible move^2)
        double total = 0;
        for (ModelEvaluation positionValue : positionValues) {
            total += Math.pow(positionValue.getScore(), 2);
        }
        double randomMove = random.nextDouble(total);
        //Now we decide which move that was (similarly to the computer making random moves)
        String originSquare = null;
        String destinationSquare = null;
        int moveIndex = 0;
        for (ModelEvaluation positionValue : positionValues) {
            if (total < Math.pow(positionValue.getScore(), 2)) break;
            else {
                total -= Math.pow(positionValue.getScore(), 2);
                moveIndex++;
            }
        }
        //The move chosen is the positionValue at position movenNdex in positionValues
        String originSquareString = null;
        String destinationSquareString = null;

        //If computer picked castling
        if (positionValues.get(moveIndex).getDestinationSquare().equals("O-O") || positionValues.get(moveIndex).getDestinationSquare().equals("O-O-O")) {
            String computerMove = positionValues.get(moveIndex).getDestinationSquare();
            ArrayList<String> newWhitePieces = new ArrayList<>();
            ArrayList<String> newBlackPieces = new ArrayList<>();
            if (modelIsWhite) {
                newBlackPieces.addAll(blackPieces);
                a1Castling = false;
                h1Castling = false;
                boolean kingsideCaslting = false;
                if (computerMove.equals("O-O")) kingsideCaslting = true;
                int pieceIndex = 0;
                if (kingsideCaslting) {
                    for (String piece : whitePieces) {
                        if (!piece.equals("e1") && !piece.equals("h1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                        pieceIndex++;
                    }
                    board.setPiece(0, 7, '.');
                    board.setPiece(0, 6, 'K');
                    board.setPiece(0, 5, 'R');
                    board.setPiece(0, 4, '.');
                    newWhitePieces.add("f1");
                    newWhitePieces.add("g1");
                    moveSquare = new Square(0,6);
                } else {
                    for (String piece : whitePieces) {
                        if (!piece.equals("e1") && !piece.equals("a1")) newWhitePieces.add(whitePieces.get(pieceIndex));
                        pieceIndex++;
                    }
                    board.setPiece(0, 4, '.');
                    board.setPiece(0, 2, 'K');
                    board.setPiece(0, 3, 'R');
                    board.setPiece(0, 1, '.');
                    board.setPiece(0, 0, '.');
                    newWhitePieces.add("c1");
                    newWhitePieces.add("d1");
                    moveSquare = new Square(0,2);
                }
            }
            else {
                newWhitePieces.addAll(whitePieces);
                a8Castling = false;
                h8Castling = false;
                boolean kingsideCaslting = false;
                if (computerMove.equals("O-O")) kingsideCaslting = true;
                int pieceIndex = 0;
                if (kingsideCaslting) {
                    for (String piece : blackPieces) {
                        if (!piece.equals("e8") && !piece.equals("h8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                        pieceIndex++;
                    }
                    board.setPiece(7, 7, '.');
                    board.setPiece(7, 6, 'k');
                    board.setPiece(7, 5, 'r');
                    board.setPiece(7, 4, '.');
                    newBlackPieces.add("f8");
                    newBlackPieces.add("g8");
                    moveSquare = new Square(7,6);
                } else {
                    for (String piece : blackPieces) {
                        if (!piece.equals("a8") && !piece.equals("e8")) newBlackPieces.add(blackPieces.get(pieceIndex));
                        pieceIndex++;
                    }
                    board.setPiece(7, 4, '.');
                    board.setPiece(7, 2, 'k');
                    board.setPiece(7, 3, 'r');
                    board.setPiece(7, 1, '.');
                    board.setPiece(7, 0, '.');
                    newBlackPieces.add("c8");
                    newBlackPieces.add("d8");
                    moveSquare = new Square(7,2);
                }
            }
        }
        //If non castling move picked
        else {
            originSquareString = positionValues.get(moveIndex).getOriginSquare();
            destinationSquareString = positionValues.get(moveIndex).getDestinationSquare();
            Square toBeMoved = StringToSquare(originSquareString);
            moveSquare = StringToSquare(destinationSquareString);

            //If white piece captured
            if (Character.isUpperCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
                whitePieces.remove(destinationSquare);
                captureMade = true;
            }
            //If black piece captured
            else if (Character.isLowerCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) {
                blackPieces.remove(destinationSquare);
                captureMade = true;
            }
            //Update enPassantSquare
            if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'P' && toBeMoved.getRow() == 1 && moveSquare.getRow() == 3)
                enPassantSquare = CoordinateToString(2, toBeMoved.getCol(), stringBuilder);
            else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'p' && toBeMoved.getRow() == 6 && moveSquare.getRow() == 4)
                enPassantSquare = CoordinateToString(5, toBeMoved.getCol(), stringBuilder);
            else enPassantSquare = null;
            //Update castling rights
            if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'K') {
                a1Castling = false;
                h1Castling = false;
            } else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'k') {
                a8Castling = false;
                h8Castling = false;
            } else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 0) a1Castling = false;
            else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 7) h1Castling = false;
            else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 0) a8Castling = false;
            else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 7) h8Castling = false;
            else if (destinationSquare.equals("a1")) a1Castling = false;
            else if (destinationSquare.equals("h1")) h1Castling = false;
            else if (destinationSquare.equals("a8")) a8Castling = false;
            else if (destinationSquare.equals("h8")) h8Castling = false;
            //Make move
            pieceSymbol = board.getPiece(toBeMoved.getRow(), toBeMoved.getCol());
            board.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            board.setPiece(moveSquare.getRow(), moveSquare.getCol(), pieceSymbol);
            //Update whitePieces + blackPieces
            int pieceCount = 0;
            if (!modelIsWhite) {
                for (String piece : blackPieces) {
                    if (piece.equals(originSquare)) break;
                    else pieceCount++;
                }
                blackPieces.remove(pieceCount);
                blackPieces.add(destinationSquare);
            } else {
                for (String piece : whitePieces) {
                    if (piece.equals(originSquare)) break;
                    else pieceCount++;
                }
                whitePieces.remove(pieceCount);
                whitePieces.add(destinationSquare);
            }
            //Handle Promotion
            if ((pieceSymbol == 'P' && moveSquare.getRow() == 7) || (pieceSymbol == 'p' && moveSquare.getRow() == 0)) {
                char newPiece = positionValues.get(moveIndex).getPiecePromotionSymbol();
                if (modelIsWhite) board.setPiece(moveSquare.getRow(), moveSquare.getCol(), newPiece);
                else board.setPiece(moveSquare.getRow(), moveSquare.getCol(), Character.toLowerCase(newPiece));
                gameNotation.append("=" + newPiece + " ");

            }

        }
        //Check opponent has legal moves. If not, checkmate/stalemate
        ArrayList<ArrayList<String>> opponentMoves = AllMoves(board, !modelIsWhite, whitePieces, blackPieces, stringBuilder, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling);
        boolean movesAvailable = false;
        for (ArrayList<String> moveList : opponentMoves) {
            if (moveList.size() > 1) {
                movesAvailable = true;
                break;
            }
        }
        //Reassign needed variables in gameState (those that don't change automatically)
        gameState.setEnPassantSquare(enPassantSquare);
        gameState.setA1Castling(a1Castling);
        gameState.setH1Castling(h1Castling);
        gameState.setA8Castling(a8Castling);
        gameState.setH8Castling(h8Castling);


        //50 move rule
        moveCount100++;
        if (!positionValues.get(moveIndex).getDestinationSquare().equals("O-O") || !positionValues.get(moveIndex).getDestinationSquare().equals("O-O-O")) {
            if (captureMade) moveCount100 = 0;
            else if (pieceSymbol == ('p' | 'P')) moveCount100 = 0;
        }
        if (moveCount100 >= 100) {
            gameState.setMoveRule50(true);
            return gameState;
        }
        gameState.setMoveCount100(moveCount100);

        //Check 3-fold repetition
        gameState = SelfHashPosition(gameState);
        //Done after as SelfHashPosition still uses old playerIsWhite to determine which colour goes next
        gameState.setPlayerIsWhite(!modelIsWhite);
        //If moves are available, pass onto player to make their move
        if (movesAvailable) return gameState;
        else {
            boolean checkmate = CheckIfCheckmate(board, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (checkmate) {
                if (modelIsWhite) {gameState.setWhiteWin(true);}
                else {gameState.setBlackWin(true);}
            }
            else {
                gameState.setStalemate(true);
            }
            return gameState;
        }
    }
    public static void CalculateScoreOfPosition(Board board, boolean modelIsWhite, ArrayList<String> whitePieces, ArrayList<String> blackPieces, ArrayList<ModelEvaluation> positionValues, String originString, String destinationString, char piecePromotionSymbol, PlayingModel model){
        double score = 0;
        //Calculate the score of the position
        double queenScore = CalculateQueenActivity(board, modelIsWhite, whitePieces, blackPieces);
        double rookScore = CalculateRookActivity(board, modelIsWhite, whitePieces, blackPieces);
        double surroundKingPointScore = CalculatePointsSurroundingKing(board, modelIsWhite, whitePieces, blackPieces);
        //Calculate score for position
        score = (model.getQueenActivity()*queenScore) + (model.getRookActivity()*rookScore) + (model.getPointsSurroundingKing()*surroundKingPointScore);
        ModelEvaluation positionValue = new ModelEvaluation(originString, destinationString, score, piecePromotionSymbol);
        positionValues.add(positionValue);

    }
    public static double CalculateQueenActivity(Board board, boolean modelIsWhite, ArrayList<String> whitePieces, ArrayList<String> blackPieces) {
        //Find square that queen is on. Add to move total. Return total
        int score = 0;
        StringBuilder stringBuilder = new StringBuilder();
        if (modelIsWhite) {
            for (String piece : whitePieces) {
                Square queenSquare = StringToSquare(piece);
                if (board.getPiece(queenSquare.getRow(), queenSquare.getCol()) == 'Q') {score += QueenAttacks(board, piece, stringBuilder).size();}
            }
        }
        else {
            for (String piece : blackPieces) {
                Square queenSquare = StringToSquare(piece);
                if (board.getPiece(queenSquare.getRow(), queenSquare.getCol()) == 'q') {score += QueenAttacks(board, piece, stringBuilder).size();}
            }
        }
        return score;
    }
    public static double CalculateRookActivity(Board board, boolean modelIsWhite, ArrayList<String> whitePieces, ArrayList<String> blackPieces) {
        //Same for rooks
        int score = 0;
        StringBuilder stringBuilder = new StringBuilder();
        if (modelIsWhite) {
            for (String piece : whitePieces) {
                Square queenSquare = StringToSquare(piece);
                if (board.getPiece(queenSquare.getRow(), queenSquare.getCol()) == 'R') {score += QueenAttacks(board, piece, stringBuilder).size();}
            }
        }
        else {
            for (String piece : blackPieces) {
                Square queenSquare = StringToSquare(piece);
                if (board.getPiece(queenSquare.getRow(), queenSquare.getCol()) == 'r') {score += QueenAttacks(board, piece, stringBuilder).size();}
            }
        }
        return score;
    }
    public static double CalculatePointsSurroundingKing(Board board, boolean modelIsWhite, ArrayList<String> whitePieces, ArrayList<String> blackPieces) {
        int score = 0;
        String kingSquareString = "null";
        Square kingSquare = new Square(0, 0);
        if (modelIsWhite) {
            for (String piece : whitePieces) {
                kingSquare = StringToSquare(piece);
                if (board.getPiece(kingSquare.getRow(), kingSquare.getCol()) == 'K') {kingSquareString = piece;}
            }
        }
        else {
            for  (String piece : blackPieces) {
                kingSquare = StringToSquare(piece);
                if (board.getPiece(kingSquare.getRow(), kingSquare.getCol()) == 'k') {kingSquareString = piece;}            }
        }
        //Now we have the square the king is on. We need to get surrounding squares
        ArrayList<String> adjacent = new ArrayList<>();
        Integer[][] offsets = {{1,-1}, {1, 0}, {1, 1} , {0, -1}, {0,1}, {-1, -1}, {-1, 0} , {-1, 1}};
        for (Integer[] offset : offsets) {
            //Get new square
            int newRow = kingSquare.getRow() + offset[0];
            int newCol = kingSquare.getCol() + offset[1];
            //Check within bounds of board
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                //Check square contains a friendly piece
                if (modelIsWhite) {
                    if (Character.isUpperCase(board.getPiece(newRow, newCol))) {
                        switch (board.getPiece(newRow, newCol)) {
                            case 'P':
                                score++;
                                break;
                            case 'R':
                                score += 5;
                                break;
                            case 'N':
                            case 'B':
                                score += 3;
                                break;
                            case 'Q':
                                score += 9;
                                break;
                            default:
                                System.out.println("Something ain't right.");
                        }
                    }
                }
                else {
                    if (Character.isLowerCase(board.getPiece(newRow, newCol))) {
                        switch (board.getPiece(newRow, newCol)) {
                            case 'p':
                                score++;
                                break;
                            case 'r':
                                score += 5;
                                break;
                            case 'n':
                            case 'b':
                                score += 3;
                                break;
                            case 'q':
                                score += 9;
                                break;
                            default:
                                System.out.println("Something ain't right.");
                        }
                    }
                }
            }
        }
        return score;
    }
}