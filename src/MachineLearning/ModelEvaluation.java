package MachineLearning;

public class ModelEvaluation {
    String originSquare;
    String destinationSquare;
    double score;

    public ModelEvaluation(String originSquare, String destinationSquare, double score) {
        this.originSquare = originSquare;
        this.destinationSquare = destinationSquare;
        this.score = score;
    }

    public void setOriginSquare(String originSquare) {this.originSquare = originSquare;}
    public void setDestinationSquare(String destinationSquare) {this.destinationSquare = destinationSquare;}
    public void setScore(double score) {this.score = score;}

    public String getOriginSquare() {return originSquare;}
    public String getDestinationSquare() {return destinationSquare;}
    public double getScore() {return score;}
}
