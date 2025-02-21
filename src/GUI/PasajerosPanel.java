package GUI;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Panel de gestión de pasajeros
 */
public class PasajerosPanel extends JPanel {

    /**
     * Inicia el panel de pasajeros
     *
     * @param imagenDeFondo ruta de la imagen de fondo
     */
    public PasajerosPanel(String imagenDeFondo) {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Insertar Pasajero", new InsertarPasajeroPanel(imagenDeFondo));
        tabbedPane.addTab("Eliminar Pasajero", new EliminarPasajeroPanel(imagenDeFondo));
        tabbedPane.addTab("Modificar Pasajero", new ModificarPasajeroPanel(imagenDeFondo));
        tabbedPane.addTab("Ver Pasajeros", new VerPasajerosPanel(imagenDeFondo));

        add(tabbedPane, BorderLayout.CENTER);
    }
}
