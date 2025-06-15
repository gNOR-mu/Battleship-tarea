package solucion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import solucion.enumerados.Direccion;
import problema.Tablero;

public class Solucionador {
    // El tablero del problema no comienza en 0, tiene una corrección de 1
    private final int CORRECCION = 1;
    private Tablero tablero;
    private MapaCalor mapaCalor;
    private MapaOceano mapaOceano;
    private Map<Character, Barco> barcos;

    public Solucionador(Tablero tablero, int largoTablero, Map<Character, Barco> barcos) {
        this.tablero = tablero;
        this.barcos = barcos;
        this.mapaOceano = new MapaOceano(largoTablero);
        this.mapaCalor = new MapaCalor(largoTablero, this.mapaOceano);
    }

    public void solucionar() {
        char disparo;
        Punto sugerencia;
        while (this.tablero.ganar() == 0) {
            this.mapaCalor.actualizarMapa(barcos);
            sugerencia = this.mapaCalor.getSugerencia();
            disparo = disparar(sugerencia);
            // deja de ser azar
            if (esDisparoExitoso(disparo)) {
                hundirBarco(sugerencia, disparo);
            }
        }
    }

    private boolean esDisparoExitoso(char disparo) {
        return (disparo != '0' && disparo != 'X');
    }

    private char disparar(Punto punto) {
        char disparo = this.tablero.disparo(punto.getFila() + this.CORRECCION, punto.getColumna() + this.CORRECCION);
        this.mapaOceano.marcar(punto, disparo);
        if (esDisparoExitoso(disparo)) {
            this.barcos.get(disparo).quitarVida();
        }
        return disparo;
    }

    private void hundirBarco(Punto punto, char nombreBarco) {
        Barco barco = barcos.get(nombreBarco);
        Punto nuevoPunto = new Punto(punto);
        char disparo;
        while (barco.getVida() > 0) {
            nuevoPunto = mapaCalor.getSugerenciaFocalizada(punto, nuevoPunto, barco);
            disparo = disparar(nuevoPunto);
            if (disparo != barco.getNombre()) {
                if (esDisparoExitoso(disparo)) {
                    Punto puntoRecursivo = new Punto(nuevoPunto);
                    puntoRecursivo.setDireccion(null);
                    hundirBarco(puntoRecursivo, disparo);
                }
                // lo dejo al azar si no le he pegado al barco 2 veces
                Direccion direccionOpuesta = (barco.getVida() < barco.getLargo() - 1)
                        ? nuevoPunto.getDireccion().direccionOpuesta()
                        : null;
                nuevoPunto = new Punto(punto);
                nuevoPunto.setDireccion(direccionOpuesta);
            }
        }
        this.barcos.remove(barco.getNombre());
    }

}
