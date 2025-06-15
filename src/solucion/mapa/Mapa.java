package solucion.mapa;

import java.util.Arrays;

import solucion.Punto;
import solucion.enumerados.Direccion;
import java.lang.reflect.Array;

public class Mapa<T> {
    protected T[][] mapa;

    @SuppressWarnings("unchecked")
    public Mapa(int largoTablero, Class<T> tipo) {
        if (largoTablero <= 0) {
            throw new IllegalArgumentException("El largo del tablero debe ser mayor que cero.");
        }
        this.mapa = (T[][]) Array.newInstance(tipo, largoTablero, largoTablero);
    }

    @Override
    public String toString() {
        return String.format("MapaCalor(%n%s)", Arrays.deepToString(mapa).replace("],", "]\n"));
    }

    protected boolean esDireccionValida(int largoBarco, Punto punto) {
        int valorEje = switch (punto.getDireccion()) {
            case ARRIBA, ABAJO -> punto.getFila();
            case IZQUIERDA, DERECHA -> punto.getColumna();
        };
        return switch (punto.getDireccion()) {
            case ARRIBA, IZQUIERDA -> valorEje - (largoBarco - 1) >= 0;
            case ABAJO, DERECHA -> valorEje + (largoBarco - 1) < mapa.length;
            default -> false;
        };
    }

    protected boolean esCoordenadaValida(Punto punto) {
        if (punto == null) {
            return false;
        }
        return (punto.getFila() >= 0
                && punto.getFila() < mapa.length
                && punto.getColumna() >= 0
                && punto.getColumna() < mapa.length);
    }
}
