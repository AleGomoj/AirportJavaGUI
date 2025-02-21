package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Métodos.metodosSQL;

/**
 * Clase ModificarPasajeroPanel.
 */
public class ModificarPasajeroPanel extends BackgroundPanel {

    private JTextField dniViejoField;
    private JTextField dniNuevoField;
    private JTextField nombreField;
    private JTextField edadField;

    /**
     * Inicia el panel de modificar pasajeros.
     *
     * @param imagePath ruta relativa de la imagen de fondo
     */
    public ModificarPasajeroPanel(String imagePath) {
    	super(imagePath);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        Dimension fieldDimension = new Dimension(200, 25);

        addCampoObligatorio("DNI antiguo:", dniViejoField = new JTextField(), fieldDimension, gbc);
        addCampoObligatorio("DNI nuevo:", dniNuevoField = new JTextField(), fieldDimension, gbc);
        addCampoObligatorio("Nombre:", nombreField = new JTextField(), fieldDimension, gbc);
        addCampoObligatorio("Edad:", edadField = new JTextField(), fieldDimension, gbc);

        JButton modificarButton = new JButton("Modificar Pasajero");
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 2;
        modificarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (camposValidos()) {
                    modificarPasajero();
                } else {
                    mostrarMensaje("Por favor, complete todos los campos con valores válidos.");
                }
            }
        });
        add(modificarButton, gbc);
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
        try {
            Integer.parseInt(edadField.getText());
        } catch (NumberFormatException e) {
            return false;
        }

        return !dniViejoField.getText().isEmpty() &&
               !dniNuevoField.getText().isEmpty() &&
               !nombreField.getText().isEmpty() &&
               edadField.getText().matches("\\d+");
    }

    /**
     * Modificar pasajero.
     */
    private void modificarPasajero() {
        try {
            String dniViejo = dniViejoField.getText();
            String dniNuevo = dniNuevoField.getText();
            String nombre = nombreField.getText();
            int edad = Integer.parseInt(edadField.getText());

            if (!metodosSQL.dniExiste(dniViejo)) {
                mostrarMensaje("El DNI antiguo no existe en la base de datos.");
                return;
            }

            if (dniViejo.equals(dniNuevo)) {
                mostrarMensaje("El DNI nuevo no puede ser el mismo que el DNI viejo.");
                return;
            }

            String mensaje = metodosSQL.modificarPasajero(dniViejo, dniNuevo, nombre, edad);
            mostrarMensaje(mensaje);
        } catch (Exception e) {
            mostrarMensaje("Error al modificar pasajero.");
            e.printStackTrace();
        }
    }

    /**
     * Mostrar mensaje.
     *
     * @param mensaje introducido para mostrar en el Dialog.
     */
    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
