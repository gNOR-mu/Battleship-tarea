package solucion.mapa;

import solucion.Barco;
import solucion.Punto;
import solucion.enumerados.Direccion;

public class MapaOceano extends Mapa<Character> {
    private static final char DESCONOCIDO;

    static {
        DESCONOCIDO = ' ';
    }

    public MapaOceano(int largoTablero) {
        super(largoTablero, Character.class, DESCONOCIDO);
    }

    public boolean esCasillaDesconocida(Punto punto) {
        return mapa[punto.getFila()][punto.getColumna()] == DESCONOCIDO;
    }
    public boolean esCasillaDesconocida(int fila, int columna) {
        return mapa[fila][columna] == DESCONOCIDO;
    }

    public boolean esPosicionDelBarco(Punto punto, char nombreBarco) {
        return mapa[punto.getFila()][punto.getColumna()] == nombreBarco;
    }

    public boolean puedeUbicarse(Barco barco, Punto punto) {
        Direccion direccion = punto.getDireccion();
        boolean direccionHorizontal = direccion == Direccion.DERECHA || direccion == Direccion.IZQUIERDA;
        int valorEje = (direccionHorizontal) ? punto.getColumna() : punto.getFila();
        int largo = barco.getLargo();
        if (!esDireccionValida(direccion, largo, valorEje)) {
            return false;
        }
        int cambio = (direccion == Direccion.DERECHA || direccion == Direccion.ABAJO) ? 1 : -1;
        char valor;
        for (int i = 0; i < largo; i++) {
            int nuevoValor = valorEje+ (i * cambio);
            if (direccionHorizontal) {
                valor = mapa[punto.getFila()][nuevoValor];
            } else {
                valor = mapa[nuevoValor][punto.getColumna()];
            }
            if (valor != DESCONOCIDO && valor != barco.getNombre()) {
                return false;
            }
        }
        return true;
    }

}