import Board.Board;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Chess");
        boolean picked = false;
        do {
            System.out.println("What do you wish to do? (Play, Generate, Free Board)");
            Scanner input = new Scanner(System.in);
            String choice = input.nextLine();
            switch (choice) {
                case "Play":
                    PlayChess();
                    picked = true;
                    break;
                case "Generate Moves":
                    GenerateMoves();
                    picked = true;
                    break;
                case "Free Board":
                    FreeBoard();
                    picked = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (!picked);
    }

    public static void PlayChess() {
        //Initialise Board
        Board board = new Board();
        Scanner input = new Scanner(System.in);
        //White to move
        char playerToMove = 'w';
        System.out.println("Play as white or black?");
        char choice = input.nextLine().charAt(0);

        //Display graphic to represent board
        DisplayBoard(board);

        //Player is white
        if (playerToMove == choice){
            PlayerMove(board, choice);
        }
        //Is black
        else {
            ComputerMove(board, choice);
        }
    }
    public static void GenerateMoves() {}
    public static void FreeBoard() {}
    public static void DisplayBoard(Board board) {
        //Simple graphic
        for (int i = 7; i >= 0; i--) {
            System.out.print("|");
            System.out.print(board.getPosition().substring((i*8), (i*8)+8));
            System.out.print("|");
            System.out.println();
        }
    }
    public static void PlayerMove(Board board, char choice) {
        Scanner input = new Scanner(System.in);
        boolean validPiece = false;
        //Repeat until player picks their own piece
        do {
            boolean validSquare = true;
            int pieceIndexInString;
            do {
                System.out.println("Enter the position of the piece you wish to move: ");
                String pieceSquare = input.nextLine();
                //Convert position to index. a1 = 0, h8 = 63
                pieceIndexInString = (((pieceSquare.charAt(0) - 'a') * 8) + ((pieceSquare.charAt(1) - '0') - 1));
                if (pieceIndexInString < 0 || pieceIndexInString > 63) {
                    validSquare = false;
                }
            } while (!validSquare);
            //This is probably really stupid
            //Check square not empty
            if (board.getPiece(pieceIndexInString) != '.') {
                //If white
                if (choice == 'w') {
                    //Piece capitalised
                    if (board.getPiece(pieceIndexInString) == Character.toUpperCase(board.getPiece(pieceIndexInString))) {
                        StartMove(pieceIndexInString, board);
                        validPiece = true;
                    } else {}
                //If black
                } else {
                    //Piece not capitalised
                    if (board.getPiece(pieceIndexInString) == Character.toLowerCase(board.getPiece(pieceIndexInString))) {
                        StartMove(pieceIndexInString, board);
                        validPiece = true;
                    } else {}
                }
            }
        } while (!validPiece);

        //Check for checkmate
        //Pass to Computer
    }

    public static void StartMove(int pieceIndexInString, Board board) {
        switch (board.getPiece(pieceIndexInString)){
            case 'p':
            case 'P':
                PawnMove(pieceIndexInString, board);
                break;
            case 'b':
            case 'B':
                BishopMove(pieceIndexInString, board);
                break;
            case 'n':
            case 'N':
                KnightMove(pieceIndexInString, board);
                break;
            case 'r':
            case 'R':
                RookMove(pieceIndexInString, board);
                break;
            case 'q':
            case 'Q':
                QueenMove(pieceIndexInString, board);
                break;
            case 'k':
            case 'K':
                KingMove(pieceIndexInString, board);
                break;
        }
    }

    public static void PawnMove(int pieceIndexInString, Board board) {
        //2nd row indexes = 8 - 15. //7th row indexes = 48 - 55. Allow double move up / down
        if ((8 <= pieceIndexInString && pieceIndexInString <= 15) || (48  <= pieceIndexInString && pieceIndexInString <= 55)) {

        }
    }
    public static void BishopMove(int pieceIndexInString, Board board) {}
    public static void KnightMove(int pieceIndexInString, Board board) {}
    public static void RookMove(int pieceIndexInString, Board board) {}
    public static void QueenMove(int pieceIndexInString, Board board) {}
    public static void KingMove(int pieceIndexInString, Board board) {}

    public static void ComputerMove(Board board, char choice) {

    }
}