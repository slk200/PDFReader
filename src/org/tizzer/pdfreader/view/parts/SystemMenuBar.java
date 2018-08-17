package org.tizzer.pdfreader.view.parts;

import org.tizzer.pdfreader.constants.SystemConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SystemMenuBar extends JMenuBar {

    private JMenuItem preference;
//    private JMenuItem

    public SystemMenuBar() {

    }

    private void initLayouts() {
        preference = new JMenuItem(SystemConstants._setting);


        JMenu file = new JMenu(SystemConstants._file);
        file.setIcon(SystemConstants._imgprop);
        file.setMnemonic(KeyEvent.VK_ALT);
        file.setMnemonic('A');


    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, getWidth(), getHeight() - 1);
        super.paintComponent(g);
    }
}
