package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class TranslucentPanel extends JPanel {
    private int alpha = 185; // Default translucent opacity

    public TranslucentPanel() {
        setOpaque(false);
    }

    public TranslucentPanel(int alpha) {
        this.alpha = alpha;
        setOpaque(false);
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color bg = getBackground();
        if (bg != null) {
            // Draw a translucent rectangle matching the background color and alpha
            g2d.setColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), alpha));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        g2d.dispose();
    }
}
