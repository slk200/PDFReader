package org.tizzer.counttool;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.tizzer.counttool.action.DoubleEditorAction;
import org.tizzer.counttool.action.IntegerEditorAction;
import org.tizzer.counttool.bean.*;
import org.tizzer.counttool.cell.FileTypeTableCell;
import org.tizzer.counttool.cell.IntegerTableCell;
import org.tizzer.counttool.cell.StateTableCell;
import org.tizzer.counttool.cell.TextTableCell;
import org.tizzer.counttool.constant.ChangeType;
import org.tizzer.counttool.constant.FileType;
import org.tizzer.counttool.constant.ImageSource;
import org.tizzer.counttool.constant.Signal;
import org.tizzer.counttool.dialog.AboutDialog;
import org.tizzer.counttool.dialog.InfoDialog;
import org.tizzer.counttool.dialog.SettingDialog;
import org.tizzer.counttool.util.AsyncTask;
import org.tizzer.counttool.util.DefineParser;
import org.tizzer.counttool.util.FileCountHandler;
import org.tizzer.counttool.util.PDFCountHandler;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tizzer on 2019/1/19.
 */
public class MainController {
    @FXML
    private TextField scanField;
    @FXML
    private Label wordNum;
    @FXML
    private Label pdfNum;
    @FXML
    private Label excelNum;
    @FXML
    private Label picNum;
    @FXML
    private Label pptNum;
    @FXML
    private Label otherNum;
    @FXML
    private Text totalPrice;
    @FXML
    private Spinner<Integer> pageSpinner;
    @FXML
    private Spinner<Double> priceSpinner;
    @FXML
    private Spinner<Integer> numSpinner;
    @FXML
    private Spinner<Double> specSpinner;
    @FXML
    private ListView<SumRecord> sumList;
    @FXML
    private ComboBox<Extra> extraComboBox;
    @FXML
    private TableView<ExtraItem> extraTable;
    @FXML
    private TableColumn<ExtraItem, String> extraColumn;
    @FXML
    private TableColumn<ExtraItem, String> priceColumn;
    @FXML
    private TableColumn<ExtraItem, Integer> numColumn;
    @FXML
    private Text totalPage;
    @FXML
    private ProgressIndicator totalProgress;
    @FXML
    private TableView<PendedFile> pendedFileTable;
    @FXML
    private TableColumn<PendedFile, FileType> typeColumn;
    @FXML
    private TableColumn<PendedFile, String> nameColumn;
    @FXML
    private TableColumn<PendedFile, Signal> stateColumn;
    @FXML
    private TableColumn<PendedFile, String> pageColumn;
    @FXML
    private TableColumn<PendedFile, String> pathColumn;

    //设置
    private Define define;
    //主舞台
    private Stage stage;
    //总价
    private double sum;
    //待解析文件夹
    private File selectedDirectory;
    //服务
    private ExecutorService service = Executors.newSingleThreadExecutor();
    //线程是否空闲
    private boolean isFree = true;
    //文件统计处理机
    private FileCountHandler fileCountHandler;
    //转换计数处机
    private PDFCountHandler pdfCountHandler;

    /**
     * 默认设置
     *
     * @param define
     */
    void setDefine(Define define) {
        if (define != null) {
            this.define = define;
        } else {
            this.define = new Define();
        }
    }

