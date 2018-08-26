package org.tizzer.pdfreader;

import com.bulenkov.darcula.DarculaLaf;
import org.tizzer.pdfreader.constants.RuntimeConstants;
import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.util.PropParser;
import org.tizzer.pdfreader.view.Theme;
import org.tizzer.pdfreader.view.Window;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class BootApplication {

    /**
     * initialize all components' font family
     *
     * @param font
     */
    private static void initGlobalFont(Font font) {
        FontUIResource fontUIResource = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontUIResource);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        PropParser.initConfig();
        initGlobalFont(SystemConstants.defaultFont);
        if (RuntimeConstants.currentTheme == Theme.LIGHT) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } else {
            UIManager.setLookAndFeel(new DarculaLaf());
        }
        SwingUtilities.invokeLater(Window::new);
    }

}
