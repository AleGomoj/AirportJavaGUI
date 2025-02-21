package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de gestión de vuelos
 */
public class VuelosPanel extends JPanel {
    
    /**
     * Inicia el panel de vuelos
     *
     * @param imagenDeFondo rutua de la imagen de fondo
     */
    public VuelosPanel(String imagenDeFondo) {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Insertar Vuelo", new InsertarVueloPanel(imagenDeFondo));
        tabbedPane.addTab("Eliminar Vuelo", new EliminarVueloPanel(imagenDeFondo));
        tabbedPane.addTab("Modificar Vuelo", new ModificarVueloPanel(imagenDeFondo));
        tabbedPane.addTab("Buscar Vuelo", new BuscarVueloPanel(imagenDeFondo));

        add(tabbedPane, BorderLayout.CENTER);
    }
}
