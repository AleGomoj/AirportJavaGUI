package Clases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import Interfaces.Aeropuerto;
import Interfaces.Vuelo;

public class AeropuertoImpl implements Aeropuerto {
    private String nombre;
    private String ciudad;
    private SortedSet<Vuelo> vuelos;

    public AeropuertoImpl(String nombre, String ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.vuelos = new TreeSet<>();
    }

    public AeropuertoImpl(String nombre, String ciudad, Collection<Vuelo> vuelos) {
        this(nombre, ciudad);
        this.vuelos.addAll(vuelos);
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public SortedSet<Vuelo> getVuelos() {
        return Collections.unmodifiableSortedSet(vuelos);
    }

    public void nuevoVuelo(Vuelo v) {
        if (v != null) {
            vuelos.add(v);
        }
    }

    public void nuevosVuelos(Collection<Vuelo> vuelos) {
        vuelos.forEach(this::nuevoVuelo);
    }

    public boolean contieneVuelo(Vuelo v) {
        return vuelos.contains(v);
    }

    public void eliminaVuelo(Vuelo v) {
        vuelos.remove(v);
    }

    public Set<Vuelo> seleccionaVuelosFecha(LocalDateTime fechaSalida) {
        Set<Vuelo> vuelosFecha = new HashSet<>();
        for (Vuelo vuelo : vuelos) {
            if (vuelo.getFechaSalida().equals(fechaSalida)) {
                vuelosFecha.add(vuelo);
            }
        }
        return vuelosFecha;
    }

    public Vuelo getVueloMasPasajeros() throws NoSuchElementException {
        return vuelos.stream()
                .max(Comparator.comparing(Vuelo::getNumeroPasajeros))
                .orElseThrow(NoSuchElementException::new);
    }

    public Persona getPasajeroMayor() throws NoSuchElementException {
        return vuelos.stream()
                .flatMap(vuelo -> vuelo.getPasajeros().stream())
                .max(Comparator.comparing(Persona::getEdad))
                .orElseThrow(NoSuchElementException::new);
    }

    public Vuelo getVueloPlazasLibresDestino(String destino) throws NoSuchElementException {
        return vuelos.stream()
                .filter(vuelo -> vuelo.getDestino().equals(destino) && !vuelo.isCompleto())
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public Integer calculaTotalPasajerosDestino(String destino) {
        return vuelos.stream()
                .filter(vuelo -> vuelo.getDestino().equals(destino))
                .mapToInt(Vuelo::getNumeroPasajeros)
                .sum();
    }

    public Double calculaMediaPasajerosPorDia() {
        return vuelos.stream()
                .mapToInt(Vuelo::getNumeroPasajeros)
                .average()
                .orElse(0.0);
    }

    public Map<String, Integer> getNumeroPasajerosPorDestino() {
        Map<String, Integer> pasajerosPorDestino = new HashMap<>();
        for (Vuelo vuelo : vuelos) {
            pasajerosPorDestino.merge(vuelo.getDestino(), vuelo.getNumeroPasajeros(), Integer::sum);
        }
        return pasajerosPorDestino;
    }

    public SortedMap<LocalDateTime, List<Vuelo>> getVuelosPorFecha() {
        SortedMap<LocalDateTime, List<Vuelo>> vuelosPorFecha = new TreeMap<>();
        for (Vuelo vuelo : vuelos) {
            LocalDateTime fechaSalida = vuelo.getFechaSalida().toLocalDate().atStartOfDay();
            vuelosPorFecha.computeIfAbsent(fechaSalida, k -> new ArrayList<>()).add(vuelo);
        }
        return vuelosPorFecha;
    }

    public String toString() {
        return nombre + " (" + ciudad + ")";
    }
}
