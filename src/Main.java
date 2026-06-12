import Board.*;
import java.util.*;
import Pieces.*;


public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Chess");
        boolean pickedMode = false;
        do {
            System.out.println("What do you wish to do? (Play, Generate, Free Board)");
            Scanner input = new Scanner(System.in);
            String choice = input.nextLine();
            switch (choice) {
                case "Play":
                    PlayChess(input);
                    pickedMode = true;
                    break;
                case "Generate Moves":
                    GenerateMoves(input);
                    pickedMode = true;
                    break;
                case "Free Board":
                    FreeBoard(input);
                    pickedMode = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (!pickedMode);
    }

    public static void PlayChess(Scanner input) {
        //Initialise Board
        Board board = new Board("RNBQKBNRPPPPPPPP................................pppppppprnbqkbnr");
        //Determine if player is white
        boolean playerIsWhite;
        int whiteMove = 1;
        System.out.println("Play as white or black?");
        char choice = input.nextLine().charAt(0);
        //Initialise lists of pieces belonging to each colour
        List<Integer> whitePieces = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        List<Integer> blackPieces = Arrays.asList(48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63);
        //Variable to vold the location of the en passantable square
        int enPassantSquare = -1;
        //Display graphic to represent board
        DisplayBoard(board);
        //Player is white
        if (choice == 'w'){
            playerIsWhite = true;
            PlayerMove(board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, input, whiteMove);
        }
        //Is black
        else {
            playerIsWhite = false;
            ComputerMove(board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, whiteMove);
        }
    }
    public static void PlayerMove(Board board, boolean playerIsWhite, int enPassantSquare, List<Integer> whitePieces, List<Integer> blackPieces,  Scanner input, int whiteMove) {
        boolean validPiece = false;
        //Repeat until player picks their own piece
        do {
            //Check that user picks piece that is on the board to move
            boolean validSquare = false;
            //Variable to store square location as an integer
            int pieceIndexInString = -1;
            //repeatedly ask user until they pick a square within the correct range
            do {
                System.out.println("Enter the position of the piece you wish to move: ");
                String pieceSquare = input.nextLine();
                //Check that square is valid
                if ((pieceSquare.charAt(0) >= 'a' && pieceSquare.charAt(0) <= 'h') && (pieceSquare.charAt(1) >= '1' &&  pieceSquare.charAt(1) <= '8') && pieceSquare.length() == 2) {
                    validSquare = true;
                    //Convert position to index. a1 = 0, h8 = 63
                    pieceIndexInString = (((pieceSquare.charAt(0) - 'a') * 8) + ((pieceSquare.charAt(1) - '0') - 1));
                    System.out.println(pieceIndexInString);
                }
            } while (!validSquare);
            //This is probably really stupid
            //Check square not empty
            if (board.getPiece(pieceIndexInString) != '.') {
                //If white
                if (playerIsWhite) {
                    //Piece capitalised
                    if (board.getPiece(pieceIndexInString) == Character.toUpperCase(board.getPiece(pieceIndexInString))) {
                        StartMove(pieceIndexInString, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, input, whiteMove);
                        validPiece = true;
                    } else {}
                //If black
                } else {
                    //Piece not capitalised
                    if (board.getPiece(pieceIndexInString) == Character.toLowerCase(board.getPiece(pieceIndexInString))) {
                        StartMove(pieceIndexInString, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, input, whiteMove);
                        validPiece = true;
                    } else {}
                }
            }
        } while (!validPiece);

        //Check for checkmate
        //Pass to Computer
    }

    public static void StartMove(int pieceIndexInString, Board board, boolean playerIsWhite, int enPassantSquare, List<Integer> whitePieces, List<Integer> blackPieces, Scanner input, int whiteMove) {
        switch (board.getPiece(pieceIndexInString)){
            case 'p':
            case 'P':
                PawnMove(pieceIndexInString, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, input, whiteMove);
                break;
            case 'b':
            case 'B':
                BishopMove(pieceIndexInString, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, input, whiteMove);
                break;
            case 'n':
            case 'N':
                KnightMove(pieceIndexInString, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, input, whiteMove);
                break;
            case 'r':
            case 'R':
                RookMove(pieceIndexInString, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, input, whiteMove);
                break;
            case 'q':
            case 'Q':
                QueenMove(pieceIndexInString, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, input, whiteMove);
                break;
            case 'k':
            case 'K':
                KingMove(pieceIndexInString, board, playerIsWhite, enPassantSquare, whitePieces, blackPieces, input, whiteMove);
                break;
        }
    }

    public static void PawnMove(int pieceIndexInString, Board board, boolean playerIsWhite, int enPassantSquare, List<Integer> whitePieces, List<Integer> blackPieces, Scanner input, int  whiteMove) {
        //We laready know user picked their own piece
        //Obtain all legal moves for picked piece
        List<Integer> legalMoves = new ArrayList<>();

//        //2nd row indexes = 8 - 15. //7th row indexes = 48 - 55. Allow double move up / down
//        if ((8 <= pieceIndexInString && pieceIndexInString <= 15) || (48  <= pieceIndexInString && pieceIndexInString <= 55)) {
//
//        }

        StringBuilder sb = new StringBuilder();

        //Pawns can move forward once (or twice, handled later) and attack vertically + en passant (google it)
        if (playerIsWhite) {
            //Player white
            //Check piece on left edge
            if (EdgePawnCheck(pieceIndexInString, board, sb, 0, 9, enPassantSquare, playerIsWhite, whitePieces, blackPieces, whiteMove)) {
                legalMoves.add(pieceIndexInString + 9);
            }
            //Check piece on right edge
            if (EdgePawnCheck(pieceIndexInString, board, sb, 7, 7, enPassantSquare, playerIsWhite, whitePieces, blackPieces, whiteMove)) {
                legalMoves.add(pieceIndexInString + 7);
            }
            //Moving up once or twice
        }
        else {
            //player black
        }
    }
    public static boolean EdgePawnCheck(int pieceIndexInString, Board board, StringBuilder sb, int edgeMod, int offset, int enPassantSquare, boolean playerIsWhite, List<Integer> whitePieces, List<Integer> blackPieces, int  whiteMove) {
        if (pieceIndexInString % 8 == edgeMod) {
            if ((board.getPiece(pieceIndexInString + offset) == Character.toLowerCase(board.getPiece(pieceIndexInString + offset))) || (pieceIndexInString + offset) == enPassantSquare){
                sb.append(board.getPosition());
                sb.replace(pieceIndexInString, pieceIndexInString + 1, ".");
                sb.replace(pieceIndexInString+offset, pieceIndexInString + offset + 1, "P");
                String newPosition = sb.toString();
                Board tempBoard = new Board(newPosition);
                if (CheckIfMoveLeadsToOwnKingExposed(tempBoard, whitePieces, blackPieces, whiteMove)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void BishopMove(int pieceIndexInString, Board board, boolean playerIsWhite, int enPassantSquare, List<Integer> whitePieces, List<Integer> blackPieces, Scanner input, int  whiteMove) {
        //Get all the possible attacks of piece, assign to list
        List<Integer> pseudoLegalMoves = BishopAttack(board, pieceIndexInString);
        List<Integer> legalMoves = new ArrayList<>();
        //For each move, create a new board state (simulating move) and check if it ends with check
        String originalPosition = board.getPosition();
        StringBuilder sb = new StringBuilder();
        sb.append(originalPosition);
        //Simulate each move
        for (int move : pseudoLegalMoves) {
            sb.replace(pieceIndexInString, pieceIndexInString + 1, ".");
            if (whiteMove == 1) {
                sb.replace(move, move+1, "B");
            }
            else {
                sb.replace(move, move+1, "b");
            }
            Board tempBoard = new Board(sb.toString());
            if (!CheckIfMoveLeadsToOwnKingExposed(tempBoard, whitePieces, blackPieces, whiteMove)) {
                legalMoves.add(move);
            }
        }
        //Print out legal moves, make user select one.
    }
    public static void KnightMove(int pieceIndexInString, Board board, boolean playerIsWhite, int enPassantSquare, List<Integer> whitePieces, List<Integer> blackPieces, Scanner input, int  whiteMove) {}
    public static void RookMove(int pieceIndexInString, Board board, boolean playerIsWhite, int enPassantSquare, List<Integer> whitePieces, List<Integer> blackPieces, Scanner input, int  whiteMove) {}
    public static void QueenMove(int pieceIndexInString, Board board, boolean playerIsWhite, int enPassantSquare, List<Integer> whitePieces, List<Integer> blackPieces, Scanner input, int  whiteMove) {}
    public static void KingMove(int pieceIndexInString, Board board, boolean playerIsWhite, int enPassantSquare, List<Integer> whitePieces, List<Integer> blackPieces, Scanner input, int  whiteMove) {}

    public static void ComputerMove(Board board, boolean playerIsWhite, int enPassantSquare, List<Integer> whitePieces, List<Integer> blackPieces, int whiteMove) {

    }
    public static boolean CheckIfMoveLeadsToOwnKingExposed(Board board, List<Integer> whitePieces, List<Integer> blackPieces, int  whiteMove) {
        List<Integer> attacks = new ArrayList<>();
        //For each opponent piece, check if it can capture the king in the new position
        if (whiteMove == 1) {
            attacks = CheckIfOwnKingCondenser(blackPieces, board, whiteMove);
        }
        else {
            attacks = CheckIfOwnKingCondenser(whitePieces, board, whiteMove);
        }
        //Once you have all the moves that can be made, check if any of them attack the king
        for (int attack : attacks) {
            if (whiteMove == 1) {
                if (board.getPiece(attack) == 'K') {
                    return false;
                }
            }
            else {
                if (board.getPiece(attack) == 'k') {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Integer> CheckIfOwnKingCondenser (List<Integer> Pieces, Board board, int whiteMove) {
        //List of all attacks
        List<Integer> attacks = new ArrayList<>();
        //For each piece get it's attack
        for (int i = 1; i < Pieces.size(); i++) {
            switch (board.getPiece(Pieces.get(i-1))){
                case 'p':
                case 'P':
                    attacks.addAll(PawnAttack(board, Pieces.get(i-1), whiteMove));
                    break;
                case 'b':
                case 'B':
                    attacks.addAll(BishopAttack(board, Pieces.get(i-1)));
                    break;
                case 'n':
                case 'N':
                    attacks.addAll(KnightAttack(board, Pieces.get(i-1)));
                    break;
                case 'r':
                case 'R':
                    attacks.addAll(RookAttack(board, Pieces.get(i-1)));
                    break;
                case 'q':
                case 'Q':
                    attacks.addAll(QueenAttack(board, Pieces.get(i-1)));
                    break;
                case 'k':
                case 'K':
                    attacks.addAll(KingAttack(board, Pieces.get(i-1)));
                    break;
            }
        }
        return attacks;
    }
    public static List<Integer> PawnAttack(Board board, int pieceIndex, int  whiteMove) {

    }
    public static List<Integer> BishopAttack(Board board, int pieceIndex) {
        //Check diagonal moves of bishop. Attacks can't go thorugh friendly pieces, and can only capture the first enemy piece in line
        List<Integer> bishopMoves = new ArrayList<>();
        boolean pieceIsWhite = (board.getPiece(pieceIndex) == 'Q') ? true : false;
        int squareIndex = pieceIndex;
        int newSquareIndex = squareIndex;
        //Array of 4 directions to explore (top right, top left, bottom right, bottom left)
        int[] directions = {9, 7, -9, -7};

        for (int direction : directions) {
            boolean inBounds = true;
            do {
                newSquareIndex = newSquareIndex + direction;
                if ((newSquareIndex > 63) || (newSquareIndex < 0)) {
                    //Blank square, add to list of moves
                    if (board.getPiece(newSquareIndex) == '.') {
                        bishopMoves.add(newSquareIndex);
                    }
                    //Square contains white piece, attacking piece is white. Do not add to list, break out of loop
                    else if (board.getPiece(newSquareIndex) == Character.toUpperCase(board.getPiece(newSquareIndex)) && pieceIsWhite) {
                        inBounds = false;
                    }
                    //Square contains black piece, attacking piece is black. Do not add to list, break out of loop
                    else if (board.getPiece(newSquareIndex) == Character.toLowerCase(board.getPiece(newSquareIndex)) && !pieceIsWhite) {
                        inBounds = false;
                    }
                    //If not either of top cases, attacking piece and found piece of different colours. Add move, stop loop
                    else {
                        bishopMoves.add(newSquareIndex);
                        inBounds = false;
                    }
                } else inBounds = false;
            } while (!inBounds);
            newSquareIndex = squareIndex;
        }
        return bishopMoves;
    }
    public static void GenerateMoves(Scanner input) {}
    public static void FreeBoard(Scanner input) {}
    public static void DisplayBoard(Board board) {
        //Simple graphic
        for (int i = 7; i >= 0; i--) {
            System.out.print("|");
            System.out.print(board.getPosition().substring((i*8), (i*8)+8));
            System.out.print("|");
            System.out.println();
        }
    }
}