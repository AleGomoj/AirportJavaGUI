package GUI;

import javax.swing.*;

import Métodos.metodosSQL;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.util.List;

/**
 * Panel para eliminar vuelos.
 */
public class EliminarVueloPanel extends BackgroundPanel {

	/** Desplegable para los ID de los vuelos. */
	private JComboBox<String> idVueloComboBox;

	/** etiqueta para el estado. */
	private JLabel statusLabel;

	/**
	 * Inicia el panel.
	 *
	 * @param imagePath ruta de la imagen de fondo
	 */
	public EliminarVueloPanel(String imagePath) {
		super(imagePath);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		// Desplegable para seleccionar el vuelo
		idVueloComboBox = new JComboBox<>();
		idVueloComboBox.setPrototypeDisplayValue("Seleccione un vuelo");
		idVueloComboBox.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				actualizarVuelos();
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
			}
		});

		JLabel idVueloLabel = new JLabel("Vuelo:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(idVueloLabel, gbc);
		gbc.gridx = 1;
		idVueloComboBox.setPreferredSize(new Dimension(420, 25)); // Aumentar el tamaño del JComboBox
		add(idVueloComboBox, gbc);

		// Botón para eliminar el vuelo
		JButton eliminarButton = new JButton("Eliminar Vuelo");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		eliminarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		eliminarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validarCampos()) {
					eliminarVuelo();
				}
			}
		});
		add(eliminarButton, gbc);

		// Etiqueta para mostrar el estado de la operación
		statusLabel = new JLabel("");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		add(statusLabel, gbc);

		// Inicializar la lista de vuelos
		actualizarVuelos();
	}

	/**
	 * validarCampos.
	 *
	 * @return Devuelve true si son válidos
	 */
	// Método para validar si los campos están vacíos
	private boolean validarCampos() {
		return idVueloComboBox.getSelectedItem() != null;
	}

	/**
	 * actualizarVuelos.
	 */
	// Método para actualizar la lista de vuelos en el desplegable
	private void actualizarVuelos() {
		List<String> vuelos = metodosSQL.obtenerVuelosConOrigenYDestino();
		idVueloComboBox.removeAllItems();
		for (String vuelo : vuelos) {
			idVueloComboBox.addItem(vuelo);
		}
	}

	/**
	 * eliminarVuelo.
	 */
	// Método para eliminar un vuelo por su id, ejecuta el método interno.
	private void eliminarVuelo() {
		try {
			String vueloSeleccionado = (String) idVueloComboBox.getSelectedItem();
			if (vueloSeleccionado != null) {
				String[] partes = vueloSeleccionado.split(" - ");
				int idVuelo = Integer.parseInt(partes[0].trim());
				String mensaje = metodosSQL.eliminarVueloPorID(idVuelo);
				JOptionPane.showMessageDialog(null, mensaje, "Resultado de Eliminación",
						JOptionPane.INFORMATION_MESSAGE);
				actualizarVuelos(); // Actualizar la lista de vuelos después de eliminar uno
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar vuelo.", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

}
