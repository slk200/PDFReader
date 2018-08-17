package org.tizzer.pdfreader;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.tizzer.pdfreader.view.Window;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class BootApplication {

//    private static void initGlobalFont(Font font) {
//        FontUIResource fontRes = new FontUIResource(font);
//        for (Enumeration<Object> keys = UIManager.getDefaults().keys();
//             keys.hasMoreElements(); ) {
//            Object key = keys.nextElement();
//            Object value = UIManager.get(key);
//            if (value instanceof FontUIResource) {
//                UIManager.put(key, fontRes);
//            }
//        }
//    }

    public static void main(String[] args) throws Exception {
//        initGlobalFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
//        UIManager.put("RootPane.setupButtonVisible", false);
//        BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
//        BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
//        BeautyEyeLNFHelper.launchBeautyEyeLNF();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(Window::new);
    }

}
