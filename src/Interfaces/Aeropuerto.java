package Interfaces;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import Clases.Persona;

public interface Aeropuerto {
	String getNombre();

	String getCiudad();

	SortedSet<Vuelo> getVuelos();

	void nuevoVuelo(Vuelo v);

	void nuevosVuelos(Collection<Vuelo> vuelos);

	boolean contieneVuelo(Vuelo v);

	void eliminaVuelo(Vuelo v);

	Set<Vuelo> seleccionaVuelosFecha(LocalDateTime fechaSalida);

	Vuelo getVueloMasPasajeros() throws NoSuchElementException;

	Persona getPasajeroMayor() throws NoSuchElementException;

	Vuelo getVueloPlazasLibresDestino(String destino) throws NoSuchElementException;

	Integer calculaTotalPasajerosDestino(String destino);

	Double calculaMediaPasajerosPorDia();

	Map<String, Integer> getNumeroPasajerosPorDestino();

	SortedMap<LocalDateTime, List<Vuelo>> getVuelosPorFecha();
}
