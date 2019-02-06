package org.tizzer.counttool.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.tizzer.counttool.action.DoubleEditorAction;
import org.tizzer.counttool.bean.Define;
import org.tizzer.counttool.bean.Extra;
import org.tizzer.counttool.cell.DoubleTableCell;
import org.tizzer.counttool.constant.ChangeType;
import org.tizzer.counttool.dialog.InfoDialog;

import java.io.File;

/**
 * Created by tizzer on 2019/1/21.
 */
public class SettingController {
    @FXML
    private TextField defaultPlace;
    @FXML
    private Text toolTip;
    @FXML
    private TableView<Extra> extraTable;
    @FXML
    private TableColumn<Extra, String> nameColumn;
    @FXML
    private TableColumn<Extra, String> priceColumn;
    @FXML
    private TextField nameField;
    @FXML
    private Spinner<Double> priceSpinner;

    private Define define;
    private Stage stage;

    public Define getDefine() {
        define.setExtra(extraTable.getItems());
        return define;
    }

    public void setDefine(Define define) {
        this.define = define;
    }

    public void setStage(Stage window) {
        this.stage = window;
    }

    public void initControl() {
        //table column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(param -> new DoubleTableCell<>());
        priceColumn.setOnEditCommit(event -> {
            event.getRowValue().setPrice(event.getNewValue());
            define.setChangeType(ChangeType.EXTRA);
        });
        //value factory
        priceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, Double.MAX_VALUE,
                0.01, 0.01));
        //editor action
        priceSpinner.getEditor().setOnAction(new DoubleEditorAction(priceSpinner.getEditor()));

        //default folder
        if (define.getDefaultDirectory() == null) {
            toolTip.setText("设置默认位置是指设置你选择执行文件夹的起始位置");
        } else if (define.getDefaultDirectory().exists()) {
            defaultPlace.setText(define.getDefaultDirectory().getAbsolutePath());
        } else {
            toolTip.setText("之前设置的地址当前已不存在！");
        }
        //table items
        extraTable.getItems().addAll(define.getExtras());
    }

    /**
     * 选择默认文件夹
     */
    public void scanFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择默认文件夹");
        File directory = directoryChooser.showDialog(stage);
        if (directory != null) {
            define.setDefaultDirectory(directory);
            defaultPlace.setText(directory.getAbsolutePath());
            toolTip.setText(null);
        }
    }

    /**
     * 添加附项
     */
    public void addExtra() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            new InfoDialog("选项名不能为空！", stage);
        }
        String price = priceSpinner.getEditor().getText().trim();
        Extra extra = new Extra(name, price);
        define.setChangeType(ChangeType.EXTRA);
        extraTable.getItems().add(extra);
    }

    public void deleteExtra() {
        int selectedIndex = extraTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            extraTable.getItems().remove(selectedIndex);
            extraTable.refresh();
        }
    }
}
