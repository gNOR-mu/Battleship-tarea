package solucion.mapa;

import solucion.Barco;
import solucion.enumerados.Direccion;
import solucion.posicion.Coordenada;

public class MapaOceano extends Mapa<Character> {
    private static final char DESCONOCIDO;

    static {
        DESCONOCIDO = ' ';
    }

    public MapaOceano(int largoTablero) {
        super(largoTablero, Character.class, DESCONOCIDO);
    }

    /**
     * Verifica si la casilla indicada por la coordenada está en estado desconocido.
     *
     * @param coordenada coordenada a verificar
     * @return true si la casilla está desconocida, false en caso contrario
     */
    public boolean esCasillaDesconocida(Coordenada coordenada) {
        return mapa[coordenada.getFila()][coordenada.getColumna()] == DESCONOCIDO;
    }

    /**
     * Verifica si la casilla indicada por fila y columna está en estado
     * desconocido.
     *
     * @param fila    índice de la fila
     * @param columna índice de la columna
     * @return true si la casilla está desconocida, false en caso contrario
     */
    public boolean esCasillaDesconocida(int fila, int columna) {
        return mapa[fila][columna] == DESCONOCIDO;
    }

    /**
     * Verifica si la casilla indicada por la coordenada contiene el nombre del
     * barco dado.
     *
     * @param coordenada  coordenada a verificar
     * @param nombreBarco carácter identificador del barco
     * @return true si la casilla contiene el barco, false en caso contrario
     */
    public boolean esPosicionDelBarco(Coordenada coordenada, char nombreBarco) {
        return mapa[coordenada.getFila()][coordenada.getColumna()] == nombreBarco;
    }

    /**
     * Determina si un barco puede ubicarse en la posición y dirección indicadas por
     * la coordenada.
     *
     * @param barco      barco a ubicar
     * @param coordenada coordenada y dirección de inicio
     * @return true si el barco puede ubicarse, false en caso contrario
     */
    public boolean puedeUbicarse(Barco barco, Coordenada coordenada) {
        Direccion direccion = coordenada.getDireccion();
        boolean direccionHorizontal = direccion == Direccion.DERECHA || direccion == Direccion.IZQUIERDA;
        int valorEje = (direccionHorizontal) ? coordenada.getColumna() : coordenada.getFila();
        int largo = barco.getLargo();
        if (!esDireccionValida(direccion, largo, valorEje)) {
            return false;
        }
        int cambio = (direccion == Direccion.DERECHA || direccion == Direccion.ABAJO) ? 1 : -1;
        for (int i = 0; i < largo; i++) {
            char valor;
            int nuevoValor = valorEje + (i * cambio);
            if (direccionHorizontal) {
                valor = mapa[coordenada.getFila()][nuevoValor];
            } else {
                valor = mapa[nuevoValor][coordenada.getColumna()];
            }
            if (valor != DESCONOCIDO) {
                return false;
            }
        }
        return true;
    }

}