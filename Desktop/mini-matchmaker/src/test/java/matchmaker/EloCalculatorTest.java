package matchmaker;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EloCalculatorTest {
    private final EloCalculator elo = new EloCalculator(32);

    @Test
    void equalRatingsHaveHalfExpectedScore() {
        assertEquals(0.5, elo.expectedScore(1500, 1500), 1e-9);
    }

    @Test
    void winnerGainsAndLoserLoses() {
        Player w = new Player(1, "w", 1500), l = new Player(2, "l", 1500);
        elo.applyResult(w, l);
        assertTrue(w.getRating() > 1500);
        assertTrue(l.getRating() < 1500);
    }

    @Test
    void upsetGivesBiggerReward() {
        int underdogGain = elo.newRating(1200, 1800, 1.0) - 1200;
        int favoriteGain = elo.newRating(1800, 1200, 1.0) - 1800;
        assertTrue(underdogGain > favoriteGain);
    }

    @Test
    void ratingPointsAreApproximatelyConserved() {
        Player w = new Player(1, "w", 1600), l = new Player(2, "l", 1400);
        elo.applyResult(w, l);
        assertEquals(3000, w.getRating() + l.getRating(), 1);
    }
}
