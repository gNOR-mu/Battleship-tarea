package solucion;

import java.util.Map;

import solucion.enumerados.Direccion;
import solucion.mapa.MapaCalor;
import solucion.mapa.MapaOceano;
import problema.Tablero;

public class Solucionador {
    // El tablero del problema no comienza en 0, tiene una corrección de 1
    private static final int CORRECCION = 1;
    private final Tablero tablero;
    private final MapaCalor mapaCalor;
    private final MapaOceano mapaOceano;
    private final Map<Character, Barco> barcos;

    public Solucionador(Tablero tablero, int largoTablero, Map<Character, Barco> barcos) {
        this.tablero = tablero;
        this.barcos = barcos;
        this.mapaOceano = new MapaOceano(largoTablero);
        this.mapaCalor = new MapaCalor(largoTablero, this.mapaOceano);
    }

    public void solucionar() {
        while (this.tablero.ganar() == 0) {
            this.mapaCalor.actualizarMapa(barcos);
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

    private char disparar(Punto punto) {
        char disparo = this.tablero.disparo(punto.getFila() + CORRECCION, punto.getColumna() + CORRECCION);
        this.mapaOceano.marcar(punto, disparo);
        Barco barco = barcos.get(disparo);
        if (barco != null) {
            barco.quitarVida();
        }
        return disparo;
    }

    private void hundirBarco(Punto puntoInicialImpacto, char nombreBarco) {
        Barco barco = barcos.get(nombreBarco);
        if (barco == null) {
            return;
        }
        Punto puntoActualDisparo = new Punto(puntoInicialImpacto);
        char resultadoDisparo;
        while (barco.getVida() > 0) {
            puntoActualDisparo = mapaCalor.getSugerenciaFocalizada(puntoInicialImpacto, puntoActualDisparo);
            resultadoDisparo = disparar(puntoActualDisparo);
            if (resultadoDisparo != barco.getNombre()) {
                if (esDisparoExitoso(resultadoDisparo)) {
                    hundirBarco(new Punto(puntoActualDisparo), resultadoDisparo);
                }
                Direccion direccionOpuesta = null;
                if (puntoActualDisparo.getDireccion() != null) {
                    direccionOpuesta = puntoActualDisparo.getDireccion().direccionOpuesta();
                }
                puntoActualDisparo = new Punto(puntoInicialImpacto, direccionOpuesta);
            }
        }
        this.barcos.remove(barco.getNombre());
    }
}