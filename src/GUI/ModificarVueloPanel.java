package GUI;

import javax.swing.*;

import Métodos.metodosSQL;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * Clase ModificarVueloPanel.
 */
public class ModificarVueloPanel extends BackgroundPanel {
    
    /** id vuelo combo box. */
    private JComboBox<String> idVueloComboBox;
    
    /** fecha salida spinner. */
    private JSpinner fechaSalidaSpinner;
    
    /** fecha llegada spinner. */
    private JSpinner fechaLlegadaSpinner;

    /**
     * Inicia el panel de modificar vuelos.
     *
     * @param imagePath ruta relativa de la imagen de fondo
     */
    public ModificarVueloPanel(String imagePath) {
        super(imagePath);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        Dimension idDimension = new Dimension(350, 25); 

        addCampoObligatorio("ID Vuelo:", idVueloComboBox = new JComboBox<>(), idDimension, gbc);

        // Añadir PopupMenuListener para actualizar el JComboBox al desplegar
        idVueloComboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                cargarIDsVuelos();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        fechaSalidaSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor fechaSalidaEditor = new JSpinner.DateEditor(fechaSalidaSpinner, "dd-MM-yyyy HH:mm:ss");
        fechaSalidaSpinner.setEditor(fechaSalidaEditor);
        ((JFormattedTextField) fechaSalidaEditor.getTextField()).setEditable(false);
        addCampoObligatorio("Fecha Salida:", fechaSalidaSpinner, null, gbc);

        fechaLlegadaSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor fechaLlegadaEditor = new JSpinner.DateEditor(fechaLlegadaSpinner, "dd-MM-yyyy HH:mm:ss");
        fechaLlegadaSpinner.setEditor(fechaLlegadaEditor);
        ((JFormattedTextField) fechaLlegadaEditor.getTextField()).setEditable(false);
        addCampoObligatorio("Fecha Llegada:", fechaLlegadaSpinner, null, gbc);

        JButton modificarButton = new JButton("Modificar Vuelo");
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        modificarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (camposValidos()) {
                    modificarVuelo();
                } else {
                    mostrarMensaje("Por favor, complete todos los campos.");
                }
            }
        });
        add(modificarButton, gbc);
    }

    /**
     * Cargar IDs de vuelos en el combo box.
     */
    private void cargarIDsVuelos() {
        idVueloComboBox.removeAllItems();
        try {
            List<String> vuelos = metodosSQL.obtenerVuelosConOrigenYDestino(); // Utilizamos el método proporcionado
            for (String vuelo : vuelos) {
                idVueloComboBox.addItem(vuelo);
            }
        } catch (Exception e) {
            mostrarMensaje("Error al cargar IDs de vuelos.");
            e.printStackTrace();
        }
    }

    /**
     * addCampoObligatorio
     *
     * @param labelText Etiqueta de texto
     * @param component Componente
     * @param dimension Dimension
     * @param gbc ajustes de componente
     */
    private void addCampoObligatorio(String labelText, JComponent component, Dimension dimension, GridBagConstraints gbc) {
        if (dimension != null) {
            component.setPreferredSize(dimension);
            component.setMinimumSize(dimension);
        }

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel(labelText + " (*)"), gbc);
        gbc.gridx = 1;
        add(component, gbc);
    }

    /**
     * Campos validos.
     *
     * @return true si son válidos
     */
    private boolean camposValidos() {
        return idVueloComboBox.getSelectedItem() != null &&
                fechaSalidaSpinner.getValue() != null &&
                fechaLlegadaSpinner.getValue() != null;
    }

    /**
     * Modificar vuelo.
     * Ejecuta el método.
     */
    private void modificarVuelo() {
        try {
            String vueloInfo = (String) idVueloComboBox.getSelectedItem();
            int idVuelo = Integer.parseInt(vueloInfo.split(" - ")[0]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String fechaSalida = dateFormat.format((Date) fechaSalidaSpinner.getValue());
            String fechaLlegada = dateFormat.format((Date) fechaLlegadaSpinner.getValue());
            
            String mensaje = metodosSQL.modificarVuelo(idVuelo, fechaSalida, fechaLlegada);
            mostrarMensaje(mensaje);
        } catch (Exception e) {
            mostrarMensaje("Error al modificar vuelo.");
            e.printStackTrace();
        }
    }

    /**
     * Mostrar mensaje.
     *
     * @param Mensaje introducido para mostrar en el Dialog.
     */
    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
    
}
