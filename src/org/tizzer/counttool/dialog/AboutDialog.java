package org.tizzer.counttool.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.tizzer.counttool.constant.ImageSource;

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
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().add(new ButtonType("关闭", ButtonBar.ButtonData.CANCEL_CLOSE));
        this.setTitle("关于");
        this.initOwner(owner);
        this.showAndWait();
    }
}
