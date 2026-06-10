package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class ViewUtils {

    public static class TranslucentButtonUI extends BasicButtonUI {
        private final Color hoverColor = new Color(255, 255, 255, 20);
        private final Color activeColor = new Color(255, 255, 255, 35);
        private final boolean isActive;

        public TranslucentButtonUI(boolean isActive) {
            this.isActive = isActive;
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.setOpaque(false);
            JButton b = (JButton) c;
            b.setContentAreaFilled(false);
            b.setFocusPainted(false);
            b.setRolloverEnabled(true);
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            JButton b = (JButton) c;
            ButtonModel model = b.getModel();
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (isActive) {
                g2d.setColor(activeColor);
                g2d.fillRect(0, 0, b.getWidth(), b.getHeight());
            } else if (model.isRollover()) {
                g2d.setColor(hoverColor);
                g2d.fillRect(0, 0, b.getWidth(), b.getHeight());
            }
            
            g2d.dispose();
            super.paint(g, c);
        }
    }

    public static void scaleBackground(JLabel bgLabel, int w, int h) {
        try {
            URL url = bgLabel.getClass().getResource("/images/Untitled design (5).png");
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                bgLabel.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("Error scaling background: " + e.getMessage());
        }
    }

    public static void applySharedDesign(JFrame frame, JPanel sidebarPanel, JPanel mainPanel, JLabel bgLabel, JButton btnLogout, JButton[] menuButtons, JButton activeBtn) {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        if (sidebarPanel != null) {
            sidebarPanel.setBackground(new Color(40, 20, 10));
            if (sidebarPanel instanceof TranslucentPanel) {
                ((TranslucentPanel) sidebarPanel).setAlpha(200);
            }
            sidebarPanel.setOpaque(false);
            sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(255, 234, 0, 120)));
        }

        if (menuButtons != null) {
            for (JButton btn : menuButtons) {
                if (btn != null) {
                    boolean isActive = (btn == activeBtn);
                    btn.setUI(new TranslucentButtonUI(isActive));
                    if (isActive) {
                        btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(255, 234, 0), 2),
                            new EmptyBorder(10, 13, 10, 13)
                        ));
                    } else {
                        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
                    }
                }
            }
        }

        if (btnLogout != null) {
            btnLogout.setUI(new TranslucentButtonUI(false));
            btnLogout.setBorder(new EmptyBorder(12, 15, 12, 15));
        }
    }

    public static void handleResize(JFrame frame, JPanel sidebarPanel, JPanel mainPanel, JLabel bgLabel, JButton btnLogout) {
        int w = frame.getContentPane().getWidth();
        int h = frame.getContentPane().getHeight();
        if (w <= 0 || h <= 0) return;

        if (sidebarPanel != null) {
            sidebarPanel.setBounds(0, 0, 220, h);
        }
        if (mainPanel != null) {
            mainPanel.setBounds(220, 0, w - 220, h);
        }
        if (bgLabel != null) {
            bgLabel.setBounds(0, 0, w, h);
            scaleBackground(bgLabel, w, h);
        }
        if (btnLogout != null) {
            btnLogout.setBounds(10, h - 70, 200, 45);
        }
    }
}
