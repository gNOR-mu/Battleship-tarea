package solucion;

import java.util.Map;

import solucion.enumerados.Direccion;
import solucion.mapa.MapaCalor;
import solucion.mapa.MapaOceano;
import problema.Tablero;

public class Solucionador {
    // El tablero del problema no comienza en 0, tiene una corrección de 1
    private static final int CORRECCION;
    private final Tablero tablero;
    private final MapaCalor mapaCalor;
    private final MapaOceano mapaOceano;
    private final Map<Character, Barco> barcos;

    static {
        CORRECCION = 1;
    }

    public Solucionador(Tablero tablero, int largoTablero, Map<Character, Barco> barcos) {
        this.tablero = tablero;
        this.barcos = barcos;
        this.mapaOceano = new MapaOceano(largoTablero);
        this.mapaCalor = new MapaCalor(largoTablero, this.mapaOceano);
    }

    public void solucionar() {
        // calculo 1 vez y luego itero sobre los disparos
        this.mapaCalor.actualizarMapa(barcos);
        while (this.tablero.ganar() == 0) {
            Punto sugerencia = this.mapaCalor.getSugerencia();
            char disparo = disparar(sugerencia);
            if (esDisparoExitoso(disparo)) {
                hundirBarco(sugerencia, disparo);
            }
        }
    }

    private boolean esDisparoExitoso(char disparo) {
        return (disparo != '0' && disparo != 'X');
    }

    private char disparar(Punto sugerencia) {
        char disparo = this.tablero.disparo(sugerencia.getFila() + CORRECCION, sugerencia.getColumna() + CORRECCION);
        this.mapaOceano.marcar(sugerencia, disparo);
        Barco barco = barcos.get(disparo);
        if (barco != null) {
            barco.quitarVida();
            if (barco.getVida() == 0) {
                this.barcos.remove(disparo);
            }
        }
        mapaCalor.actualizarMapaCercano(barcos, sugerencia);
        return disparo;
    }

    private void hundirBarco(Punto puntoInicial, char nombreBarco) {
        Barco barco = barcos.get(nombreBarco);
        Punto puntoActualDisparo = new Punto(puntoInicial);
        char resultadoDisparo;
        while (barco.getVida() > 0) {
            puntoActualDisparo = mapaCalor.getSugerenciaFocalizada(puntoInicial, puntoActualDisparo);
            resultadoDisparo = disparar(puntoActualDisparo);
            if (resultadoDisparo != barco.getNombre()) {
                if (esDisparoExitoso(resultadoDisparo)) {
                    hundirBarco(new Punto(puntoActualDisparo), resultadoDisparo);
                }
                Direccion direccionOpuesta = null;
                if (puntoActualDisparo.getDireccion() != null) {
                    direccionOpuesta = puntoActualDisparo.getDireccion().direccionOpuesta();
                }
                puntoActualDisparo = new Punto(puntoInicial, direccionOpuesta);
            }
        }
    }
}