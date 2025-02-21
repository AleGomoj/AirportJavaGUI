package Clases;

import java.util.Objects;

public class Persona {
    private String nombre;
    private int edad;

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Persona persona = (Persona) obj;
        return Objects.equals(nombre, persona.nombre);
    }

    public int hashCode() {
        return Objects.hash(nombre);
    }

    public String toString() {
        return nombre + " (Edad: " + edad + ")";
    }
}

