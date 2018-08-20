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
    private JFileChooser mFileChooser;

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

        mFileChooser = new JFileChooser();
        mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (Window.currentDirectory != null) {
            File tempFile = new File(Window.currentDirectory);
            if (tempFile.exists()) {
                mFileDirectory.setText(Window.currentDirectory);
                mFileChooser.setCurrentDirectory(tempFile);
            }
        }
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
                mFileChooser.showDialog(SettingDialog.this, SystemConstants.CONFIRM);
                if (mFileChooser.getSelectedFile() != null) {
                    PropParser.writeProp(mFileChooser.getSelectedFile().getAbsolutePath());
                    mFileDirectory.setText(mFileChooser.getSelectedFile().getAbsolutePath());
                    Window.currentDirectory = mFileChooser.getSelectedFile().getAbsolutePath();
                }
            }
        });

        mScanBtn.addActionListener(event -> {
            mFileChooser.showDialog(SettingDialog.this, SystemConstants.CONFIRM);
            if (mFileChooser.getSelectedFile() != null) {
                PropParser.writeProp(mFileChooser.getSelectedFile().getAbsolutePath());
                mFileDirectory.setText(mFileChooser.getSelectedFile().getAbsolutePath());
                Window.currentDirectory = mFileChooser.getSelectedFile().getAbsolutePath();
            }
        });
    }

    private void initProperties() {
        this.setTitle(SystemConstants.PREFERENCE);
        this.pack();
        this.setModal(true);
    }
}
