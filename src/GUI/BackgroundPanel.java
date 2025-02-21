package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Clase BackgroundPanel, para el fondo.
 */
public class BackgroundPanel extends JPanel {
    
    /** Imagen de fondo. */
    private Image backgroundImage;

    /**
     * Inicio el panel del fondo de pantalla.
     *
     * @param imagePath the image path
     */
    public BackgroundPanel(String imagePath) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(imagePath);
            backgroundImage = ImageIO.read(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Paint component
     * Dibuja la imagen
     *
     * @param g Parametro para el método necesario para invocar al super.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
