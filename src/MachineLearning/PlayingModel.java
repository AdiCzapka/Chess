package MachineLearning;

public class PlayingModel {
    double queenActivity;
    double rookActivity;
    double pointsSurroundingKing;
    double queenActivityAdvantage;
    double rookActivityAdvantage;
    double pointsSurroundingKingAdvantage;
    double materialAdvantage;
    double checkmate;

    public PlayingModel(double queenActivity, double rookActivity, double pointsSurroundingKing, double checkmate, double queenActivityAdvantage, double rookActivityAdvantage, double pointsSurroundingKingAdvantage,  double materialAdvantage) {
        this.queenActivity = queenActivity;
        this.rookActivity = rookActivity;
        this.pointsSurroundingKing = pointsSurroundingKing;
        this.checkmate = checkmate;
        this.queenActivityAdvantage = queenActivityAdvantage;
        this.rookActivityAdvantage = rookActivityAdvantage;
        this.pointsSurroundingKingAdvantage = pointsSurroundingKingAdvantage;
        this.materialAdvantage = materialAdvantage;
    }

    public void setQueenActivity(double queenActivity) {this.queenActivity = queenActivity;}
    public void setRookActivity(double rookActivity) {this.rookActivity = rookActivity;}
    public void setPointsSurroundingKing(double pointsSurroundingKing) {this.pointsSurroundingKing = pointsSurroundingKing;}
    public void setCheckmate(double checkmate) {this.checkmate = checkmate;}
    public void setQueenActivityAdvantage(double queenActivityAdvantage) {this.queenActivityAdvantage = queenActivityAdvantage;}
    public void setRookActivityAdvantage(double rookActivityAdvantage) {this.rookActivityAdvantage = rookActivityAdvantage;}
    public void setPointsSurroundingKingAdvantage(double pointsSurroundingKingAdvantage) {this.pointsSurroundingKingAdvantage = pointsSurroundingKingAdvantage;}
    public void setMaterialAdvantage(double materialAdvantage) {this.materialAdvantage = materialAdvantage;}

    public double getQueenActivity() {return this.queenActivity;}
    public double getRookActivity() {return this.rookActivity;}
    public double getPointsSurroundingKing() {return this.pointsSurroundingKing;}
    public double getCheckmate() {return this.checkmate;}
    public double getQueenActivityAdvantage() {return this.queenActivityAdvantage;}
    public double getRookActivityAdvantage() {return this.rookActivityAdvantage;}
    public double getPointsSurroundingKingAdvantage() {return this.pointsSurroundingKingAdvantage;}
    public double getMaterialAdvantage() {return this.materialAdvantage;}

}
