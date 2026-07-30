import Board.*;
import java.util.*;
import Pieces.*;


public class Main {
    public static void main(String[] args) {
        //Start();
    }

    public static void Start() {
        System.out.println("Welcome to Chess");
        //Set up the board, input, player colours, etc
        Board board = new Board();
        Scanner input = new Scanner(System.in);
        StringBuilder stringBuilder = new StringBuilder();
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
        PlayGame(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling);
    }

    public static void PlayGame(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling) {
        //Determine if plaeyr or computer goes first
        if  (playerIsWhite) {
            PlayerMove(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling);
        }
        else ComputerMove(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling);
    }
    public static void PlayerMove(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare,  ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder,  boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling) {
        //Let player pick piece to move
        boolean validSquareOnBoard = false;
        boolean pieceIsUsers = false;
        Square toBeMoved = null;
        String userMove;
        ArrayList<String> playerMoves = new ArrayList<>();
        boolean pickedValidMove = false;
        do {
            //Check square belongs on board, then check that the piece belongs to player
            do {
                do {
                    //Inner loop checks that the square is on the board
                    System.out.println("Piece to move: "); //Enter something like 'e4'
                    //Convert 'e4' into a 2d array coordinate
                    toBeMoved = StringToSquare(input.nextLine());
                    if ((toBeMoved.getRow() < 8) && (toBeMoved.getCol() < 8) && (toBeMoved.getRow() >= 0) && (toBeMoved.getCol() >= 0))
                        validSquareOnBoard = true;
                } while (!validSquareOnBoard);
                //Outer loop checks user picks their own respective piece
                if (playerIsWhite && Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())))
                    pieceIsUsers = true;
                else if (!playerIsWhite && Character.isLowerCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())))
                    pieceIsUsers = true;
            } while (!pieceIsUsers);
            System.out.println(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()));

            //By now the user has selected their own piece to move, now we need to get a list of all the moves that piece can make
            switch (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())) {
                case ('P'):
                case ('p'):
                    playerMoves = PawnMove(board, toBeMoved, whitePieces, blackPieces, stringBuilder, enPassantSquare);
                    break;
                case ('R'):
                case ('r'):
                    playerMoves = RookMove(board, toBeMoved, whitePieces, blackPieces, stringBuilder);
                    break;
                case ('B'):
                case ('b'):
                    playerMoves = BishopMove(board, toBeMoved, whitePieces, blackPieces, stringBuilder);
                    break;
                case ('N'):
                case ('n'):
                    playerMoves = KnightMove(board, toBeMoved, whitePieces, blackPieces, stringBuilder);
                    break;
                case ('Q'):
                case ('q'):
                    playerMoves = QueenMove(board, toBeMoved, whitePieces, blackPieces, stringBuilder);
                    break;
                case ('K'):
                case ('k'):
                    playerMoves = KingMove(board, toBeMoved, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling);
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
            for (String move : playerMoves) if (userMove.equalsIgnoreCase(move)) pickedValidMove = true;
        } while (!pickedValidMove);
        //Make the move and update whitePieces/blackPieces with capture
        Square moveSquare = StringToSquare(userMove);
        //If white piece captured
        if (Character.isUpperCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) whitePieces.remove(userMove);
        //If black piece captured
        else if (Character.isLowerCase(board.getPiece(moveSquare.getRow(), moveSquare.getCol()))) blackPieces.remove(userMove);
        //Update enPassantSquare
        if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'P' && toBeMoved.getRow() == 1 && moveSquare.getRow() == 3) enPassantSquare = CoordinateToString(2, toBeMoved.getCol(), stringBuilder);
        else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'p' && toBeMoved.getRow() == 6 && moveSquare.getRow() == 4) enPassantSquare = CoordinateToString(5, toBeMoved.getCol(), stringBuilder);
        else enPassantSquare = null;
        //Update castling rights
        if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'K') { a1Castling = false; h1Castling = false; }
        else if (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()) == 'k') { a8Castling = false; h8Castling = false; }
        else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 0) a1Castling = false;
        else if (toBeMoved.getRow() == 0 && toBeMoved.getCol() == 7) h1Castling = false;
        else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 0) a1Castling = false;
        else if (toBeMoved.getRow() == 7 && toBeMoved.getCol() == 7) h1Castling = false;
        //Make the move
        char pieceSymbol = board.getPiece(toBeMoved.getRow(), toBeMoved.getCol());
        board.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
        board.setPiece(moveSquare.getRow(), moveSquare.getCol(), pieceSymbol);
        //Check opponent has legal moves. If not, checkmate/stalemate
        boolean whiteMoveNext = !playerIsWhite;
        ArrayList<ArrayList<String>> opponentMoves = AllMoves(board, whiteMoveNext, whitePieces, blackPieces, stringBuilder, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling);
        boolean movesAvailable = false;
        for (ArrayList<String> moveList : opponentMoves) {
            if (moveList.size() > 1) {
                movesAvailable = true;
                break;
            }
        }
        //If moves are available, pass onto computer to make their move
        if (movesAvailable) ComputerMove(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling);
        else {
            boolean checkmate = CheckIfCheck(board, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (checkmate) {
                System.out.println("Player Wins!");
                System.exit(0);
            }
            else {
                System.out.println("Stalemate! Lol skill issue");
                System.exit(0);
            }
        }
    }
    public static ArrayList<String> KingMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling) {
        //Get all the theoretical moves that the king can make
        ArrayList<String> PseudoMoves = KingAttacks(board, SquareToCoordinate(toBeMoved, stringBuilder), stringBuilder);
        ArrayList<String> finalMoves = new ArrayList<>();
        boolean pieceIsWhite = (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())));
        //For each one, check if it leads to the king being in check
        //Simulate the move
        Board tempBoard;
        for (String move : PseudoMoves) {
            tempBoard = board;
            Square moveSquare = StringToSquare(move);
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'K');
            else tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'k');
            boolean isCheck = CheckIfCheck(tempBoard, whitePieces, blackPieces, moveSquare, stringBuilder);
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
            tempBoard = board;
            Square moveSquare = StringToSquare(move);
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'P');
            else  tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'p');
            boolean IsCheck = CheckIfCheck(tempBoard, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (!IsCheck) finalMoves.add(move);
        }
        return finalMoves;
    }
    public static ArrayList<String> KnightMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder) {
        //Get all theoretical moves that knight can make
        ArrayList<String> pseudoMoves = KnightAttacks(board, SquareToCoordinate(toBeMoved, stringBuilder), stringBuilder);
        ArrayList<String> finalMoves = new ArrayList<>();
        boolean pieceIsWhite = (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())));
        //Simulate move
        Board tempBoard;
        for (String move : pseudoMoves) {
            tempBoard = board;
            Square moveSquare = StringToSquare(move);
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'N');
            else tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'n');
            boolean isCheck = CheckIfCheck(tempBoard, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (!isCheck) finalMoves.add(move);
        }
        return finalMoves;
    }
    public static ArrayList<String> RookMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder) {
        //Get theoretical moves
        ArrayList<String> pseudoMoves = RookAttacks(board, SquareToCoordinate(toBeMoved, stringBuilder), stringBuilder);
        ArrayList<String> finalMoves = new ArrayList<>();
        boolean pieceIsWhite = (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())));
        //Simulate move
        Board tempBoard;
        for (String move : pseudoMoves) {
            tempBoard = board;
            Square moveSquare = StringToSquare(move);
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'R');
            else tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'r');
            boolean isCheck = CheckIfCheck(tempBoard, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (!isCheck) finalMoves.add(move);
        }
        return finalMoves;
    }
    public static ArrayList<String> BishopMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder) {
        //Get theoretical moves
        ArrayList<String> pseudoMoves = BishopAttacks(board, SquareToCoordinate(toBeMoved, stringBuilder), stringBuilder);
        ArrayList<String> finalMoves = new ArrayList<>();
        boolean pieceIsWhite = (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())));
        //Simulate move
        Board tempBoard;
        for (String move : pseudoMoves) {
            tempBoard = board;
            Square moveSquare = StringToSquare(move);
            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
            if (pieceIsWhite) tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'B');
            else tempBoard.setPiece(moveSquare.getRow(), moveSquare.getCol(), 'b');
            boolean isCheck = CheckIfCheck(tempBoard, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (!isCheck) finalMoves.add(move);
        }
        return finalMoves;
    }
    public static ArrayList<String> QueenMove(Board board, Square toBeMoved, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder) {
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
                if (tempBoard.getPiece(pieceSquare.getRow(), pieceSquare.getCol()) == 'K') {
                    kingSquare = piece;
                }
            }
            for (String attack : allAttacks) {
                if (attack.equals(kingSquare)) return true;
            }
            return false;
        }
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
        int row = Integer.parseInt(coordinate.substring(1,2)) - 1;
        char letter = coordinate.charAt(0);
        int col = letter - 'a';
        return new Square(row, col);
    }
    public static String SquareToCoordinate(Square square, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        int row = square.getRow();
        int col = square.getCol();
        String letter = Character.toString(row + 97);
        String number = Character.toString(col + 49);
        stringBuilder.append(letter);
        stringBuilder.append(number);
        return stringBuilder.toString();
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
        ArrayList<String> castling =  new ArrayList<>();
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
                        Board tempBoard = board;
                        //Check it's empty
                        if (board.getPiece(StringToSquare(square).getRow(), StringToSquare(square).getCol()) == '.') {
                            //Simulate the move and check if king would be in check
                            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
                            tempBoard.setPiece(StringToSquare(square).getRow(), StringToSquare(square).getCol(), 'K');
                            check = CheckIfCheck(tempBoard, whitePieces, blackPieces, toBeMoved, stringBuilder);
                            if (check) biggerCheck  = true;
                        }
                        else biggerCheck = true;
                    }
                    if (!biggerCheck) castling.add("O-O-O");
                }
                check = false;
                biggerCheck = false;
                //Check if right rook has moved
                if (h1Castling) {
                    //For each square
                    String[] squares = {"f1", "g1"};
                    for (String square : squares) {
                        Board tempBoard = board;
                        //Check it's empty
                        if (board.getPiece(StringToSquare(square).getRow(), StringToSquare(square).getCol()) == '.') {
                            //Simulate the move and check if king would be in check
                            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
                            tempBoard.setPiece(StringToSquare(square).getRow(), StringToSquare(square).getCol(), 'K');
                            check = CheckIfCheck(tempBoard, whitePieces, blackPieces, toBeMoved, stringBuilder);
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
                        Board tempBoard = board;
                        //Check it's empty
                        if (board.getPiece(StringToSquare(square).getRow(), StringToSquare(square).getCol()) == '.') {
                            //Simulate the move and check if king would be in check
                            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
                            tempBoard.setPiece(StringToSquare(square).getRow(), StringToSquare(square).getCol(), 'k');
                            check = CheckIfCheck(tempBoard, whitePieces, blackPieces, toBeMoved, stringBuilder);
                            if (check) biggerCheck  = true;
                        }
                        else biggerCheck = true;
                    }
                    if (!biggerCheck) castling.add("O-O-O");
                }
                check = false;
                biggerCheck = false;
                //Check if right rook has moved
                if (h1Castling) {
                    //For each square
                    String[] squares = {"f8", "g8"};
                    for (String square : squares) {
                        Board tempBoard = board;
                        //Check it's empty
                        if (board.getPiece(StringToSquare(square).getRow(), StringToSquare(square).getCol()) == '.') {
                            //Simulate the move and check if king would be in check
                            tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
                            tempBoard.setPiece(StringToSquare(square).getRow(), StringToSquare(square).getCol(), 'k');
                            check = CheckIfCheck(tempBoard, whitePieces, blackPieces, toBeMoved, stringBuilder);
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
        //Get all the moves
        ArrayList<ArrayList<String>> moves = new ArrayList<>();
        ArrayList<String> newMoves = new ArrayList<>();
        //If white moves next, get all the moves for the white pieces
        if (whiteMove) {
            for (String piece : whitePieces) {
                newMoves.add(piece);
                switch (board.getPiece(StringToSquare(piece).getRow(), StringToSquare(piece).getCol())) {
                    case 'P':
                        newMoves = PawnMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder, enPassantSquare);
                        moves.add(newMoves);
                        break;
                    case 'R':
                        newMoves = RookMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder);
                        moves.add(newMoves);
                        break;
                    case 'B':
                        newMoves = BishopMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder);
                        moves.add(newMoves);
                        break;
                    case 'N':
                        newMoves = KnightMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder);
                        moves.add(newMoves);
                        break;
                    case 'Q':
                        newMoves = QueenMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder);
                        moves.add(newMoves);
                        break;
                    case 'K':
                        newMoves = KingMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling);
                        moves.add(newMoves);
                        break;

                }
                newMoves.clear();
            }
        }
        else {
            for (String piece : blackPieces) {
                newMoves.add(piece);
                switch (board.getPiece(StringToSquare(piece).getRow(), StringToSquare(piece).getCol())) {
                    case 'P':
                        newMoves = PawnMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder, enPassantSquare);
                        moves.add(newMoves);
                        break;
                    case 'R':
                        newMoves = RookMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder);
                        moves.add(newMoves);
                        break;
                    case 'B':
                        newMoves = BishopMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder);
                        moves.add(newMoves);
                        break;
                    case 'N':
                        newMoves = KnightMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder);
                        moves.add(newMoves);
                        break;
                    case 'Q':
                        newMoves = QueenMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder);
                        moves.add(newMoves);
                        break;
                    case 'K':
                        newMoves = KingMove(board, StringToSquare(piece), whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling);
                        moves.add(newMoves);
                        break;

                }
                newMoves.clear();
            }
        }
        return moves;
    }

    public static void ComputerMove(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare, ArrayList<String> whitePieces, ArrayList<String> blackPieces, StringBuilder stringBuilder, boolean a1Castling, boolean h1Castling, boolean a8Castling, boolean h8Castling) {
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
            if ((moveList.size() - 1) > randomMove) break;
            else {
                randomMove -= moveList.size() - 1;
                listNumber++;
            }
        }
        //So the random move picked is in the 'listNumber' list, and is the nth move where n = randomMove + 1
        originSquare = allMoves.get(listNumber).getFirst();
        destinationSquare = allMoves.get(listNumber).get(randomMove);
        Square toBeMoved = StringToSquare(originSquare);
        Square moveSquare = StringToSquare(destinationSquare);
        //And now, execute the move

        char pieceSymbol = board.getPiece(toBeMoved.getRow(), toBeMoved.getCol());
        board.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
        board.setPiece(moveSquare.getRow(), moveSquare.getCol(), pieceSymbol);
        //Check opponent has legal moves. If not, checkmate/stalemate
        ArrayList<ArrayList<String>> opponentMoves = AllMoves(board, playerIsWhite, whitePieces, blackPieces, stringBuilder, enPassantSquare, a1Castling, h1Castling, a8Castling, h8Castling);
        boolean movesAvailable = false;
        for (ArrayList<String> moveList : opponentMoves) {
            if (moveList.size() > 1) {
                movesAvailable = true;
                break;
            }
        }
        //If moves are available, pass onto computer to make their move
        if (movesAvailable) PlayerMove(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder, a1Castling, h1Castling, a8Castling, h8Castling);
        else {
            boolean checkmate = CheckIfCheck(board, whitePieces, blackPieces, moveSquare, stringBuilder);
            if (checkmate) {
                System.out.println("Computer Wins!");
                System.exit(0);
            }
            else {
                System.out.println("Stalemate! Lol skill issue");
                System.exit(0);
            }
        }
    }
}