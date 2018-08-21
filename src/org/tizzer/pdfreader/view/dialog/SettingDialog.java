package org.tizzer.pdfreader.view.dialog;

import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.util.PropParser;
import org.tizzer.pdfreader.view.Window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class SettingDialog extends JDialog {

    private JTextField mFileDirectory;
    private JButton mScanBtn;

    private SettingDialog() {
        initComponents();
        initLayout();
        initListeners();
        initProperties();
    }

    public static void display(Component component) {
        SettingDialog settingDialog = new SettingDialog();
        settingDialog.setLocationRelativeTo(component);
        settingDialog.setVisible(true);
    }

    private void initComponents() {
        mFileDirectory = new JTextField(20);
        mFileDirectory.setEditable(false);
        mFileDirectory.setBackground(SystemConstants._fieldcolor);

        mScanBtn = new JButton(SystemConstants.SCAN);
        mScanBtn.setIcon(SystemConstants._imgfolder);
    }

    private void initLayout() {
        JPanel bgPane = new JPanel();
        bgPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        bgPane.add(mFileDirectory);
        bgPane.add(mScanBtn);
        this.add(bgPane);
    }

    private void initListeners() {
        mFileDirectory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (Window.currentDirectory != null) {
                    File tempFile = new File(Window.currentDirectory);
                    if (tempFile.exists()) {
                        mFileDirectory.setText(Window.currentDirectory);
                        fileChooser.setCurrentDirectory(tempFile);
                    }
                }
                fileChooser.showDialog(SettingDialog.this, SystemConstants.CONFIRM);
                if (fileChooser.getSelectedFile() != null) {
                    PropParser.writeProp(fileChooser.getSelectedFile().getAbsolutePath());
                    mFileDirectory.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    Window.currentDirectory = fileChooser.getSelectedFile().getAbsolutePath();
                }
            }
        });

        mScanBtn.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (Window.currentDirectory != null) {
                File tempFile = new File(Window.currentDirectory);
                if (tempFile.exists()) {
                    mFileDirectory.setText(Window.currentDirectory);
                    fileChooser.setCurrentDirectory(tempFile);
                }
            }
            fileChooser.showDialog(SettingDialog.this, SystemConstants.CONFIRM);
            if (fileChooser.getSelectedFile() != null) {
                PropParser.writeProp(fileChooser.getSelectedFile().getAbsolutePath());
                mFileDirectory.setText(fileChooser.getSelectedFile().getAbsolutePath());
                Window.currentDirectory = fileChooser.getSelectedFile().getAbsolutePath();
            }
        });
    }

    private void initProperties() {
        this.setTitle(SystemConstants.PREFERENCE);
        this.pack();
        this.setModal(true);
    }
}
