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

    /**
     * Ejecuta el proceso de resolución del tablero de batalla naval.
     * Debe ser implementado por las subclases.
     */
    public abstract void solucionar();

    /**
     * Hundir un barco a partir de una coordenada inicial y el nombre del barco.
     * Debe ser implementado por las subclases.
     *
     * @param puntoInicial coordenada inicial donde se detectó el barco
     * @param nombreBarco  carácter identificador del barco
     */
    protected abstract void hundirBarco(Coordenada puntoInicial, char nombreBarco);

    /**
     * Realiza un disparo en la coordenada sugerida y devuelve el resultado.
     * Debe ser implementado por las subclases.
     *
     * @param sugerencia coordenada donde se realiza el disparo
     * @return carácter que indica el resultado del disparo (agua, fallido o nombre
     *         de barco)
     */
    protected abstract char disparar(Coordenada sugerencia);

    /**
     * Determina si el resultado de un disparo corresponde a un impacto exitoso en
     * un barco.
     *
     * @param letraDisparo carácter resultado del disparo
     * @return true si el disparo fue exitoso (impactó un barco), false si fue agua
     *         o fallido
     */
    protected boolean esDisparoExitoso(char letraDisparo) {
        return (letraDisparo != AGUA && letraDisparo != DISPARO_FALLIDO);
    }

}
