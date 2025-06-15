package solucion.mapa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import solucion.Barco;
import solucion.Punto;
import solucion.enumerados.Direccion;

import java.security.SecureRandom;

public class MapaCalor extends Mapa<Integer> {
    private static final SecureRandom RANDOM = new SecureRandom();
    private MapaOceano mapaOceano;

    public MapaCalor(int largoTablero, MapaOceano mapaOceano) {
        super(largoTablero, Integer.class);
        this.mapaOceano = mapaOceano;
    }

    public void actualizarMapa(Map<Character, Barco> barcos) {
        reiniciarMapa();
        IntStream.range(0, mapa.length * mapa[0].length).forEach(idx -> {
            int fila = idx / mapa.length;
            int columna = idx % mapa[0].length;
            int posibilidad = 0;
            Punto punto = new Punto(fila, columna);
            for (Barco barco : barcos.values()) {
                punto.setDireccion(Direccion.ARRIBA);
                posibilidad += posibilidadDireccion(barco, punto);
                punto.setDireccion(Direccion.ABAJO);
                posibilidad += posibilidadDireccion(barco, punto);
                punto.setDireccion(Direccion.IZQUIERDA);
                posibilidad += posibilidadDireccion(barco, punto);
                punto.setDireccion(Direccion.DERECHA);
                posibilidad += posibilidadDireccion(barco, punto);
            }
            mapa[fila][columna] += posibilidad;
        });
    }

    private int posibilidadDireccion(Barco barco, Punto punto) {
        return (esDireccionValida(barco.getLargo(), punto) && mapaOceano.puedeUbicarse(barco, punto)) ? 1 : 0;
    }

    private void reiniciarMapa() {
        for (Integer[] fila : mapa) {
            Arrays.fill(fila, 0);
        }
    }

    public Punto getSugerencia() {
        int mayorPosibilidad = 0;
        ArrayList<Punto> sugerencias = new ArrayList<>();
        for (int fila = 0; fila < mapa.length; fila++) {
            for (int columna = 0; columna < mapa[0].length; columna++) {
                int valor = mapa[fila][columna];
                if (valor > mayorPosibilidad) {
                    mayorPosibilidad = valor;
                    sugerencias.clear();
                }
                if (valor == mayorPosibilidad && valor > 0 && mapaOceano.esCasillaDesconocida(new Punto(fila, columna))) {
                    sugerencias.add(new Punto(fila, columna));
                }
            }
        }
        if (sugerencias.isEmpty()) {
            return null;
        }
        return sugerencias.get(RANDOM.nextInt(sugerencias.size()));
    }

    public boolean chocaConBorde(Punto punto) {
        return switch (punto.getDireccion()) {
            case IZQUIERDA -> punto.getFila() == 0;
            case ARRIBA -> punto.getColumna() == 0;
            case DERECHA -> punto.getFila() == mapa.length - 1;
            case ABAJO -> punto.getColumna() == mapa[0].length - 1;
            default -> false;
        };
    }

      public Punto getSugerenciaFocalizada(Punto puntoOriginal, Punto puntoActualDisparo, Barco barco) {
        boolean direccionIntentadaCambiada = false;
        Punto posiblePuntoSugerido;
        do {
            posiblePuntoSugerido = new Punto(puntoActualDisparo);
            if (posiblePuntoSugerido.getDireccion() == null) {
                posiblePuntoSugerido.setDireccion(Direccion.direccionAleatoria());
            }
            switch (posiblePuntoSugerido.getDireccion()) {
                case IZQUIERDA -> posiblePuntoSugerido.mover(Direccion.IZQUIERDA);
                case DERECHA -> posiblePuntoSugerido.mover(Direccion.DERECHA);
                case ARRIBA -> posiblePuntoSugerido.mover(Direccion.ARRIBA);
                case ABAJO -> posiblePuntoSugerido.mover(Direccion.ABAJO);
            }
            if (puntoActualDisparo.getDireccion() != null) {
                if (direccionIntentadaCambiada) {
                    puntoActualDisparo.setDireccion(null);
                } else if (chocaConBorde(posiblePuntoSugerido) || !esCoordenadaValida(posiblePuntoSugerido)
                        || !mapaOceano.esCasillaDesconocida(posiblePuntoSugerido)) {
                    direccionIntentadaCambiada = true;
                    Direccion nuevaDireccion = posiblePuntoSugerido.getDireccion().direccionOpuesta();
                    puntoActualDisparo = new Punto(puntoOriginal, nuevaDireccion);
                    continue;
                }
            }
            if (esCoordenadaValida(posiblePuntoSugerido) && mapaOceano.esCasillaDesconocida(posiblePuntoSugerido)) {
                return posiblePuntoSugerido;
            }
        } while (true);
    }

}
