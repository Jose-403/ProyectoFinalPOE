package Vista;

import Modelo.Participante;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class SimulacionCarrera extends JPanel {

    private List<Participante> participantes;
    private int[] posicionesX;
    private final int META = 700;
    private BufferedImage spritePato;

    public SimulacionCarrera(List<Participante> participantes) {
        this.participantes = participantes;
        this.posicionesX = new int[participantes.size()];

        try {
            spritePato = ImageIO.read(
                    getClass().getResource("/Sprites/pato.png")
            );
        } catch (IOException | IllegalArgumentException e) {
            spritePato = null;
        }
    }

    public void avanzarPato(int index, int avance) {
        posicionesX[index] += avance;
        repaint();
    }

    public int getPosicionX(int index) {
        return posicionesX[index];
    }
    public int getMeta() {
        return META;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ----Pinta la meta---
        g.setColor(Color.RED);
        g.fillRect(META, 0, 5, getHeight());

        // ----Pinta la pista y cargar el sprite del pato----
        for (int i = 0; i < participantes.size(); i++) {
            int y = 50 + i * 50;

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, y + 15, META, 5);

            if (spritePato != null) {
                g.drawImage(spritePato, posicionesX[i], y, 40, 40, this);
            } else {
                g.setColor(Color.BLUE);
                g.fillOval(posicionesX[i], y, 30, 30);
            }

            g.setColor(Color.BLACK);
            g.drawString(participantes.get(i).getNombre(), 10, y - 5);
        }
    }
}
