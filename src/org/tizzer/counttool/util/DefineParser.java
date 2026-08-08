package org.tizzer.counttool.util;

import org.tizzer.counttool.bean.Define;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
            objectInputStream = new ObjectInputStream(Files.newInputStream(file.toPath()));
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
                System.out.println(e.getMessage());
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
            objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get("setting.dat")));
            objectOutputStream.writeObject(define);
            objectOutputStream.flush();
            System.out.println(define);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
