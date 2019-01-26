package org.tizzer.counttool.dialog;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.tizzer.counttool.constant.ImageSource;

/**
 * Created by tizzer on 2019/01/21.
 */
public class InfoDialog {

    private InfoDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageSource.LOGO);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
        alert.close();
    }

    public static void toast(String content) {
        new InfoDialog(content);
    }
}
