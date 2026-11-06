package matchmaker;

public class Player {
    private final int id;
    private final String name;
    private int rating;
    private int enqueuedAt;

    public Player(int id, String name, int rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public int getEnqueuedAt() { return enqueuedAt; }
    public void setEnqueuedAt(int tick) { this.enqueuedAt = tick; }

    @Override
    public String toString() { return name + "(" + rating + ")"; }
}
