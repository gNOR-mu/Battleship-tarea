package solucion.mapa;

import java.util.Arrays;
import solucion.Barco;
import solucion.Punto;
import solucion.enumerados.Direccion;

public class MapaOceano extends Mapa<Character> {
    private static final char DESCONOCIDO = ' ';

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
        if (!esDireccionValida(barco.getLargo(), punto)) {
            return false;
        }
        Direccion dir = punto.getDireccion();
        if (dir == Direccion.IZQUIERDA || dir == Direccion.DERECHA) {
            return puedeUbicarseHorizontal(barco, punto);
        } else if (dir == Direccion.ARRIBA || dir == Direccion.ABAJO) {
            return puedeUbicarseVertical(barco, punto);
        }
        return false;
    }

    private boolean puedeUbicarseHorizontal(Barco barco, Punto punto) {
        int cambio = (punto.getDireccion() == Direccion.DERECHA) ? 1 : -1;
        int fila = punto.getFila();
        int nombre = barco.getNombre();
        int largo = barco.getLargo();
        int columnaInicial = punto.getColumna();

        for (int i = 0; i < largo; i++) {
            int nuevaY = columnaInicial + (i * cambio);
            char valor = mapa[fila][nuevaY];
            if (valor != DESCONOCIDO && valor != nombre) {
                return false;
            }
        }
        return true;
    }

    private boolean puedeUbicarseVertical(Barco barco, Punto punto) {
        int cambio = (punto.getDireccion() == Direccion.ABAJO) ? 1 : -1;
        int columna = punto.getColumna();
        char nombre = barco.getNombre();
        int largo = barco.getLargo();
        int filaInicial = punto.getFila();
        for (int i = 0; i < largo; i++) {
            int nuevaX = filaInicial + (i * cambio);
            char valor = mapa[nuevaX][columna];
            if (valor != DESCONOCIDO && valor != nombre) {
                return false;
            }
        }
        return true;
    }
}