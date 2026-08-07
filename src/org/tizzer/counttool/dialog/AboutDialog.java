package org.tizzer.counttool.dialog;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.tizzer.counttool.constant.ImageSource;
import org.tizzer.counttool.util.ThemeManager;

/**
 * Created by tizzer on 2019/01/26.
 */
public class AboutDialog extends Dialog {

    public AboutDialog(Window owner) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/tizzer/counttool/fxml/about.fxml"));
        Parent content = fxmlLoader.load();

        DialogPane dialogPane = getDialogPane();
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(ImageSource.LOGO);
        dialogPane.getStylesheets().add(getClass().getResource("/org/tizzer/counttool/css/custom.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        ThemeManager.decorate(dialogPane);
        dialogPane.setContent(content);
//        dialogPane.setPadding(new Insets(0, 0, 100, 0));
        dialogPane.getButtonTypes().add(new ButtonType("关闭", ButtonBar.ButtonData.CANCEL_CLOSE));
        this.setTitle("关于");
        this.initOwner(owner);
        this.showAndWait();
    }
}
