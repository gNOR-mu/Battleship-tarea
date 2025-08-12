package solucion.mapa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import solucion.Barco;
import solucion.enumerados.Direccion;
import solucion.posicion.Coordenada;

public class MapaCalor extends Mapa<Integer> {
    private MapaOceano mapaOceano;

    public MapaCalor(int largoTablero, MapaOceano mapaOceano) {
        super(largoTablero, Integer.class, 0);
        this.mapaOceano = mapaOceano;
    }

    /**
     * Actualiza el mapa de calor sumando las posibilidades de ubicación de cada
     * barco en cada celda del tablero, excluyendo los bordes.
     *
     * @param barcos mapa de barcos activos en el juego
     */
    public void actualizarMapa(Map<Character, Barco> barcos) {
        Coordenada coordenada = new Coordenada(0, 0);
        for (int fila = 1; fila < LARGO_MAPA; fila++) {
            coordenada.setFila(fila);
            for (int columna = 0; columna < LARGO_MAPA; columna++) {
                int posibilidad = 0;
                coordenada.setColumna(columna);
                for (Barco barco : barcos.values()) {
                    posibilidad += posibilidadesBarcos(barco, coordenada);
                }
                marcar(coordenada, posibilidad);
            }
        }
    }

    /**
     * Calcula la cantidad de formas en que un barco puede ubicarse en una
     * coordenada dada,
     * <p>
     * El tablero del problema propuesto no ubica completamente un barco en la
     * primera ni última fila o columna, puede pasar que un extremo del barco esté
     * ubicado en un borde, sin embargo las posibilidades de ocurrencia son menores
     * a las de que sea ubicado en otra parte, ergo es viable dejar los bordes
     * como 0, para reducir el número de intentos.
     * 
     * @param barco      barco a iterar para calcular su coordenada de calor
     * @param coordenada coordenada sobre la que se itera
     * @return número de posibilidades para ubicar el barco en esa coordenada
     * 
     */
    private int posibilidadesBarcos(Barco barco, Coordenada coordenada) {
        //comprobar si es borde reduce aprox 3 disparos debido a la posicion del problema
        if (esBorde(coordenada)) {
            return 0;
        }
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
        return posibilidad;
    }

    /**
     * Actualiza únicamente una casillas adyacentes a la coordenada de disparo,
     * sumando las posibilidades de ubicación de los barcos en las posiciones de
     * arriba,abajo, izquierda y derecha.
     *
     * @param barcos  barcos que siguen en juego
     * @param disparo última coordenada de disparo
     */
    public void actualizarMapaCercano(Map<Character, Barco> barcos, Coordenada disparo) {
        int[][] direcciones = {
                { -1, 0 }, // arriba
                { 1, 0 }, // abajo
                { 0, -1 }, // izquierda
                { 0, 1 } // derecha
        };
        for (Barco barco : barcos.values()) {
            for (int i = 0; i < 4; i++) {
                int nuevaFila = disparo.getFila() + direcciones[i][0];
                int nuevaColumna = disparo.getColumna() + direcciones[i][1];
                if (!esCoordenadaValida(nuevaFila, nuevaColumna)
                        || !mapaOceano.esCasillaDesconocida(nuevaFila, nuevaColumna)) {
                    continue;
                }
                Coordenada coordenada = new Coordenada(nuevaFila, nuevaColumna);
                int posibilidad = posibilidadesBarcos(barco, coordenada);
                marcar(coordenada, posibilidad);
            }
        }
    }

    /**
     * Obtiene una única sugerencia de coordenada para disparar, eligiendo el primer
     * valor más probable.
     *
     * @return coordenada sugerida para el próximo disparo
     */
    public Coordenada getSugerencia() {
        // al momento de realizar el disparo se reemplaza el impacto por -1
        int[] coordenada = new int[2];
        int mayorPosibilidad = 0;
        for (int fila = 0; fila < LARGO_MAPA; fila++) {
            for (int columna = 0; columna < LARGO_MAPA; columna++) {
                if (mapa[fila][columna] > mayorPosibilidad) {
                    mayorPosibilidad = mapa[fila][columna];
                    coordenada[0] = fila;
                    coordenada[1] = columna;
                }
            }
        }
        return new Coordenada(coordenada[0], coordenada[1]);
    }

    /**
     * Obtiene una sugerencia de coordenada focalizada, continuando en la dirección
     * actual o alternando direcciones si no es posible, para hundir un barco
     * parcialmente descubierto.
     *
     * @param coordenadaOriginal coordenada inicial del disparo exitoso
     * @param coordenadaActual   coordenada actual desde la que se busca continuar
     * @return coordenada sugerida para el siguiente disparo focalizado, o null si
     *         no hay opciones válidas
     */
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
            if ((i & 1) == 0) {
                nuevaDireccion = coordenadaActual.getDireccion().getDireccionOpuesta();
            } else {
                nuevaDireccion = coordenadaActual.getDireccion().direccionSentidoOpuestoAleatorio();
            }
            coordenadaActual = new Coordenada(coordenadaOriginal, nuevaDireccion);
        }
        return null;
    }
}
