package org.tizzer.counttool.dialog;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.tizzer.counttool.constant.ImageSource;
import org.tizzer.counttool.util.ThemeManager;

/**
 * Created by tizzer on 2019/01/21.
 */
public class InfoDialog {

    public InfoDialog(String content, Window owner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageSource.LOGO);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/org/tizzer/counttool/css/custom.css").toExternalForm());
        ThemeManager.decorate(alert.getDialogPane());
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
