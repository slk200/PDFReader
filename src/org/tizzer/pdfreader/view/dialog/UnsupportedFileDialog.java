package org.tizzer.pdfreader.view.dialog;

import org.tizzer.pdfreader.constants.SystemConstants;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class UnsupportedFileDialog extends JDialog {

    private JEditorPane mEditorPane;

    public static void display(Component component, StringBuilder html) {
        UnsupportedFileDialog unsupportedFileDialog = new UnsupportedFileDialog(html);
        unsupportedFileDialog.setLocationRelativeTo(component);
        unsupportedFileDialog.setVisible(true);
    }

    private UnsupportedFileDialog(StringBuilder html) {
        initComponents(html);
        initLayout();
        initListeners();
        initProperties();
    }

    private void initComponents(StringBuilder html) {
        mEditorPane = new JEditorPane();
        mEditorPane.setBorder(null);
        mEditorPane.setContentType(SystemConstants.TEXT_HTML);
        mEditorPane.setEditable(false);
        mEditorPane.setText(html.toString());
        mEditorPane.setCaretPosition(0);
    }

    private void initLayout() {
        this.add(new JScrollPane(mEditorPane));
    }

    private void initListeners() {
        mEditorPane.addHyperlinkListener(event -> {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().open(new File(event.getURL().getFile()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void initProperties() {
        this.setTitle(SystemConstants.DIALOG_TITLE);
        this.setIconImage(SystemConstants._imglogo.getImage());
        this.setSize(640, 480);
        this.setModal(true);
    }
}
