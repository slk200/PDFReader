package org.tizzer.pdfreader.util;

import org.tizzer.pdfreader.constants.RuntimeConstants;
import org.tizzer.pdfreader.entity.Prop;
import org.tizzer.pdfreader.view.Theme;

import java.io.*;

public class PropParser {

    /**
     * read local config
     *
     * @return
     */
    public static void initConfig() {
        try {
            File file = new File("config.prop");
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Prop prop = (Prop) ois.readObject();
            Theme theme = prop.getTheme();
            if (theme == null) {
                RuntimeConstants.currentTheme = Theme.DARK;
            }
            RuntimeConstants.currentTheme = theme;
            String directory = prop.getFile();
            if (directory != null && new File(directory).exists()) {
                RuntimeConstants.currentDirectory = directory;
            }
            theme = null;
            directory = null;
            prop = null;
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof IOException) {
                RuntimeConstants.currentTheme = Theme.DARK;
                RuntimeConstants.currentDirectory = null;
            }
        }
    }

    /**
     * write local config
     *
     * @param prop
     */
    public static void writeProp(Prop prop) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("config.prop"));
            oos.writeObject(prop);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
