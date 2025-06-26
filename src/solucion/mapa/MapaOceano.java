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

    public boolean esCasillaDesconocida(Coordenada coordenada) {
        return mapa[coordenada.getFila()][coordenada.getColumna()] == DESCONOCIDO;
    }

    public boolean esCasillaDesconocida(int fila, int columna) {
        return mapa[fila][columna] == DESCONOCIDO;
    }

    public boolean esPosicionDelBarco(Coordenada coordenada, char nombreBarco) {
        return mapa[coordenada.getFila()][coordenada.getColumna()] == nombreBarco;
    }

    public boolean puedeUbicarse(Barco barco, Coordenada coordenada) {
        Direccion direccion = coordenada.getDireccion();
        boolean direccionHorizontal = direccion == Direccion.DERECHA || direccion == Direccion.IZQUIERDA;
        int valorEje = (direccionHorizontal) ? coordenada.getColumna() : coordenada.getFila();
        int largo = barco.getLargo();
        if (!esDireccionValida(direccion, largo, valorEje)) {
            return false;
        }
        int cambio = (direccion == Direccion.DERECHA || direccion == Direccion.ABAJO) ? 1 : -1;
        char valor;
        for (int i = 0; i < largo; i++) {
            int nuevoValor = valorEje + (i * cambio);
            if (direccionHorizontal) {
                valor = mapa[coordenada.getFila()][nuevoValor];
            } else {
                valor = mapa[nuevoValor][coordenada.getColumna()];
            }
            if (valor != DESCONOCIDO && valor != barco.getNombre()) {
                return false;
            }
        }
        return true;
    }

}