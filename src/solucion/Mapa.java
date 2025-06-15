package solucion;

import solucion.enumerados.Direccion;

public class Mapa {
    private int largoTablero;

    public Mapa(int largoTablero) {
        if (largoTablero <= 0) {
            throw new IllegalArgumentException("El largo del tablero debe ser mayor que cero.");
        }
        this.largoTablero = largoTablero;
    }

    protected boolean esDireccionValida(int largoBarco, Punto punto) {
        int valorEje = (punto.getDireccion() == Direccion.ARRIBA || punto.getDireccion() == Direccion.ABAJO) ? punto.getFila() : punto.getColumna();
        return switch (punto.getDireccion()) {
            case ARRIBA, IZQUIERDA -> valorEje - (largoBarco - 1) >= 0;
            case ABAJO, DERECHA -> valorEje + (largoBarco - 1) < largoTablero;
            default -> false;
        };
    }

    protected boolean esCoordenadaValida(Punto punto) {
        if (punto == null) {
            return false;
        }
        return (punto.getFila() >= 0 && punto.getFila() < largoTablero &&
                punto.getColumna() >= 0 && punto.getColumna() < largoTablero);
    }
}
