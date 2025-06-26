package solucion.mapa;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import solucion.Barco;
import solucion.enumerados.Direccion;
import solucion.posicion.Coordenada;

public class MapaCalor extends Mapa<Integer> {
    private MapaOceano mapaOceano;

    /*
     * Variar el puntaje de mapa de calor calculado afecta el promedio de disparos,
     * lo puede mejorar o empeorar
     */
    private static final double CAMBIO_PUNTAJE_CALOR;

    static {
        // mejor 1.102 con promedio 37,9439
        CAMBIO_PUNTAJE_CALOR = 1.102;
    }

    public MapaCalor(int largoTablero, MapaOceano mapaOceano) {
        super(largoTablero, Integer.class, 0);
        this.mapaOceano = mapaOceano;
    }

    public void actualizarMapa(Map<Character, Barco> barcos) {
        Coordenada coordenada = new Coordenada(0, 0);
        for (int fila = 0; fila < LARGO_MAPA; fila++) {
            coordenada.setFila(fila);
            for (int columna = 0; columna < LARGO_MAPA; columna++) {
                coordenada.setColumna(columna);
                int posibilidad = 0;
                for (Barco barco : barcos.values()) {
                    posibilidad += posibilidadesBarcos(barco, coordenada);
                }
                marcar(coordenada, posibilidad);
            }
        }
    }

    private int posibilidadesBarcos(Barco barco, Coordenada coordenada) {
        int posibilidad = 0;
        Coordenada coordenadaAuxiliar = new Coordenada(coordenada);
        coordenadaAuxiliar.setDireccion(Direccion.ARRIBA);
        posibilidad += mapaOceano.puedeUbicarse(barco, coordenadaAuxiliar) ? 1 : 0;
        coordenadaAuxiliar.setDireccion(Direccion.ABAJO);
        posibilidad += mapaOceano.puedeUbicarse(barco, coordenadaAuxiliar) ? 1 : 0;
        coordenadaAuxiliar.setDireccion(Direccion.IZQUIERDA);
        posibilidad += mapaOceano.puedeUbicarse(barco, coordenadaAuxiliar) ? 1 : 0;
        coordenadaAuxiliar.setDireccion(Direccion.DERECHA);
        posibilidad += mapaOceano.puedeUbicarse(barco, coordenadaAuxiliar) ? 1 : 0;
        return (int) ((3.5 * posibilidad * CAMBIO_PUNTAJE_CALOR * penalizacionBorde(coordenada)) / 0.1);
    }

    private double penalizacionBorde(Coordenada coordenada) {
        int fila = coordenada.getFila();
        int columna = coordenada.getColumna();
        int minDist = Math.min(Math.min(fila, LARGO_MAPA - 1 - fila), Math.min(columna, LARGO_MAPA - 1 - columna));
        return Math.pow((0.42 + minDist) / LARGO_MAPA, 1.5);
    }

    /**
     * 
     * @param barcos  Los barcos que siguen en juego
     * @param disparo Último disparo
     */
    public void actualizarMapaCercano(Map<Character, Barco> barcos, Coordenada disparo) {
        int[][] direcciones = {
                { -1, 0 }, // arriba
                { 1, 0 }, // abajo
                { 0, -1 }, // izquierda
                { 0, 1 } // derecha
        };
        marcar(disparo, 0);

        int disparoFila = disparo.getFila();
        int disparoCol = disparo.getColumna();
        for (Barco barco : barcos.values()) {
            for (int i = 0; i < 4; i++) {
                int nuevaFila = disparoFila + direcciones[i][0];
                int nuevaColumna = disparoCol + direcciones[i][1];
                if (!esCoordenadaValida(nuevaFila, nuevaColumna)
                        || !mapaOceano.esCasillaDesconocida(nuevaFila, nuevaColumna)) {
                    continue;
                }
                Coordenada coordenada = new Coordenada(nuevaFila, nuevaColumna);
                int posibilidad = 0;
                posibilidad += posibilidadesBarcos(barco, coordenada);
                marcar(coordenada, posibilidad);
            }
        }
    }

    public Coordenada getSugerencia() {
        int mayorPosibilidad = 0;
        int[] sugerencias = new int[LARGO_MAPA * LARGO_MAPA * 2];
        int sugerenciasCount = 0;
        for (int fila = 0; fila < LARGO_MAPA; fila++) {
            for (int columna = 0; columna < LARGO_MAPA; columna++) {
                if (!mapaOceano.esCasillaDesconocida(fila, columna)) {
                    continue;
                }
                int valor = mapa[fila][columna];
                if (valor > mayorPosibilidad) {
                    mayorPosibilidad = valor;
                    sugerenciasCount = 0;
                }
                if (valor == mayorPosibilidad) {
                    sugerencias[sugerenciasCount * 2] = fila;
                    sugerencias[sugerenciasCount * 2 + 1] = columna;
                    sugerenciasCount++;
                }
            }
        }
        if (sugerenciasCount == 0) {
            return null;
        }
        int idx = ThreadLocalRandom.current().nextInt(sugerenciasCount);
        return new Coordenada(sugerencias[idx * 2], sugerencias[idx * 2 + 1]);
    }

    public Coordenada getSugerenciaFocalizada(Coordenada coordenadaOriginal, Coordenada coordenadaActual) {
        if (coordenadaActual.getDireccion() == null) {
            coordenadaActual.setDireccion(Direccion.direccionAleatoria());
        }
        for (int i = 0; i < 4; i++) {
            Coordenada coordenadaSugerido = new Coordenada(coordenadaActual);
            Direccion nuevaDireccion;
            coordenadaSugerido.mover();
            if (esCoordenadaValida(coordenadaSugerido) && mapaOceano.esCasillaDesconocida(coordenadaSugerido)) {
                return coordenadaSugerido;
            }
            if (i % 2 == 0) {
                nuevaDireccion = coordenadaActual.getDireccion().direccionOpuesta();
            } else {
                nuevaDireccion = coordenadaActual.getDireccion().direccionSentidoOpuestoAleatorio();
            }
            coordenadaActual = new Coordenada(coordenadaOriginal);
            coordenadaActual.setDireccion(nuevaDireccion);
        }
        return null;
    }
}
