package org.tizzer.pdfreader.view;

import org.tizzer.pdfreader.callback.CountListener;
import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.layout.VerticalFlowLayout;
import org.tizzer.pdfreader.util.PropParser;
import org.tizzer.pdfreader.util.ThreadPool;
import org.tizzer.pdfreader.util.WPS2PDFHandler;
import org.tizzer.pdfreader.view.dialog.SettingDialog;
import org.tizzer.pdfreader.view.dialog.UnsupportedFileDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Window extends JFrame {
    private static final int MAX_TIMES = 3;
    //菜单
    private JMenuBar mMenuBar;
    private JMenu mFileMenu;
    private JMenuItem mSettingMenuItem;
    private JMenuItem mExitMenuItem;
    //组件
    private JTextField mFilePath;
    private JButton mScanBtn;
    private JButton mAnalysisBtn;
    private JFileChooser mFileChooser;
    private DefaultTableModel mTableModel;
    private JScrollPane mTablePane;
    private JProgressBar mProgress;
    private JLabel mWordLabel;
    private JLabel mPptLabel;
    private JLabel mExcelLabel;
    private JLabel mPDFLabel;
    private JLabel mPageLabel;
    private JButton mCalcBtn;
    private JTextArea mConsole;
    private JScrollPane mConsolePane;
    private JLabel mErrorLabel;
    private JButton mUnsupportedFileBtn;
    //文件集合
    private Set<String> fileSet;
    //目标路径
    private File mSelectedFile;
    //是否置底标志
    private int isTableNeedBottom = 0;
    private int isConsoleNeedBottom = 0;
    //分析任务
    private ProcessTask mTask = new ProcessTask();
    private WPS2PDFHandler wps2PDFHandler = new WPS2PDFHandler();

    public Window() {
        initComponents();
        initLayout();
        initListeners();
        startShow();

    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        mMenuBar = new JMenuBar();
        mFileMenu = new JMenu(SystemConstants._file);
        mFileMenu.setMnemonic(KeyEvent.VK_F);
        mFileMenu.setMnemonic('F');
        mSettingMenuItem = new JMenuItem(SystemConstants._setting);
        mSettingMenuItem.setIcon(SystemConstants._imgprop);
        mSettingMenuItem.setMnemonic(KeyEvent.VK_P);
        mSettingMenuItem.setMnemonic('S');
        mExitMenuItem = new JMenuItem(SystemConstants._exit);
        mExitMenuItem.setMnemonic(KeyEvent.VK_E);
        mExitMenuItem.setMnemonic('E');

        SystemConstants._cuurrentdirectory = PropParser.readProp();
        this.mSelectedFile = SystemConstants._cuurrentdirectory != null && new File(SystemConstants._cuurrentdirectory).exists() ? new File(SystemConstants._cuurrentdirectory) : null;
        mFilePath = new JTextField();
        mFilePath.setEditable(false);
        mFilePath.setBackground(SystemConstants._fieldcolor);
        mFilePath.setText(SystemConstants._cuurrentdirectory != null && new File(SystemConstants._cuurrentdirectory).exists() ? SystemConstants._cuurrentdirectory : null);
        mScanBtn = new JButton(SystemConstants._scan);
        mScanBtn.setIcon(SystemConstants._imgfolder);
        mScanBtn.setFocusable(false);
        mAnalysisBtn = new JButton(SystemConstants._analysis);
        mAnalysisBtn.setIcon(SystemConstants._imgstart);
        mFileChooser = new JFileChooser();
        mFileChooser.setCurrentDirectory(mSelectedFile);
        mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        mTableModel = new DefaultTableModel(null, SystemConstants._tableTitle);
        mProgress = new JProgressBar();
        mProgress.setString(SystemConstants._analysing);
        mProgress.setStringPainted(true);
        mProgress.setIndeterminate(true);
        mProgress.setVisible(false);
        mWordLabel = new JLabel(SystemConstants._word);
        mWordLabel.setIcon(SystemConstants._imgword);
        mPptLabel = new JLabel(SystemConstants._ppt);
        mPptLabel.setIcon(SystemConstants._imgppt);
        mExcelLabel = new JLabel(SystemConstants._excel);
        mExcelLabel.setIcon(SystemConstants._imgexcel);
        mPDFLabel = new JLabel(SystemConstants._pdf);
        mPDFLabel.setIcon(SystemConstants._imgpdf);
        mPageLabel = new JLabel(SystemConstants._page);
        mPageLabel.setIcon(SystemConstants._imgpage);
        mCalcBtn = new JButton(SystemConstants._calc);
        mCalcBtn.setIcon(SystemConstants._imgcalc);
        mCalcBtn.setFocusable(false);
        mConsole = new JTextArea(0, 40);
        mConsole.setEditable(false);
        mConsole.setBackground(Color.BLACK);
        mConsole.setForeground(Color.WHITE);
        mConsole.setLineWrap(true);
        mConsole.setWrapStyleWord(true);
        mErrorLabel = new JLabel(SystemConstants._noerror);
        mErrorLabel.setIcon(SystemConstants._imgerroroff);
        mUnsupportedFileBtn = new JButton(SystemConstants._locfile);
        mUnsupportedFileBtn.setIcon(SystemConstants._imglocfileoff);
        mUnsupportedFileBtn.setEnabled(false);
    }

    /**
     * 初始化布局
     */
    private void initLayout() {
        mMenuBar.add(mFileMenu);
        mFileMenu.add(mSettingMenuItem);
        mFileMenu.addSeparator();
        mFileMenu.add(mExitMenuItem);
        this.setJMenuBar(mMenuBar);

        JPanel panel1 = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.MIDDLE));
        panel1.add(mFilePath);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, SystemConstants._gap, SystemConstants._gap));
        panel2.add(mScanBtn);
        panel2.add(mAnalysisBtn);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.add(panel1);
        panel3.add(panel2, "East");
        panel3.add(mProgress, "South");

        panel1 = new JPanel(new GridLayout(2, 3, 5, 5));
        panel1.add(mWordLabel);
        panel1.add(mPptLabel);
        panel1.add(mExcelLabel);
        panel1.add(mPDFLabel);
        panel1.add(mPageLabel);
        panel1.add(mCalcBtn);

        JTable table = new JTable(mTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFont(new Font(SystemConstants._fontFamily, Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font(SystemConstants._fontFamily, Font.BOLD, 12));
        table.getTableHeader().setReorderingAllowed(false);
        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.setBorder(new EmptyBorder(0, SystemConstants._gap, SystemConstants._gap, SystemConstants._gap));
        leftPane.add(panel3, "North");
        leftPane.add(mTablePane = new JScrollPane(table), "Center");
        leftPane.add(panel1, "South");

        panel1 = new JPanel(new BorderLayout());
        panel1.setBorder(new EmptyBorder(SystemConstants._gap, 0, SystemConstants._gap, SystemConstants._gap));
        panel1.add(mErrorLabel, "Center");
        panel1.add(mUnsupportedFileBtn, "East");

        panel3 = new JPanel(new BorderLayout());
        JLabel label = new JLabel(SystemConstants._console);
        label.setBorder(new EmptyBorder(0, SystemConstants._gap, SystemConstants._gap, 0));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        panel3.add(label, "North");
        mConsolePane = new JScrollPane(mConsole);
        panel3.add(mConsolePane, "Center");

        JPanel rightPane = new JPanel(new BorderLayout());
        rightPane.setBorder(new EmptyBorder(0, 0, SystemConstants._gap, SystemConstants._gap));
        rightPane.add(panel1, "North");
        rightPane.add(panel3, "Center");

        this.setLayout(new GridLayout(1, 2));
        this.add(leftPane);
        this.add(rightPane);
    }

    /**
     * 初始化监听器
     */
    private void initListeners() {
        mScanBtn.addActionListener(event -> {
            mFileChooser.showDialog(Window.this, SystemConstants._confirm);
            if (mFileChooser.getSelectedFile() != null) {
                mSelectedFile = mFileChooser.getSelectedFile();
                mFilePath.setText(mFileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        mFilePath.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                mFileChooser.showDialog(Window.this, SystemConstants._confirm);
                if (mFileChooser.getSelectedFile() != null) {
                    mSelectedFile = mFileChooser.getSelectedFile();
                    mFilePath.setText(mFileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        mAnalysisBtn.addActionListener(event -> {
            if (mSelectedFile != null) {
                reset();
                ThreadPool.submit(mTask);
            }
        });

        mCalcBtn.addActionListener(event -> {
            try {
                Runtime.getRuntime().exec(SystemConstants._command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mTablePane.getVerticalScrollBar().addAdjustmentListener(event -> {
            if (event.getAdjustmentType() == AdjustmentEvent.TRACK && isTableNeedBottom <= MAX_TIMES) {
                mTablePane.getVerticalScrollBar().setValue(mTablePane.getVerticalScrollBar().getModel().getMaximum()
                        - mTablePane.getVerticalScrollBar().getModel().getExtent());
                isTableNeedBottom++;
            }
        });

        mConsolePane.getVerticalScrollBar().addAdjustmentListener(event -> {
            if (event.getAdjustmentType() == AdjustmentEvent.TRACK && isConsoleNeedBottom <= MAX_TIMES) {
                mConsolePane.getVerticalScrollBar().setValue(mConsolePane.getVerticalScrollBar().getModel().getMaximum()
                        - mConsolePane.getVerticalScrollBar().getModel().getExtent());
                isConsoleNeedBottom++;
            }
        });

        mUnsupportedFileBtn.addActionListener(event -> {
            new Thread(() -> {
                System.out.println(mUnsupportedFileBtn);
                mUnsupportedFileBtn.setEnabled(false);
                StringBuilder unsupportedFileHtml = new StringBuilder();
                for (String file : fileSet) {
                    unsupportedFileHtml.append("<a href='file://")
                            .append(file)
                            .append("'>")
                            .append(file)
                            .append("</a><br/><br/>");
                }
                UnsupportedFileDialog.display(Window.this, unsupportedFileHtml);
                mUnsupportedFileBtn.setEnabled(true);
            }).start();
        });

        mSettingMenuItem.addActionListener(event -> {
            SettingDialog.display(Window.this);
        });

        mExitMenuItem.addActionListener(event -> {
            System.exit(0);
        });
    }

    /**
     * 开启窗口
     */
    private void startShow() {
        this.setTitle(SystemConstants._title);
        this.setIconImage(SystemConstants._imglogo.getImage());
        this.setResizable(false);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.getRootPane().setDefaultButton(mAnalysisBtn);
    }

    /**
     * 重置参数
     */
    private void reset() {
        mTableModel.setDataVector(null, SystemConstants._tableTitle);
        mWordLabel.setText(SystemConstants._word);
        mPptLabel.setText(SystemConstants._ppt);
        mExcelLabel.setText(SystemConstants._excel);
        mPDFLabel.setText(SystemConstants._pdf);
        mPageLabel.setText(SystemConstants._page);
        mErrorLabel.setText(SystemConstants._noerror);
        mErrorLabel.setIcon(SystemConstants._imgerroroff);
        mUnsupportedFileBtn.setIcon(SystemConstants._imglocfileoff);
        fileSet = null;
        mConsole.setText(null);
        wps2PDFHandler.reset();
    }

    private class ProcessTask implements Runnable {

        @Override
        public void run() {
            mProgress.setVisible(true);
            mAnalysisBtn.setEnabled(false);
            if (!wps2PDFHandler.isStarted()) {
                wps2PDFHandler.addLogListener(message -> {
                    isConsoleNeedBottom = 0;
                    mConsole.append(message);
                });
                wps2PDFHandler.addCountListener(new CountListener() {
                    @Override
                    public void countWord(int num) {
                        mWordLabel.setText(SystemConstants._word + num);
                    }

                    @Override
                    public void countPpt(int num) {
                        mPptLabel.setText(SystemConstants._ppt + num);
                    }

                    @Override
                    public void countExcel(int num) {
                        mExcelLabel.setText(SystemConstants._excel + num);
                    }

                    @Override
                    public void countPDF(int num) {
                        mPDFLabel.setText(SystemConstants._pdf + num);
                    }

                    @Override
                    public void countPages(String filename, int page, int num) {
                        isTableNeedBottom = 0;
                        mTableModel.addRow(new Object[]{filename, page});
                        mPageLabel.setText(SystemConstants._page + num);
                    }
                });
                wps2PDFHandler.addProcessListener(directory -> {
                    if (fileSet == null) {
                        fileSet = new HashSet<>();
                        mErrorLabel.setText(SystemConstants._existerror);
                        mErrorLabel.setIcon(SystemConstants._imgerroron);
                    }
                    fileSet.add(directory);
                });
                wps2PDFHandler.start();
            }
            wps2PDFHandler.process(mSelectedFile);
            mProgress.setVisible(false);
            mAnalysisBtn.setEnabled(true);
            if (fileSet != null) {
                mUnsupportedFileBtn.setIcon(SystemConstants._imglocfileon);
                mUnsupportedFileBtn.setEnabled(true);
            }
        }
    }
}
