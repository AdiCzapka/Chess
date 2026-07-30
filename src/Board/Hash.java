package Board;

public class Hash {
    String hashedPosition;
    int count;

    public Hash(String hashedPosition, int count) {
        this.hashedPosition = hashedPosition;
        this.count = count;
    }

    public int getCount() {
        return count;
    }
    public void increment() {
        count++;
    }
    public String getHashedPosition() {
        return hashedPosition;
    }
}
