package solucion.posicion;

import solucion.enumerados.Direccion;

public final class Coordenada extends Punto {
    private Direccion direccion;

    public Coordenada(int fila, int columna) {
        super(fila, columna);
        this.direccion = null;
    }

    public Coordenada(Coordenada otro) {
        super(otro.fila, otro.columna);
        this.direccion = otro.direccion;
    }

    @Override
    public String toString() {
        return "Coordenada [" + super.toString() + ", direccion=" + direccion + "]";
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void mover() {
        switch (direccion) {
            case ABAJO -> moverFila(1);
            case ARRIBA -> moverFila(-1);
            case DERECHA -> moverColumna(1);
            case IZQUIERDA -> moverColumna(-1);
        }
    }

}
