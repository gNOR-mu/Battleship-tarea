package solucion.mapa;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import solucion.Barco;
import solucion.Punto;
import solucion.enumerados.Direccion;

public class MapaCalor extends Mapa<Integer> {
    private MapaOceano mapaOceano;
    private static final double CAMBIO_PUNTAJE; // sirve como ajuste de puntaje, afecta el promedio
    private static final double PENALIZACION_BORDE;

    static {
        CAMBIO_PUNTAJE = 1.25;
        PENALIZACION_BORDE = 1.189;
    }

    public MapaCalor(int largoTablero, MapaOceano mapaOceano) {
        super(largoTablero, Integer.class, 0);
        this.mapaOceano = mapaOceano;
    }

    public void actualizarMapa(Map<Character, Barco> barcos) {
        Punto punto = new Punto(0, 0);
        for (int fila = 0; fila < LARGO_MAPA; fila++) {
            punto.setFila(fila);
            for (int columna = 0; columna < LARGO_MAPA; columna++) {
                punto.setColumna(columna);
                int posibilidad = 0;
                for (Barco barco : barcos.values()) {
                    posibilidad += posibilidadesBarcos(barco, punto); // como debe ser promedia 41.6
                    // posibilidad = posibilidadesBarcos(barco, punto); //curioso que promedia 40.3
                }
                // mapa[fila][columna] += posibilidad;
                marcar(punto, posibilidad);// llegando a las conclusiones correctas por las razones equivocadas
            }
        }
    }

    private int posibilidadesBarcos(Barco barco, Punto punto) {
        int posibilidad = 0;
        Punto puntoAuxiliar = new Punto(punto, Direccion.ARRIBA);
        posibilidad += mapaOceano.puedeUbicarse(barco, puntoAuxiliar) ? 1 : 0;
        puntoAuxiliar.setDireccion(Direccion.ABAJO);
        posibilidad += mapaOceano.puedeUbicarse(barco, puntoAuxiliar) ? 1 : 0;
        puntoAuxiliar.setDireccion(Direccion.IZQUIERDA);
        posibilidad += mapaOceano.puedeUbicarse(barco, puntoAuxiliar) ? 1 : 0;
        puntoAuxiliar.setDireccion(Direccion.DERECHA);
        posibilidad += mapaOceano.puedeUbicarse(barco, puntoAuxiliar) ? 1 : 0;
        return (int) (posibilidad * CAMBIO_PUNTAJE * penalizacionBorde(punto));
    }

    private double penalizacionBorde(Punto punto) {
        int fila = punto.getFila();
        int columna = punto.getColumna();
        int distArriba = fila;
        int distAbajo = LARGO_MAPA - 1 - fila;
        int distIzquierda = columna;
        int distDerecha = LARGO_MAPA - 1 - columna;
        int minDist = Math.min(Math.min(distArriba, distAbajo), Math.min(distIzquierda, distDerecha));
        // Penalización suave: cuanto más lejos del borde, mayor el factor (puedes ajustar el +1 o el divisor)
        return PENALIZACION_BORDE + (minDist / (double) LARGO_MAPA);
    }

    /**
     * reduce aprox 1.5 disparos en comparación a actualizar el mapa a cada rato
     * los disparos empeoran al recorrer la vida de cada barco -> necesita un cambio de estrategia para aplicarla
     */
    public void actualizarMapaCercano(Map<Character, Barco> barcos, Punto disparo) {
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
                if (!esCoordenadaValida(nuevaFila, nuevaColumna) || !mapaOceano.esCasillaDesconocida(nuevaFila, nuevaColumna)) {
                    continue;
                }
                Punto punto = new Punto(nuevaFila, nuevaColumna);
                int posibilidad = 0;
                posibilidad += posibilidadesBarcos(barco, punto);
                marcar(punto, posibilidad);
            }
        }
    }

    public Punto getSugerencia() {
        int mayorPosibilidad = 0; // siempre tiene almenos 1 posibilidad, así ignora los lugares 0
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
            return null; // o lanza una excepción, o maneja el caso como prefieras
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
            if (i % 2 == 0) {
                nuevaDireccion = puntoActual.getDireccion().direccionOpuesta();
            } else {
                nuevaDireccion = puntoActual.getDireccion().direccionSentidoOpuestoAleatorio();
            }
            puntoActual = new Punto(puntoOriginal, nuevaDireccion);
        }
        return null;
    }

}
