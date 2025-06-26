package solucion.solucionador;

import solucion.posicion.Coordenada;

public abstract sealed class SolucionadorBase permits Solucionador {
    protected static final char AGUA;
    protected static final char DISPARO_FALLIDO;
    protected static final int CORRECCION;

    static {
        AGUA = '0';
        DISPARO_FALLIDO = 'X';
        CORRECCION = 1;
    }

    public abstract void solucionar();

    protected abstract void hundirBarco(Coordenada puntoInicial, char nombreBarco);

    protected abstract char disparar(Coordenada sugerencia);

    protected boolean esDisparoExitoso(char letraDisparo) {
        return (letraDisparo != AGUA && letraDisparo != DISPARO_FALLIDO);
    }

}
