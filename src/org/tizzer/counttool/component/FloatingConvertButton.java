package org.tizzer.counttool.component;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.tizzer.counttool.constant.ImageSource;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

/**
 * 右下角悬浮转换按钮
 * <note>点击弹出文件选择框；支持拖拽文件到按钮上转换；转换中显示流光边框</note>
 */
public class FloatingConvertButton extends StackPane {

    private static final double SIZE = 56.0;

    private final Button button;
    private final Region ring;
    private final Label toast;
    private final Popup toastPopup;
    private final RotateTransition ringSpin;
    private Timeline toastHide;
    private Consumer<List<File>> filesConsumer;

    public FloatingConvertButton() {
        setAlignment(Pos.BOTTOM_RIGHT);
        setPadding(new Insets(18, 36, 36, 18));
        setPickOnBounds(false);

        //流光圆环（转换中旋转）
        ring = new Region();
        ring.getStyleClass().add("float-ring");
        ring.setPrefSize(SIZE , SIZE );
        ring.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        ring.setMouseTransparent(true);
        ring.setVisible(false);

        //主按钮
        button = new Button();
        button.getStyleClass().add("float-button");
        button.setPrefSize(SIZE, SIZE);
        button.setMinSize(SIZE, SIZE);
        button.setMaxSize(SIZE, SIZE);
        ImageView icon = new ImageView(ImageSource.CONVERT);
        icon.setFitWidth(28);
        icon.setFitHeight(28);
        button.setGraphic(icon);
        button.setTooltip(new Tooltip("点击选择文件转换，或直接将文件/文件夹拖到按钮上"));

        getChildren().addAll(ring, button);

        //吐司（独立弹层，不影响按钮定位；内联样式保证弹层中生效）
        toast = new Label();
        toast.setStyle("-fx-background-color: rgba(35, 42, 56, 0.94); -fx-text-fill: #ffffff; " +
                "-fx-font-size: 12px; -fx-padding: 10 14 10 14; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.3, 0, 3);");
        toast.setWrapText(true);
        toast.setPrefWidth(320);
        toastPopup = new Popup();
        toastPopup.getContent().add(toast);

        //点击选择文件
        button.setOnAction(event -> pickFiles());

        //拖拽支持
        button.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        button.setOnDragEntered(event -> {
            if (event.getDragboard().hasFiles()) {
                animateScale(1.12);
                button.getStyleClass().add("float-hover");
            }
        });
        button.setOnDragExited(event -> {
            animateScale(1.0);
            button.getStyleClass().remove("float-hover");
        });
        button.setOnDragDropped(event -> {
            animateScale(1.0);
            button.getStyleClass().remove("float-hover");
            if (event.getDragboard().hasFiles() && filesConsumer != null) {
                filesConsumer.accept(event.getDragboard().getFiles());
            }
            event.setDropCompleted(true);
            event.consume();
        });

        //流光旋转动画
        ringSpin = new RotateTransition(Duration.millis(1200), ring);
        ringSpin.setByAngle(360);
        ringSpin.setCycleCount(RotateTransition.INDEFINITE);
        ringSpin.setInterpolator(Interpolator.LINEAR);
    }

    /**
     * 设置收到文件后的回调（转换逻辑由控制器提供）
     */
    public void setOnFilesReceived(Consumer<List<File>> consumer) {
        this.filesConsumer = consumer;
    }

    /**
     * 点击按钮弹出文件选择框（支持多选）
     */
    private void pickFiles() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("选择要转换的文件");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有文件", "*.*"),
                new FileChooser.ExtensionFilter("Office 文档",
                        "*.doc", "*.docx", "*.wps", "*.dot", "*.wpt",
                        "*.xls", "*.xlsx", "*.csv", "*.xlt", "*.et", "*.ett",
                        "*.ppt", "*.pptx", "*.dps", "*.dpt", "*.pot", "*.pps"));
        List<File> files = chooser.showOpenMultipleDialog(getScene().getWindow());
        if (files != null && !files.isEmpty() && filesConsumer != null) {
            filesConsumer.accept(files);
        }
    }

    /**
     * 转换中/转换结束：显示或隐藏流光边框
     */
    public void setConverting(boolean converting) {
        ring.setVisible(converting);
        if (converting) {
            ringSpin.playFromStart();
        } else {
            ringSpin.stop();
        }
    }

    /**
     * 在按钮上方弹出吐司消息，数秒后自动淡出
     */
    public void showToast(String text) {
        if (toastHide != null) {
            toastHide.stop();
        }
        toast.setOpacity(1);
        toast.setText(text);
        Bounds bounds = button.localToScreen(button.getBoundsInLocal());
        if (getScene() != null && bounds != null) {
            toastPopup.show(getScene().getWindow(),
                    bounds.getMaxX() - toast.getPrefWidth(),
                    bounds.getMinY() - toast.prefHeight(-1) - 12);
        }
        toastHide = new Timeline(new KeyFrame(Duration.millis(4800), event -> {
            FadeTransition fade = new FadeTransition(Duration.millis(400), toast);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setOnFinished(ev -> toastPopup.hide());
            fade.play();
        }));
        toastHide.play();
    }

    /**
     * 按钮缩放示意动画
     */
    private void animateScale(double target) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(150),
                new KeyValue(button.scaleXProperty(), target, Interpolator.EASE_BOTH),
                new KeyValue(button.scaleYProperty(), target, Interpolator.EASE_BOTH)));
        timeline.play();
    }
}
