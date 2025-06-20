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

    protected boolean esDireccionValida(Direccion direccion, int largoBarco, int valorEje) {
        largoBarco--;
        if (direccion == Direccion.ARRIBA || direccion == Direccion.IZQUIERDA) {
            return valorEje - largoBarco >= 0;
        } else if (direccion == Direccion.ABAJO || direccion == Direccion.DERECHA) {
            return valorEje + largoBarco < mapa.length;
        }
        return false;
    }

    protected boolean esCoordenadaValida(Punto punto) {
        return (punto != null
                && punto.getFila() >= 0
                && punto.getFila() < mapa.length
                && punto.getColumna() >= 0
                && punto.getColumna() < mapa[0].length);
    }
}
