package solucion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import solucion.enumerados.Direccion;

import java.security.SecureRandom;

public class MapaCalor extends Mapa {
    private int[][] mapa;
    private MapaOceano mapaOceano;

    public MapaCalor(int largoTablero, MapaOceano mapaOceano) {
        super(largoTablero);
        this.mapa = new int[largoTablero][largoTablero];
        this.mapaOceano = mapaOceano;
    }

    @Override
    public String toString() {
        return String.format("MapaCalor(%n%s)", Arrays.deepToString(mapa).replace("],", "]\n"));
    }

    public void actualizarMapa(Map<Character, Barco> barcos) {
        reiniciarMapa();
        IntStream.range(0, mapa.length * mapa[0].length).forEach(idx -> {
            int x = idx / mapa.length;
            int y = idx % mapa[0].length;
            int posibilidad = 0;
            for (Barco barco : barcos.values()) {
                posibilidad += posibilidadDireccion(barco, new Punto(x, y, Direccion.IZQUIERDA));
                posibilidad += posibilidadDireccion(barco, new Punto(x, y, Direccion.ARRIBA));
                posibilidad += posibilidadDireccion(barco, new Punto(x, y, Direccion.ABAJO));
                posibilidad += posibilidadDireccion(barco, new Punto(x, y, Direccion.DERECHA));
            }
            mapa[x][y] += posibilidad;
        });
    }

    private int posibilidadDireccion(Barco barco, Punto punto) {
        if (esDireccionValida(barco.getLargo(), punto) && mapaOceano.puedeUbicarse(barco, punto)) {
            return 1;
        }
        return 0;
    }

    private void reiniciarMapa() {
        for (int[] fila : mapa) {
            Arrays.fill(fila, 0);
        }
    }

    private int getMayorPosibilidad() {
        return Arrays.stream(mapa).flatMapToInt(Arrays::stream).max().orElse(0);
    }

    public Punto getSugerencia() {
        int mayorPosibilidad = this.getMayorPosibilidad();
        ArrayList<Punto> puntos = new ArrayList<>();
        if (mayorPosibilidad == 0) {
            return null; // No hay posibilidades
        }

        for (int x = 0; x < mapa.length; x++) {
            for (int y = 0; y < mapa.length; y++) {
                if (mapa[x][y] == mayorPosibilidad && mapaOceano.esCasillaDesconocida(new Punto(x, y))) {
                    puntos.add(new Punto(x, y));
                }
            }
        }
        if (puntos.isEmpty()) {
            return null; // No hay sugerencias disponibles
        }
        return puntos.get(new SecureRandom().nextInt(puntos.size()));
    }

    public boolean chocaConBorde(Punto punto) {
        return switch (punto.getDireccion()) {
            case IZQUIERDA, ARRIBA -> punto.getFila() == 0;
            case DERECHA -> punto.getFila() == mapa.length - 1;
            case ABAJO -> punto.getColumna() == mapa[0].length - 1;
            default -> false;
        };
    }

    public Punto getSugerenciaFocalizada(Punto puntoOriginal, Punto puntoNuevo, Barco barco) {
        Punto temp = null;
        Punto puntoContrario = null;
        boolean direccionCambiada = false;
        boolean posibleUbicar = false;
        do {
            temp = new Punto(puntoNuevo);

            if (temp.getDireccion() == null) {
                temp.setDireccion(Direccion.direccionAleatoria());
            }
            switch (temp.getDireccion()) {
                case IZQUIERDA -> temp.mover(Direccion.IZQUIERDA);
                case DERECHA -> temp.mover(Direccion.DERECHA);
                case ARRIBA -> temp.mover(Direccion.ARRIBA);
                case ABAJO -> temp.mover(Direccion.ABAJO);
            }
            // para optimizar esto tengo que considerar que si direccion: izquierda y
            // no puedo ubicar el barco hacia la derecha deberia ser una posicion invalida
            // ahora bien considerar tambien que puede que no sea el inicio del barco, solo voy a saber si la casilla contraria es 0
            if (puntoNuevo.getDireccion() != null) {
                if (direccionCambiada) {
                    puntoNuevo.setDireccion(null);
                } else if (chocaConBorde(temp) || !esCoordenadaValida(temp) || !mapaOceano.esCasillaDesconocida(temp)) {
                    direccionCambiada = true;
                    Direccion nuevaDireccion = puntoNuevo.getDireccion().direccionOpuesta();
                    puntoNuevo = new Punto(puntoOriginal);
                    puntoNuevo.setDireccion(nuevaDireccion);

                }
            }
            // puedo calcular el punto minimo en donde podria empezar un barco
            // puede que la direccion opuesta sea si mismo
            if (esCoordenadaValida(temp)) {
                puntoContrario = new Punto(puntoOriginal);
                puntoContrario.setDireccion(temp.getDireccion().direccionOpuesta());
                // si nuevopunto = punto significa que estoy en el inicio pero no necesariamente es la primera vez
                // deberia considerar que ya recorrio el otro laddo del baro, entonces no hay necesidad de validar este caso

                if (!(mapaOceano.esCasillaDesconocida(puntoContrario)
                        || mapaOceano.esPosicionDelBarco(puntoContrario, barco.getNombre()))
                        && (posibilidadDireccion(barco, puntoOriginal) == 0)) {
                    // aqui ya se que el punto contrario no es parte del barco
                    posibleUbicar = false;
                } else if (mapaOceano.esPosicionDelBarco(puntoContrario, barco.getNombre())) {
                    posibleUbicar = true;
                }
                // ahora podria ser que empiezo desde la mitad del barco u otro lugar que no sea el borde

                // evaluo si la direccion temporal es posible de ubicar, deberia tomar en cuenta si ya dio vuelta la nava
            }

        } while (!(esCoordenadaValida(temp) && mapaOceano.esCasillaDesconocida(temp) && posibleUbicar));
        return temp;

    }

}
