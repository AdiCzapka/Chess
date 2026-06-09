package Board;

public class Board {
    int row = 8;
    int col = 8;
    static String position = "RNBQKBNRPPPPPPPP................................pppppppprnbqkbnr";

    public Board() {}

    public static String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public char getPiece(int Index) {
        return position.charAt(Index);
    }
}
