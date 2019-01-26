package org.tizzer.counttool.util;

import org.tizzer.counttool.bean.Define;

import java.io.*;

/**
 * Created by tizzer on 2019/1/25.
 */
public class DefineParser {
    /**
     * 读取设置
     *
     * @return
     */
    public static Define readSetting() {
        Define define = null;
        ObjectInputStream objectInputStream = null;
        try {
            File file = new File("setting.dat");
            if (!file.exists()) {
                saveSetting(new Define());
            }
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            define = (Define) objectInputStream.readObject();
            System.out.println(define);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return define;
    }

    /**
     * 保存设置
     *
     * @param define
     */
    public static void saveSetting(Define define) {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream("setting.dat"));
            objectOutputStream.writeObject(define);
            objectOutputStream.flush();
            System.out.println(define);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
