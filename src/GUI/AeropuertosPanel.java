package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AeropuertosPanel extends BackgroundPanel {

    public AeropuertosPanel(String imagePath) {
	super("/imagen/fondo.jpg");
	setLayout(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(5, 5, 5, 5);

	// Botones para insertar, modificar, eliminar y listar aeropuertos
	JButton insertarButton = new JButton("Insertar Aeropuerto");
	insertarButton.setPreferredSize(new Dimension(180, 40)); // Ajustar tamaño
	insertarButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		abrirVentanaInsertarAeropuerto();
	    }
	});
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.anchor = GridBagConstraints.CENTER;
	add(insertarButton, gbc);

	JButton modificarButton = new JButton("Modificar Aeropuerto");
	modificarButton.setPreferredSize(new Dimension(180, 40)); // Ajustar tamaño
	modificarButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		abrirVentanaModificarAeropuerto();
	    }
	});
	gbc.gridy = 1;
	add(modificarButton, gbc);

	JButton eliminarButton = new JButton("Eliminar Aeropuerto");
	eliminarButton.setPreferredSize(new Dimension(180, 40)); // Ajustar tamaño
	eliminarButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		abrirVentanaEliminarAeropuerto();
	    }
	});
	gbc.gridy = 2;
	add(eliminarButton, gbc);

	JButton listarButton = new JButton("Listar Aeropuertos");
	listarButton.setPreferredSize(new Dimension(180, 40)); // Ajustar tamaño
	listarButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		abrirVentanaListarVuelos();
	    }
	});
	gbc.gridy = 3;
	add(listarButton, gbc);
    }

    private void abrirVentanaInsertarAeropuerto() {
	// Crear ventana para insertar aeropuerto
	JFrame insertarFrame = new JFrame("Insertar Aeropuerto");
	insertarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	insertarFrame.setSize(300, 180); // Tamaño ajustado
	insertarFrame.setLocationRelativeTo(null); // Centrar la ventana

	// Panel para los campos de inserción
	JPanel insertarPanel = new JPanel(new GridBagLayout());
	insertarPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir bordes
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(5, 5, 5, 5);

	JLabel nombreLabel = new JLabel("Nombre:");
	JTextField nombreField = new JTextField(15); // Tamaño ajustado
	JLabel ciudadLabel = new JLabel("Ciudad:");
	JTextField ciudadField = new JTextField(15); // Tamaño ajustado

	gbc.gridx = 0;
	gbc.gridy = 0;
	insertarPanel.add(nombreLabel, gbc);
	gbc.gridx = 1;
	insertarPanel.add(nombreField, gbc);

	gbc.gridx = 0;
	gbc.gridy = 1;
	insertarPanel.add(ciudadLabel, gbc);
	gbc.gridx = 1;
	insertarPanel.add(ciudadField, gbc);

	JButton insertarButton = new JButton("Insertar");
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.gridwidth = 2;
	insertarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	insertarButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// Validación de campos vacíos
		if (nombreField.getText().trim().isEmpty() || ciudadField.getText().trim().isEmpty()) {
		    JOptionPane.showMessageDialog(insertarFrame, "Todos los campos son obligatorios.", "Error",
			    JOptionPane.ERROR_MESSAGE);
		    return;
		}
		// Lógica para insertar el aeropuerto
		String nombre = nombreField.getText();
		String ciudad = ciudadField.getText();
		String resultado = Métodos.metodosSQL.insertarAeropuertoBD(nombre, ciudad);
		// Mostrar mensaje de resultado
		JOptionPane.showMessageDialog(insertarFrame, resultado, "Resultado de Inserción",
			JOptionPane.INFORMATION_MESSAGE);
		// Cerrar la ventana después de la inserción
		insertarFrame.dispose();
	    }
	});

	insertarPanel.add(insertarButton, gbc); // Añadir el botón al panel

	insertarFrame.getContentPane().add(insertarPanel, BorderLayout.CENTER);
	insertarFrame.setVisible(true);
    }

    private void abrirVentanaEliminarAeropuerto() {
	// Crear ventana para eliminar aeropuerto
	JFrame eliminarFrame = new JFrame("Eliminar Aeropuerto");
	eliminarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	eliminarFrame.setSize(250, 120);
	eliminarFrame.setLocationRelativeTo(null); // Centrar la ventana

	// Panel para los campos de eliminación
	JPanel eliminarPanel = new JPanel(new GridBagLayout());
	eliminarPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir bordes
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.insets = new Insets(5, 5, 5, 5);

	JLabel nombreLabel = new JLabel("Nombre Aeropuerto:");
	JComboBox<String> nombreComboBox = new JComboBox<>();

	// Llenar el JComboBox con la lista de aeropuertos
	try {
	    Connection cnx = Métodos.metodosSQL.getConnection();
	    String query = "SELECT nombre FROM aeropuertos";
	    Statement stm = cnx.createStatement();
	    ResultSet rs = stm.executeQuery(query);
	    while (rs.next()) {
		nombreComboBox.addItem(rs.getString("nombre"));
	    }
	    rs.close();
	    stm.close();
	    cnx.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	gbc.gridx = 0;
	gbc.gridy = 0;
	eliminarPanel.add(nombreLabel, gbc);
	gbc.gridx = 1;
	eliminarPanel.add(nombreComboBox, gbc);

	JButton eliminarButton = new JButton("Eliminar");
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.gridwidth = 2;
	eliminarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	eliminarButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		String nombre = (String) nombreComboBox.getSelectedItem();
		if (nombre == null || nombre.trim().isEmpty()) {
		    JOptionPane.showMessageDialog(eliminarFrame, "Debe seleccionar un aeropuerto.", "Error",
			    JOptionPane.ERROR_MESSAGE);
		    return;
		}
		// Lógica para eliminar el aeropuerto
		String resultado = Métodos.metodosSQL.borrarAeropuertoBDPorNombre(nombre);
		// Mostrar mensaje de resultado
		JOptionPane.showMessageDialog(eliminarFrame, resultado, "Resultado de Eliminación",
			JOptionPane.INFORMATION_MESSAGE);
		// Cerrar la ventana después de la eliminación
		eliminarFrame.dispose();
	    }
	});

	eliminarPanel.add(eliminarButton, gbc); // Añadir el botón al panel

	eliminarFrame.getContentPane().add(eliminarPanel, BorderLayout.CENTER);
	eliminarFrame.setVisible(true);

    }

    private void abrirVentanaListarVuelos() {
	// Crear ventana para listar vuelos
	JFrame listarFrame = new JFrame("Aeropuertos");
	listarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	listarFrame.setSize(300, 200);
	listarFrame.setLocationRelativeTo(null);

	JTextArea textArea = new JTextArea();
	textArea.setEditable(false);
	JScrollPane scrollPane = new JScrollPane(textArea);
	scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

	listarAeropuertos(textArea);

	listarFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
	listarFrame.setVisible(true);
    }

    private void listarAeropuertos(JTextArea textArea) {
	try {
	    Connection cnx = Métodos.metodosSQL.getConnection();
	    String query = "SELECT * FROM aeropuertos";
	    Statement stm = cnx.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	    ResultSet rs = stm.executeQuery(query);
	    StringBuilder sb = new StringBuilder();
	    while (rs.next()) {
		int id = rs.getInt("id_aeropuerto");
		String nombre = rs.getString("nombre");
		String ciudad = rs.getString("ciudad");

		sb.append("ID: ").append(id).append(", Nombre: ").append(nombre).append(", Ciudad: ").append(ciudad)
			.append("\n");
	    }
	    textArea.setText(sb.toString());
	    stm.close();
	    cnx.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    textArea.setText("Error al obtener la lista de aeropuertos.");
	}
    }

    private void abrirVentanaModificarAeropuerto() {
	    // Crear ventana para modificar aeropuerto
	    JFrame modificarFrame = new JFrame("Modificar Aeropuerto");
	    modificarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    modificarFrame.setSize(400, 250); // Tamaño ajustado
	    modificarFrame.setLocationRelativeTo(null); // Centrar la ventana

	    // Panel para los campos de modificación
	    JPanel modificarPanel = new JPanel(new GridBagLayout());
	    modificarPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir bordes
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    JLabel nombreAntiguoLabel = new JLabel("Aeropuerto a modificar:");
	    JComboBox<String> nombreComboBox = new JComboBox<>();

	    // Llenar el JComboBox con la lista de aeropuertos
	    try {
	        Connection cnx = Métodos.metodosSQL.getConnection();
	        String query = "SELECT nombre FROM aeropuertos";
	        Statement stm = cnx.createStatement();
	        ResultSet rs = stm.executeQuery(query);
	        while (rs.next()) {
	            nombreComboBox.addItem(rs.getString("nombre"));
	        }
	        rs.close();
	        stm.close();
	        cnx.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    JLabel nuevoNombreLabel = new JLabel("Nuevo nombre:");
	    JTextField nuevoNombreField = new JTextField(); // Tamaño ajustado
	    nuevoNombreField.setColumns(20); // Establecer el ancho del campo de texto

	    JLabel nuevaCiudadLabel = new JLabel("Nueva ciudad:");
	    JTextField nuevaCiudadField = new JTextField(); // Tamaño ajustado
	    nuevaCiudadField.setColumns(20); // Establecer el ancho del campo de texto

	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    modificarPanel.add(nombreAntiguoLabel, gbc);
	    gbc.gridx = 1;
	    modificarPanel.add(nombreComboBox, gbc);

	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    modificarPanel.add(nuevoNombreLabel, gbc);
	    gbc.gridx = 1;
	    modificarPanel.add(nuevoNombreField, gbc);

	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    modificarPanel.add(nuevaCiudadLabel, gbc);
	    gbc.gridx = 1;
	    modificarPanel.add(nuevaCiudadField, gbc);

	    JButton modificarButton = new JButton("Modificar");
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gbc.gridwidth = 2;
	    modificarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    modificarButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String nombreAntiguo = (String) nombreComboBox.getSelectedItem();
	            String nuevoNombre = nuevoNombreField.getText();
	            String nuevaCiudad = nuevaCiudadField.getText();
	            if (nombreAntiguo == null || nombreAntiguo.trim().isEmpty() || nuevoNombre.trim().isEmpty()
	                    || nuevaCiudad.trim().isEmpty()) {
	                JOptionPane.showMessageDialog(modificarFrame, "Todos los campos son obligatorios.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            String resultado = Métodos.metodosSQL.modificarAeropuertoBD(nombreAntiguo, nuevoNombre, nuevaCiudad);
	            JOptionPane.showMessageDialog(modificarFrame, resultado, "Resultado de Modificación",
	                    JOptionPane.INFORMATION_MESSAGE);
	            modificarFrame.dispose();
	        }
	    });

	    modificarPanel.add(modificarButton, gbc); // Añadir el botón al panel

	    modificarFrame.getContentPane().add(modificarPanel, BorderLayout.CENTER);
	    modificarFrame.setVisible(true);
	}
}
