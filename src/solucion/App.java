package solucion;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import problema.Tablero;

public class App {
    public static void main(String[] args) throws Exception {
        final int LARGO_TABLERO = 10;
        final int CANTIDAD_JUEGOS = obtenerCantidadJuegos();
        // mejor tiempo 6.42s para 500_000 juegos en i5-14600k
        AtomicInteger intentos = new AtomicInteger(0);
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        long startTime = System.currentTimeMillis();
        forkJoinPool.submit(() -> IntStream.range(0, CANTIDAD_JUEGOS).parallel().forEach(i -> {
            Tablero tablero = new Tablero(LARGO_TABLERO);
            Map<Character, Barco> barcos = crearBarcos();
            Solucionador solucionador = new Solucionador(tablero, LARGO_TABLERO, barcos);
            solucionador.solucionar();
            intentos.addAndGet(tablero.ganar());
        })).get();
        forkJoinPool.close();
        double tiempoTermino = System.currentTimeMillis() - startTime;
        System.out.format("Intentos promedio: %.2f%n", (double) intentos.get() / CANTIDAD_JUEGOS);
        System.out.println("Tiempo de ejecucion: " + tiempoTermino / 1000 + " segundos");
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

    public static int obtenerCantidadJuegos() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Cantidad de juegos: ");
        int cantidadJuegos = sc.nextInt();
        sc.close();
        return cantidadJuegos;
    }
}
