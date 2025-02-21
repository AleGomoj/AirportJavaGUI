package Interfaces;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import Clases.Persona;

public interface Vuelo {
	String getCodigo();

	String getOrigen();

	String getDestino();

	Date getFechaSalida();

	Date getFechaLlegada();

	Time getDuracion();

	Integer getNumeroPlazas();

	Integer getNumeroPasajeros();

	Boolean isCompleto();

	Set<Persona> getPasajeros();

	void nuevoPasajero(Persona p);

	void eliminaPasajero(Persona p);
}


