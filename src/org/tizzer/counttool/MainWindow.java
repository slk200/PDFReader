package org.tizzer.counttool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tizzer.counttool.constant.ImageSource;
import org.tizzer.counttool.util.DefineParser;

/**
 * Created by tizzer on 2019/1/19.
 */
public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();

        MainController mainController = fxmlLoader.getController();
        mainController.setDefine(DefineParser.readSetting());
        mainController.setStage(primaryStage);
        mainController.initControl();

        primaryStage.getIcons().add(ImageSource.LOGO);
        primaryStage.setTitle("价格统计工具");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }
}
