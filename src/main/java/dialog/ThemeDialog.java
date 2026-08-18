package dialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import constant.ImageSource;
import constant.ThemeMode;
import util.ThemeManager;

import java.util.Objects;

/**
 * 主题设置对话框
 *
 * <p>提供浅色、深色、跟随系统三个选项；确定后应用所选主题，取消则保持不变。</p>
 */
public class ThemeDialog extends Dialog<ThemeMode> {

    public ThemeDialog(Window owner) {
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageSource.LOGO);
        getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/custom.css")).toExternalForm());

        setTitle("主题设置");
        initOwner(owner);
        ThemeManager.decorate(getDialogPane());

        //选项组
        ToggleGroup group = new ToggleGroup();
        RadioButton lightRadio = createRadio("浅色模式", ThemeMode.LIGHT, group);
        RadioButton darkRadio = createRadio("深色模式", ThemeMode.DARK, group);
        RadioButton systemRadio = createRadio("跟随系统主题", ThemeMode.SYSTEM, group);

        //选中当前模式
        ThemeMode current = ThemeManager.getCurrentMode();
        if (current == ThemeMode.DARK) {
            darkRadio.setSelected(true);
        } else if (current == ThemeMode.LIGHT) {
            lightRadio.setSelected(true);
        } else {
            systemRadio.setSelected(true);
        }

        HBox hintBox = new HBox(new Label("跟随系统主题时，软件会自动随Windows的浅色/深色设置切换。"));

        VBox content = new VBox(14.0, lightRadio, darkRadio, systemRadio, hintBox);
        content.setPadding(new Insets(10.0, 4.0, 4.0, 4.0));
        hintBox.setAlignment(Pos.CENTER_LEFT);

        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        //确定时返回选中的主题模式，取消时返回null（保持主题不变）
        setResultConverter(param -> {
            if (param == ButtonType.OK) {
                return (ThemeMode) group.getSelectedToggle().getUserData();
            }
            return null;
        });
    }

    private RadioButton createRadio(String text, ThemeMode mode, ToggleGroup group) {
        RadioButton radio = new RadioButton(text);
        radio.setToggleGroup(group);
        radio.setUserData(mode);
        return radio;
    }
}
