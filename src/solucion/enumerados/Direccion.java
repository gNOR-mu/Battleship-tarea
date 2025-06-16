package solucion.enumerados;

import java.util.concurrent.ThreadLocalRandom;

public enum Direccion {
    ARRIBA, ABAJO, IZQUIERDA, DERECHA;

    public static Direccion direccionAleatoria() {
        return values()[ThreadLocalRandom.current().nextInt(values().length)];
    }
    public Direccion direccionOpuesta() {
        return switch (this) {
            case IZQUIERDA -> Direccion.DERECHA;
            case DERECHA -> Direccion.IZQUIERDA;
            case ARRIBA -> Direccion.ABAJO;
            case ABAJO -> Direccion.ARRIBA;
            default -> null;
        };
    }
}
