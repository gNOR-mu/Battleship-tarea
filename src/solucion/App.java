package solucion;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import problema.Tablero;

public class App {
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger(App.class.getName());

        final int LARGO_TABLERO = 10;
        final int CANTIDAD_JUEGOS = 500_000;
        final int CANTIDAD_HILOS = Runtime.getRuntime().availableProcessors();
        // mejor tiempo 1.22s para 500_000 juegos en i5-14600k
        AtomicInteger intentos = new AtomicInteger(0);
        AtomicLong tiempoTotalTablero = new AtomicLong(0);

        long startTime = System.currentTimeMillis();
        try (ForkJoinPool forkJoinPool = new ForkJoinPool(CANTIDAD_HILOS)) {
            forkJoinPool.submit(() -> IntStream.range(0, CANTIDAD_JUEGOS).parallel().forEach(i -> {
                // Inicio de la medición del tiempo de creación del Tablero
                long startTableroTime = System.nanoTime();
                Tablero tablero = new Tablero(LARGO_TABLERO);
                long endTableroTime = System.nanoTime();
                tiempoTotalTablero.addAndGet(endTableroTime - startTableroTime);

                Map<Character, Barco> barcos = crearBarcos();
                Solucionador solucionador = new Solucionador(tablero, LARGO_TABLERO, barcos);
                solucionador.solucionar();
                intentos.addAndGet(tablero.ganar());
            })).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InterruptedException();
        }

        double tiempoTermino = ((double) System.currentTimeMillis() - startTime) / 1000;
        double tiempoTableros = (double) tiempoTotalTablero.get() / 1_000_000_000 / CANTIDAD_HILOS;
        double tiempoResolviendo = tiempoTermino - tiempoTableros;
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, String.format(
                    "Intentos: %d%nIntentos promedio: %.4f%nTiempo de ejecucion: %.3f segundos%nTiempo creando Tableros: %.3f segundos%nTiempo resolviendo %.3f segundos ",
                    intentos.get(),
                    (double) intentos.get() / CANTIDAD_JUEGOS,
                    tiempoTermino,
                    tiempoTableros, // este tiempo ya depende del problema base.
                    tiempoResolviendo));
        }
    }

    public static Map<Character, Barco> crearBarcos() {
        Map<Character, Barco> barcos = new HashMap<>();
        barcos.put('A', new Barco('A', 5)); // PortaAviones, A, 5
        barcos.put('B', new Barco('B', 4)); // Buque, B, 4
        barcos.put('S', new Barco('S', 3)); // Submarino, S, 3
        barcos.put('C', new Barco('C', 3)); // Crucero, C, 3
        barcos.put('D', new Barco('D', 2)); // Destructor, D, 2
        return barcos;
    }

}
