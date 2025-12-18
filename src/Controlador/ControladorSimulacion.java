package Controlador;

import Modelo.Carrera;
import Modelo.Participante;
import Modelo.ResultadoCarrera;
import Vista.SimulacionCarrera;

import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class ControladorSimulacion {

    private Timer timer;
    private SimulacionCarrera vista;
    private List<Participante> participantes;
    private Carrera carrera;
    private List<ResultadoCarrera> resultados = new ArrayList<>();
    private Set<Participante> yaLlegaron = new HashSet<>();
    private long inicioCarrera;

    private ControladorPrincipal controladorPrincipal;

    public ControladorSimulacion(
            SimulacionCarrera vista,
            List<Participante> participantes,
            Carrera carrera,
            ControladorPrincipal controladorPrincipal) {

        this.vista = vista;
        this.participantes = participantes;
        this.carrera = carrera;
        this.controladorPrincipal = controladorPrincipal;

        timer = new Timer(100, e -> avanzar());
    }

    public void iniciar() {
        inicioCarrera = System.currentTimeMillis();
        timer.start();
    }

    private void avanzar() {
        Random r = new Random();

        long tiempo = System.currentTimeMillis() - inicioCarrera;

        for (int i = 0; i < participantes.size(); i++) {

            if (yaLlegaron.contains(participantes.get(i))) continue;

            int avance = r.nextInt(10) + 1;
            vista.avanzarPato(i, avance);

            if (vista.getPosicionX(i) >= vista.getMeta()) {



                yaLlegaron.add(participantes.get(i));
                resultados.add(
                        new ResultadoCarrera(
                                carrera,
                                participantes.get(i),
                                resultados.size() + 1,
                                tiempo
                        )
                );

                if (resultados.size() == 3) {
                    timer.stop();
                    mostrarPodio();
                }

            }

        }
    }

    private void mostrarPodio() {

        String texto = " PODIO \n\n";

        for (ResultadoCarrera r : resultados) {
            texto += r.getPosicion() + "° - "
                    + r.getParticipante().getNombre() + "\n";
        }

        JOptionPane.showMessageDialog(null, texto);

        // Guardar resultados
        controladorPrincipal.agregarResultados(resultados);
    }

    public List<ResultadoCarrera> getResultados(){
        return resultados;
    }
}