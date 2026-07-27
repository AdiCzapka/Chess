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
        String[] whitePieces = {"a1","b1","c1","d1","e1","f1","g1","h1","a2","b2","c2","d2","e2","f2","g2","h2"};
        String[] blackPieces = {"a7","b7","c7","d7","e7","f7","g7","h7","a8","b8","c8","d8","e8","f8","g8","h8"};
        //Start game loop
        PlayGame(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder);
    }

    public static void PlayGame(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare, String[] whitePieces, String[] blackPieces, StringBuilder stringBuilder) {
        //Determine if plaeyr or computer goes first
        if  (1 == 1) {
            PlayerMove(input, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, stringBuilder);
        }
        else ComputerMove(board, playerIsWhite, enPassantSquare, whitePieces, blackPieces);
    }
    public static void PlayerMove(Scanner input, Board board, boolean playerIsWhite, String enPassantSquare,  String[] whitePieces, String[] blackPieces, StringBuilder stringBuilder) {
        //Let player pick piece to move
        boolean validSquareOnBoard = false;
        boolean pieceIsUsers = false;
        Square toBeMoved = null;
        //Check square belongs on board, then check that the piece belongs to player
        do {
            do {
                //Inner loop checks that the square is on the board
                System.out.println("Piece to move: "); //Enter something like 'e4'
                //Convert 'e4' into a 2d array coordinate
                toBeMoved = CoordinateToSquare(input.nextLine());
                if ((toBeMoved.getRow() < 8) && (toBeMoved.getCol() < 8) && (toBeMoved.getRow() >= 0) && (toBeMoved.getCol() >= 0))
                    validSquareOnBoard = true;
            } while (!validSquareOnBoard);
            //Outer loop checks user picks their own respective piece
            if (playerIsWhite && Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()))) pieceIsUsers = true;
            else if (!playerIsWhite && Character.isLowerCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()))) pieceIsUsers = true;
        } while (!pieceIsUsers);
        System.out.println(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()));

        //By now the user has selected their own piece to move, now we need to get a list of all the moves that piece can make
        switch (board.getPiece(toBeMoved.getRow(), toBeMoved.getCol())) {
            case ('P'):
            case ('p'):
                //PawnMove();
                break;
            case ('R'):
            case ('r'):
                //RookMove();
                break;
            case ('B'):
            case ('b'):
                //BishopMove();
                break;
            case ('N'):
            case ('n'):
                //KnightMove();
                break;
            case ('Q'):
            case ('q'):
                //QueenMove();
                break;
            case ('K'):
            case ('k'):
                KingMove(board, toBeMoved, whitePieces, blackPieces, stringBuilder);
                break;
        }
    }
    public static void KingMove(Board board, Square toBeMoved, String[] whitePieces, String[] blackPieces, StringBuilder stringBuilder) {
        Integer[][] moveOffsets = {{1, -1}, {1, 0}, {1, 1}, {0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}};
        boolean pieceIsWhite = false;
        //Determine if piece is white
        if (Character.isUpperCase(board.getPiece(toBeMoved.getRow(), toBeMoved.getCol()))) pieceIsWhite = true;
        //If piece is white, other white pieces are friendly, black are not
        if (pieceIsWhite) {
            //For each move
            for (Integer[] moveOffset : moveOffsets) {
                //Get new square
                Square destinationSquare = CoordinateToSquare(CoordinateToString((toBeMoved.getRow() + moveOffset[0]), (toBeMoved.getCol() + moveOffset[1]), stringBuilder));
                //Check that there is either no piece on that square
                if (board.getPiece(destinationSquare.getRow(), destinationSquare.getCol()) == '.') {
                    //Each of these moves is possible if it doesn't leave the king in check, so check for check
                    //Create temporary board
                    Board tempBoard = new Board();
                    //Make move on temporary board
                    tempBoard.setPiece(destinationSquare.getRow(), destinationSquare.getCol(), 'K');
                    tempBoard.setPiece(toBeMoved.getRow(), toBeMoved.getCol(), '.');
                    //Cehck the new position doesn't leave king in check
                    boolean IsCheck = CheckIfCheck(tempBoard, whitePieces, blackPieces, destinationSquare, stringBuilder);
                //Next check if there is an enemy piece there
                }
            }
        }

    }

    public static void ComputerMove(Board board, boolean playerIsWhite, String enPassantSquare, String[] whitePieces, String[] blackPieces) {}

    public static boolean CheckIfCheck(Board tempBoard,  String[] whitePieces, String[] blackPieces, Square destinationSquare, StringBuilder stringBuilder) {
        //Determine attacking colour
        boolean blackAttack = false;
        ArrayList<String> attacks = new ArrayList<>();
        //If white piece is the one that got moved, black is attacking, and vice versa
        if (Character.isUpperCase(tempBoard.getPiece(destinationSquare.getRow(), destinationSquare.getCol()))) blackAttack = true;
        if (blackAttack) {
            //For each black piece, get it's attacks, and if any are attacking the king then it's check
            for (String piece : blackPieces) {
                //Each piece is represesnted as coordinate: "e7". Take each coordinate, convert to board position, check board position
                //for piece type (represented by letter) and get it's attacks
                Square attackSquare =  CoordinateToSquare(piece);
                switch (tempBoard.getPiece(attackSquare.getRow(), attackSquare.getCol())) {
                    case ('p'):
                        attacks = PawnAttacks(tempBoard, piece, stringBuilder);
                        break;
                    case ('r'):
                        attacks = RookAttacks(tempBoard, piece, stringBuilder);
                        break;
                    case ('b'):
                        attacks = BishopAttacks(tempBoard, piece, stringBuilder);
                        break;
                    case ('n'):
                        attacks = KnightAttacks(tempBoard, piece, stringBuilder);
                        break;
                    case ('q'):
                        attacks = QueenAttacks(tempBoard, piece, stringBuilder);
                        break;
                    case ('k'):
                        //attacks = KingAttacks(tempBoard, piece, stringBuilder);
                        break;

                }
            }
        }
        boolean IsCheck = false;
        return IsCheck;
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
    public static Square CoordinateToSquare(String coordinate) {
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
        Square pieceSquare = CoordinateToSquare(piece);
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
        Square pieceSquare = CoordinateToSquare(piece);
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
        Square pieceSquare = CoordinateToSquare(piece);
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
        Square pieceSquare = CoordinateToSquare(piece);
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


}