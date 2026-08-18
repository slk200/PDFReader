package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import action.DoubleEditorAction;
import component.FloatingConvertButton;
import action.IntegerEditorAction;
import bean.*;
import cell.FileTypeTableCell;
import cell.IntegerTableCell;
import cell.StateTableCell;
import cell.TextTableCell;
import constant.*;
import dialog.AboutDialog;
import dialog.HistoryDialog;
import dialog.InfoDialog;
import dialog.SettingDialog;
import dialog.ThemeDialog;
import util.AsyncTask;
import util.DefineParser;
import util.FileCountHandler;
import util.PDFCountHandler;
import util.ThemeManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private Label imageNum;
    @FXML
    private Label pptNum;
    @FXML
    private Label otherNum;
    @FXML
    private Label totalPrice;
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
    private Label totalPage;
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
    @FXML
    private FloatingConvertButton convertButton;

    //设置
    private Define define;
    //主舞台
    private Stage stage;
    //总价
    private double sum;
    //待解析文件夹
    private File selectedDirectory;
    //服务
    private final ExecutorService service = Executors.newSingleThreadExecutor();
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
    public void setDefine(Define define) {
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
    public void setStage(Stage stage) {
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
    public void initControl() {
        //placeholder
        sumList.setPlaceholder(new Label("累计历史记录"));
        ImageView extraPlaceholder = new ImageView(ImageSource.NO_EXTRA);
        extraTable.setPlaceholder(extraPlaceholder);
        ImageView pendedFilePlaceholder = new ImageView(ImageSource.NO_TRADE);
        pendedFileTable.setPlaceholder(pendedFilePlaceholder);
        pendedFileTable.setOnMousePressed(event -> {
            PendedFile pendedFile = pendedFileTable.getSelectionModel().getSelectedItem();
            if (pendedFile == null) {
                return;
            }
            String page =  pendedFile.getPage();
            if (!page.equals("等待计数")){
                pageSpinner.getEditor().setText(pendedFile.getPage());
            }
        });
        //valueFactory
        pageSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,
                1, 1));
        priceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, Double.MAX_VALUE,
                0.01, 0.01));
        numSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,
                1, 1));
        specSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, Double.MAX_VALUE,
                0, 0.01));
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
            double price = Double.parseDouble(event.getRowValue().getPrice());
            sum += gap * price;
            totalPrice.setText(String.format(StringSource.TOTAL_PRICE + StringSource.FORMAT + StringSource.UNIT, this.sum));
            event.getRowValue().setNum(event.getNewValue());
        });
        //pendedFileTable
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
        //choiceBox
        extraComboBox.setVisibleRowCount(8);
        //悬浮转换按钮
        convertButton.setOnFilesReceived(this::convertFiles);


        extraComboBox.getItems().addAll(define.getExtras());
    }

    //------------------------------------- 菜单功能区 -------------------------------------//

    /**
     * 选择一个文件夹（与浏览按钮共享）
     */
    public void scanFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (define.getDefaultDirectory() != null && new File(define.getDefaultDirectory().toURI()).exists()) {
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
            SettingDialog settingDialog = new SettingDialog(define, stage);
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
            new InfoDialog("当前有任务处理中，请稍后执行！", stage);
            return;
        }
        if (selectedDirectory == null) {
            new InfoDialog("请先选择文件夹！", stage);
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
                        pendedFile.setState(Signal.PENDING_AND_COUNT);
                        //发送开始 转换并计数 信号
                        sendSignal(Signal.PENDING_AND_COUNT);

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
                    if (pendedFile.getState() == Signal.ONLY_COUNT) {
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
                    case PENDING_AND_COUNT:
                    case DONE:
                        pendedFileTable.refresh();
                        break;
                    case READY:
                        pendedFileTable.getItems().addAll(fileCountHandler.getPendedFiles());
                        wordNum.setText(StringSource.WORD + fileCountHandler.getWordNum());
                        excelNum.setText(StringSource.EXCEL + fileCountHandler.getExcelNum());
                        pptNum.setText(StringSource.PPT + fileCountHandler.getPptNum());
                        pdfNum.setText(StringSource.PDF + fileCountHandler.getPdfNum());
                        imageNum.setText(StringSource.IMAGE + fileCountHandler.getPicNum());
                        otherNum.setText(StringSource.OTHER + fileCountHandler.getOtherNum());
                        break;
                    default:
                }
            }

            @Override
            protected void succeeded() {
                typeColumn.setSortable(true);
                totalPage.setText(StringSource.TOTAL_PAGE + pdfCountHandler.getTotalPage());
                fileCountHandler = null;
                pdfCountHandler = null;
                isFree = true;
            }

            @Override
            protected void failed() {
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
     * 主题设置
     */
    public void changeTheme() {
        ThemeDialog themeDialog = new ThemeDialog(stage);
        themeDialog.showAndWait().ifPresent(ThemeManager::setMode);
    }

    /**
     * 转换历史
     */
    public void history() {
        new HistoryDialog(stage).showAndWait();
    }

    /**
     * 关于
     */
    public void about() {
        try {
            new AboutDialog(stage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //------------------------------------- 悬浮转换功能区 -------------------------------------//

    private void convertFiles(List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        Map<File, FileType> convertible = new LinkedHashMap<>();
        Map<String, Integer> skippedBySuffix = new LinkedHashMap<>();
        for (File file : files) {
            collectConvertibles(file, convertible, skippedBySuffix);
        }
        //不可转换文件：吐司提示文件名与格式
        if (!skippedBySuffix.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Integer> entry : skippedBySuffix.entrySet()) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(entry.getValue() > 1
                        ? entry.getKey() + " 等 " + entry.getValue() + " 个"
                        : entry.getKey());
                sb.append("：该格式文件无法转换");
            }
            convertButton.showToast(sb.toString());
        }
        if (convertible.isEmpty()) {
            return;
        }
        //流光示意转换中
        convertButton.setConverting(true);
        service.execute(new AsyncTask() {
            @Override
            protected Object call() {
                PDFCountHandler handler = new PDFCountHandler();
                for (Map.Entry<File, FileType> entry : convertible.entrySet()) {
                    handler.office2pdf(entry.getKey().getAbsolutePath(), entry.getValue());
                }
                return null;
            }

            @Override
            protected void succeeded() {
                convertButton.setConverting(false);
                convertButton.showToast("转换完成，共处理 " + convertible.size() + " 个文件，结果保存在原目录");
            }

            @Override
            protected void failed() {
                convertButton.setConverting(false);
                convertButton.showToast("转换过程出错，请重试");
            }
        });
    }

    /**
     * 递归收集可转换文件，不可转换的按文件名统计
     *
     * @param file
     * @param convertible
     * @param skipped
     */
    private void collectConvertibles(File file, Map<File, FileType> convertible, Map<String, Integer> skipped) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    collectConvertibles(child, convertible, skipped);
                }
            }
            return;
        }
        String name = file.getName().toLowerCase();
        //跳过隐藏文件与office缓存文件
        if (file.isHidden() || name.startsWith("~$")) {
            return;
        }
        int dot = name.lastIndexOf('.');
        String suffix = dot >= 0 ? name.substring(dot + 1) : "";
        FileType type;
        switch (suffix) {
            case "doc":
            case "docx":
            case "wps":
            case "dot":
            case "wpt":
                type = FileType.WORD;
                break;
            case "xls":
            case "xlsx":
            case "csv":
            case "xlt":
            case "et":
            case "ett":
                type = FileType.EXCEL;
                break;
            case "ppt":
            case "pptx":
            case "dps":
            case "dpt":
            case "pot":
            case "pps":
                type = FileType.PPT;
                break;
            default:
                skipped.merge(file.getName(), 1, Integer::sum);
                return;
        }
        convertible.put(file, type);
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
            totalPrice.setText(String.format(StringSource.TOTAL_PRICE + StringSource.FORMAT + StringSource.UNIT, this.sum));
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
            totalPrice.setText(String.format(StringSource.TOTAL_PRICE + StringSource.FORMAT + StringSource.UNIT, this.sum));
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
            if (pendedFile != null) {
                switch (pendedFile.getFileType()) {
                    case PDF:
                    case WORD:
                    case EXCEL:
                    case PPT:
                        String filePath = pendedFile.getPath();
                        int lastIndexOf = filePath.lastIndexOf('.');
                        File PDFFile = new File(filePath.substring(0, lastIndexOf) + ".pdf");
                        if (PDFFile.exists()) {
                            Desktop.getDesktop().open(PDFFile);
                        }
                        break;
                    default:
                }
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
            wordNum.setText(StringSource.DEFAULT_WORD);
            excelNum.setText(StringSource.DEFAULT_EXCEL);
            pptNum.setText(StringSource.DEFAULT_PPT);
            pdfNum.setText(StringSource.DEFAULT_PDF);
            imageNum.setText(StringSource.DEFAULT_IMAGE);
            otherNum.setText(StringSource.DEFAULT_OTHER);
        }
        totalPrice.setText(StringSource.DEFAULT_TOTAL_PRICE);
        sum = 0;
        sumList.getItems().clear();
        extraTable.getItems().clear();
        totalPage.setText(StringSource.DEFAULT_TOTAL_PAGE);
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
        totalPrice.setText(String.format(StringSource.TOTAL_PRICE + StringSource.FORMAT + StringSource.UNIT, this.sum));
        SumRecord sumRecord = new SumRecord(page, price, num, spec, String.format(StringSource.FORMAT, sum));
        sumList.getItems().add(0, sumRecord);
    }

    /**
     * 添加附项
     */
    public void addExtra() {
        Extra selectedItem = extraComboBox.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            extraTable.getItems().add(new ExtraItem(selectedItem.getName(), selectedItem.getPrice(), 1));
            this.sum += Double.parseDouble(selectedItem.getPrice());
            totalPrice.setText(String.format(StringSource.TOTAL_PRICE + StringSource.FORMAT + StringSource.UNIT, this.sum));
        }
    }
}
