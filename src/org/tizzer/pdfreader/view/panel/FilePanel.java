package org.tizzer.pdfreader.view.panel;

import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.layout.TableLayout;
import org.tizzer.pdfreader.util.PropParser;
import org.tizzer.pdfreader.view.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class FilePanel extends JPanel {
    //components
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
    //selected file
    private File mSelectedFile;
    //flag of scrolling to bottom
    private int isTableNeedBottom = 0;

    public FilePanel() {
        initComponents();
        initLayout();
        initListeners();
    }

    /**
     * initialize components
     */
    private void initComponents() {
        Window.currentDirectory = PropParser.readProp();
        mFilePath = new JTextField();
        mFilePath.setEditable(false);
        mFilePath.setCursor(new Cursor(Cursor.HAND_CURSOR));
        File tempFile = new File(Window.currentDirectory);
        if (Window.currentDirectory != null) {
            if (tempFile.exists()) {
                this.mSelectedFile = tempFile;
                mFilePath.setText(Window.currentDirectory);
            }
        }

        mScanBtn = new JButton(SystemConstants.SCAN);
        mScanBtn.setIcon(SystemConstants._imgfolder);

        mAnalysisBtn = new JButton(SystemConstants.ANALYSIS);
        mAnalysisBtn.setIcon(SystemConstants._imgstart);

        mFileChooser = new JFileChooser();
        mFileChooser.setCurrentDirectory(mSelectedFile);
        mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        mTableModel = new DefaultTableModel(null, SystemConstants.TABLE_TITLE);
        JTable table = new JTable(mTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFont(SystemConstants.defaultFont);
        table.setRowHeight(30);
        table.getTableHeader().setFont(SystemConstants.defaultFont.deriveFont(Font.BOLD));
        table.getTableHeader().setReorderingAllowed(false);
        mTablePane = new JScrollPane(table);

        mProgress = new JProgressBar();
        mProgress.setString(SystemConstants.ANALYSING);
        mProgress.setStringPainted(true);
        mProgress.setIndeterminate(true);
        mProgress.setVisible(false);

        mWordLabel = new JLabel(SystemConstants.WORD);
        mWordLabel.setIcon(SystemConstants._imgword);

        mPptLabel = new JLabel(SystemConstants.PPT);
        mPptLabel.setIcon(SystemConstants._imgppt);

        mExcelLabel = new JLabel(SystemConstants.EXCEL);
        mExcelLabel.setIcon(SystemConstants._imgexcel);

        mPDFLabel = new JLabel(SystemConstants.PDF);
        mPDFLabel.setIcon(SystemConstants._imgpdf);

        mPageLabel = new JLabel(SystemConstants.PAGE);
        mPageLabel.setIcon(SystemConstants._imgpage);

        mCalcBtn = new JButton(SystemConstants.CALC);
        mCalcBtn.setIcon(SystemConstants._imgcalc);
    }

    /**
     * initialize layout
     */
    private void initLayout() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double g = 5;
        this.setLayout(new TableLayout(new double[][]{{g, f, g, f, g, f, g, f, g}, {g, p, g, f, g, p, g, p, g}}));
        this.add(mFilePath, "1,1,3,1");
        this.add(mScanBtn, "5,1");
        this.add(mAnalysisBtn, "7,1");
        this.add(mTablePane, "1,3,7,3");
        this.add(mWordLabel, "1,5");
        this.add(mPptLabel, "3,5");
        this.add(mExcelLabel, "5,5");
        this.add(mPDFLabel, "7,5");
        this.add(mPageLabel, "1,7");
        this.add(mCalcBtn, "5,7,7,7");
    }

    /**
     * initialize actions
     */
    private void initListeners() {
        mScanBtn.addActionListener(event -> {
            mFileChooser.showDialog(mScanBtn.getRootPane(), SystemConstants.CONFIRM);
            if (mFileChooser.getSelectedFile() != null) {
                mSelectedFile = mFileChooser.getSelectedFile();
                mFilePath.setText(mFileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        mFilePath.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                mFileChooser.showDialog(mFilePath.getRootPane(), SystemConstants.CONFIRM);
                if (mFileChooser.getSelectedFile() != null) {
                    mSelectedFile = mFileChooser.getSelectedFile();
                    mFilePath.setText(mFileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        mCalcBtn.addActionListener(event -> {
            try {
                Runtime.getRuntime().exec(SystemConstants.COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        mTablePane.getVerticalScrollBar().addAdjustmentListener(event -> {
            if (event.getAdjustmentType() == AdjustmentEvent.TRACK && isTableNeedBottom <= 3) {
                mTablePane.getVerticalScrollBar().setValue(mTablePane.getVerticalScrollBar().getModel().getMaximum()
                        - mTablePane.getVerticalScrollBar().getModel().getExtent());
                isTableNeedBottom++;
            }
        });
    }

    /**
     * set analysis button's action
     *
     * @param action
     */
    public void setAnalysisAction(ActionListener action) {
        mAnalysisBtn.addActionListener(action);
    }

    /**
     * reset all components' status
     */
    public void reset() {
        mTableModel.setDataVector(null, SystemConstants.TABLE_TITLE);
        mWordLabel.setText(SystemConstants.WORD);
        mPptLabel.setText(SystemConstants.PPT);
        mExcelLabel.setText(SystemConstants.EXCEL);
        mPDFLabel.setText(SystemConstants.PDF);
        mPageLabel.setText(SystemConstants.PAGE);
    }

    /**
     * get the file selected
     *
     * @return
     */
    public File getSelectedFile() {
        return mSelectedFile;
    }

    /**
     * while handler is running, count the number of word documents
     *
     * @param num
     */
    public void countWORD(int num) {
        mWordLabel.setText(SystemConstants.WORD + num);
    }

    /**
     * while handler is running, count the number of ppt documents
     *
     * @param num
     */
    public void countPPT(int num) {
        mPptLabel.setText(SystemConstants.PPT + num);
    }

    /**
     * while handler is running, count the number of excel documents
     *
     * @param num
     */
    public void countEXCEL(int num) {
        mExcelLabel.setText(SystemConstants.EXCEL + num);
    }

    /**
     * while handler is running, count the number of pdf documents
     *
     * @param num
     */
    public void countPDF(int num) {
        mPDFLabel.setText(SystemConstants.PDF + num);
    }

    /**
     * while handler is running, count the page of pdf documents
     *
     * @param filename
     * @param page
     * @param num
     */
    public void countPages(String filename, int page, int num) {
        isTableNeedBottom = 0;
        mTableModel.addRow(new Object[]{filename, page});
        mPageLabel.setText(SystemConstants.PAGE + num);
    }

    /**
     * do task before handler is starting
     */
    public void startTask() {
        mProgress.setVisible(true);
        mAnalysisBtn.setEnabled(false);
    }

    /**
     * do task after handler is over
     */
    public void overTask() {
        mProgress.setVisible(false);
        mAnalysisBtn.setEnabled(true);
    }
}
