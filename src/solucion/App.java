package solucion;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import problema.Tablero;
import solucion.solucionador.Solucionador;

public class App {
    public static void main(String[] args) {
        final int LARGO_TABLERO = 10;
        final int CANTIDAD_JUEGOS = 500_000;
        final int CANTIDAD_HILOS = Runtime.getRuntime().availableProcessors();
        // mejor tiempo resolviendo 1.029s para 500_000 juegos en i5-14600k
        AtomicInteger intentos = new AtomicInteger(0);
        AtomicLong tiempoTotalTablero = new AtomicLong(0);

        long startTime = System.currentTimeMillis();
        IntStream.range(0, CANTIDAD_JUEGOS).parallel().forEach(i -> {
            long startTableroTime = System.nanoTime();
            Tablero tablero = new Tablero(LARGO_TABLERO);
            long endTableroTime = System.nanoTime();
            tiempoTotalTablero.addAndGet(endTableroTime - startTableroTime);

            Map<Character, Barco> barcos = crearBarcos();
            Solucionador solucionador = new Solucionador(tablero, LARGO_TABLERO, barcos);
            solucionador.solucionar();
            intentos.addAndGet(tablero.ganar());
        });

        double tiempoTermino = ((double) System.currentTimeMillis() - startTime) / 1000;
        double tiempoTableros = (double) tiempoTotalTablero.get() / 1_000_000_000 / CANTIDAD_HILOS;
        double tiempoResolviendo = tiempoTermino - tiempoTableros;

        System.out.format("Intentos: %d%n", intentos.get());
        System.out.format("Intentos promedio: %.4f%n", (double) intentos.get() / CANTIDAD_JUEGOS);
        System.out.format("Tiempo de ejecucion: %.3f segundos%n", tiempoTermino);
        System.out.format("Tiempo creando Tableros: %.3f segundos%n", tiempoTableros);
        System.out.format("Tiempo resolviendo: %.3f segundos%n", tiempoResolviendo);
    }

    
    /** 
     * Crea los barcos del juego
     * @return Map<Character, Barco> Map de barcos con key: inicial, valor new Barco
     */
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
