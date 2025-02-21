package GUI;

import javax.swing.*;

import Métodos.metodosSQL;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * EliminarPasajeroPanel.
 */
public class EliminarPasajeroPanel extends BackgroundPanel {
  
  /** Campo para el dni. */
  private JTextField dniField;

  /**
   * EliminarPasajeroPanel
   * Inicia el panel de EliminarPasajeroPanel
   *
   * @param imagePath ruta de la imagen de fondo
   */
  public EliminarPasajeroPanel(String imagePath) {
    super(imagePath);
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    gbc.gridx = 0;
    gbc.gridy = 0;
    add(new JLabel("DNI:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    dniField = new JTextField(15);
    add(dniField, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    JButton eliminarButton = new JButton("Eliminar Pasajero");
    add(eliminarButton, gbc);

    eliminarButton.addActionListener(new ActionListener() {
      
      /**
       * actionPerformed.
       *
       * @param e Comprueba si el campo está completo y ejecuta el método para borrar el pasajero por Dni.
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        String dni = dniField.getText().trim();

        if (dni.isEmpty()) {
          JOptionPane.showMessageDialog(null, "El campo es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        String mensaje = metodosSQL.borrarPasajeroBD(dni);
        JOptionPane.showMessageDialog(null, mensaje);

        // Reiniciar el campo después de eliminar
        dniField.setText("");
      }
    });
  }
}
