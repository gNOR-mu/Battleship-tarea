package solucion.posicion;

public abstract class Punto {
    protected int fila;
    protected int columna;

    Punto(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public String toString() {
        return "Punto [fila=" + fila + ", columna=" + columna + "]";
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void moverFila(int cantidad) {
        this.fila += cantidad;
    }

    public void moverColumna(int cantidad) {
        this.columna += cantidad;
    }
}
