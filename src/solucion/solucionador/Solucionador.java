package solucion.solucionador;

import java.util.Map;

import problema.Tablero;
import solucion.Barco;
import solucion.enumerados.Direccion;
import solucion.mapa.MapaCalor;
import solucion.mapa.MapaOceano;
import solucion.posicion.Coordenada;

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

    /**
     * Ejecuta el solucionador del tablero, no termina la ejecución mientras no se
     * hayan hundido todos los barcos.
     */
    @Override
    public void solucionar() {
        this.mapaCalor.actualizarMapa(barcos);
        while (this.tablero.ganar() == 0) {
            Coordenada sugerencia = this.mapaCalor.getSugerencia();
            char disparo = disparar(sugerencia);
            if (esDisparoExitoso(disparo)) {
                hundirBarco(sugerencia, disparo);
            }
        }
    }

    /**
     * Realiza el disparo hacia el tablero. 
     * <p> Actualiza elmapa de calor y crea la marca en el mapa oceano.
     * @param sugerencia
     * @return char
     */
    @Override
    protected char disparar(Coordenada sugerencia) {
        char disparo = this.tablero.disparo(sugerencia.getFila() + CORRECCION, sugerencia.getColumna() + CORRECCION);
        mapaCalor.actualizarMapaCercano(barcos, sugerencia);
        this.mapaOceano.marcar(sugerencia, disparo);
        return disparo;
    }

    /**
     * Hunde el barco encontrado, si en el proceso encuentra uno nuevo lo hunde de forma recursiva
     * @param coordenadaInicial Coordenada en donde se encontró el barco
     * @param nombreBarco Nombre del barco
     */
    @Override
    protected void hundirBarco(Coordenada coordenadaInicial, char nombreBarco) {
        if (!barcos.containsKey(nombreBarco)) {
            return;
        }
        Barco barco = barcos.get(nombreBarco);
        Coordenada coordenadaActual = new Coordenada(coordenadaInicial);
        char resultadoDisparo;
        barco.quitarVida();
        while (barco.getVida() > 0) {
            coordenadaActual = mapaCalor.getSugerenciaFocalizada(coordenadaInicial, coordenadaActual);
            resultadoDisparo = disparar(coordenadaActual);
            if (resultadoDisparo != nombreBarco) {
                if (esDisparoExitoso(resultadoDisparo)) {
                    hundirBarco(new Coordenada(coordenadaActual), resultadoDisparo);
                }
                Direccion direccionOpuesta = coordenadaActual.getDireccion().direccionOpuesta();
                coordenadaInicial.setDireccion(direccionOpuesta);
                coordenadaActual = new Coordenada(coordenadaInicial);
            } else {
                barco.quitarVida();
            }
        }
        barcos.remove(nombreBarco);
    }
}