package matchmaker;

import java.util.Random;

public class Simulation {
    public static void main(String[] args) {
        Random rnd = new Random(42);
        EloCalculator elo = new EloCalculator();
        MatchmakingQueue queue = new MatchmakingQueue(50, 10);

        int totalPlayers = 1000, arrivalsPerTick = 5, maxTicks = 2000;
        int created = 0, matchCount = 0, tick = 0;
        long totalWait = 0, totalDiff = 0;

        while ((created < totalPlayers || queue.size() > 1) && tick < maxTicks) {
            for (int i = 0; i < arrivalsPerTick && created < totalPlayers; i++) {
                int rating = Math.max(100, (int) Math.round(1500 + rnd.nextGaussian() * 300));
                queue.add(new Player(created, "P" + created, rating), tick);
                created++;
            }
            for (Match m : queue.findMatches(tick)) {
                double expectedA = elo.expectedScore(m.a().getRating(), m.b().getRating());
                if (rnd.nextDouble() < expectedA) elo.applyResult(m.a(), m.b());
                else elo.applyResult(m.b(), m.a());
                matchCount++;
                totalWait += m.waitA() + m.waitB();
                totalDiff += m.ratingDiff();
            }
            tick++;
        }

        System.out.println("Partidas jugadas:            " + matchCount);
        System.out.println("Espera promedio (ticks):     " + String.format("%.2f", totalWait / (2.0 * matchCount)));
        System.out.println("Diferencia media de rating:  " + String.format("%.2f", totalDiff / (double) matchCount));
        System.out.println("Jugadores sin pareja:        " + queue.size());
    }
}
