package matchmaker;

public final class EloCalculator {
    private final int k;

    public EloCalculator() { this(32); }

    public EloCalculator(int k) { this.k = k; }

    /** Probabilidad esperada de que el jugador con ratingA gane contra ratingB. O(1). */
    public double expectedScore(int ratingA, int ratingB) {
        return 1.0 / (1.0 + Math.pow(10, (ratingB - ratingA) / 400.0));
    }

    /** score: 1.0 si gana, 0.5 si empata, 0.0 si pierde. O(1). */
    public int newRating(int rating, int opponentRating, double score) {
        return (int) Math.round(rating + k * (score - expectedScore(rating, opponentRating)));
    }

    /** Actualiza ambos jugadores usando los ratings previos de los dos. */
    public void applyResult(Player winner, Player loser) {
        int w = winner.getRating();
        int l = loser.getRating();
        winner.setRating(newRating(w, l, 1.0));
        loser.setRating(newRating(l, w, 0.0));
    }
}
