package solucion.solucionador;

import java.util.Map;

import solucion.Barco;
import solucion.enumerados.Direccion;
import solucion.mapa.MapaCalor;
import solucion.mapa.MapaOceano;
import solucion.posicion.Coordenada;
import problema.Tablero;

public final class Solucionador extends SolucionadorBase {
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
        // calculo 1 vez y luego itero sobre los disparos
        this.mapaCalor.actualizarMapa(barcos);
        while (this.tablero.ganar() == 0) {
            Coordenada sugerencia = this.mapaCalor.getSugerencia();
            char disparo = disparar(sugerencia);
            if (esDisparoExitoso(disparo)) {
                hundirBarco(sugerencia, disparo);
            }
        }
    }

    protected char disparar(Coordenada sugerencia) {
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

    protected void hundirBarco(Coordenada coordenadaInicial, char nombreBarco) {
        Barco barco = barcos.get(nombreBarco);
        Coordenada coordenadaActual = new Coordenada(coordenadaInicial);
        char resultadoDisparo;
        while (barco.getVida() > 0) {
            coordenadaActual = mapaCalor.getSugerenciaFocalizada(coordenadaInicial, coordenadaActual);
            resultadoDisparo = disparar(coordenadaActual);
            if (resultadoDisparo != barco.getNombre()) {
                if (esDisparoExitoso(resultadoDisparo)) {
                    hundirBarco(new Coordenada(coordenadaActual), resultadoDisparo);
                }
                Direccion direccionOpuesta = null;
                if (coordenadaActual.getDireccion() != null) {
                    direccionOpuesta = coordenadaActual.getDireccion().direccionOpuesta();
                }
                coordenadaActual = new Coordenada(coordenadaInicial);
                coordenadaActual.setDireccion(direccionOpuesta);
            }
        }
    }
}