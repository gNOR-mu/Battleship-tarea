package solucion.enumerados;

import java.util.concurrent.ThreadLocalRandom;

public enum Direccion {
    ARRIBA, ABAJO, IZQUIERDA, DERECHA;

    private static final Direccion[] VERTICALES = { ARRIBA, ABAJO };
    private static final Direccion[] HORIZONTALES = { IZQUIERDA, DERECHA };

    /**
     * Devuelve una dirección aleatoria entre ARRIBA, ABAJO, IZQUIERDA y DERECHA.
     *
     * @return una dirección aleatoria
     */
    public static Direccion direccionAleatoria() {
        return values()[ThreadLocalRandom.current().nextInt(values().length)];
    }

    /**
     * Devuelve la dirección opuesta a la actual.
     * Por ejemplo, ARRIBA retorna ABAJO, IZQUIERDA retorna DERECHA, etc.
     *
     * @return la dirección opuesta
     */
    public Direccion direccionOpuesta() {
        return switch (this) {
            case ARRIBA -> ABAJO;
            case ABAJO -> ARRIBA;
            case IZQUIERDA -> DERECHA;
            case DERECHA -> IZQUIERDA;
        };

    }

    /**
     * Devuelve una dirección aleatoria en el sentido opuesto (vertical u
     * horizontal) a la actual.
     * Si la dirección es horizontal, retorna una vertical aleatoria; si es
     * vertical, retorna una horizontal aleatoria.
     *
     * @return una dirección aleatoria en el sentido opuesto
     */
    public Direccion direccionSentidoOpuestoAleatorio() {
        return switch (this) {
            case IZQUIERDA, DERECHA -> VERTICALES[ThreadLocalRandom.current().nextInt(VERTICALES.length)];
            case ARRIBA, ABAJO -> HORIZONTALES[ThreadLocalRandom.current().nextInt(HORIZONTALES.length)];
        };
    }
}
