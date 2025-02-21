package GUI;

import javax.swing.*;
import Métodos.metodosSQL;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class BuscarVueloPanel extends BackgroundPanel {

    public BuscarVueloPanel(String imagePath) {
        super(imagePath);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton buscarPorIDButton = new JButton("Buscar por ID");
        buscarPorIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaBuscarPorID();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buscarPorIDButton, gbc);

        JButton buscarPorOrigenYDestinoButton = new JButton("Buscar por Origen y Destino");
        buscarPorOrigenYDestinoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaBuscarPorOrigenYDestino();
            }
        });
        gbc.gridy = 1;
        add(buscarPorOrigenYDestinoButton, gbc);
    }

    private void abrirVentanaBuscarPorID() {
        JFrame frame = new JFrame("Buscar por ID de Vuelo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new BuscarPorIDPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void abrirVentanaBuscarPorOrigenYDestino() {
        JFrame frame = new JFrame("Buscar por Origen y Destino");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new BuscarPorOrigenYDestinoPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class BuscarPorIDPanel extends JPanel {
        private JTextField idVueloField;
        private JTextArea resultadoArea;

        public BuscarPorIDPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            JLabel idVueloLabel = new JLabel("ID Vuelo:");
            idVueloField = new JTextField(15);
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(idVueloLabel, gbc);
            gbc.gridx = 1;
            add(idVueloField, gbc);

            JButton buscarButton = new JButton("Buscar Vuelo");
            buscarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buscarVueloPorID();
                }
            });
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            add(buscarButton, gbc);

            resultadoArea = new JTextArea(10, 30);
            resultadoArea.setLineWrap(true);
            resultadoArea.setWrapStyleWord(true);
            resultadoArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(resultadoArea);
            gbc.gridy = 2;
            add(scrollPane, gbc);

            JButton insertarPasajeroButton = new JButton("Insertar Pasajero");
            insertarPasajeroButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    abrirVentanaInsertarPasajero();
                }
            });
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            add(insertarPasajeroButton, gbc);
        }

        private void buscarVueloPorID() {
            int idVuelo;
            try {
                idVuelo = Integer.parseInt(idVueloField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un ID de vuelo válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String resultado = metodosSQL.buscarVueloPorID(idVuelo);
            resultadoArea.setText(resultado);
        }

        private void abrirVentanaInsertarPasajero() {
            JFrame frame = new JFrame("Insertar Pasajero");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new InsertarPasajeroPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        private class InsertarPasajeroPanel extends JPanel {
            private JTextField dniField;
            private JTextField nombreField;
            private JTextField edadField;

            public InsertarPasajeroPanel() {
                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 5, 5);

                JLabel dniLabel = new JLabel("DNI:");
                dniField = new JTextField(15);
                gbc.gridx = 0;
                gbc.gridy = 0;
                add(dniLabel, gbc);
                gbc.gridx = 1;
                add(dniField, gbc);

                JLabel nombreLabel = new JLabel("Nombre:");
                nombreField = new JTextField(15);
                gbc.gridy = 1;
                gbc.gridx = 0;
                add(nombreLabel, gbc);
                gbc.gridx = 1;
                add(nombreField, gbc);

                JLabel edadLabel = new JLabel("Edad:");
                edadField = new JTextField(15);
                gbc.gridy = 2;
                gbc.gridx = 0;
                add(edadLabel, gbc);
                gbc.gridx = 1;
                add(edadField, gbc);

                JButton confirmarButton = new JButton("Confirmar Inserción");
                confirmarButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        insertarPasajero();
                    }
                });
                gbc.gridy = 3;
                gbc.gridx = 0;
                gbc.gridwidth = 2;
                add(confirmarButton, gbc);
            }

            private void insertarPasajero() {
                String dni = dniField.getText().trim();
                String nombre = nombreField.getText().trim();
                int edad;
                try {
                    edad = Integer.parseInt(edadField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese una edad válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idVuelo;
                try {
                    idVuelo = Integer.parseInt(idVueloField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID de vuelo no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String resultado = metodosSQL.insertarPasajeroBD(dni, nombre, edad, idVuelo);
                JOptionPane.showMessageDialog(null, resultado, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class BuscarPorOrigenYDestinoPanel extends JPanel {
        private JComboBox<String> origenComboBox;
        private JComboBox<String> destinoComboBox;

        public BuscarPorOrigenYDestinoPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);

            JLabel origenLabel = new JLabel("Origen:");
            origenComboBox = new JComboBox<>();
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(origenLabel, gbc);
            gbc.gridx = 1;
            add(origenComboBox, gbc);

            JLabel destinoLabel = new JLabel("Destino:");
            destinoComboBox = new JComboBox<>();
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(destinoLabel, gbc);
            gbc.gridx = 1;
            add(destinoComboBox, gbc);

            JButton buscarButton = new JButton("Buscar Vuelo");
            buscarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buscarVueloPorOrigenYDestino();
                }
            });
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            add(buscarButton, gbc);

            actualizarAeropuertos();

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentShown(ComponentEvent e) {
                    super.componentShown(e);
                    actualizarAeropuertos();
                }
            });
        }

        private void buscarVueloPorOrigenYDestino() {
            String origen = (String) origenComboBox.getSelectedItem();
            String destino = (String) destinoComboBox.getSelectedItem();

            if (origen == null || destino == null) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione ambos campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String resultado = metodosSQL.buscarVueloPorOrigenYDestino(origen, destino);

            if (resultado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se ha encontrado ningún vuelo con esos parámetros.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JTextArea resultadoArea = new JTextArea(resultado);
                resultadoArea.setLineWrap(true);
                resultadoArea.setWrapStyleWord(true);
                resultadoArea.setEditable(false);

                JFrame resultadoFrame = new JFrame("Resultado de Búsqueda");
                resultadoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resultadoFrame.getContentPane().add(new JScrollPane(resultadoArea));
                resultadoFrame.setSize(400, 300);
                resultadoFrame.setLocationRelativeTo(null);
                resultadoFrame.setVisible(true);
            }
        }

        private void actualizarAeropuertos() {
            List<String> aeropuertos = metodosSQL.obtenerNombresAeropuertos();
            origenComboBox.setModel(new DefaultComboBoxModel<>(aeropuertos.toArray(new String[0])));
            destinoComboBox.setModel(new DefaultComboBoxModel<>(aeropuertos.toArray(new String[0])));
        }
    }
}
