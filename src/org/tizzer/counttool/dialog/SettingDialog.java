package org.tizzer.counttool.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.tizzer.counttool.bean.Define;
import org.tizzer.counttool.constant.ImageSource;
import org.tizzer.counttool.controller.SettingController;

/**
 * Created by tizzer on 2019/1/21.
 */
public class SettingDialog extends Dialog<Define> {

    public SettingDialog(Define define, Window owner) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/tizzer/counttool/fxml/setting.fxml"));
        Parent content = fxmlLoader.load();

        SettingController settingController = fxmlLoader.getController();
        settingController.setDefine(define);
        settingController.initControl();

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageSource.LOGO);
        settingController.setStage(stage);

        this.setTitle("设置");
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
