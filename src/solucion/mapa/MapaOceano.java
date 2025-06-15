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

    @Override
    public String toString() {
        return String.format("Mapa del océano:%n%s", Arrays.deepToString(mapa).replace("],", "]\n"));
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
        return switch (punto.getDireccion()) {
            case IZQUIERDA, DERECHA -> puedeUbicarseHorizontal(barco, punto);
            case ARRIBA, ABAJO -> puedeUbicarseVertical(barco, punto);
            default -> false;
        };
    }

    private boolean puedeUbicarseHorizontal(Barco barco, Punto punto) {
        int cambio = (punto.getDireccion() == Direccion.DERECHA) ? 1 : -1;
        int fila = punto.getFila();
        int nombre = barco.getNombre();
        for (int i = 0; i < barco.getLargo(); i++) {
            int nuevaY = punto.getColumna() + (i * cambio);
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
        int nombre = barco.getNombre();
        for (int i = 0; i < barco.getLargo(); i++) {
            int nuevaX = punto.getFila() + (i * cambio);
            char valor = mapa[nuevaX][columna];
            if (valor != DESCONOCIDO && valor != nombre) {
                return false;
            }
        }
        return true;
    }
}