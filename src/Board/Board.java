package Board;

public class Board {
    int row = 8;
    int col = 8;
    String position;

    public Board(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void SetPosition(String position) {
        this.position = position;
    }
    public char getPiece(int Index) {
        return position.charAt(Index);
    }
}
