package solucion.enumerados;

import java.util.concurrent.ThreadLocalRandom;

public enum Direccion {
    ARRIBA, ABAJO, IZQUIERDA, DERECHA;

    private static final Direccion[] VERTICALES = { ARRIBA, ABAJO };
    private static final Direccion[] HORIZONTALES = { IZQUIERDA, DERECHA };

    public static Direccion direccionAleatoria() {
        return values()[ThreadLocalRandom.current().nextInt(values().length)];
    }

    public Direccion direccionOpuesta() {
        return switch (this) {
            case IZQUIERDA -> DERECHA;
            case DERECHA -> IZQUIERDA;
            case ARRIBA -> ABAJO;
            case ABAJO -> ARRIBA;
        };
    }

    public Direccion direccionSentidoOpuestoAleatorio() {
        return switch (this) {
            case IZQUIERDA, DERECHA -> VERTICALES[ThreadLocalRandom.current().nextInt(VERTICALES.length)];
            case ARRIBA, ABAJO -> HORIZONTALES[ThreadLocalRandom.current().nextInt(HORIZONTALES.length)];
        };
    }
}
