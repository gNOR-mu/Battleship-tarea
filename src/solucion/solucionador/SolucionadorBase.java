package solucion.solucionador;

import solucion.posicion.Coordenada;

public abstract sealed class SolucionadorBase implements Solucion permits Solucionador {
    protected static final char AGUA;
    protected static final char DISPARO_FALLIDO;
    protected static final int CORRECCION;

    static {
        AGUA = '0';
        DISPARO_FALLIDO = 'X';
        CORRECCION = 1;
    }

    protected abstract void hundirBarco(Coordenada coordenadaInicial, char nombreBarco);

    protected abstract char disparar(Coordenada sugerencia);

    protected boolean esDisparoExitoso(char disparo) {
        return (disparo != AGUA && disparo != DISPARO_FALLIDO);
    }

}
