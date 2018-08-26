package org.tizzer.pdfreader.view.dialog;

import org.tizzer.pdfreader.constants.RuntimeConstants;
import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.entity.Prop;
import org.tizzer.pdfreader.util.PropParser;
import org.tizzer.pdfreader.view.Theme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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
        mFileDirectory.setText(RuntimeConstants.currentDirectory);
        mFileDirectory.setEditable(false);
        mFileDirectory.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mFileDirectory.setBackground(SystemConstants.fieldcolor);

        mScanBtn = new JButton(SystemConstants.SCAN);
        if (RuntimeConstants.currentTheme == Theme.DARK) {
            mScanBtn.setIcon(SystemConstants._imgfolder);
        } else {
            mScanBtn.setIcon(SystemConstants._imgfolderdark);
        }
    }

    private void initLayout() {
        JPanel bgPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        bgPane.setBorder(new TitledBorder(SystemConstants.SET_DEFAULT));
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
                if (RuntimeConstants.currentDirectory != null) {
                    mFileDirectory.setText(RuntimeConstants.currentDirectory);
                    fileChooser.setCurrentDirectory(new File(RuntimeConstants.currentDirectory));
                }
                fileChooser.showDialog(SettingDialog.this, SystemConstants.CONFIRM);
                if (fileChooser.getSelectedFile() != null) {
                    PropParser.writeProp(new Prop(fileChooser.getSelectedFile().getAbsolutePath(), RuntimeConstants.currentTheme));
                    mFileDirectory.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    RuntimeConstants.currentDirectory = fileChooser.getSelectedFile().getAbsolutePath();
                }
                fileChooser = null;
            }
        });

        mScanBtn.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (RuntimeConstants.currentDirectory != null) {
                mFileDirectory.setText(RuntimeConstants.currentDirectory);
                fileChooser.setCurrentDirectory(new File(RuntimeConstants.currentDirectory));
            }
            fileChooser.showDialog(SettingDialog.this, SystemConstants.CONFIRM);
            if (fileChooser.getSelectedFile() != null) {
                PropParser.writeProp(new Prop(fileChooser.getSelectedFile().getAbsolutePath(), RuntimeConstants.currentTheme));
                mFileDirectory.setText(fileChooser.getSelectedFile().getAbsolutePath());
                RuntimeConstants.currentDirectory = fileChooser.getSelectedFile().getAbsolutePath();
            }
            fileChooser = null;
        });
    }

    private void initProperties() {
        this.setTitle(SystemConstants.PREFERENCE);
        this.setResizable(false);
        this.pack();
        this.setModal(true);
    }
}
