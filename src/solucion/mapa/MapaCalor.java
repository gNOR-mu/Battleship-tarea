package solucion.mapa;

import java.util.Map;
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
        for (int fila = 1; fila < LARGO_MAPA - 1; fila++) {
            coordenada.setFila(fila);
            for (int columna = 0; columna < LARGO_MAPA - 1; columna++) {
                coordenada.setColumna(columna);
                int posibilidad = 0;
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
     * Obtiene una sugerencia de coordenada para disparar, eligiendo aleatoriamente
     * entre las posiciones con mayor probabilidad según el mapa de calor.
     *
     * @return coordenada sugerida para el próximo disparo
     */
    public Coordenada getSugerencia() {
        int mayorPosibilidad = 0;
        int[] sugerencias = new int[(LARGO_MAPA) * (LARGO_MAPA) * 2];
        int sugerenciasCount = 0;
        for (int fila = 1; fila < LARGO_MAPA - 1; fila++) {
            for (int columna = 1; columna < LARGO_MAPA - 1; columna++) {
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
        int idx = ThreadLocalRandom.current().nextInt(sugerenciasCount);
        return new Coordenada(sugerencias[idx * 2], sugerencias[idx * 2 + 1]);
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
