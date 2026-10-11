package GUI;

public class PromotionSignal extends GUISignal {
    private final char promotionSymbol;
    public PromotionSignal(char promotionSymbol) {
        this.promotionSymbol = promotionSymbol;
    }
    public char getPromotionSymbol() {
        return promotionSymbol;
    }
}