package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Participante implements Serializable {
    private static final long serialVersionUID = 1L;
    String nombre;
    String documento;
    String categoria;
    int edad;
    int numPato;


    public Participante(String nombre, String documento, int edad, int numPato) {
        this.nombre = nombre;
        this.documento = documento;
        this.categoria = asignarCategoria(edad);
        this.edad = edad;
        this.numPato = numPato;
    }

    private String asignarCategoria(int edad){
        if (edad >= 0 && edad <= 10) return "Niño";
        if (edad >= 11 && edad <= 17) return "Joven";
        return "Adulto";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participante)) return false;
        Participante p = (Participante) o;
        return documento.equals(p.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento);
    }


    public String getNombre() {
        return nombre;
    }


    public String getDocumento() {
        return documento;
    }


    public String getCategoria() {
        return categoria;
    }


    public int getEdad() {
        return edad;
    }


    public int getNumPato() {
        return numPato;
    }

}
