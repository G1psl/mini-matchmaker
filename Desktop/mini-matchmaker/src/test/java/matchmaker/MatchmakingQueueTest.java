package matchmaker;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class MatchmakingQueueTest {
    @Test
    void closePlayersAreMatchedImmediately() {
        MatchmakingQueue q = new MatchmakingQueue(50, 10);
        q.add(new Player(1, "a", 1500), 0);
        q.add(new Player(2, "b", 1520), 0);
        List<Match> matches = q.findMatches(0);
        assertEquals(1, matches.size());
        assertEquals(20, matches.get(0).ratingDiff());
        assertEquals(0, q.size());
    }

    @Test
    void distantPlayersAreNotMatchedAtFirst() {
        MatchmakingQueue q = new MatchmakingQueue(50, 10);
        q.add(new Player(1, "a", 1000), 0);
        q.add(new Player(2, "b", 2000), 0);
        assertTrue(q.findMatches(0).isEmpty());
        assertEquals(2, q.size());
    }

    @Test
    void rangeGrowsWithWaitingTime() {
        MatchmakingQueue q = new MatchmakingQueue(50, 10);
        q.add(new Player(1, "a", 1000), 0);
        q.add(new Player(2, "b", 2000), 0);
        assertEquals(1, q.findMatches(100).size()); // 50 + 10*100 = 1050 >= 1000
    }

    @Test
void picksTheClosestOpponent() {
    MatchmakingQueue q = new MatchmakingQueue(500, 10);
    Player me = new Player(1, "me", 1500);
    Player near = new Player(2, "near", 1510);
    Player far = new Player(3, "far", 1700);
    q.add(me, 0);
    q.add(near, 0);
    q.add(far, 0);
    Match m = q.findMatches(0).get(0);
    assertEquals(10, m.ratingDiff());
    assertEquals(1, q.size());
}

    @Test
    void aPlayerIsNeverMatchedTwice() {
        MatchmakingQueue q = new MatchmakingQueue(500, 10);
        for (int i = 0; i < 5; i++) q.add(new Player(i, "p" + i, 1500 + i), 0);
        List<Match> matches = q.findMatches(0);
        assertEquals(2, matches.size());
        assertEquals(1, q.size());
    }

    @Test
    void samePlayerCannotBeMatchedWithItself() {
        MatchmakingQueue q = new MatchmakingQueue(50, 10);
        q.add(new Player(1, "solo", 1500), 0);
        assertTrue(q.findMatches(0).isEmpty());
    }
}
