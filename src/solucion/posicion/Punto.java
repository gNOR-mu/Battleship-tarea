package solucion.posicion;

public abstract class Punto {
    protected int fila;
    protected int columna;

    /**
     * Crea un nuevo punto con la fila y columna especificadas.
     *
     * @param fila    la fila del punto
     * @param columna la columna del punto
     */
    Punto(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Devuelve una representación en forma de cadena del punto.
     *
     * @return una cadena que representa la fila y columna del punto
     */
    @Override
    public String toString() {
        return "Punto [fila=" + fila + ", columna=" + columna + "]";
    }

    /**
     * Obtiene la fila del punto.
     *
     * @return el valor de la fila
     */
    public int getFila() {
        return fila;
    }

    /**
     * Establece la fila del punto.
     *
     * @param fila el nuevo valor de la fila
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * Obtiene la columna del punto.
     *
     * @return el valor de la columna
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Establece la columna del punto.
     *
     * @param columna el nuevo valor de la columna
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }

    /**
     * Mueve la fila del punto en la cantidad especificada.
     *
     * @param cantidad cantidad de posiciones a mover la fila (puede ser negativa)
     */
    public void moverFila(int cantidad) {
        this.fila += cantidad;
    }

    /**
     * Mueve la columna del punto en la cantidad especificada.
     *
     * @param cantidad cantidad de posiciones a mover la columna (puede ser
     *                 negativa)
     */
    public void moverColumna(int cantidad) {
        this.columna += cantidad;
    }
}
