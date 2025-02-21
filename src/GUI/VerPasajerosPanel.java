package GUI;

import javax.swing.*;

import Métodos.metodosSQL;

import java.awt.*;
import java.awt.event.*;

public class VerPasajerosPanel extends BackgroundPanel {
    public VerPasajerosPanel(String imagePath) {
        super(imagePath);
        setLayout(new FlowLayout(FlowLayout.CENTER)); // Establecer el diseño de FlowLayout y centrarlo

        JButton botonVerTodos = new JButton("Ver todos los pasajeros");
        botonVerTodos.setPreferredSize(new Dimension(200, 30)); // Establecer el tamaño preferido del botón
        botonVerTodos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (autenticarUsuario()) {
                   String resultado = metodosSQL.verPasajeros();
                    JOptionPane.showMessageDialog(null, resultado, "Listado de pasajeros", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(botonVerTodos); // Agregar el botón al panel
    }

    private boolean autenticarUsuario() {
        // Panel de autenticación personalizado
        JPanel panelAutenticacion = new JPanel(new GridLayout(2, 2));
        JTextField usuarioField = new JTextField();
        JPasswordField contraseñaField = new JPasswordField();
        panelAutenticacion.add(new JLabel("Usuario:"));
        panelAutenticacion.add(usuarioField);
        panelAutenticacion.add(new JLabel("Contraseña:"));
        panelAutenticacion.add(contraseñaField);

        int resultado = JOptionPane.showConfirmDialog(null, panelAutenticacion, "Autenticación", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado == JOptionPane.OK_OPTION) {
            // Verificar el usuario y la contraseña
            String usuarioCorrecto = "admin";
            String contraseñaCorrecta = "admin123";
            String usuarioIngresado = usuarioField.getText();
            String contraseñaIngresada = new String(contraseñaField.getPassword());
            return usuarioCorrecto.equals(usuarioIngresado) && contraseñaCorrecta.equals(contraseñaIngresada);
        }
        return false;
    }
}