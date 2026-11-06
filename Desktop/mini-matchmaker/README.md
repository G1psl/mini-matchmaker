# Mini Matchmaker (Java)

Prototipo de matchmaking con ranking Elo. Empareja jugadores de nivel parecido y
amplía el rango de búsqueda según el tiempo de espera.

## Cómo ejecutarlo
```
mvn test                                   # pruebas unitarias
mvn -q compile exec:java -Dexec.mainClass=matchmaker.Simulation
# o sin Maven:
javac -d out src/main/java/matchmaker/*.java && java -cp out matchmaker.Simulation
```

## Diseño
- `Player`, `Match`: modelo de datos.
- `EloCalculator`: probabilidad esperada y actualización de rating, O(1).
- `MatchmakingQueue`: `TreeMap<Integer, Deque<Player>>` ordenado por rating.
  - `add`: O(log n)
  - búsqueda del rival más cercano: O(log n) con `lowerKey` / `higherKey`
- `Simulation`: 1,000 jugadores con rating ~ N(1500, 300); reporta espera media y diferencia de rating.

## Limitaciones y siguientes pasos
Es un prototipo de un solo hilo y en memoria. Para escalarlo: API REST/gRPC,
Redis o Kafka para la cola, concurrencia segura, tener en cuenta latencia y región,
y usar Glicko-2 para modelar la incertidumbre del rating.
