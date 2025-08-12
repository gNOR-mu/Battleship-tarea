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

    /**
     * Inicializa el mapa con el valor especificado en todas las posiciones.
     *
     * @param valor valor con el que se inicializa cada celda del mapa
     */
    protected void iniciarMapa(T valor) {
        for (int i = 0; i < LARGO_MAPA; i++) {
            Arrays.fill(mapa[i], valor);
        }
    }

    /**
     * Verifica si una dirección es válida para ubicar un barco de cierto largo
     * desde un valor de eje dado.
     *
     * @param direccion  dirección a verificar
     * @param largoBarco longitud del barco
     * @param valorEje   valor de fila o columna desde donde se evalua
     * @return true si la dirección es válida, false en caso contrario
     */
    protected boolean esDireccionValida(Direccion direccion, int largoBarco, int valorEje) {
        // case ARRIBA, IZQUIERDA -> valorEje - largoBarco >= -1; con -1 debido al largo
        // del barco, se le debería restar a (largoBarco -1) para que sea >= 0
        // sin embargo es costoso si se repite más de 18 millones de veces
        if (direccion == Direccion.ARRIBA || direccion == Direccion.IZQUIERDA) {
            return valorEje - largoBarco >= -1;
        } else {
            return valorEje + largoBarco <= LARGO_MAPA;
        }
    }

    /**
     * Marca la coordenada especificada en el mapa con el valor dado.
     *
     * @param coordenada coordenada a marcar
     * @param valor      valor a asignar en la celda
     */
    public void marcar(Coordenada coordenada, T valor) {
        if (esCoordenadaValida(coordenada)) {
            mapa[coordenada.getFila()][coordenada.getColumna()] = valor;
        }
    }

    /**
     * Verifica si la coordenada está dentro de los límites del mapa.
     *
     * @param coordenada coordenada a verificar
     * @return true si la coordenada es válida, false en caso contrario
     */
    protected boolean esCoordenadaValida(Coordenada coordenada) {
        return (coordenada != null
                && coordenada.getFila() >= 0
                && coordenada.getColumna() >= 0
                && coordenada.getFila() < LARGO_MAPA
                && coordenada.getColumna() < LARGO_MAPA);
    }

    /**
     * Verifica si la fila y columna están dentro de los límites del mapa.
     *
     * @param fila    índice de la fila
     * @param columna índice de la columna
     * @return true si ambos índices son válidos, false en caso contrario
     */
    protected boolean esCoordenadaValida(int fila, int columna) {
        return (fila >= 0
                && columna >= 0
                && fila < LARGO_MAPA
                && columna < LARGO_MAPA);
    }

    /**
     * Determina si la coordenada corresponde a un borde del mapa.
     *
     * @param coordenada coordenada a verificar
     * @return true si la coordenada está en el borde, false en caso contrario
     */
    protected boolean esBorde(Coordenada coordenada) {

        return (coordenada.getFila() == 0
                || coordenada.getColumna() == 0
                || coordenada.getFila() == LARGO_MAPA - 1
                || coordenada.getColumna() == LARGO_MAPA - 1);
    }
}
