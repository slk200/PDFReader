package org.tizzer.pdfreader.view;

import com.bulenkov.darcula.DarculaLaf;
import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.util.ThreadPool;
import org.tizzer.pdfreader.util.WPS2PDFHandler;
import org.tizzer.pdfreader.util.callback.CountListener;
import org.tizzer.pdfreader.view.dialog.SettingDialog;
import org.tizzer.pdfreader.view.dialog.TaoBaoDialog;
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
    private JMenuItem mSettingItem;
    private JMenuItem mExitItem;
    private JMenu mThemeMenu;
    private JRadioButtonMenuItem mSystemItem;
    private JRadioButtonMenuItem mDarculaItem;
    private JMenu mHelpMenu;
    private JMenuItem mInfoItem;
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

        mSettingItem = new JMenuItem(SystemConstants.SETTING);
        mSettingItem.setIcon(SystemConstants._imgprop);
        mSettingItem.setMnemonic(KeyEvent.VK_P);
        mSettingItem.setMnemonic('S');

        mExitItem = new JMenuItem(SystemConstants.EXIT);
        mExitItem.setMnemonic(KeyEvent.VK_E);
        mExitItem.setMnemonic('E');

        mThemeMenu = new JMenu("主题 (T)");
        mThemeMenu.setMnemonic(KeyEvent.VK_T);
        mThemeMenu.setMnemonic('T');
        mDarculaItem = new JRadioButtonMenuItem("Darcula");
        mDarculaItem.setSelected(true);
        mSystemItem = new JRadioButtonMenuItem("System");

        mHelpMenu = new JMenu("帮助 (H)");
        mHelpMenu.setMnemonic(KeyEvent.VK_H);
        mHelpMenu.setMnemonic('H');
        mInfoItem = new JMenuItem("宝贝 (I)");
        mInfoItem.setIcon(SystemConstants._imgsale);
        mInfoItem.setMnemonic(KeyEvent.VK_I);
        mInfoItem.setMnemonic('I');


        mFilePanel = new FilePanel();
        mConsolePanel = new ConsolePanel();
    }

    /**
     * initialize layout
     */
    private void initLayout() {
        this.setJMenuBar(mMenuBar);
        mMenuBar.add(mFileMenu);
        mMenuBar.add(mThemeMenu);
        mMenuBar.add(mHelpMenu);

        mFileMenu.add(mSettingItem);
        mFileMenu.addSeparator();
        mFileMenu.add(mExitItem);
        mThemeMenu.add(mDarculaItem);
        mThemeMenu.add(mSystemItem);
        mHelpMenu.add(mInfoItem);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(mDarculaItem);
        buttonGroup.add(mSystemItem);

        this.setLayout(new GridLayout(1, 2));
        this.add(mFilePanel);
        this.add(mConsolePanel);
    }

    /**
     * initialize actions
     */
    private void initListeners() {
        mSettingItem.addActionListener(event -> {
            ThreadPool.submit(() -> SettingDialog.display(Window.this));
        });
        mExitItem.addActionListener(event -> {
            System.exit(0);
        });
        mDarculaItem.addActionListener(event -> {
            if (!UIManager.getLookAndFeel().getName().equals(DarculaLaf.NAME)) {
                try {
                    UIManager.setLookAndFeel(new DarculaLaf());
                    SwingUtilities.updateComponentTreeUI(this);
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                System.out.println("do it 1");
            }
        });
        mSystemItem.addActionListener(event -> {
            if (!UIManager.getLookAndFeel().getName().equals(UIManager.getSystemLookAndFeelClassName())) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    SwingUtilities.updateComponentTreeUI(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("do it 2");
            }

        });
        mInfoItem.addActionListener(event -> {
            TaoBaoDialog.newInstance();
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
