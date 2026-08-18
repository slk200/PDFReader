import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import constant.ImageSource;
import controller.MainController;
import util.DefineParser;
import util.ThemeManager;

/**
 * Created by tizzer on 2019/1/19.
 */
public class BootApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        Parent root = fxmlLoader.load();

        MainController mainController = fxmlLoader.getController();
        mainController.setDefine(DefineParser.readSetting());
        mainController.setStage(primaryStage);
        mainController.initControl();

        primaryStage.getIcons().add(ImageSource.LOGO);
        primaryStage.setTitle("价格统计工具");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        ThemeManager.init(scene);
        primaryStage.setMinWidth(1300);
        primaryStage.setMinHeight(850);
    }
}
