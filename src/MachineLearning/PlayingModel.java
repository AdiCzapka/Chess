package MachineLearning;

public class PlayingModel {
    double queenActivity;
    double rookActivity;
    double pointsSurroundingKing;
    double checkmate;

    public PlayingModel(double queenActivity, double rookActivity, double pointsSurroundingKing, double checkmate) {
        this.queenActivity = queenActivity;
        this.rookActivity = rookActivity;
        this.pointsSurroundingKing = pointsSurroundingKing;
        this.checkmate = checkmate;
    }

    public void setQueenActivity(double queenActivity) {this.queenActivity = queenActivity;}
    public void setRookActivity(double rookActivity) {this.rookActivity = rookActivity;}
    public void setPointsSurroundingKing(double pointsSurroundingKing) {this.pointsSurroundingKing = pointsSurroundingKing;}
    public void setCheckmate(double checkmate) {this.checkmate = checkmate;}

    public double getQueenActivity() {return this.queenActivity;}
    public double getRookActivity() {return this.rookActivity;}
    public double getPointsSurroundingKing() {return this.pointsSurroundingKing;}
    public double getCheckmate() {return this.checkmate;}

}
