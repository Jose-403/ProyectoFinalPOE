package Vista;

import Controlador.ControladorPrincipal;
import Modelo.Carrera;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegistroPrincipal extends JFrame {
    private JPanel panel1;
    private JTextField tfNombreP;
    private JTextField tfDocumentoP;
    private JTextField tfEdadP;
    private JTextField tfNumPato;
    private JButton btnRegistrarP;
    private JTable table1;
    private JPanel panelP;
    private JButton btnRegresar;
    private JTextField tfNombreC;
    private JComboBox cmbCategoriaC;
    private JTextField tfFecha;
    private JTable table2;
    private JButton btnRegistrarC;
    private JButton btnEliminar;
    private JComboBox<Carrera> cmbCarreras;
    private ControladorPrincipal controladorPrincipal;

    private ControladorPrincipal controladorP;

    public RegistroPrincipal(ControladorPrincipal controladorP) {
        setTitle("Área de registro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setContentPane(panelP);
        this.controladorPrincipal = controladorPrincipal;
        cmbCarreras = new JComboBox<>();



        // ----Modelo de tabla de participantes----
        table1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Nombre", "Documento", "Edad", "Categoría", "Pato"}
        ));

        // ----Modelo de tabla de participantes por categoria----
        table2.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Nombre", "Documento", "Edad", "Categoría", "Pato"}
        ));

        cmbCategoriaC.setModel(new DefaultComboBoxModel<>(
                new String[]{"Niño", "Joven", "Adulto"}
        ));


        // ----Bloquear signo "-" para tfEdadP----
        tfEdadP.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '-') {
                    e.consume();
                }
            }
        });

        tfEdadP.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                field.setText(field.getText().replace("-", ""));
                return true;
            }
        });

        // ---- Bloquear signo "-" paratfNumPato ----
        tfNumPato.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '-') {
                    e.consume();
                }
            }
        });

        tfNumPato.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                field.setText(field.getText().replace("-", ""));
                return true;
            }
        });

        new ControladorPrincipal(this);
    }

    //----Getter de participante----
    public JButton getBtnRegistrarP() {
        return btnRegistrarP;
    }
    public JButton getBtnRegresar() {
        return btnRegresar;
    }
    public String getNombreP() {
        return tfNombreP.getText();
    }
    public String getDocumento() {
        return tfDocumentoP.getText();
    }
    public JTextField getEdadField() {
        return tfEdadP;
    }
    public JTextField getNumPatoField() {
        return tfNumPato;
    }
    public JTable getTabla() {
        return table1;
    }

    //----Getters de carrera----
    public String getNombreC() {
        return tfNombreC.getText();
    }
    public JComboBox getCmbCategoriaC() {
        return cmbCategoriaC;
    }
    public JComboBox<Carrera> getCmbCarreras() {
        return cmbCarreras;
    }
    public String getFecha() {
        return tfFecha.getText();
    }
    public JTable getTable2() {
        return table2;
    }
    public JButton getBtnRegistrarC() {
        return btnRegistrarC;
    }
    public JButton getBtnEliminar() {
        return btnEliminar;
    }




}
