// File: src/view/GradientPanel.java
package PBO_4C_SI_KELOMPOK_7.view;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private Color color1;
    private Color color2;
    private boolean horizontal;

    public GradientPanel(Color color1, Color color2, boolean horizontal) {
        this.color1 = color1;
        this.color2 = color2;
        this.horizontal = horizontal;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();

        GradientPaint gp;
        if (horizontal) {
            gp = new GradientPaint(0, 0, color1, w, 0, color2);
        } else {
            gp = new GradientPaint(0, 0, color1, 0, h, color2);
        }
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}