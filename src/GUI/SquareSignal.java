package GUI;
import Board.Square;

public class SquareSignal extends GUISignal {
    private final Square square;
    public SquareSignal(Square square) {
        this.square = square;
    }
    public Square getSquare() {
        return square;
    }
}