package GUI;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import Métodos.metodosSQL;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * InsertarVueloPanel. Panel de la inserción de vuelos
 */
public class InsertarVueloPanel extends BackgroundPanel {

	/** Caja Origen. */
	private JComboBox<String> origenComboBox;

	/** Caja destino */
	private JComboBox<String> destinoComboBox;

	/** Spinner para fecha de salida */
	private JSpinner fechaSalidaSpinner;

	/** Spinner para fecha de llegada */
	private JSpinner fechaLlegadaSpinner;

	/** Campo para el número de plazas */
	private JTextField numeroPlazasField;

	/**
	 * Inicia el panel de insertarVueloPanel
	 *
	 * @param imagePath ruta de la imagen de fondo.
	 */
	public InsertarVueloPanel(String imagePath) {
		super(imagePath);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		Dimension textFieldDimension = new Dimension(200, 25);

		addCampoObligatorio("Origen: (*)", origenComboBox = new JComboBox<>(), textFieldDimension, gbc);
		addCampoObligatorio("Destino: (*)", destinoComboBox = new JComboBox<>(), textFieldDimension, gbc);

		fechaSalidaSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor fechaSalidaEditor = new JSpinner.DateEditor(fechaSalidaSpinner, "dd-MM-yyyy HH:mm:ss");
		fechaSalidaSpinner.setEditor(fechaSalidaEditor);
		((JFormattedTextField) fechaSalidaEditor.getTextField()).setEditable(false);
		addCampoObligatorio("Fecha Salida: (*)", fechaSalidaSpinner, textFieldDimension, gbc);

		fechaLlegadaSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor fechaLlegadaEditor = new JSpinner.DateEditor(fechaLlegadaSpinner, "dd-MM-yyyy HH:mm:ss");
		fechaLlegadaSpinner.setEditor(fechaLlegadaEditor);
		((JFormattedTextField) fechaLlegadaEditor.getTextField()).setEditable(false);
		addCampoObligatorio("Fecha Llegada: (*)", fechaLlegadaSpinner, textFieldDimension, gbc);

		addCampoObligatorio("Número de Plazas: (*)", numeroPlazasField = new JTextField(5), textFieldDimension, gbc);

		JButton insertarButton = new JButton("Insertar Vuelo");
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		insertarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		insertarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertarVuelo();
			}
		});
		add(insertarButton, gbc);

		actualizarAeropuertos();
		// He tenido problemas con esta funcionalidad, para hacer que se me recargase el
		// listado de aeropuertos y tuviese en cuenta los cambios hechos con la app
		// abierta, he tenido que usar un popupMenu
		origenComboBox.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				actualizarOrigenes();
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});

		origenComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarDestinos();
			}
		});
	}

	/**
	 * addCampoObligatorio
	 *
	 * @param labelText Etiqueta de texto
	 * @param component Componente
	 * @param dimension Dimension
	 * @param gbc       ajustes de componente
	 */
	private void addCampoObligatorio(String labelText, JComponent component, Dimension dimension,
			GridBagConstraints gbc) {
		component.setPreferredSize(dimension);
		component.setMinimumSize(dimension);

		gbc.gridx = 0;
		gbc.gridy++;
		add(new JLabel(labelText), gbc);
		gbc.gridx = 1;
		add(component, gbc);
	}

	/**
	 * Insertar vuelo. Se ejecuta el método y se comprueba que los campos están
	 * llenos.
	 */
	private void insertarVuelo() {
		String orig = (String) origenComboBox.getSelectedItem();
		String destino = (String) destinoComboBox.getSelectedItem();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String fechaSalida = dateFormat.format((Date) fechaSalidaSpinner.getValue());
		String fechaLlegada = dateFormat.format((Date) fechaLlegadaSpinner.getValue());
		String numeroPlazasText = numeroPlazasField.getText().trim();

		if (orig.isEmpty() || destino.isEmpty() || fechaSalida.isEmpty() || fechaLlegada.isEmpty()
				|| numeroPlazasText.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		int numeroPlazas;
		try {
			numeroPlazas = Integer.parseInt(numeroPlazasText);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Número de plazas debe ser un valor numérico.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String resultado = metodosSQL.insertarVueloBD(orig, destino, fechaSalida, fechaLlegada, numeroPlazas, 0);
		JOptionPane.showMessageDialog(null, resultado, "Resultado de Inserción", JOptionPane.INFORMATION_MESSAGE);

		actualizarAeropuertos(); // Actualizar ambos ComboBox después de insertar el vuelo

		origenComboBox.setSelectedIndex(0);
		destinoComboBox.setSelectedIndex(0);
		fechaSalidaSpinner.setValue(new Date());
		fechaLlegadaSpinner.setValue(new Date());
		numeroPlazasField.setText("");
	}

	/**
	 * Actualizar aeropuertos. Actualiza la lista de origen y destino en tiempo real
	 * para no tener que reiniciar la aplicación.
	 */
	public void actualizarAeropuertos() {
		actualizarOrigenes();
		actualizarDestinos();
	}

	/**
	 * Actualizar origenes. Actualiza la lista de origen en tiempo real.
	 */
	private void actualizarOrigenes() {
		List<String> aeropuertos = Métodos.metodosSQL.obtenerNombresAeropuertos();
		origenComboBox.removeAllItems();
		aeropuertos.forEach(aeropuerto -> origenComboBox.addItem(aeropuerto));
	}

	/**
	 * Actualizar destinos. Actualiza la lista de destinos para que no coincida.
	 */
	private void actualizarDestinos() {
		String aeropuertoOrigen = (String) origenComboBox.getSelectedItem();
		destinoComboBox.removeAllItems();
		List<String> aeropuertos = Métodos.metodosSQL.obtenerNombresAeropuertos();
		aeropuertos.stream().filter(aeropuerto -> !aeropuerto.equals(aeropuertoOrigen))
				.forEach(destinoComboBox::addItem);
	}
}