    /**
     * 设置主舞台
     *
     * @param stage
     */
    void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> {
            if (!service.isShutdown()) {
                service.shutdownNow();
            }
        });
    }

    /**
     * 设置组件的一些默认属性
     */
    void initControl() {
        //placeholder
        sumList.setPlaceholder(new Text("累计历史记录"));
        ImageView extraPlaceholder = new ImageView(ImageSource.NOEXTRA);
        extraTable.setPlaceholder(extraPlaceholder);
        ImageView pendedFilePlaceholder = new ImageView(ImageSource.NOTRADE);
        pendedFileTable.setPlaceholder(pendedFilePlaceholder);
        //valueFactory
        pageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,
                1, 1));
        priceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, Double.MAX_VALUE,
                0.01, 0.01));
        numSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,
                1, 1));
        specSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, Double.MAX_VALUE,
                0.01, 0.01));
        //editorAction
        pageSpinner.getEditor().setOnAction(new IntegerEditorAction(pageSpinner.getEditor()));
        priceSpinner.getEditor().setOnAction(new DoubleEditorAction(priceSpinner.getEditor()));
        numSpinner.getEditor().setOnAction(new IntegerEditorAction(numSpinner.getEditor()));
        specSpinner.getEditor().setOnAction(new DoubleEditorAction(specSpinner.getEditor()));
        //extraTable
        extraColumn.setCellValueFactory(new PropertyValueFactory<>("extra"));
        extraColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num"));
        numColumn.setCellFactory(param -> new IntegerTableCell<>());
        numColumn.setOnEditCommit(event -> {
            int gap = event.getNewValue() - event.getOldValue();
            Double price = Double.valueOf(event.getRowValue().getPrice());
            sum += gap * price;
            totalPrice.setText(String.format("总价：%.2f", this.sum));
            event.getRowValue().setNum(event.getNewValue());
        });
        //pendedfileTable
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("fileType"));
        typeColumn.setCellFactory(param -> new FileTypeTableCell<>());
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(param -> new TextTableCell<>());
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        stateColumn.setCellFactory(param -> new StateTableCell<>());
        pageColumn.setCellValueFactory(new PropertyValueFactory<>("page"));
        pageColumn.setCellFactory(param -> new TextTableCell<>(Pos.CENTER));
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        pathColumn.setCellFactory(param -> new TextTableCell<>());
        //choicebox
        extraComboBox.setVisibleRowCount(8);
        extraComboBox.getItems().addAll(define.getExtras());
    }

    //------------------------------------- 菜单功能区 -------------------------------------//

    /**
     * 选择一个文件夹（与浏览按钮共享）
     */
    public void scanFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (define.getDefaultDirectory() != null) {
            directoryChooser.setInitialDirectory(define.getDefaultDirectory());
        }
        directoryChooser.setTitle("选择文件夹");
        File directory = directoryChooser.showDialog(stage);
        if (directory != null) {
            selectedDirectory = directory;
            scanField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * 退出
     */
    public void exit() {
        stage.close();
        if (!service.isShutdown()) {
            service.shutdownNow();
        }
    }

    /**
     * 设置
     */
    public void setting() {
        try {
            SettingDialog settingDialog = new SettingDialog(define);
            Define result = settingDialog.getResult();
            if (result != null) {
                define = result;
                if (define.getChangeType() == ChangeType.EXTRA) {
                    extraComboBox.getItems().clear();
                    extraComboBox.getItems().addAll(define.getExtras());
                    define.setChangeType(ChangeType.NONE);
                }
                DefineParser.saveSetting(define);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行（与执行按钮共享）
     */
    public void execute() {
        if (!isFree) {
            InfoDialog.toast("当前有任务处理中，请稍后执行！");
            return;
        }
        if (selectedDirectory == null) {
            InfoDialog.toast("请先选择文件夹！");
            return;
        }
        isFree = false;
        prepare(false);
        service.execute(new AsyncTask() {
            @Override
            protected void scheduled() {
                super.scheduled();
            }

            @Override
            protected Object call() throws Exception {
                //显示进度
                totalProgress.setVisible(true);

                //文件统计器
                fileCountHandler = new FileCountHandler();
                //执行文件统计
                fileCountHandler.process(selectedDirectory);
                //获取文件总数量
                int size = fileCountHandler.getPendedFiles().size();
                //发送 统计结束 信号
                sendSignal(Signal.READY);

                //等待UI更新完毕
                do {
                    Thread.sleep(10);
                } while (pendedFileTable.getItems().size() < size);

                //获取表格数据集
                ObservableList<PendedFile> items = pendedFileTable.getItems();
                //初始化转换计数器
                pdfCountHandler = new PDFCountHandler();
                for (PendedFile pendedFile : items) {
                    //待转换并计数入口
                    if (pendedFile.getState() == Signal.READY) {
                        //切换为 转换并计数 状态
                        pendedFile.setState(Signal.PENDINGANDCOUNT);
                        //发送开始 转换并计数 信号
                        sendSignal(Signal.PENDINGANDCOUNT);

                        //office文档转换并计数
                        pdfCountHandler.office2pdf(pendedFile.getPath(), pendedFile.getFileType());

                        //设置页数
                        pendedFile.setPage(String.valueOf(pdfCountHandler.getPage()));
                        //设置状态
                        pendedFile.setState(Signal.DONE);
                        //发送任务结束信号
                        sendSignal(Signal.DONE);
                    }

                    //待计数入口
                    if (pendedFile.getState() == Signal.ONLYCOUNT) {
                        //计数
                        pdfCountHandler.count(new File(pendedFile.getPath()));

                        //设置页数
                        pendedFile.setPage(String.valueOf(pdfCountHandler.getPage()));
                        //发送任务结束信号
                        sendSignal(Signal.DONE);
                    }
                }
                return null;
            }

            @Override
            public void doInFXThread() {
                switch (getSignal()) {
                    case PENDINGANDCOUNT:
                    case DONE:
                        pendedFileTable.refresh();
                        break;
                    case READY:
                        pendedFileTable.getItems().addAll(fileCountHandler.getPendedFiles());
                        wordNum.setText(String.valueOf(fileCountHandler.getWordNum()));
                        excelNum.setText(String.valueOf(fileCountHandler.getExcelNum()));
                        pptNum.setText(String.valueOf(fileCountHandler.getPptNum()));
                        pdfNum.setText(String.valueOf(fileCountHandler.getPdfNum()));
                        picNum.setText(String.valueOf(fileCountHandler.getPicNum()));
                        otherNum.setText(String.valueOf(fileCountHandler.getOtherNum()));
                        break;
                    default:
                }
            }

            @Override
            protected void succeeded() {
                typeColumn.setSortable(true);
                totalPage.setText("总页数：" + pdfCountHandler.getTotalPage());
                totalProgress.setVisible(false);
                fileCountHandler = null;
                pdfCountHandler = null;
                isFree = true;
            }

            @Override
            protected void failed() {
                totalProgress.setVisible(false);
                fileCountHandler = null;
                pdfCountHandler = null;
                isFree = true;
            }
        });
    }

    /**
     * 恢复
     */
    public void reset() {
        prepare(true);
    }

    /**
     * 关于
     */
    public void about() {
        try {
            new AboutDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------------------------------- 组件菜单功能区 -------------------------------------//

    /**
     * 删除累计记录
     */
    public void deleteSum() {
        int selectedIndex = sumList.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            SumRecord sumRecord = sumList.getItems().get(selectedIndex);
            double sum = Double.parseDouble(sumRecord.getSum().toString());
            this.sum -= sum;
            totalPrice.setText(String.format("总价：%.2f", this.sum));
            sumList.getItems().remove(selectedIndex);
            sumList.refresh();
        }
    }

    /**
     * 删除附项
     */
    public void deleteExtra() {
        int selectedIndex = extraTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            ExtraItem extraItem = extraTable.getItems().get(selectedIndex);
            this.sum -= extraItem.getNum() * Double.parseDouble(extraItem.getPrice());
            totalPrice.setText(String.format("总价：%.2f", this.sum));
            extraTable.getItems().remove(selectedIndex);
            extraTable.refresh();
        }
    }

    /**
     * 打开文件
     */
    public void openFile() {
        try {
            PendedFile pendedFile = pendedFileTable.getSelectionModel().getSelectedItem();
            if (pendedFile != null) {
                Desktop.getDesktop().open(new File(pendedFile.getPath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开PDF文件
     */
    public void openPDFFile() {
        try {
            PendedFile pendedFile = pendedFileTable.getSelectionModel().getSelectedItem();
            switch (pendedFile.getFileType()) {
                case PDF:
                case WORD:
                case EXCEL:
                case PPT:
                    String filePath = pendedFile.getPath();
                    int lastIndexOf = filePath.lastIndexOf('.');
                    File PDFFile = new File(filePath.substring(0, lastIndexOf) + ".pdf");
                    Desktop.getDesktop().open(PDFFile);
                    break;
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开文件夹
     */
    public void showInExplore() {
        try {
            PendedFile pendedFile = pendedFileTable.getSelectionModel().getSelectedItem();
            if (pendedFile != null) {
                File file = new File(pendedFile.getPath());
                Desktop.getDesktop().open(new File(file.getParent()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清场 or 清理
     * <note>清场使软件恢复最初状态</note>
     * <note>清理只清理输入框、表格以及列表的数据</note>
     *
     * @param isReset 清场标志
     */
    private void prepare(boolean isReset) {
        if (isReset) {
            selectedDirectory = null;
            scanField.setText(null);
            wordNum.setText("0");
            excelNum.setText("0");
            pptNum.setText("0");
            pdfNum.setText("0");
            picNum.setText("0");
            otherNum.setText("0");
        }
        totalPrice.setText("总价：");
        sum = 0;
        sumList.getItems().clear();
        extraTable.getItems().clear();
        totalPage.setText("总页数：");
        pendedFileTable.getItems().clear();
        typeColumn.setSortable(false);
    }

    //------------------------------------- 按钮功能区 -------------------------------------//

    /**
     * 累计价格
     */
    public void sum() {
        int page = Integer.parseInt(pageSpinner.getEditor().getText());
        double price = Double.parseDouble(priceSpinner.getEditor().getText());
        int num = Integer.parseInt(numSpinner.getEditor().getText());
        double spec = Double.parseDouble(specSpinner.getEditor().getText());
        double sum = page * price * num + spec;
        this.sum += sum;
        totalPrice.setText(String.format("总价：%.2f", this.sum));
        SumRecord sumRecord = new SumRecord(page, price, num, spec, String.format("%.2f", sum));
        sumList.getItems().add(0, sumRecord);
    }

    /**
     * 添加附项
     */
    public void addExtra() {
        Extra selectedItem = extraComboBox.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            extraTable.getItems().add(new ExtraItem(selectedItem.getName(), selectedItem.getPrice(), 1));
        }
        this.sum += Double.parseDouble(selectedItem.getPrice());
        totalPrice.setText(String.format("总价：%.2f", this.sum));
    }
}
