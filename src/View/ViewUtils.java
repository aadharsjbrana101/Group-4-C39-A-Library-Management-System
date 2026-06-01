package View;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ViewUtils {

    public static void setupMenuHoverEffects(JButton btn) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setContentAreaFilled(true);
                btn.setBackground(new Color(255, 255, 255, 20));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setContentAreaFilled(false);
            }
        });
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
                    if (btn == activeBtn) {
                        btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(255, 234, 0), 2),
                            new EmptyBorder(10, 13, 10, 13)
                        ));
                        btn.setBackground(new Color(255, 255, 255, 20));
                        btn.setContentAreaFilled(true);
                        btn.setOpaque(true);
                    } else {
                        btn.setContentAreaFilled(false);
                        btn.setOpaque(false);
                        setupMenuHoverEffects(btn);
                    }
                }
            }
        }

        if (btnLogout != null) {
            btnLogout.setContentAreaFilled(false);
            btnLogout.setOpaque(false);
            setupMenuHoverEffects(btnLogout);
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
