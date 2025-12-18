package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrera implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String fecha;
    private String categoria;

    //Participantes propios de la carrera
    private List<Participante> participantes;

    public Carrera(String nombre, String fecha, String categoria) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.categoria = categoria;
        this.participantes = new ArrayList<>();
    }


    public List<Participante> getParticipantes() {
        return participantes;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }


    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }

    @Override
    public String toString() {
        return nombre + " (" + categoria + ")";
    }
}
