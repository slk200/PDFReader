package org.tizzer.pdfreader.util;

import java.io.*;

public class PropParser {

    public static String readProp() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("config.prop"));
            String defaultPath = ois.readUTF();
            ois.close();
            return defaultPath;
        } catch (IOException e) {
            return null;
        }
    }

    public static void writeProp(String defaultPath) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("config.prop"));
            oos.writeUTF(defaultPath);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
