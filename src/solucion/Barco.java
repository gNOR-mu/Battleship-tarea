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

    @Override
    public String toString() {
        return String.format("Barco(Nombre:%s, Largo:%d, Vida:%d)", nombre, largo, vida);
    }

    public Barco(Barco otro) {
        this.nombre = otro.nombre;
        this.largo = otro.largo;
        this.vida = otro.vida;
    }

    public char getNombre() {
        return this.nombre;
    }

    public int getLargo() {
        return this.largo;
    }

    public int getVida() {
        return this.vida;
    }

    public void quitarVida() {
        if (vida > 0) {
            vida--;
        }
    }

}
