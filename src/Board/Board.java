package Board;

public class Board {
    int rows;
    int cols;
    char[][] board;
    public Board() {
        rows = 8;
        cols = 8;
        board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                //i is row. i = 0 = rank 1
                //j is column. j = 0 = a
                if ((i == 0 && (j == 0 || j == 7))) board[i][j] = 'R';
                else if ((i == 7 && (j == 0 || j == 7))) board[i][j] = 'r';
                else if ((i == 0 && (j == 1 || j == 6))) board[i][j] = 'N';
                else if ((i == 7 && (j == 1 || j == 6))) board[i][j] = 'n';
                else if ((i == 0 && (j == 2 || j == 5))) board[i][j] = 'B';
                else if ((i == 7 && (j == 2 || j == 5))) board[i][j] = 'b';
                else if (i == 0 && j == 3) board[i][j] = 'Q';
                else if (i == 7 && j == 3) board[i][j] = 'q';
                else if (i == 0 && j == 4) board[i][j] = 'K';
                else if (i == 7 && j == 4) board[i][j] = 'k';
                else if (i == 1) board[i][j] = 'P';
                else if (i == 6) board[i][j] = 'p';
                //temp
                else if (i == 5 && j == 4)  board[i][j] = 'N';
                else board[i][j] = '.';
            }
        }
    }

    public void printBoard() {
        System.out.println("print ran");
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                sb.append("|");
                sb.append(board[i][j]);
                sb.append("|");
            }
            sb.append("\n");
        }
        System.out.println("print ran");
        System.out.println(sb.toString());
        System.out.println("print ran");
    }

    public char[][] getBoard() {
        return board;
    }
    public void setBoard(char[][] board) {
        this.board = board;
    }
    public char getPiece(int row, int col) {
        return board[row][col];
    }
    public void setPiece(int row, int col, char p) {
        board[row][col] = p;
    }

}
