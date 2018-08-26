package org.tizzer.pdfreader.view.panel;

import org.tizzer.pdfreader.constants.RuntimeConstants;
import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.layout.TableLayout;
import org.tizzer.pdfreader.view.Theme;

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
        mFilePath = new JTextField();
        mFilePath.setEditable(false);
        mFilePath.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (RuntimeConstants.currentDirectory != null) {
            this.mSelectedFile = new File(RuntimeConstants.currentDirectory);
            mFilePath.setText(RuntimeConstants.currentDirectory);
        }

        mScanBtn = new JButton(SystemConstants.SCAN);

        mAnalysisBtn = new JButton(SystemConstants.ANALYSIS);

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

        mPptLabel = new JLabel(SystemConstants.PPT);

        mExcelLabel = new JLabel(SystemConstants.EXCEL);

        mPDFLabel = new JLabel(SystemConstants.PDF);

        mPageLabel = new JLabel(SystemConstants.PAGE);

        mCalcBtn = new JButton(SystemConstants.CALC);
    }

    /**
     * initialize layout
     */
    private void initLayout() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double g = 5;
        this.setLayout(new TableLayout(new double[][]{{g, f, g, f, g, f, g, f, g}, {g, p, g, p, g, f, g, p, g, p, g}}));
        this.add(mFilePath, "1,1,3,1");
        this.add(mScanBtn, "5,1");
        this.add(mAnalysisBtn, "7,1");
        this.add(mProgress, "1,3,7,3");
        this.add(mTablePane, "1,5,7,5");
        this.add(mWordLabel, "1,7");
        this.add(mPptLabel, "3,7");
        this.add(mExcelLabel, "5,7");
        this.add(mPDFLabel, "7,7");
        this.add(mPageLabel, "1,9,7,9");
        this.add(mCalcBtn, "5,9,7,9");
    }

    /**
     * initialize actions
     */
    private void initListeners() {
        mScanBtn.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(mSelectedFile);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.showDialog(mScanBtn.getRootPane(), SystemConstants.CONFIRM);
            if (fileChooser.getSelectedFile() != null) {
                mSelectedFile = fileChooser.getSelectedFile();
                mFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        mFilePath.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(mSelectedFile);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showDialog(mFilePath.getRootPane(), SystemConstants.CONFIRM);
                if (fileChooser.getSelectedFile() != null) {
                    mSelectedFile = fileChooser.getSelectedFile();
                    mFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
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

    /**
     * perform the components' icon
     *
     * @param theme
     */
    public void performThemeChanged(Theme theme) {
        switch (theme) {
            case LIGHT:
                mScanBtn.setIcon(SystemConstants._imgfolderdark);
                mAnalysisBtn.setIcon(SystemConstants._imgstartdark);
                mWordLabel.setIcon(SystemConstants._imgworddark);
                mPptLabel.setIcon(SystemConstants._imgpptdark);
                mExcelLabel.setIcon(SystemConstants._imgexceldark);
                mPDFLabel.setIcon(SystemConstants._imgpdfdark);
                mPageLabel.setIcon(SystemConstants._imgpagedark);
                mCalcBtn.setIcon(SystemConstants._imgcalcdark);
                break;
            case DARK:
                mScanBtn.setIcon(SystemConstants._imgfolder);
                mAnalysisBtn.setIcon(SystemConstants._imgstart);
                mWordLabel.setIcon(SystemConstants._imgword);
                mPptLabel.setIcon(SystemConstants._imgppt);
                mExcelLabel.setIcon(SystemConstants._imgexcel);
                mPDFLabel.setIcon(SystemConstants._imgpdf);
                mPageLabel.setIcon(SystemConstants._imgpage);
                mCalcBtn.setIcon(SystemConstants._imgcalc);
                break;
        }
    }
}
