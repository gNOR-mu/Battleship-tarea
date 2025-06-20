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
        Punto punto = new Punto(0, 0);
        for (int fila = 0; fila < filas; fila++) {
            punto.setFila(fila);
            for (int columna = 0; columna < columnas; columna++) {
                punto.setColumna(columna);
                if (!mapaOceano.esCasillaDesconocida(punto)) {
                    continue;
                }
                posibilidad = 0;
                for (Barco barco : barcos.values()) {
                    posibilidad += posibilidadesBarcos(barco, punto);
                }
                mapa[fila][columna] += posibilidad;
            }
        }
    }

    private int posibilidadesBarcos(Barco barco, Punto punto) {
        int posibilidad = 0;
        punto.setDireccion(Direccion.ARRIBA);
        posibilidad += Boolean.compare(mapaOceano.puedeUbicarse(barco, punto), false);
        punto.setDireccion(Direccion.ABAJO);
        posibilidad += Boolean.compare(mapaOceano.puedeUbicarse(barco, punto), false);
        punto.setDireccion(Direccion.IZQUIERDA);
        posibilidad += Boolean.compare(mapaOceano.puedeUbicarse(barco, punto), false);
        punto.setDireccion(Direccion.DERECHA);
        posibilidad += Boolean.compare(mapaOceano.puedeUbicarse(barco, punto), false);

        return posibilidad;
    }

    public void actualizarMapaCercano(Map<Character, Barco> barcos, Punto disparo) {
        int filas = mapa.length;
        int columnas = mapa[0].length;
        int[][] direcciones = {
                { -1, 0 }, // arriba
                { 1, 0 }, // abajo
                { 0, -1 }, // izquierda
                { 0, 1 } // derecha
        };
        mapa[disparo.getFila()][disparo.getColumna()] = 0;
        for (int[] direccion : direcciones) {
            int nuevaFila = disparo.getFila() + direccion[0];
            int nuevaColumna = disparo.getColumna() + direccion[1];
            if (nuevaFila >= 0 && nuevaFila < filas && nuevaColumna >= 0 && nuevaColumna < columnas) {
                Punto punto = new Punto(nuevaFila, nuevaColumna);
                if (!mapaOceano.esCasillaDesconocida(punto)) {
                    continue;
                }
                int posibilidad = 0;
                for (Barco barco : barcos.values()) {
                    posibilidad += posibilidadesBarcos(barco, punto);
                }
                mapa[nuevaFila][nuevaColumna] = posibilidad;
            }
        }
    }

    private void reiniciarMapa() {
        int filas = mapa.length;
        for (int i = 0; i < filas; i++) {
            Arrays.fill(mapa[i], 0);
        }
    }

    public Punto getSugerencia() {
        int mayorPosibilidad = 0;
        int filas = mapa.length;
        int columnas = mapa[0].length;
        int[] sugerencias = new int[filas * columnas * 2];
        int sugerenciasCount = 0;

        for (int i = 0; i < filas; i++) {
            for (int columna = 0; columna < columnas; columna++) {
                int valor = mapa[i][columna];
                if (valor > mayorPosibilidad) {
                    mayorPosibilidad = valor;
                    sugerenciasCount = 0;
                }
                if (valor == mayorPosibilidad && valor > 0) {
                    sugerencias[sugerenciasCount * 2] = i;
                    sugerencias[sugerenciasCount * 2 + 1] = columna;
                    sugerenciasCount++;
                }
            }
        }
        int idx = ThreadLocalRandom.current().nextInt(sugerenciasCount);
        return new Punto(sugerencias[idx * 2], sugerencias[idx * 2 + 1]);
    }

    public Punto getSugerenciaFocalizada(Punto puntoOriginal, Punto puntoActual) {
        if (puntoActual.getDireccion() == null) {
            puntoActual.setDireccion(Direccion.direccionAleatoria());
        }

        for (int i = 0; i < 4; i++) {
            Punto puntoSugerido = new Punto(puntoActual);
            Direccion nuevaDireccion;
            puntoSugerido.mover();
            if (esCoordenadaValida(puntoSugerido) && mapaOceano.esCasillaDesconocida(puntoSugerido)) {
                return puntoSugerido;
            }
            if (i == 0 || i == 2) {
                nuevaDireccion = puntoActual.getDireccion().direccionOpuesta();
            } else {
                nuevaDireccion = puntoActual.getDireccion().direccionSentidoOpuestoAleatorio();
            }
            puntoActual = new Punto(puntoOriginal, nuevaDireccion);
        }
        return null;
    }

}
