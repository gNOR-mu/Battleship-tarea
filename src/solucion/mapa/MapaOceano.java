package solucion.mapa;

import java.util.Arrays;
import solucion.Barco;
import solucion.Punto;
import solucion.enumerados.Direccion;

public class MapaOceano extends Mapa<Character> {
    private static final char DESCONOCIDO;

    static {
        DESCONOCIDO = ' ';
    }

    public MapaOceano(int largoTablero) {
        super(largoTablero, Character.class);
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (Character[] fila : mapa) {
            Arrays.fill(fila, DESCONOCIDO);
        }
    }

    public boolean esCasillaDesconocida(Punto punto) {
        return mapa[punto.getFila()][punto.getColumna()] == DESCONOCIDO;
    }

    public boolean esPosicionDelBarco(Punto punto, char nombreBarco) {
        return mapa[punto.getFila()][punto.getColumna()] == nombreBarco;
    }

    public void marcar(Punto punto, char simbolo) {
        if (esCoordenadaValida(punto)) {
            mapa[punto.getFila()][punto.getColumna()] = simbolo;
        }
    }

    public boolean puedeUbicarse(Barco barco, Punto punto) {
        Direccion direccion = punto.getDireccion();
        int valorEje = (punto.getDireccion() == Direccion.IZQUIERDA || punto.getDireccion() == Direccion.DERECHA)
                ? punto.getColumna()
                : punto.getFila();
        int largo = barco.getLargo();
        if (!esDireccionValida(direccion, largo, valorEje)) {
            return false;
        }
        int cambio = (direccion == Direccion.DERECHA || direccion == Direccion.ABAJO) ? 1 : -1;
        char valor;
        for (int i = 0; i < largo; i++) {
            int nuevoValor = valorEje + (i * cambio);
            if (direccion == Direccion.DERECHA || direccion == Direccion.IZQUIERDA) {
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