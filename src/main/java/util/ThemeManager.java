package util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.util.Duration;
import constant.ThemeMode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;

/**
 * 主题管理器
 *
 * <p>负责应用浅色/深色主题，支持跟随Windows系统主题自动切换。</p>
 */
public class ThemeManager {

    private static final String DARK_STYLE_CLASS = "dark";
    private static final String PREF_KEY_THEME = "themeMode";
    private static final String PERSONALIZE_KEY =
            "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize";

    private static ThemeMode currentMode = ThemeMode.LIGHT;
    private static Scene scene;
    private static Timeline systemWatcher;

    /**
     * 初始化主题管理器（在显示窗口后调用）
     *
     * @param appScene 主窗口场景
     */
    public static void init(Scene appScene) {
        scene = appScene;
        currentMode = loadMode();
        applyMode(currentMode);
        startSystemWatcher();
    }

    /**
     * 为对话框装饰当前主题（在对话框显示前调用）
     *
     * @param dialogPane 对话框内容面板
     */
    public static void decorate(DialogPane dialogPane) {
        if (dialogPane == null) {
            return;
        }
        boolean dark = isDarkEffective(currentMode);
        boolean hasDark = dialogPane.getStyleClass().contains(DARK_STYLE_CLASS);
        if (dark && !hasDark) {
            dialogPane.getStyleClass().add(DARK_STYLE_CLASS);
        } else if (!dark && hasDark) {
            dialogPane.getStyleClass().remove(DARK_STYLE_CLASS);
        }
    }

    /**
     * 获取当前主题模式
     *
     * @return 当前主题模式
     */
    public static ThemeMode getCurrentMode() {
        return currentMode;
    }

    /**
     * 设置主题模式并立即生效、持久化
     *
     * @param mode 目标主题模式
     */
    public static void setMode(ThemeMode mode) {
        currentMode = mode;
        saveMode(mode);
        applyMode(mode);
    }

    /**
     * 根据主题模式渲染界面
     *
     * @param mode 主题模式
     */
    private static void applyMode(ThemeMode mode) {
        if (scene == null) {
            return;
        }
        boolean dark = isDarkEffective(mode);
        boolean hasDark = scene.getRoot().getStyleClass().contains(DARK_STYLE_CLASS);
        if (dark && !hasDark) {
            scene.getRoot().getStyleClass().add(DARK_STYLE_CLASS);
        } else if (!dark && hasDark) {
            scene.getRoot().getStyleClass().remove(DARK_STYLE_CLASS);
        }
    }

    /**
     * 判断该模式下实际应使用的深浅色
     *
     * @param mode 主题模式
     * @return true表示深色
     */
    private static boolean isDarkEffective(ThemeMode mode) {
        if (mode == ThemeMode.DARK) {
            return true;
        }
        if (mode == ThemeMode.LIGHT) {
            return false;
        }
        return isSystemDark();
    }

    /**
     * 读取Windows系统当前的深浅色设置
     *
     * <p>注册表 AppsUseLightTheme：1=浅色，0=深色。读取失败时默认浅色。</p>
     *
     * @return true表示系统当前为深色模式
     */
    public static boolean isSystemDark() {
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[]{"reg", "query", PERSONALIZE_KEY, "/v", "AppsUseLightTheme"});
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("AppsUseLightTheme")) {
                        String value = line.trim();
                        //取行末数值（格式形如 "... REG_DWORD    0x1"）
                        String number = value.substring(value.lastIndexOf(' ') + 1).trim();
                        return number.endsWith("0");
                    }
                }
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 启动系统主题监听（跟随模式下每2秒检查一次）
     */
    private static void startSystemWatcher() {
        if (systemWatcher != null) {
            systemWatcher.stop();
        }
        boolean[] lastDark = {isDarkEffective(currentMode)};
        systemWatcher = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            if (currentMode != ThemeMode.SYSTEM) {
                return;
            }
            boolean dark = isSystemDark();
            if (dark != lastDark[0]) {
                lastDark[0] = dark;
                applyMode(currentMode);
            }
        }));
        systemWatcher.setCycleCount(Timeline.INDEFINITE);
        systemWatcher.play();
    }

    /**
     * 从持久化存储读取主题模式
     *
     * @return 主题模式，默认跟随系统
     */
    private static ThemeMode loadMode() {
        String name = Preferences.userNodeForPackage(ThemeManager.class)
                .get(PREF_KEY_THEME, ThemeMode.SYSTEM.name());
        try {
            return ThemeMode.valueOf(name);
        } catch (IllegalArgumentException e) {
            return ThemeMode.SYSTEM;
        }
    }

    /**
     * 持久化主题模式
     *
     * @param mode 主题模式
     */
    private static void saveMode(ThemeMode mode) {
        Preferences.userNodeForPackage(ThemeManager.class)
                .put(PREF_KEY_THEME, mode.name());
    }
}
