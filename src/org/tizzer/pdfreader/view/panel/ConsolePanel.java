package org.tizzer.pdfreader.view.panel;

import org.tizzer.pdfreader.constants.RuntimeConstants;
import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.layout.TableLayout;
import org.tizzer.pdfreader.util.ThreadPool;
import org.tizzer.pdfreader.view.Theme;
import org.tizzer.pdfreader.view.dialog.UnsupportedFileDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.util.HashSet;
import java.util.Set;

public class ConsolePanel extends JPanel {
    //components
    private JTextArea mConsole;
    private JScrollPane mConsolePane;
    private JLabel mErrorLabel;
    private JButton mUnsupportedFileBtn;
    //file set
    private Set<String> fileSet;
    //flag of scrolling to bottom
    private int isConsoleNeedBottom = 0;
    //flag of state
    private boolean isOn;

    public ConsolePanel() {
        initComponents();
        initLayout();
        initListeners();
    }

    /**
     * initialize components
     */
    private void initComponents() {
        mErrorLabel = new JLabel(SystemConstants.NO_ERROR);

        mUnsupportedFileBtn = new JButton(SystemConstants.LOC_FILE);
        mUnsupportedFileBtn.setEnabled(false);

        mConsole = new JTextArea(0, 40);
        mConsole.setEditable(false);
        mConsole.setBackground(Color.BLACK);
        mConsole.setForeground(Color.GREEN);
        mConsole.setLineWrap(true);
        mConsole.setWrapStyleWord(true);
        mConsolePane = new JScrollPane(mConsole);
    }

    /**
     * initialize layout
     */
    private void initLayout() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double g = 5;
        this.setLayout(new TableLayout(new double[][]{{g, f, g, f, g, f, g, f, g}, {g, p, g, p, g, f, g}}));
        this.add(mErrorLabel, "1,1,3,1,l,c");
        this.add(mUnsupportedFileBtn, "5,1,7,1,r,c");
        this.add(new JLabel(SystemConstants.CONSOLE), "3,3,5,3,c,c");
        this.add(mConsolePane, "1,5,7,5");
    }

    /**
     * initialize actions
     */
    private void initListeners() {
        mConsolePane.getVerticalScrollBar().addAdjustmentListener(event -> {
            if (event.getAdjustmentType() == AdjustmentEvent.TRACK && isConsoleNeedBottom <= 3) {
                mConsolePane.getVerticalScrollBar().setValue(mConsolePane.getVerticalScrollBar().getModel().getMaximum()
                        - mConsolePane.getVerticalScrollBar().getModel().getExtent());
                isConsoleNeedBottom++;
            }
        });

        mUnsupportedFileBtn.addActionListener(event -> {
            ThreadPool.submit(() -> {
                mUnsupportedFileBtn.setEnabled(false);
                StringBuilder unsupportedFileHtml = new StringBuilder("<body bgcolor=#3c3f41>");
                for (String file : fileSet) {
                    unsupportedFileHtml.append("<a href='file://")
                            .append(file)
                            .append("' style='color:white'>")
                            .append(file)
                            .append("</a><br/><br/>");
                }
                UnsupportedFileDialog.display(mUnsupportedFileBtn.getRootPane(), unsupportedFileHtml);
                mUnsupportedFileBtn.setEnabled(true);
                unsupportedFileHtml = null;
            });
        });
    }

    /**
     * reset all components' status
     */
    public void reset() {
        isOn = false;
        mErrorLabel.setText(SystemConstants.NO_ERROR);
        performThemeChanged(RuntimeConstants.currentTheme);
        mUnsupportedFileBtn.setEnabled(false);
        mConsole.setText(null);
        fileSet = null;
    }

    /**
     * receive message and scroll to bottom
     *
     * @param message
     */
    public void receiveAndScroll(String message) {
        isConsoleNeedBottom = 0;
        mConsole.append(message);
    }

    /**
     * while handler is running, if unsupported file
     * appearing, call it
     *
     * @param directory
     */
    public void processTask(String directory) {
        isOn = true;
        if (fileSet == null) {
            fileSet = new HashSet<>();
            mErrorLabel.setText(SystemConstants.EXIST_ERROR);
            mErrorLabel.setIcon(SystemConstants._imgerroron);
        }
        fileSet.add(directory);
    }

    /**
     * do task after handler is over
     */
    public void overTask() {
        if (fileSet != null) {
            mUnsupportedFileBtn.setIcon(SystemConstants._imglocfileon);
            mUnsupportedFileBtn.setEnabled(true);
        }
    }

    public void performThemeChanged(Theme theme) {
        if (!isOn) {
            switch (theme) {
                case LIGHT:
                    mErrorLabel.setIcon(SystemConstants._imgerroroffdark);
                    mUnsupportedFileBtn.setIcon(SystemConstants._imglocfileoffdark);
                    break;
                case DARK:
                    mErrorLabel.setIcon(SystemConstants._imgerroroff);
                    mUnsupportedFileBtn.setIcon(SystemConstants._imglocfileoff);
                    break;
            }
        }
    }
}
