package solucion.mapa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import solucion.Barco;
import solucion.Punto;
import solucion.enumerados.Direccion;

public class MapaCalor extends Mapa<Integer> {
    private MapaOceano mapaOceano;

    public MapaCalor(int largoTablero, MapaOceano mapaOceano) {
        super(largoTablero, Integer.class);
        this.mapaOceano = mapaOceano;
    }

    public void actualizarMapa(Map<Character, Barco> barcos) {
        reiniciarMapa();
        int filas = mapa.length;
        int columnas = mapa[0].length;
        int posibilidad;
        Punto punto = new Punto(0,0);
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                posibilidad = 0;
                punto.setFila(fila);
                punto.setColumna(columna);
                for (Barco barco : barcos.values()) {
                    punto.setDireccion(Direccion.ARRIBA);
                    posibilidad += posibilidadDireccion(barco, punto);
                    punto.setDireccion(Direccion.ABAJO);
                    posibilidad += posibilidadDireccion(barco, punto);
                    punto.setDireccion(Direccion.IZQUIERDA);
                    posibilidad += posibilidadDireccion(barco, punto);
                    punto.setDireccion(Direccion.DERECHA);
                    posibilidad += posibilidadDireccion(barco, punto);
                }
                mapa[fila][columna] = posibilidad;
            }
        }
    }

    private int posibilidadDireccion(Barco barco, Punto punto) {
        return (esDireccionValida(barco.getLargo(), punto) && mapaOceano.puedeUbicarse(barco, punto)) ? 1 : 0;
    }

    private void reiniciarMapa() {
        int filas = mapa.length;
        for (int i = 0; i < filas; i++) {
            Arrays.fill(mapa[i], 0);
        }
    }

    public Punto getSugerencia() {
        int mayorPosibilidad = 0;
        ArrayList<Punto> sugerencias = new ArrayList<>();
        int filas = mapa.length;
        int columnas = mapa[0].length;
        for (int i = 0; i < filas; i++) {
            for (int columna = 0; columna < columnas; columna++) {
                int valor = mapa[i][columna];
                if (valor > mayorPosibilidad) {
                    mayorPosibilidad = valor;
                    sugerencias.clear();
                }
                boolean esDesconocida = mapaOceano.esCasillaDesconocida(new Punto(i, columna));
                if (valor == mayorPosibilidad && valor > 0 && esDesconocida) {
                    sugerencias.add(new Punto(i, columna));
                }
            }
        }
        if (sugerencias.isEmpty()) {
            return null;
        }
        return sugerencias.get(ThreadLocalRandom.current().nextInt(sugerencias.size()));
    }

    public boolean chocaConBorde(Punto punto) {
        return switch (punto.getDireccion()) {
            case IZQUIERDA -> punto.getFila() == 0;
            case ARRIBA -> punto.getColumna() == 0;
            case DERECHA -> punto.getFila() == mapa.length - 1;
            case ABAJO -> punto.getColumna() == mapa[0].length - 1;
            default -> false;
        };
    }

    public Punto getSugerenciaFocalizada(Punto puntoOriginal, Punto puntoActualDisparo) {
        boolean direccionIntentadaCambiada = false;
        Punto posiblePuntoSugerido;
        while (true) {
            posiblePuntoSugerido = new Punto(puntoActualDisparo);
            if (posiblePuntoSugerido.getDireccion() == null) {
                posiblePuntoSugerido.setDireccion(Direccion.direccionAleatoria());
            }
            switch (posiblePuntoSugerido.getDireccion()) {
                case IZQUIERDA -> posiblePuntoSugerido.mover(Direccion.IZQUIERDA);
                case DERECHA -> posiblePuntoSugerido.mover(Direccion.DERECHA);
                case ARRIBA -> posiblePuntoSugerido.mover(Direccion.ARRIBA);
                case ABAJO -> posiblePuntoSugerido.mover(Direccion.ABAJO);
            }
            if (puntoActualDisparo.getDireccion() != null) {
                if (direccionIntentadaCambiada) {
                    puntoActualDisparo.setDireccion(null);
                } else if (chocaConBorde(posiblePuntoSugerido) || !esCoordenadaValida(posiblePuntoSugerido)
                        || !mapaOceano.esCasillaDesconocida(posiblePuntoSugerido)) {
                    direccionIntentadaCambiada = true;
                    Direccion nuevaDireccion = posiblePuntoSugerido.getDireccion().direccionOpuesta();
                    puntoActualDisparo = new Punto(puntoOriginal, nuevaDireccion);
                    continue;
                }
            }
            if (esCoordenadaValida(posiblePuntoSugerido) && mapaOceano.esCasillaDesconocida(posiblePuntoSugerido)) {
                return posiblePuntoSugerido;
            }
        }
    }

}
