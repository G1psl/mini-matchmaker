package matchmaker;

/** Resultado de un emparejamiento. Los datos se capturan antes de actualizar el Elo. */
public record Match(Player a, Player b, int waitA, int waitB, int ratingDiff) { }
