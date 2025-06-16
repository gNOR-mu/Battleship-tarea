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
        int maxFilas = Math.min(5, mapa.length);
        StringBuilder sb = new StringBuilder("Mapa del océano:\n");
        for (int i = 0; i < maxFilas; i++) {
            sb.append(Arrays.toString(mapa[i])).append("\n");
        }
        if (mapa.length > maxFilas)
            sb.append("...\n");
        return sb.toString();
    }

    protected boolean esDireccionValida(int largoBarco, Punto punto) {
        int valorEje = (punto.getDireccion() == Direccion.ARRIBA || punto.getDireccion() == Direccion.ABAJO) ? punto.getFila() : punto.getColumna();
        return switch (punto.getDireccion()) {
            case ARRIBA, IZQUIERDA -> valorEje - (largoBarco - 1) >= 0;
            case ABAJO, DERECHA -> valorEje + (largoBarco - 1) < mapa.length;
            default -> false;
        };
    }

    protected boolean esCoordenadaValida(Punto punto) {
        return (punto != null
                && punto.getFila() >= 0
                && punto.getFila() < mapa.length
                && punto.getColumna() >= 0
                && punto.getColumna() < mapa[0].length);
    }
}
