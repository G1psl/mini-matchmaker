package matchmaker;

import java.util.*;

/**
 * Cola de emparejamiento. Los jugadores se guardan en un TreeMap ordenado por rating,
 * de modo que buscar el rival mas cercano cuesta O(log n) con lowerKey/higherKey.
 * El rango de busqueda crece con el tiempo de espera para evitar esperas infinitas.
 */
public class MatchmakingQueue {
    private final TreeMap<Integer, Deque<Player>> byRating = new TreeMap<>();
    private final Set<Player> waiting = new LinkedHashSet<>();
    private final int baseRange;
    private final int growthPerTick;

    public MatchmakingQueue(int baseRange, int growthPerTick) {
        this.baseRange = baseRange;
        this.growthPerTick = growthPerTick;
    }

    public int size() { return waiting.size(); }

    /** O(log n) */
    public void add(Player p, int tick) {
        p.setEnqueuedAt(tick);
        byRating.computeIfAbsent(p.getRating(), r -> new ArrayDeque<>()).addLast(p);
        waiting.add(p);
    }

    public int allowedRange(Player p, int tick) {
        return baseRange + growthPerTick * (tick - p.getEnqueuedAt());
    }

    /** Intenta emparejar a todos los jugadores en espera. Cada jugador aparece en a lo mas un Match. */
    public List<Match> findMatches(int tick) {
        List<Match> matches = new ArrayList<>();
        for (Player p : new ArrayList<>(waiting)) {
            if (!waiting.contains(p)) continue; // ya fue emparejado en esta ronda
            Player opponent = findClosest(p, allowedRange(p, tick));
            if (opponent == null) continue;
            remove(p);
            remove(opponent);
            matches.add(new Match(p, opponent,
                    tick - p.getEnqueuedAt(), tick - opponent.getEnqueuedAt(),
                    Math.abs(p.getRating() - opponent.getRating())));
        }
        return matches;
    }

    private Player findClosest(Player p, int range) {
        int r = p.getRating();

        Deque<Player> same = byRating.get(r);
        if (same != null) {
            for (Player c : same) if (c != p) return c;
        }

        Integer lo = byRating.lowerKey(r);
        Integer hi = byRating.higherKey(r);
        Integer best = null;
        if (lo != null && r - lo <= range) best = lo;
        if (hi != null && hi - r <= range && (best == null || hi - r < r - best)) best = hi;
        return best == null ? null : byRating.get(best).peekFirst();
    }

    private void remove(Player p) {
        Deque<Player> bucket = byRating.get(p.getRating());
        bucket.remove(p);
        if (bucket.isEmpty()) byRating.remove(p.getRating());
        waiting.remove(p);
    }
}
