package org.tizzer.pdfreader.view.dialog;

import org.tizzer.pdfreader.constants.SystemConstants;

import javax.swing.*;
import java.awt.*;

public class TaoBaoDialog extends JDialog {
    private static TaoBaoDialog taoBaoDialog;

    private TaoBaoDialog() {
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(SystemConstants._imgtaobao.getImage(), 0, 0,
                        getWidth(), getHeight(), this);
            }
        };
        this.add(panel);
        this.setTitle(SystemConstants.GOODS_INFO);
        this.setResizable(false);
        this.setSize(SystemConstants._imgtaobao.getIconWidth(),
                SystemConstants._imgtaobao.getIconHeight() + this.getInsets().top);
        this.setLocationRelativeTo(null);
    }

    public static void newInstance() {
        if (taoBaoDialog != null) {
            if (!taoBaoDialog.isVisible()) {
                taoBaoDialog.setVisible(true);
            }
        } else {
            taoBaoDialog = new TaoBaoDialog();
            taoBaoDialog.setVisible(true);
        }
    }
}
