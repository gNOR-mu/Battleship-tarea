package solucion;

public class Barco {
    private final char nombre;
    private final int largo;
    private int vida;

    public Barco(char nombre, int largo) {
        this.nombre = nombre;
        this.largo = largo;
        this.vida = largo;
    }

    /** 
     * @return String
     */
    @Override
    public String toString() {
        return String.format("Barco(Nombre:%s, Largo:%d, Vida:%d)", nombre, largo, vida);
    }

    /** 
     * @return char
     */
    public char getNombre() {
        return this.nombre;
    }

    /** 
     * @return int
     */
    public int getLargo() {
        return this.largo;
    }

    /** 
     * @return int
     */
    public int getVida() {
        return this.vida;
    }

    public void quitarVida() {
        vida--;
    }
}
