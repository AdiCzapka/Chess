package MachineLearning;

public class ModelEvaluation {
    String originSquare;
    String destinationSquare;
    double score;
    char piecePromotionSymbol;

    public ModelEvaluation(String originSquare, String destinationSquare, double score, char piecePromotionSymbol) {
        this.originSquare = originSquare;
        this.destinationSquare = destinationSquare;
        this.score = score;
        this.piecePromotionSymbol = piecePromotionSymbol;
    }

    public void setOriginSquare(String originSquare) {this.originSquare = originSquare;}
    public void setDestinationSquare(String destinationSquare) {this.destinationSquare = destinationSquare;}
    public void setScore(double score) {this.score = score;}
    public void setPiecePromotionSymbol(char piecePromotionSymbol) {this.piecePromotionSymbol = piecePromotionSymbol;}

    public String getOriginSquare() {return originSquare;}
    public String getDestinationSquare() {return destinationSquare;}
    public double getScore() {return score;}
    public char getPiecePromotionSymbol() {return piecePromotionSymbol;}
}
