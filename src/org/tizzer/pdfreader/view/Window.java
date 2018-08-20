package org.tizzer.pdfreader.view;

import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.util.ThreadPool;
import org.tizzer.pdfreader.util.WPS2PDFHandler;
import org.tizzer.pdfreader.util.callback.CountListener;
import org.tizzer.pdfreader.view.dialog.SettingDialog;
import org.tizzer.pdfreader.view.panel.ConsolePanel;
import org.tizzer.pdfreader.view.panel.FilePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Window extends JFrame {
    //global scope
    public static String currentDirectory;
    //menu
    private JMenuBar mMenuBar;
    private JMenu mFileMenu;
    private JMenuItem mSettingMenuItem;
    private JMenuItem mExitMenuItem;
    //components
    private FilePanel mFilePanel;
    private ConsolePanel mConsolePanel;
    //handler
    private ProcessTask processTask;
    private WPS2PDFHandler wps2PDFHandler;

    public Window() {
        initComponents();
        initLayout();
        initListeners();
        open();
    }

    /**
     * initialize components
     */
    private void initComponents() {
        mMenuBar = new JMenuBar();

        mFileMenu = new JMenu(SystemConstants.FILE);
        mFileMenu.setMnemonic(KeyEvent.VK_F);
        mFileMenu.setMnemonic('F');

        mSettingMenuItem = new JMenuItem(SystemConstants.SETTING);
        mSettingMenuItem.setIcon(SystemConstants._imgprop);
        mSettingMenuItem.setMnemonic(KeyEvent.VK_P);
        mSettingMenuItem.setMnemonic('S');

        mExitMenuItem = new JMenuItem(SystemConstants.EXIT);
        mExitMenuItem.setMnemonic(KeyEvent.VK_E);
        mExitMenuItem.setMnemonic('E');

        mFilePanel = new FilePanel();
        mConsolePanel = new ConsolePanel();
    }

    /**
     * initialize layout
     */
    private void initLayout() {
        mMenuBar.add(mFileMenu);
        mFileMenu.add(mSettingMenuItem);
        mFileMenu.addSeparator();
        mFileMenu.add(mExitMenuItem);
        this.setJMenuBar(mMenuBar);

        this.setLayout(new GridLayout(1, 2));
        this.add(mFilePanel);
        this.add(mConsolePanel);
    }

    /**
     * initialize actions
     */
    private void initListeners() {
        mSettingMenuItem.addActionListener(event -> {
            ThreadPool.submit(() -> SettingDialog.display(Window.this));
        });

        mExitMenuItem.addActionListener(event -> {
            System.exit(0);
        });
        mFilePanel.setAnalysisAction(event -> {
            mFilePanel.reset();
            mConsolePanel.reset();
            if (processTask == null) {
                processTask = new ProcessTask();
                wps2PDFHandler = new WPS2PDFHandler();
            } else {
                wps2PDFHandler.reset();
            }
            ThreadPool.submit(processTask);
        });
    }

    /**
     * initialize window's properties and
     * open a window
     */
    private void open() {
        this.setTitle(SystemConstants.TITLE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * a task for transform WPS document to PDF document
     */
    private class ProcessTask implements Runnable {

        @Override
        public void run() {
            mFilePanel.startTask();
            if (!wps2PDFHandler.isStarted()) {
                wps2PDFHandler.addLogListener(message -> {
                    mConsolePanel.receiveAndScroll(message);
                });
                wps2PDFHandler.addCountListener(new CountListener() {
                    @Override
                    public void countWORD(int num) {
                        mFilePanel.countWORD(num);
                    }

                    @Override
                    public void countPPT(int num) {
                        mFilePanel.countPPT(num);
                    }

                    @Override
                    public void countExcel(int num) {
                        mFilePanel.countEXCEL(num);
                    }

                    @Override
                    public void countPDF(int num) {
                        mFilePanel.countPDF(num);
                    }

                    @Override
                    public void countPages(String filename, int page, int num) {
                        mFilePanel.countPages(filename, page, num);
                    }
                });
                wps2PDFHandler.addProcessListener(directory -> {
                    mConsolePanel.processTask(directory);
                });
                wps2PDFHandler.start();
            }
            wps2PDFHandler.process(mFilePanel.getSelectedFile());
            mFilePanel.overTask();
            mConsolePanel.overTask();
        }
    }
}
