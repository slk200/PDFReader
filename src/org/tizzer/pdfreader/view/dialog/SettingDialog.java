package org.tizzer.pdfreader.view.dialog;

import org.tizzer.pdfreader.constants.SystemConstants;
import org.tizzer.pdfreader.util.PropParser;

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

    public static void display(Component component) {
        SettingDialog settingDialog = new SettingDialog();
        settingDialog.setLocationRelativeTo(component);
        settingDialog.setVisible(true);
    }

    private SettingDialog() {
        initComponents();
        initLayout();
        initListeners();
        initProperties();
    }

    private void initComponents() {
        mFileDirectory = new JTextField(20);
        mFileDirectory.setEditable(false);
        mFileDirectory.setBackground(SystemConstants._fieldcolor);
        mFileDirectory.setText(SystemConstants._cuurrentdirectory != null && new File(SystemConstants._cuurrentdirectory).exists() ? SystemConstants._cuurrentdirectory : null);
        mScanBtn = new JButton(SystemConstants._scan);
        mScanBtn.setIcon(SystemConstants._imgfolder);
        mFileChooser = new JFileChooser();
        mFileChooser.setCurrentDirectory(SystemConstants._cuurrentdirectory != null && new File(SystemConstants._cuurrentdirectory).exists() ? new File(SystemConstants._cuurrentdirectory) : null);
        mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    private void initLayout() {
        JPanel bgPane = new JPanel();
        bgPane.setBorder(new EmptyBorder(SystemConstants._gap, SystemConstants._gap, SystemConstants._gap, SystemConstants._gap));
        bgPane.add(mFileDirectory);
        bgPane.add(mScanBtn);
        this.add(bgPane);
    }

    private void initListeners() {
        mFileDirectory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                mFileChooser.showDialog(SettingDialog.this, SystemConstants._confirm);
                if (mFileChooser.getSelectedFile() != null) {
                    PropParser.writeProp(mFileChooser.getSelectedFile().getAbsolutePath());
                    mFileDirectory.setText(mFileChooser.getSelectedFile().getAbsolutePath());
                    SystemConstants._cuurrentdirectory = mFileChooser.getSelectedFile().getAbsolutePath();
                }
            }
        });

        mScanBtn.addActionListener(event -> {
            mFileChooser.showDialog(SettingDialog.this, SystemConstants._confirm);
            if (mFileChooser.getSelectedFile() != null) {
                PropParser.writeProp(mFileChooser.getSelectedFile().getAbsolutePath());
                mFileDirectory.setText(mFileChooser.getSelectedFile().getAbsolutePath());
                SystemConstants._cuurrentdirectory = mFileChooser.getSelectedFile().getAbsolutePath();
            }
        });
    }

    private void initProperties() {
        this.setTitle(SystemConstants._preference);
        this.setIconImage(SystemConstants._imglogo.getImage());
        this.pack();
        this.setModal(true);
    }
}
