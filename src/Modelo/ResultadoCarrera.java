package Modelo;

import java.io.Serializable;

public class ResultadoCarrera implements Serializable {
    private static final long serialVersionUID = 1L;
    private Carrera carrera;
    private Participante participante;
    private int posicion;
    private long tiempo;

    public long getTiempo() {
        return tiempo;
    }


    public ResultadoCarrera(Carrera carrera, Participante participante, int posicion, long tiempo) {
        this.carrera = carrera;
        this.participante = participante;
        this.posicion = posicion;
        this.tiempo = tiempo;

    }

    public Carrera getCarrera() {
        return carrera;
    }

    public Participante getParticipante() {
        return participante;
    }

    public int getPosicion() {
        return posicion;
    }
}
