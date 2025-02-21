package GUI;

import javax.swing.*;

import Métodos.metodosSQL;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * @author Alejandro Gómez Ojeda
 * @version 31/05/2024
 */
public class MainGUI extends JFrame {
    /**
     * Inicia la interfaz gráfica.
     */
    public MainGUI() {

	setTitle("Gestión de Vuelos y Aeropuertos");
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	setSize(600, 400);
	setLocationRelativeTo(null);
	setLayout(new BorderLayout());

	String imagenDeFondo = "/imagen/fondo.jpg";
	JTabbedPane tabbedPane = new JTabbedPane();
	tabbedPane.addTab("Aeropuertos", new AeropuertosPanel(imagenDeFondo));
	tabbedPane.addTab("Vuelos", new VuelosPanel(imagenDeFondo));
	tabbedPane.addTab("Pasajeros", new PasajerosPanel(imagenDeFondo));
	add(tabbedPane, BorderLayout.CENTER);

	addWindowListener(new WindowAdapter() {

	    public void windowClosing(WindowEvent e) {
		int response = JOptionPane.showConfirmDialog(MainGUI.this, "¿Está seguro de que desea salir?",
			"Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
		    dispose();
		}
	    }
	});
    }

    public static void main(String[] args) {
	metodosSQL.prepararBaseDatos();
	SwingUtilities.invokeLater(() -> {
	    new MainGUI().setVisible(true);
	});
    }
}
