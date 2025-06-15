package solucion;

import solucion.enumerados.Direccion;

public class Punto {
    private int fila;
    private int columna;
    private Direccion direccion;

    public Punto(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.direccion = null;
    }

    public Punto(Punto otro, Direccion direccion) {
        this.fila = otro.fila;
        this.columna = otro.columna;
        this.direccion = direccion;
    }

    public Punto(Punto otro) {
        this.fila = otro.fila;
        this.columna = otro.columna;
        this.direccion = otro.direccion;
    }

    @Override
    public boolean equals(Object o) {
        Punto p = (Punto) o;
        return (this.fila == p.fila && this.columna == p.columna);
    }

    @Override
    public String toString() {
        return String.format("Punto(Fila:%d, Columna:%d, Direccion:%s)", this.fila, this.columna, this.direccion);
    }

    public int getFila() {
        return this.fila;
    }

    public int getColumna() {
        return this.columna;
    }

    public Direccion getDireccion() {
        return this.direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void mover(Direccion direccion) {
        switch (direccion) {
            case ABAJO -> this.fila += 1;
            case ARRIBA -> this.fila -= 1;
            case DERECHA -> this.columna += 1;
            case IZQUIERDA -> this.columna -= 1;
        }
    }

}
