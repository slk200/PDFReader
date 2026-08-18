package dialog;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import bean.ConvertRecord;
import util.HistoryStore;
import util.ThemeManager;

import java.util.Objects;

/**
 * 转换历史记录对话框
 *
 * <p>以表格展示每次转换的时间、文件名、路径与状态，
 * 支持按状态筛选、按文件名或路径关键词搜索。</p>
 */
public class HistoryDialog extends Dialog<Void> {

    private final TextField keywordField = new TextField();
    private final ComboBox<String> statusBox = new ComboBox<>();
    private final DatePicker fromPicker = new DatePicker();
    private final DatePicker toPicker = new DatePicker();
    private final TableView<ConvertRecord> table = new TableView<>();

    public HistoryDialog(Stage owner) {
        setTitle("转换历史");
        initOwner(owner);
        ThemeManager.decorate(getDialogPane());
        getDialogPane().getStylesheets().add(
                Objects.requireNonNull(HistoryDialog.class.getResource("/css/custom.css")).toExternalForm());
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        setResizable(true);

        //筛选行
        Label keywordLabel = new Label("搜索");
        keywordField.setPromptText("输入文件名或路径关键词");
        keywordField.setPrefWidth(260);
        Label statusLabel = new Label("状态");
        statusBox.getItems().addAll("全部", "成功", "失败");
        statusBox.setValue("全部");
        Button resetButton = new Button("重置");
        Label countLabel = new Label();
        HBox filterBar = new HBox(8, keywordLabel, keywordField, statusLabel, statusBox, resetButton, countLabel);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(countLabel, Priority.ALWAYS);
        countLabel.setAlignment(Pos.CENTER_RIGHT);

        //日期范围行（精确到年月日）
        Label fromLabel = new Label("起始日期");
        fromPicker.setPromptText("年/月/日");
        fromPicker.setPrefWidth(140);
        Label toLabel = new Label("截止日期");
        toPicker.setPromptText("年/月/日");
        toPicker.setPrefWidth(140);
        HBox dateBar = new HBox(8, fromLabel, fromPicker, toLabel, toPicker);
        dateBar.setAlignment(Pos.CENTER_LEFT);

        //记录表格
        TableColumn<ConvertRecord, String> timeColumn = new TableColumn<>("时间");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeColumn.setPrefWidth(150);
        TableColumn<ConvertRecord, String> nameColumn = new TableColumn<>("文件名");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        nameColumn.setPrefWidth(180);
        TableColumn<ConvertRecord, String> pathColumn = new TableColumn<>("路径");
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        pathColumn.setPrefWidth(300);
        TableColumn<ConvertRecord, String> statusColumn = new TableColumn<>("状态");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(60);
        table.getColumns().add(timeColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(pathColumn);
        table.getColumns().add(statusColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("暂无转换记录"));

        VBox content = new VBox(12, filterBar, dateBar, table);
        content.setPadding(new Insets(16));
        VBox.setVgrow(table, Priority.ALWAYS);
        content.setPrefSize(780, 460);
        getDialogPane().setContent(content);

        //筛选与搜索实时生效
        keywordField.textProperty().addListener((obs, o, n) -> refresh(countLabel));
        statusBox.valueProperty().addListener((obs, o, n) -> refresh(countLabel));
        fromPicker.valueProperty().addListener((obs, o, n) -> refresh(countLabel));
        toPicker.valueProperty().addListener((obs, o, n) -> refresh(countLabel));
        resetButton.setOnAction(event -> {
            keywordField.clear();
            statusBox.setValue("全部");
            fromPicker.setValue(null);
            toPicker.setValue(null);
            refresh(countLabel);
        });
        refresh(countLabel);
    }

    /**
     * 按当前条件重新查询并填充表格
     *
     * @param countLabel 记录数标签
     */
    private void refresh(Label countLabel) {
        String from = fromPicker.getValue() == null ? null : fromPicker.getValue().toString();
        String to = toPicker.getValue() == null ? null : toPicker.getValue().toString();
        table.setItems(FXCollections.observableArrayList(
                HistoryStore.query(keywordField.getText(), statusBox.getValue(), from, to)));
        countLabel.setText("共 " + table.getItems().size() + " 条记录");
    }
}
