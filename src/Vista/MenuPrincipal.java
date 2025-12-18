package Vista;

import Controlador.ControladorPrincipal;
import Modelo.Carrera;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {

    private JPanel mainPanel;
    private JPanel panel1;
    private JButton INICIARCARRERAButton;
    private JButton REGISTRARCOMPETIDORButton;
    private JButton RESULTADOSButton;
    private JButton ESTADISTICASButton;


    // Constructor que recibe el controlador
    public MenuPrincipal(ControladorPrincipal controladorPrincipal) {


        setTitle("Duck App Pro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);


        //----Actions Listeneres de los botones principales----

        INICIARCARRERAButton.addActionListener(e ->
                controladorPrincipal.iniciarCarreraDesdeMenu()
        );

        REGISTRARCOMPETIDORButton.addActionListener(e -> {
            RegistroPrincipal vista = new RegistroPrincipal(controladorPrincipal);
            vista.setVisible(true);
            dispose();
        });


        RESULTADOSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controladorPrincipal.mostrarHistorialResultados();

            }
        });

        ESTADISTICASButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //controladorPrincipal.patoMasRapido();
                //controladorPrincipal.participanteConMasPodios();
                controladorPrincipal.exportarResultadosTXT();
            }
        });
    }

    //----Metodo main----

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            ControladorPrincipal controlador = new ControladorPrincipal();
            MenuPrincipal menu = new MenuPrincipal(controlador);
            menu.setVisible(true);

        });
    }
}
