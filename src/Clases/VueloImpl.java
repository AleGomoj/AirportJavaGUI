package Clases;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import Interfaces.Vuelo;

public class VueloImpl implements Vuelo, Comparable<Vuelo> {
    private String id;
    private String origen;
    private String destino;
    private Date fechaSalida;
    private Date fechaLlegada;
    private Time duracion;
    private Integer numeroPlazas;
    private Integer numeroPasajeros;
    private Boolean completo;
    private Set<Persona> pasajeros;

    public VueloImpl(String codigo, String origen, String destino, Date fechaSalida,
                     Date fechaLlegada, Time duracion, Integer numeroPlazas) {
        this.id = codigo;
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;
        this.duracion = duracion;
        this.numeroPlazas = numeroPlazas;
        this.numeroPasajeros = 0;
        this.completo = false;
        this.pasajeros = new HashSet<>();
    }

    public VueloImpl(String codigo, String origen, String destino, Date fechaSalida,
                     Date fechaLlegada, Time duracion, Integer numeroPlazas,
                     Set<Persona> pasajeros) {
        this(codigo, origen, destino, fechaSalida, fechaLlegada, duracion, numeroPlazas);
        this.pasajeros.addAll(pasajeros);
        this.numeroPasajeros = pasajeros.size();
        actualizarCompleto();
    }

    public String getCodigo() {
        return id;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public Time getDuracion() {
    	 return duracion;
    }
  
    public Integer getNumeroPlazas() {
        return numeroPlazas;
    }

    public Integer getNumeroPasajeros() {
        return numeroPasajeros;
    }

    public Boolean isCompleto() {
        return completo;
    }

    public Set<Persona> getPasajeros() {
        return Collections.unmodifiableSet(pasajeros);
    }

    public void nuevoPasajero(Persona p) {
        if (!completo && p != null) {
            pasajeros.add(p);
            numeroPasajeros++;
            actualizarCompleto();
        } else {
            System.out.println("El vuelo está completo o la persona es nula.");
        }
    }

    public void eliminaPasajero(Persona p) {
        pasajeros.remove(p);
        numeroPasajeros--;
        actualizarCompleto();
    }

    private void actualizarCompleto() {
        completo = numeroPasajeros.equals(numeroPlazas);
    }

    public String toString() {
        return "(" + id + ") " + origen + " - " + destino + " " + fechaSalida;
    }
    public int compareTo(Vuelo otroVuelo) {
        // Comparar por fecha de salida
        return this.getFechaSalida().compareTo(otroVuelo.getFechaSalida());
    }
}