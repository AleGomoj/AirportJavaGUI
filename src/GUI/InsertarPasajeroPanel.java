/*
 * 
 */
package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

/**
 * Clase InsertarPasajeroPanel.
 */
public class InsertarPasajeroPanel extends BackgroundPanel {
    
    /** Campo dni */
    private JTextField dniField;
    
    /** Campo nombre */
    private JTextField nombreField;
    
    /** Campo edad */
    private JTextField edadField;
    
    /** Combobox de vuelos */
    private JComboBox<Integer> vueloComboBox;

    /**
     * Inicia el panel de insertar
     *
     * @param imagePath ruta a la imagen de fondo
     */
    public InsertarPasajeroPanel(String imagePath) {
        super(imagePath);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        
        configurarCampos(gbc);

       
        configurarBotonInsertar(gbc);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                actualizarVuelos();
            }
        });
    }

    /**
     * Configurar campos.
     *
     * @param gbc the gbc
     */
    private void configurarCampos(GridBagConstraints gbc) {
       
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("DNI: (*)"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        dniField = new JTextField(15);
        add(dniField, gbc);

       
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(new JLabel("Nombre: (*)"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        nombreField = new JTextField(15);
        add(nombreField, gbc);

       
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(new JLabel("Edad: (*)"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        edadField = new JTextField(15);
        add(edadField, gbc);

       
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(new JLabel("ID del Vuelo: (*)"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        vueloComboBox = new JComboBox<>();
        add(vueloComboBox, gbc);
    }

    /**
     * Configurar boton insertar.
     *
     * @param gbc the gbc
     */
    private void configurarBotonInsertar(GridBagConstraints gbc) {
       
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton insertarButton = new JButton("Insertar Pasajero");
        add(insertarButton, gbc);

        insertarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              
                String dni = dniField.getText().trim();
                String nombre = nombreField.getText().trim();
                String edadText = edadField.getText().trim();
                Integer idVuelo = (Integer) vueloComboBox.getSelectedItem();

                
                if (dni.isEmpty() || nombre.isEmpty() || edadText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int edad;
                try {
                    edad = Integer.parseInt(edadText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Edad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                
                String mensaje = Métodos.metodosSQL.insertarPasajeroBD(dni, nombre, edad, idVuelo);
                JOptionPane.showMessageDialog(null, mensaje);

              
                dniField.setText("");
                nombreField.setText("");
                edadField.setText("");
            }
        });
    }

    /**
     * Actualizar vuelos.
     */
    private void actualizarVuelos() {
        List<Integer> vuelos = Métodos.metodosSQL.obtenerIdsVuelos();
        vueloComboBox.setModel(new DefaultComboBoxModel<>(vuelos.toArray(new Integer[0])));
    }
}
