package Controlador;

import Modelo.Carrera;
import Modelo.Participante;
import Modelo.ResultadoCarrera;
import Vista.MenuPrincipal;
import Vista.RegistroPrincipal;
import Vista.SimulacionCarrera;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class ControladorPrincipal {
    private RegistroPrincipal vistaP;

    private List<Participante> listaParticipantes;
    private List<Carrera> listaCarreras;
    private List<ResultadoCarrera> historialResultados;

    // ----Constructores----

    // Constructor base para cargar los datos
    public ControladorPrincipal() {
        cargarParticipantes();
        cargarCarreras();
        cargarResultados();
    }

    // Constructor con vista
    public ControladorPrincipal(RegistroPrincipal vistaP) {
        this();
        this.vistaP = vistaP;

        // ----- Validaciones ----
        vistaP.getEdadField().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });

        vistaP.getNumPatoField().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
        });

        //----Conexión de botones con metodos----
        vistaP.getBtnRegistrarP().addActionListener(e -> validaciones());
        vistaP.getBtnRegistrarC().addActionListener(e -> validacionesC());
        vistaP.getBtnRegresar().addActionListener(e -> volver());
        vistaP.getCmbCategoriaC().addActionListener(e -> actualizarTablaCarrera());
        vistaP.getBtnEliminar().addActionListener(e -> eliminarParticipante());


        actualizarTabla();
        actualizarTablaCarrera();
        cargarCarrerasCombo();
    }

    //----Persistencia de datos----

    @SuppressWarnings("unchecked")
    private void cargarParticipantes() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream("participantes.dat"))) {
            listaParticipantes = (List<Participante>) ois.readObject();
        } catch (Exception e) {
            listaParticipantes = new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarCarreras() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("carreras.dat"))) {

            listaCarreras = (List<Carrera>) ois.readObject();

            for (Carrera c : listaCarreras) {
                if (c.getParticipantes() == null) {
                    c.setParticipantes(new ArrayList<>());
                }
            }

        } catch (Exception e) {
            listaCarreras = new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarResultados() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream("resultados.dat"))) {
            historialResultados = (List<ResultadoCarrera>) ois.readObject();
        } catch (Exception e) {
            historialResultados = new ArrayList<>();
        }
    }

    private void guardarParticipantes() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream("participantes.dat"))) {
            oos.writeObject(listaParticipantes);
        } catch (Exception ignored) {}
    }

    private void guardarCarreras() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream("carreras.dat"))) {
            oos.writeObject(listaCarreras);
        } catch (Exception ignored) {}
    }

    private void guardarResultados() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream("resultados.dat"))) {
            oos.writeObject(historialResultados);
        } catch (Exception ignored) {}
    }

    // ----Metodos de participante----

    private void validaciones() {
        String nombre = vistaP.getNombreP().trim();
        String documento = vistaP.getDocumento().trim();
        String edadStr = vistaP.getEdadField().getText().trim();
        String numPatoStr = vistaP.getNumPatoField().getText().trim();

        //----1. Verifica que se llenen todos los campos---
        if (nombre.isEmpty() || documento.isEmpty() ||
                edadStr.isEmpty() || numPatoStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
            return;
        }

        int edad = Integer.parseInt(edadStr);
        int numPato = Integer.parseInt(numPatoStr);

        //----2. Verifica que no se repita documento---
        for (Participante p : listaParticipantes) {
            if (p.getDocumento().equals(documento)) {
                JOptionPane.showMessageDialog(null, "Documento duplicado.");
                return;
            }
        }

        //----3. Valida los datos y agrega correctamente----
        Participante p = new Participante(nombre, documento, edad, numPato);
        listaParticipantes.add(p);
        guardarParticipantes();
        actualizarTabla();

        JOptionPane.showMessageDialog(null, "Participante registrado.");
    }

    //----Se llena la tabla con los datos registrados----
    private void actualizarTabla() {
        DefaultTableModel model =
                (DefaultTableModel) vistaP.getTabla().getModel();
        model.setRowCount(0);

        for (Participante p : listaParticipantes) {
            model.addRow(new Object[]{
                    p.getNombre(),
                    p.getDocumento(),
                    p.getEdad(),
                    p.getCategoria(),
                    p.getNumPato()
            });
        }
    }

    //----Elimina un participante de la tabla----
    private void eliminarParticipante() {

        int fila = vistaP.getTabla().getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione un participante."
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de eliminar este participante?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        String documento =
                vistaP.getTabla().getValueAt(fila, 1).toString();

        Participante eliminado = null;

        Iterator<Participante> it = listaParticipantes.iterator();
        while (it.hasNext()) {
            Participante p = it.next();
            if (p.getDocumento().equals(documento)) {
                eliminado = p;
                it.remove();
                break;
            }
        }

        if (eliminado == null) return;

        for (Carrera c : listaCarreras) {
            c.getParticipantes().remove(eliminado);
        }

        guardarParticipantes();
        guardarCarreras();
        actualizarTabla();
        actualizarTablaCarrera();

        JOptionPane.showMessageDialog(
                null,
                "Participante eliminado correctamente."
        );
    }


    //----Metodos de la carrera----

    private void validacionesC() {
        String nombre = vistaP.getNombreC();
        String fecha = vistaP.getFecha();
        String categoria = vistaP.getCmbCategoriaC().getSelectedItem().toString();

        if (nombre.isEmpty() || fecha.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campos vacíos.");
            return;
        }

        for (Carrera c : listaCarreras) {
            if (c.getNombre().equals(nombre)) {
                JOptionPane.showMessageDialog(null, "Carrera duplicada.");
                return;
            }
        }

        Carrera c = new Carrera(nombre, fecha, categoria);
        listaCarreras.add(c);
        guardarCarreras();

        vistaP.getCmbCarreras().addItem(c);
        actualizarTablaCarrera();

        JOptionPane.showMessageDialog(null, "Carrera registrada.");
    }

    private void actualizarTablaCarrera() {
        String categoria =
                vistaP.getCmbCategoriaC().getSelectedItem().toString();

        DefaultTableModel model =
                (DefaultTableModel) vistaP.getTable2().getModel();
        model.setRowCount(0);

        for (Participante p : listaParticipantes) {
            if (p.getCategoria().equals(categoria)) {
                model.addRow(new Object[]{
                        p.getNombre(),
                        p.getDocumento(),
                        p.getEdad(),
                        p.getCategoria(),
                        p.getNumPato()
                });
            }
        }
    }

    private void cargarCarrerasCombo() {
        for (Carrera c : listaCarreras) {
            vistaP.getCmbCarreras().addItem(c);
        }
    }

    // ----Metodos de la simulación----

    public void agregarResultados(List<ResultadoCarrera> resultados) {
        historialResultados.addAll(resultados);
        guardarResultados();
    }

    public void iniciarCarreraDesdeMenu() {

        if (listaCarreras.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No hay carreras registradas."
            );
            return;
        }

        String nombreCarrera = JOptionPane.showInputDialog(
                null,
                "Ingrese el nombre de la carrera a iniciar:"
        );

        if (nombreCarrera == null || nombreCarrera.trim().isEmpty()) {
            return;
        }

        Carrera carreraSeleccionada = null;

        for (Carrera c : listaCarreras) {
            if (c.getNombre().equalsIgnoreCase(nombreCarrera.trim())) {
                carreraSeleccionada = c;
                break;
            }
        }

        if (carreraSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Carrera no encontrada."
            );
            return;
        }

        carreraSeleccionada.getParticipantes().clear();

        for (Participante p : listaParticipantes) {
            if (p.getCategoria().equals(carreraSeleccionada.getCategoria())) {
                carreraSeleccionada.getParticipantes().add(p);
            }
        }

        List<Participante> participantesCarrera =
                carreraSeleccionada.getParticipantes();

        if (participantesCarrera.size() < 2) {
            JOptionPane.showMessageDialog(
                    null,
                    "Participantes insuficientes."
            );
            return;
        }

        SimulacionCarrera panel =
                new SimulacionCarrera(participantesCarrera);

        JFrame frame =
                new JFrame("Simulación: " + carreraSeleccionada.getNombre());

        frame.setSize(900, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        ControladorSimulacion controlador =
                new ControladorSimulacion(
                        panel,
                        participantesCarrera,
                        carreraSeleccionada,
                        this
                );

        controlador.iniciar();
    }

    private void volver() {
        MenuPrincipal menu = new MenuPrincipal(this);
        vistaP.dispose();
        menu.setVisible(true);
    }

    // ----Metodos para estadisticas y resultados----
    public Participante participanteConMasPodios() {

        Participante mejor = null;
        int maxPodios = 0;

        for (Participante p : listaParticipantes) {

            int contador = 0;

            for (ResultadoCarrera r : historialResultados) {

                if (r.getParticipante().equals(p) &&
                        r.getPosicion() <= 3) {
                    contador++;
                }
            }

            if (contador > maxPodios) {
                maxPodios = contador;
                mejor = p;
            }
        }

        return mejor;
    }


    public Participante patoMasRapido() {

        Participante mejor = null;
        double mejorPromedio = Double.MAX_VALUE;

        for (Participante p : listaParticipantes) {

            long sumaTiempos = 0;
            int carrerasCorridas = 0;

            for (ResultadoCarrera r : historialResultados) {

                if (r.getParticipante().equals(p)) {
                    sumaTiempos += r.getTiempo();
                    carrerasCorridas++;
                }
            }

            if (carrerasCorridas > 0) {

                double promedio = (double) sumaTiempos / carrerasCorridas;

                if (promedio < mejorPromedio) {
                    mejorPromedio = promedio;
                    mejor = p;
                }
            }
        }

        return mejor;
    }

    public void exportarResultadosTXT() {

        try (PrintWriter pw =
                     new PrintWriter(new FileWriter("resultados_carreras.txt", true))) {

            pw.println("\n===== CARRERA MÁS RECIENTE =====\n");

            for (ResultadoCarrera r : historialResultados) {
                pw.println("Carrera: " + r.getCarrera().getNombre());
                pw.println("Participante: " + r.getParticipante().getNombre());
                pw.println("Posición: " + r.getPosicion());
                pw.println("Tiempo: " + r.getTiempo() + " ms");
                pw.println("-----------------------------");
            }

            pw.println("\n===== ESTADÍSTICAS GENERALES =====");

            Participante masPodios = participanteConMasPodios();
            if (masPodios != null) {
                pw.println("Participante con más podios: " +
                        masPodios.getNombre());
            }

            Participante masRapido = patoMasRapido();
            if (masRapido != null) {
                pw.println("Pato más rápido: " +
                        masRapido.getNombre() +
                        " | Pato #" + masRapido.getNumPato());
            }
            //
            pw.println("===============================\n");

            JOptionPane.showMessageDialog(
                    null,
                    "Resultados exportados correctamente."
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error al exportar archivo."
            );
        }
    }

    public void mostrarHistorialResultados() {

        if (historialResultados.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No hay resultados registrados."
            );
            return;
        }

        String texto = "===== HISTORIAL DE CARRERAS =====\n\n";

        List<Carrera> carrerasMostradas = new ArrayList<>();

        for (ResultadoCarrera r : historialResultados) {

            if (r.getPosicion() == 1 &&
                    !carrerasMostradas.contains(r.getCarrera())) {

                texto += "Carrera: " + r.getCarrera().getNombre() + "\n";
                texto += "Ganador: " + r.getParticipante().getNombre() + "\n";
                texto += "-----------------------------\n";

                carrerasMostradas.add(r.getCarrera());
            }
        }

        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setFont(new java.awt.Font("Monospaced", 0, 12));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(420, 300));

        JOptionPane.showMessageDialog(
                null,
                scroll,
                "Resultados de Carreras",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
