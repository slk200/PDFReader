package org.tizzer.counttool.action;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

/**
 * Created by tizzer on 2019/1/21.
 */
public class DoubleEditorAction implements EventHandler<ActionEvent> {

    private TextField editor;

    public DoubleEditorAction(TextField editor) {
        this.editor = editor;
    }

    @Override
    public void handle(ActionEvent event) {
        String text = editor.getText().trim();
        if (text.isEmpty() || !text.matches("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|^[1-9]\\d*")) {
            editor.setText("0.01");
        }
    }
}
