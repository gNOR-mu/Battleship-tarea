package solucion;

import java.util.Arrays;

import solucion.enumerados.Direccion;

public class MapaOceano extends Mapa {
    private char DESCONOCIDO;
    private char[][] mapa;

    public MapaOceano(int largoTablero) {
        super(largoTablero);
        this.mapa = new char[largoTablero][largoTablero];
        this.DESCONOCIDO = ' ';
        this.inicializarTablero();
    }

    @Override
    public String toString() {
        return String.format("Mapa del océano:%n%s", Arrays.deepToString(mapa).replace("],", "]\n"));
    }

    private void inicializarTablero() {
        for (char[] fila : mapa) {
            Arrays.fill(fila, DESCONOCIDO);
        }
    }

    public boolean esCasillaDesconocida(Punto punto) {
        return this.mapa[punto.getFila()][punto.getColumna()] == this.DESCONOCIDO;
    }

    public boolean esPosicionDelBarco(Punto punto, char nombreBarco) {
        return this.mapa[punto.getFila()][punto.getColumna()] == nombreBarco;
    }

    public void marcar(Punto punto, char simbolo) {
        if (this.esCoordenadaValida(punto)) {
            this.mapa[punto.getFila()][punto.getColumna()] = simbolo;
        }
    }

    public boolean puedeUbicarse(Barco barco, Punto punto) {
        if (!esDireccionValida(barco.getLargo(), punto) ) {
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
        for (int i = 0; i < barco.getLargo(); i++) {
            int nuevaY = punto.getColumna() + (i * cambio);
            if (mapa[punto.getFila()][nuevaY] != DESCONOCIDO && mapa[punto.getFila()][nuevaY] != barco.getNombre()) {
                return false;
            }
        }
        return true;
    }

    private boolean puedeUbicarseVertical(Barco barco, Punto punto) {
        int cambio = (punto.getDireccion() == Direccion.ABAJO) ? 1 : -1;
        for (int i = 0; i < barco.getLargo(); i++) {
            int nuevaX = punto.getFila() + (i * cambio);
            if (mapa[nuevaX][punto.getColumna()] != DESCONOCIDO && mapa[nuevaX][punto.getColumna()] != barco.getNombre()) {
                return false;
            }
        }
        return true;
    }

}
