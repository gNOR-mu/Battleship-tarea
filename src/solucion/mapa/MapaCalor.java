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
                posibilidad = 0;
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
                mapa[fila][columna] += posibilidad;
            }
        }
    }

    private int posibilidadDireccion(Barco barco, Punto punto) {
        return Boolean.compare(mapaOceano.puedeUbicarse(barco, punto), false);
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
