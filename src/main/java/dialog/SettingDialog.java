package dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.stage.Window;
import bean.Define;
import constant.ImageSource;
import controller.SettingController;
import util.ThemeManager;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by tizzer on 2019/1/21.
 */
public class SettingDialog extends Dialog<Define> {

    public SettingDialog(Define define, Window owner) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/setting.fxml"));
        Parent content = fxmlLoader.load();

        SettingController settingController = fxmlLoader.getController();
        settingController.setDefine(define);
        settingController.initControl();

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageSource.LOGO);
        settingController.setStage(stage);

        this.setTitle("设置");
        this.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/custom.css")).toExternalForm());
        ThemeManager.decorate(this.getDialogPane());
        this.getDialogPane().setContent(content);
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                return settingController.getDefine();
            } else {
                return null;
            }
        });
        this.initOwner(owner);
        this.showAndWait();
    }
}
