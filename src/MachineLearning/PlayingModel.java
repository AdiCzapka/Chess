package MachineLearning;

public class PlayingModel {
    double queenActivity;
    double rookActivity;
    double pointsSurroundingKing;

    public PlayingModel(double queenActivity, double rookActivity, double pointsSurroundingKing) {
        this.queenActivity = queenActivity;
        this.rookActivity = rookActivity;
        this.pointsSurroundingKing = pointsSurroundingKing;
    }

    public void setQueenActivity(double queenActivity) {this.queenActivity = queenActivity;}
    public void setRookActivity(double rookActivity) {this.rookActivity = rookActivity;}
    public void setPointsSurroundingKing(double pointsSurroundingKing) {this.pointsSurroundingKing = pointsSurroundingKing;}

    public double getQueenActivity() {return this.queenActivity;}
    public double getRookActivity() {return this.rookActivity;}
    public double getPointsSurroundingKing() {return this.pointsSurroundingKing;}

}
