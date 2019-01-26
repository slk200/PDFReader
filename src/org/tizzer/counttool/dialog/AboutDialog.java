package org.tizzer.counttool.dialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import org.tizzer.counttool.constant.ImageSource;

/**
 * Created by tizzer on 2019/01/26.
 */
public class AboutDialog extends Dialog {

    public AboutDialog() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));
        Parent content = fxmlLoader.load();

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(ImageSource.LOGO);
        this.setTitle("关于");
        this.getDialogPane().setContent(content);
        ButtonType closeType = new ButtonType("关闭", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().add(closeType);
        this.showAndWait();
    }
}
