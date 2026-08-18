package util;

import bean.Define;

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
            File file = new File("data/setting.dat");
            if (!file.exists()) {
                saveSetting(new Define());
            }
            objectInputStream = new ObjectInputStream(Files.newInputStream(file.toPath()));
            define = (Define) objectInputStream.readObject();
            System.out.println(define);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get("data/setting.dat")))) {
            try {
                objectOutputStream.writeObject(define);
                objectOutputStream.flush();
                System.out.println(define);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
