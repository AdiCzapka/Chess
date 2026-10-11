package GUI;

public class ColourSignal extends GUISignal{
    private final String colour;
    public ColourSignal(String isWhite) {this.colour = isWhite;}
    public String getColourOption() {return colour;}
}
