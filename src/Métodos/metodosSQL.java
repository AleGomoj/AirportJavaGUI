package Métodos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase metodosSQL recoge los mdtodos que se utilizaran para el tratamiento
 * de datos con JBDC.
 */
public class metodosSQL {

	/** Variable estatica para usarla en diferentes metodos de la clase. */
	private static Connection cnx;

	/**
	 * Cadena estatica que he creado para evitar errores al iniciar la base, ya que
	 * concatena el nombre de la base de datos despues de la primera ejecución del
	 * programa en un nuevo dispositivo.
	 */
	private static String nombreDataBase = "";

	/**
	 * getConnection.
	 *
	 * @return Devuelve un driver manager. Se usara para agilizar el trabajo = "cnx
	 *         = getConnection();"
	 * @throws SQLException Lanza una excepcion SQL que impide la conexión del
	 *                      software con la base de datos.
	 */
	public static Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost/" + nombreDataBase;
		String user = "root";
		String password = "";
		return DriverManager.getConnection(url, user, password);

	}

	/**
	 * Comprobar conexion, devuelve por consola "Conexion: Abierta" si consigue
	 * conectar con la base de datos.
	 */
	public static void comprobarConexion() {
		try {
			cnx = getConnection();
			System.out.print("Conexión: ");
			System.out.println(cnx.isClosed() ? "Cerrada" : "Abierta");
			cnx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Preparar base datos. Ejecuta un lote de Querys que crean la base de datos si
	 * no existe y todas sus tablas.
	 */
	public static void prepararBaseDatos() {
		try {
			cnx = getConnection();
			Statement stm = cnx.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			stm.addBatch("CREATE DATABASE IF NOT EXISTS airportinterfaces");
			stm.addBatch("use airportinterfaces");
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS pasajeros (id_pasajero INT PRIMARY KEY AUTO_INCREMENT, dni TEXT NOT NULL, nombre TEXT NOT NULL, edad INT NOT NULL );");
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS vuelos (id_vuelo INT PRIMARY KEY AUTO_INCREMENT, origen TEXT NOT NULL, destino TEXT NOT NULL, fechaSalida DATETIME NOT NULL, fechaLlegada DATETIME NOT NULL, duracion TEXT, numeroPlazas INT, numeroPasajeros INT, completo BOOLEAN);");
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS aeropuertos (id_aeropuerto INT PRIMARY KEY AUTO_INCREMENT, nombre TEXT NOT NULL, ciudad TEXT NOT NULL);");
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS vuelos_aeropuertos (id_vuelo INT NOT NULL, id_aeropuerto INT NOT NULL, PRIMARY KEY (id_vuelo, id_aeropuerto), FOREIGN KEY (id_vuelo) REFERENCES vuelos(id_vuelo), FOREIGN KEY (id_aeropuerto) REFERENCES aeropuertos(id_aeropuerto) );");
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS vuelos_pasajeros (id_pasajero INT NOT NULL, id_vuelo INT NOT NULL, PRIMARY KEY (id_pasajero, id_vuelo), FOREIGN KEY (id_pasajero) REFERENCES pasajeros(id_pasajero), FOREIGN KEY (id_vuelo) REFERENCES vuelos(id_vuelo) );");
			int[] resultados = stm.executeBatch();
			for (int resultado : resultados) {
				if (resultado == Statement.EXECUTE_FAILED) {
					System.err.println("Error al ejecutar una consulta en el lote.");
				}
			}
			stm.close();
			cnx.close();
			System.out.println("Base de datos preparada.");
			nombreDataBase = "airportinterfaces";

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// ======================== ===========================
	// SECCIÓN 1: Métodos para el tratamiento de pasajeros:
	//
	// - insertarPasajeroBD
	// - dniExiste
	// - modificarPasajero
	// - borrarPasajeroBD
	// - obtenerIdsVuelos
	// - verPasajeros
	// ======================== ===========================

	/**
	 * InsertarPasajeroBD. Inserta un pasajero en las tablas correspondientes.
	 *
	 * @param dni     Dni del pasajero
	 * @param nombre  Nombre del pasajero
	 * @param edad    Edad del pasajero
	 * @param idVuelo Id del vuelo al que se añadirá el pasajero.
	 * @return Devuelve la cadena que utilizaré para la apliación gráfica, para el
	 *         status de ejecución.
	 */
	public static String insertarPasajeroBD(String dni, String nombre, int edad, int idVuelo) {
		String res = "";
		Connection cnx = null;
		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;
		PreparedStatement pstm3 = null;
		PreparedStatement pstmCheck = null;
		ResultSet rsCheck = null;
		ResultSet generatedKeys = null;

		try {
			cnx = getConnection();
			cnx.setAutoCommit(false); // Iniciar la transacción

			// Verificar si el DNI ya existe
			String checkQuery = "SELECT COUNT(*) FROM pasajeros WHERE dni = ?";
			pstmCheck = cnx.prepareStatement(checkQuery);
			pstmCheck.setString(1, dni);
			rsCheck = pstmCheck.executeQuery();

			if (rsCheck.next() && rsCheck.getInt(1) > 0) {
				res = "El DNI ya existe en la base de datos.";
				return res;
			}

			// Insertar el pasajero
			String query1 = "INSERT INTO pasajeros (dni, nombre, edad) VALUES (?, ?, ?)";
			pstm1 = cnx.prepareStatement(query1, PreparedStatement.RETURN_GENERATED_KEYS);
			pstm1.setString(1, dni);
			pstm1.setString(2, nombre);
			pstm1.setInt(3, edad);
			int filasAfectadas = pstm1.executeUpdate();

			if (filasAfectadas == 0) {
				res = "No se pudo insertar el pasajero.";
				return res;
			}

			// Obtener el id_pasajero generado
			generatedKeys = pstm1.getGeneratedKeys();
			int idPasajero = 0;
			if (generatedKeys.next()) {
				idPasajero = generatedKeys.getInt(1);
			} else {
				res = "No se pudo obtener el ID del pasajero insertado.";
				return res;
			}

			// Insertar la relación en la tabla vuelos_pasajeros
			String query2 = "INSERT INTO vuelos_pasajeros (id_pasajero, id_vuelo) VALUES (?, ?)";
			pstm2 = cnx.prepareStatement(query2);
			pstm2.setInt(1, idPasajero);
			pstm2.setInt(2, idVuelo);
			pstm2.executeUpdate();

			// Actualizar el número de pasajeros en la tabla vuelos
			String query3 = "UPDATE vuelos SET numeroPasajeros = numeroPasajeros + 1 WHERE id_vuelo = ?";
			pstm3 = cnx.prepareStatement(query3);
			pstm3.setInt(1, idVuelo);
			pstm3.executeUpdate();

			cnx.commit(); // Confirmar la transacción
			res = "Pasajero insertado y relación establecida correctamente, número de pasajeros actualizado.";
		} catch (SQLException e) {
			if (cnx != null) {
				try {
					cnx.rollback(); // Revertir la transacción en caso de error
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			res = "Error al insertar el pasajero: " + e.getMessage();
		} finally {
			if (generatedKeys != null) {
				try {
					generatedKeys.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rsCheck != null) {
				try {
					rsCheck.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmCheck != null) {
				try {
					pstmCheck.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstm1 != null) {
				try {
					pstm1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstm2 != null) {
				try {
					pstm2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstm3 != null) {
				try {
					pstm3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (cnx != null) {
				try {
					cnx.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	public static boolean dniExiste(String dni) {
		boolean existe = false;
		try {
			cnx = getConnection();
			PreparedStatement pstm = cnx.prepareStatement("SELECT COUNT(*) FROM pasajeros WHERE dni = ?");
			pstm.setString(1, dni);
			ResultSet rs = pstm.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				existe = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}

	public static String modificarPasajero(String dniViejo, String dniNuevo, String nombre, int edad) {
		String res = "";
		try {
			cnx = getConnection();
			PreparedStatement pstm = cnx
					.prepareStatement("UPDATE pasajeros SET dni = ?, nombre = ?, edad = ? WHERE dni = ?");
			pstm.setString(1, dniNuevo);
			pstm.setString(2, nombre);
			pstm.setInt(3, edad);
			pstm.setString(4, dniViejo);
			int filasAfectadas = pstm.executeUpdate();
			if (filasAfectadas > 0) {
				res = "Pasajero actualizado";
			} else {
				res = "Error al actualizar el pasajero";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * BorrarPasajeroBD. Elimina al pasajero de la base de datos y sus relaciones
	 * con vuelos.
	 * 
	 * @param dni Dni del pasajero
	 * @return Devuelve una cadena como log de la interfaz grafica, para saber si se
	 *         ha ejecutado correctamente la eliminación
	 */
	public static String borrarPasajeroBD(String dni) {
		String res = "";
		try {
			cnx = getConnection();
			cnx.setAutoCommit(false);

			// Eliminar las filas relacionadas en vuelos_pasajeros
			String query1 = "DELETE FROM vuelos_pasajeros WHERE id_pasajero = (SELECT id_pasajero FROM pasajeros WHERE dni = ?)";
			PreparedStatement pstm1 = cnx.prepareStatement(query1);
			pstm1.setString(1, dni);
			pstm1.executeUpdate();
			pstm1.close();

			// Guardo su nombre
			PreparedStatement pstm3 = cnx.prepareStatement("SELECT nombre FROM pasajeros WHERE dni = ?");
			pstm3.setString(1, dni);
			ResultSet rs = pstm3.executeQuery();
			String nombreDelEliminado = "";

			// Eliminar el pasajero
			String query2 = "DELETE FROM pasajeros WHERE dni = ?";
			PreparedStatement pstm2 = cnx.prepareStatement(query2);
			pstm2.setString(1, dni);
			int resultado = pstm2.executeUpdate();
			pstm2.close();

			if (rs.next()) {
				nombreDelEliminado = rs.getString(1);
			}

			if (resultado > 0) {
				res = "Se ha eliminado correctamente al pasajero con Dni: " + dni + ", Nombre: " + nombreDelEliminado;
			} else {
				res = "No se ha eliminado, compruebe que el Dni corresponde con algún pasajero";
			}

			cnx.commit();
		} catch (SQLException e) {
			try {
				if (cnx != null) {
					cnx.rollback();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			res = "Error al eliminar el pasajero: " + e.getMessage();
		} finally {
			try {
				if (cnx != null) {
					cnx.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * obtenerIdsVuelos.
	 *
	 *
	 * @return Devuelve una lista de vuelos que se utilizara para hacer un
	 *         desplegable en la parte de insertarPasajero, para que siempre se esté
	 *         añadiendo a un vuelo existente.
	 */
	public static List<Integer> obtenerIdsVuelos() {
		List<Integer> vuelos = new ArrayList<>();
		try {
			cnx = getConnection(); // Obtener la conexión JDBC
			String query = "SELECT id_vuelo FROM vuelos";
			PreparedStatement pstm = cnx.prepareStatement(query);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				int idVuelo = rs.getInt("id_vuelo");
				vuelos.add(idVuelo);
			}
			rs.close();
			pstm.close();
			cnx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vuelos;
	}
	public static String verPasajeros() {
		String res = "";
		try {
			cnx = getConnection();
			Statement stm = cnx.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stm.executeQuery("SELECT * FROM pasajeros");
			while (rs.next()) {
				int idPasajero = rs.getInt("id_pasajero");
				String dniPasajero = rs.getString("dni");
				String nombrePasajero = rs.getString("nombre");
				int edadPasajero = rs.getInt("edad");
				res += "ID: " + idPasajero + ", DNI: " + dniPasajero + ", Nombre: " + nombrePasajero + ", Edad: "
						+ edadPasajero + "\n";
			}
			rs.close();
			stm.close();
			cnx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}
	// ======================== ===========================
	// SECCIÓN 2: Métodos para el tratamiento de vuelos:
	//
	// - insertarVueloBD
	// - eliminarVueloPorID
	// - modificarVuelo
	// - buscarVueloPorID
	// - buscarVueloPorOrigenYDestino
	//
	// ======================== ===========================

	/**
	 * InsertarVueloBD.
	 *
	 * @param origen          Origen del vuelo
	 * @param destino         Destino del vuelo
	 * @param fechaSalida     Fecha de salida
	 * @param fechaLlegada    Fecha de llegada
	 * @param numeroPlazas    Número de plazas disponibles.
	 * @param numeroPasajeros Número de pasajeros. (Por defecto siempre será 0
	 *                        cuando se ejecute el metodo desde la interfaz
	 *                        gráfica.)
	 * @return Devuelve una cadena para conocer el estado de ejecución del método
	 */
	// METODOS VUELOS
	public static String insertarVueloBD(String origen, String destino, String fechaSalida, String fechaLlegada,
			int numeroPlazas, int numeroPasajeros) {
		String res = "";
		Connection cnx = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			// Obtener la conexión
			cnx = getConnection();

			// Formatear las fechas
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			LocalDateTime localTimeSalida = LocalDateTime.parse(fechaSalida, formatter);
			LocalDateTime localTimeLlegada = LocalDateTime.parse(fechaLlegada, formatter);
			Timestamp timestampSalida = Timestamp.valueOf(localTimeSalida);
			Timestamp timestampLlegada = Timestamp.valueOf(localTimeLlegada);
			Duration duracionVuelo = Duration.between(localTimeSalida, localTimeLlegada);
			String duracionString = String.valueOf(duracionVuelo);

			// Insertar vuelo en la tabla vuelos
			pstm = cnx.prepareStatement(
					"INSERT INTO vuelos (origen, destino, fechaSalida, fechaLlegada, duracion, numeroPlazas, numeroPasajeros, completo) VALUES (?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			pstm.setString(1, origen);
			pstm.setString(2, destino);
			pstm.setTimestamp(3, timestampSalida);
			pstm.setTimestamp(4, timestampLlegada);
			pstm.setString(5, duracionString);
			pstm.setInt(6, numeroPlazas);
			pstm.setInt(7, numeroPasajeros);
			pstm.setBoolean(8, numeroPasajeros >= numeroPlazas);
			int filasAfectadas = pstm.executeUpdate();

			if (filasAfectadas > 0) {
				System.out.println("Inserción de vuelo correcta");
				res = "Inserción de vuelo correcta";

				// Obtengo la ID del vuelo recién insertado
				rs = pstm.getGeneratedKeys();
				if (rs.next()) {
					int idVuelo = rs.getInt(1);

					// Obtengo la ID del aeropuerto correspondiente al origen
					PreparedStatement pstm2 = cnx
							.prepareStatement("SELECT id_aeropuerto FROM aeropuertos WHERE nombre = ?");
					pstm2.setString(1, origen);
					ResultSet rs2 = pstm2.executeQuery();
					if (rs2.next()) {
						int idAeropuerto = rs2.getInt("id_aeropuerto");

						// Inserto en la tabla intermedia vuelos_aeropuertos
						PreparedStatement pstm3 = cnx.prepareStatement(
								"INSERT INTO vuelos_aeropuertos (id_vuelo, id_aeropuerto) VALUES (?, ?)");
						pstm3.setInt(1, idVuelo);
						pstm3.setInt(2, idAeropuerto);
						pstm3.executeUpdate();
						pstm3.close();
					}
					rs2.close();
					pstm2.close();
				}
				rs.close();
			} else {
				System.out.println("Error al insertar vuelo");
				res = "Error al insertar el vuelo";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
				if (cnx != null)
					cnx.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * eliminarVueloPorID.
	 *
	 * @param idVuelo the id vuelo
	 * @return the string
	 */
	public static String eliminarVueloPorID(int idVuelo) {
		Connection cnx = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		String res = "";
		try {
			cnx = getConnection();
			cnx.setAutoCommit(false); // Deshabilitar la confirmación automática de la transacción
			// Eliminar registros de vuelos_aeropuertos asociados al vuelo
			String deleteVuelosAeropuertos = "DELETE FROM vuelos_aeropuertos WHERE id_vuelo = ?";
			stmt1 = cnx.prepareStatement(deleteVuelosAeropuertos);
			stmt1.setInt(1, idVuelo);
			stmt1.executeUpdate();
			// Eliminar el vuelo de la tabla vuelos
			String deleteVuelo = "DELETE FROM vuelos WHERE id_vuelo = ?";
			stmt2 = cnx.prepareStatement(deleteVuelo);
			stmt2.setInt(1, idVuelo);
			stmt2.executeUpdate();
			// Confirmar la transacción
			cnx.commit();
			res = "Eliminación satisfactoria";
		} catch (Exception e) {
			try {
				if (cnx != null)
					cnx.rollback(); // Revertir la transacción en caso de error
				res = "Error en la eliminación";
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (stmt1 != null)
					stmt1.close();
				if (stmt2 != null)
					stmt2.close();
				if (cnx != null) {
					cnx.setAutoCommit(true); // Restaurar la configuración de confirmación automática
					cnx.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return res;
	}
	/**
	 * Modificar vuelo.
	 *
	 * @param id_vuelo     Id del vuelo a modificar.
	 * @param fechaSalida  Nueva fecha de salida
	 * @param fechaLlegada Nueva fecha de llegada
	 * @return Devuelve una cadena de texto que indica el estado de ejecución del
	 *         método.
	 */
	public static String modificarVuelo(int id_vuelo, String fechaSalida, String fechaLlegada) {
		String res = "";
		try {
			cnx = getConnection();
			PreparedStatement pstm = cnx
					.prepareStatement("UPDATE vuelos SET fechaSalida=?, fechaLlegada=?, duracion=? WHERE id_vuelo = ?");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			LocalDateTime localTimeSalida = LocalDateTime.parse(fechaSalida, formatter);
			LocalDateTime localTimeLlegada = LocalDateTime.parse(fechaLlegada, formatter);
			Timestamp timestampSalida = Timestamp.valueOf(localTimeSalida);
			Timestamp timestampLlegada = Timestamp.valueOf(localTimeLlegada);
			Duration duracionVuelo = Duration.between(localTimeSalida, localTimeLlegada);
			String duracionString = String.valueOf(duracionVuelo);
			pstm.setTimestamp(1, timestampSalida);
			pstm.setTimestamp(2, timestampLlegada);
			pstm.setString(3, duracionString);
			pstm.setInt(4, id_vuelo);

			int filasAfectadas = pstm.executeUpdate();
			if (filasAfectadas > 0) {
				System.out.println("Modificación de vuelo correcta");
				res = "Modificación de vuelo correcta";

			} else {
				System.out.println("Error al modificar vuelo");
				res = "Error al modificar el vuelo";
			}
			pstm.close();
			cnx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * buscarVueloPorID.
	 *
	 * @param id_vuelo ID del vuelo a buscar, este metodo siempre dara un unico
	 *                 resultado.
	 * @return Devuelve la cadena con todos los datos del vuelo, en el caso de no
	 *         encontrarse, devuelve un mensaje para informar al usuario.
	 */
	public static String buscarVueloPorID(int id_vuelo) {
		String res = "";
		try {
			cnx = getConnection();
			String query = "SELECT * FROM vuelos WHERE id_vuelo = ?";
			PreparedStatement pstm = cnx.prepareStatement(query);
			pstm.setInt(1, id_vuelo);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id_vuelo");
				String origen = rs.getString("origen");
				String destino = rs.getString("destino");
				Timestamp fechaSalida = rs.getTimestamp("fechaSalida");
				Timestamp fechaLlegada = rs.getTimestamp("fechaLlegada");
				String duracion = rs.getString("duracion");
				int numeroPlazas = rs.getInt("numeroPlazas");
				int numeroPasajeros = rs.getInt("numeroPasajeros");
				boolean isCompleto = rs.getBoolean("completo");
				String cadenaBoolean;
				if (isCompleto) {
					cadenaBoolean = "Sí";

				} else {
					cadenaBoolean = "No";
				}

				System.out.println("Se ha encontrado el siguiente vuelo: " + "\n");
				System.out.println("ID: " + id + " Origen: " + origen + " Destino: " + destino + " Salida: "
						+ fechaSalida + " Llegada: " + fechaLlegada + " Duración: " + duracion + " Número de plazas "
						+ numeroPlazas + " Pasajeros: " + numeroPasajeros + " Está completo: " + cadenaBoolean);
				res = "Se ha encontrado el siguiente vuelo: " + "ID: " + id + " Origen: " + origen + " Destino: "
						+ destino + " Salida: " + fechaSalida + " Llegada: " + fechaLlegada + " Duración: " + duracion
						+ " Número de plazas " + numeroPlazas + " Pasajeros: " + numeroPasajeros + " Está completo: "
						+ cadenaBoolean;
			} else {
				System.out.println("No se ha encontrado ningun vuelo con la id proporcionada.");
				res = "No se ha encontrado ningun vuelo con la id proporcionada.";
			}
			pstm.close();
			cnx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * buscarVueloPorOrigenYDestino.
	 *
	 * @param origen  Origen a buscar
	 * @param destino Destino a buscar
	 * @return Devolverá todos los resultados que coincidan con el origen y destino
	 *         indicado por parámetros.
	 */
	public static String buscarVueloPorOrigenYDestino(String origen, String destino) {
		String res = "";
		try {
			cnx = getConnection();
			String query = "SELECT * FROM vuelos WHERE origen = ? AND destino = ?";
			PreparedStatement pstm = cnx.prepareStatement(query);
			pstm.setString(1, origen);
			pstm.setString(2, destino);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id_vuelo");
				String origin = rs.getString("origen");
				String destine = rs.getString("destino");
				Timestamp fechaSalida = rs.getTimestamp("fechaSalida");
				Timestamp fechaLlegada = rs.getTimestamp("fechaLlegada");
				String duracion = rs.getString("duracion");
				int numeroPlazas = rs.getInt("numeroPlazas");
				int numeroPasajeros = rs.getInt("numeroPasajeros");
				boolean isCompleto = rs.getBoolean("completo");
				String cadenaBoolean;
				if (isCompleto) {
					cadenaBoolean = "Sí";

				} else {
					cadenaBoolean = "No";
				}

				System.out.println("ID: " + id + " Origen: " + origin + " Destino: " + destine + " Salida: "
						+ fechaSalida + " Llegada: " + fechaLlegada + " Duración: " + duracion + " Número de plazas "
						+ numeroPlazas + " Pasajeros: " + numeroPasajeros + " Está completo: " + cadenaBoolean);
				res += "ID: " + id + " Origen: " + origin + " Destino: " + destino + " Salida: " + fechaSalida
						+ " Llegada: " + fechaLlegada + " Duración: " + duracion + " Número de plazas " + numeroPlazas
						+ " Pasajeros: " + numeroPasajeros + " Está completo: " + cadenaBoolean + "\n" + "\n";
			}
			pstm.close();
			cnx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;

	}

	// ======================== ===========================
	// SECCIÓN 3: Métodos para el tratamiento de aeropuertos:
	//
	// - insertarAeropuertoBD
	// - borrarAeropuertoBDPorNombre
	// - modificarAeropuertoBD
	// - obtenerNombresAeropuertos
	// - obtenerVuelosConOrigenYDestino
	//
	// ======================== ===========================
	
	/**
	 * insertarAeropuertoBD.
	 *
	 * @param nombre Nombre del aeropuerto (Siglas)
	 * @param ciudad Ciudad del aeropuerto
	 * @return Status del metodo como cadena de texto.
	 */

	public static String insertarAeropuertoBD(String nombre, String ciudad) {
		String res = "";
		try {
			cnx = getConnection();
			String query = "INSERT INTO aeropuertos (nombre, ciudad) VALUES (?,?)";
			PreparedStatement pstm = cnx.prepareStatement(query);
			pstm.setString(1, nombre);
			pstm.setString(2, ciudad);
			int filasAfectadas = pstm.executeUpdate();
			if (filasAfectadas > 0) {
				System.out.println("Insercíon de aeropuerto correcta.");
				res = "Insercíon de aeropuerto correcta.";
			} else {
				System.out.println("Error en la inserción de aeropuerto");
				res = "Error en la inserción de aeropuerto";
			}
			pstm.close();
			cnx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * borrarAeropuertoBDPorNombre. Este método eliminara el aeropuerto, sus vuelos
	 * asociados, y todos los pasajeros que estén en esos vuelos.
	 * 
	 * @param nombre Nombre del aeropuerto a eliminar.
	 * @return Devuelve una cadena con el status del método. Se recogera con una
	 *         etiqueta en la interfaz grafica.
	 */
	public static String borrarAeropuertoBDPorNombre(String nombre) {
		Connection cnx = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String res = "";
		try {
			cnx = getConnection();
			String queryVueloAeropuerto = "DELETE FROM vuelos_aeropuertos WHERE id_aeropuerto IN (SELECT id_aeropuerto FROM aeropuertos WHERE nombre = ?)";
			pstm = cnx.prepareStatement(queryVueloAeropuerto);
			pstm.setString(1, nombre);
			pstm.executeUpdate();
			pstm.close();
			String queryVuelos = "SELECT id_vuelo FROM vuelos WHERE id_vuelo IN (SELECT id_vuelo FROM vuelos_aeropuertos WHERE id_aeropuerto IN (SELECT id_aeropuerto FROM aeropuertos WHERE nombre = ?))";
			pstm = cnx.prepareStatement(queryVuelos);
			pstm.setString(1, nombre);
			rs = pstm.executeQuery();
			while (rs.next()) {
				int idVuelo = rs.getInt("id_vuelo");
				System.out.println("Se encontraron vuelos asociados. Eliminando vuelo con ID: " + idVuelo);
				metodosSQL.eliminarVueloPorID(idVuelo);
			}

			rs.close();
			pstm.close();
			String queryFinal = "DELETE FROM aeropuertos WHERE nombre = ?";
			pstm = cnx.prepareStatement(queryFinal);
			pstm.setString(1, nombre);
			int filasAfectadas = pstm.executeUpdate();

			if (filasAfectadas > 0) {
				System.out.println("Eliminación satisfactoria del aeropuerto con nombre: " + nombre);
				res = "Eliminación satisfactoria del aeropuerto con nombre: " + nombre;
			} else {
				System.out.println("No se encontró un aeropuerto con ese nombre.");
				res = "No se encontró un aeropuerto con ese nombre.";
			}
			cnx.close();
			pstm.close();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Modificar aeropuerto BD.
	 *
	 * @param nombreAntiguo the nombre antiguo
	 * @param nuevoNombre   the nuevo nombre
	 * @param nuevaCiudad   the nueva ciudad
	 * @return the string
	 */
	public static String modificarAeropuertoBD(String nombreAntiguo, String nuevoNombre, String nuevaCiudad) {
		String res = "";
		try {

			cnx = getConnection();
			String query = "SELECT * FROM aeropuertos WHERE nombre = ?";
			PreparedStatement pstm = cnx.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_UPDATABLE);
			pstm.setString(1, nombreAntiguo);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				rs.updateString("nombre", nuevoNombre);
				rs.updateString("ciudad", nuevaCiudad);
				rs.updateRow();
				res = "Aeropuerto modificado correctamente";

			} else {
				res = "No se encontró un vuelo con ese nombre";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * obtenerNombresAeropuertos. Se usara para los desplegables que tengan que ver
	 * con origen y destino de vuelos.
	 * 
	 * @return Lista con los nombres de los aeropuertos
	 */
	public static List<String> obtenerNombresAeropuertos() {
		List<String> aeropuertos = new ArrayList<>();
		try {
			Connection cnx = getConnection();
			PreparedStatement pstm = cnx.prepareStatement("SELECT nombre FROM aeropuertos");
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				aeropuertos.add(rs.getString("nombre"));
			}
			rs.close();
			pstm.close();
			cnx.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return aeropuertos;
	}

	/**
	 * Obtener vuelos con origen Y destino.
	 *
	 * @return Una lista con los vuelos, origen, destino y fechas para que la
	 *         selección sea lo mas intuitiva posible y con poco margen de error.
	 */
	public static List<String> obtenerVuelosConOrigenYDestino() {
		List<String> vuelos = new ArrayList<>();
		cnx = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// Conexión a la base de datos
			cnx = getConnection();
			stmt = cnx.createStatement();
			String sql = "SELECT id_vuelo, origen, destino, fechaSalida, fechaLlegada FROM vuelos";
			rs = stmt.executeQuery(sql);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			while (rs.next()) {
				int id = rs.getInt("id_vuelo");
				String origen = rs.getString("origen");
				String destino = rs.getString("destino");
				Timestamp fechaSalida = rs.getTimestamp("fechaSalida");
				String fechaFormateada = dateFormat.format(fechaSalida);
				Timestamp fechaLlegada = rs.getTimestamp("fechaLlegada");
				String fechaFormateada2 = dateFormat.format(fechaLlegada);
				vuelos.add(
						id + " - " + origen + " -> " + destino + " || " + fechaFormateada + " -> " + fechaFormateada2);
			}
			rs.close();
			stmt.close();
			cnx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vuelos;
	}

	

}
