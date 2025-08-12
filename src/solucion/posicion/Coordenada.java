package solucion.posicion;

import solucion.enumerados.Direccion;

public final class Coordenada extends Punto {
    private Direccion direccion;

    /**
     * Crea una nueva coordenada con la fila y columna especificadas.
     *
     * @param fila    la fila de la coordenada
     * @param columna la columna de la coordenada
     */
    public Coordenada(int fila, int columna) {
        super(fila, columna);
        this.direccion = null;
    }

    /**
     * Crea una nueva coordenada copiando otra coordenada.
     *
     * @param otro la coordenada a copiar
     */
    public Coordenada(Coordenada otro) {
        super(otro.fila, otro.columna);
        this.direccion = otro.direccion;
    }

        /**
     * Crea una nueva coordenada copiando otra coordenada.
     *
     * @param otro la coordenada a copiar
     */
    public Coordenada(Coordenada otro, Direccion direccion) {
        super(otro.fila, otro.columna);
        this.direccion = direccion;
    }

    /**
     * Devuelve una representación en forma de cadena de la coordenada, incluyendo
     * la dirección.
     *
     * @return una cadena que representa la coordenada y su dirección
     */
    @Override
    public String toString() {
        return "Coordenada [" + super.toString() + ", direccion=" + direccion + "]";
    }

    /**
     * Obtiene la dirección asociada a esta coordenada.
     *
     * @return la dirección actual, o null si no está definida
     */
    public Direccion getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección para esta coordenada.
     *
     * @param direccion la dirección a establecer
     */
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    /**
     * Mueve la coordenada una posición en la dirección actual.
     * Si la dirección es null, no realiza ningún movimiento.
     */
    public void mover() {
        switch (direccion) {
            case ABAJO -> moverFila(1);
            case ARRIBA -> moverFila(-1);
            case DERECHA -> moverColumna(1);
            case IZQUIERDA -> moverColumna(-1);
        }
    }

}
