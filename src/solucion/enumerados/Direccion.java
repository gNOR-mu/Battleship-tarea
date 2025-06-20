package solucion.enumerados;

import java.util.concurrent.ThreadLocalRandom;

public enum Direccion {
    ARRIBA, ABAJO, IZQUIERDA, DERECHA;

    public static Direccion direccionAleatoria() {
        return values()[ThreadLocalRandom.current().nextInt(values().length)];
    }

    public Direccion direccionOpuesta() {
        if (this == IZQUIERDA) {
            return DERECHA;
        } else if (this == DERECHA) {
            return IZQUIERDA;
        } else if (this == ARRIBA) {
            return ABAJO;
        } else if (this == ABAJO) {
            return ARRIBA;
        }
        return null;
    }

    public Direccion direccionSentidoOpuestoAleatorio() {
        if (this == IZQUIERDA || this == DERECHA) {
            Direccion[] direcciones = { ARRIBA, ABAJO };
            return direcciones[ThreadLocalRandom.current().nextInt(direcciones.length)];

        } else if (this == ARRIBA || this == ABAJO) {
            Direccion[] direcciones = { IZQUIERDA, DERECHA };
            return direcciones[ThreadLocalRandom.current().nextInt(direcciones.length)];
        }
        return null;
    }
}
