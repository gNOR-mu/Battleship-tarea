package solucion.mapa;

import java.util.Arrays;

import solucion.enumerados.Direccion;
import solucion.posicion.Coordenada;

import java.lang.reflect.Array;

public class Mapa<T> {
    static final int MAXIMO_LINEAS_STRING;
    protected final int LARGO_MAPA;
    protected T[][] mapa;

    static {
        MAXIMO_LINEAS_STRING = 10;
    }

    @SuppressWarnings("unchecked")
    public Mapa(int largoTablero, Class<T> tipo, T valorInicial) {
        if (largoTablero <= 0) {
            throw new IllegalArgumentException("El largo del tablero debe ser mayor que cero.");
        }
        this.LARGO_MAPA = largoTablero;
        this.mapa = (T[][]) Array.newInstance(tipo, largoTablero, largoTablero);
        this.iniciarMapa(valorInicial);
    }

    protected void iniciarMapa(T valor) {
        for (int i = 0; i < LARGO_MAPA; i++) {
            Arrays.fill(mapa[i], valor);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Mapa del océano:\n");
        for (int i = 0; i < MAXIMO_LINEAS_STRING; i++) {
            sb.append(Arrays.toString(mapa[i])).append("\n");
        }
        if (LARGO_MAPA > MAXIMO_LINEAS_STRING) {
            sb.append("...\n");
        }
        return sb.toString();
    }

    protected boolean esDireccionValida(Direccion direccion, int largoBarco, int valorEje) {
        return switch (direccion) {
            case ARRIBA, IZQUIERDA -> valorEje - (largoBarco - 1) >= 0;
            case ABAJO, DERECHA -> valorEje + largoBarco <= LARGO_MAPA;
            default -> false;
        };
    }

    public void marcar(Coordenada coordenada, T valor) {
        int fila = coordenada.getFila();
        int columna = coordenada.getColumna();
        if (esCoordenadaValida(fila, columna)) {
            mapa[fila][columna] = valor;
        }
    }

    protected boolean esCoordenadaValida(Coordenada coordenada) {
        return (coordenada != null
                && coordenada.getFila() >= 0
                && coordenada.getFila() < LARGO_MAPA
                && coordenada.getColumna() >= 0
                && coordenada.getColumna() < LARGO_MAPA);
    }

    protected boolean esCoordenadaValida(int fila, int columna) {
        return (fila >= 0
                && fila < LARGO_MAPA
                && columna >= 0
                && columna < LARGO_MAPA);
    }

}
