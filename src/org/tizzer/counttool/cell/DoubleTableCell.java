package org.tizzer.counttool.cell;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;

/**
 * Created by tizzer on 2019/01/25.
 */
public class DoubleTableCell<S> extends TableCell<S, String> {

    private Spinner<Double> spinner;

    @Override
    public void startEdit() {
        super.startEdit();
        createSpinner();
        spinner.getEditor().setText(getItem());
        setText(null);
        setGraphic(spinner);
        spinner.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText(null);
            setGraphic(null);
        } else {
            createSpinner();
            setText(item);
        }
    }


    private void createSpinner() {
        if (spinner == null) {
            spinner = new Spinner<>();
            spinner.setEditable(true);
            spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, Double.MAX_VALUE,
                    0.01, 0.01));
            spinner.getEditor().setOnAction(event -> {
                String text = spinner.getEditor().getText().trim();
                if (text.isEmpty() || !text.matches("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|^[1-9]\\d*")) {
                    commitEdit("0.01");
                } else {
                    commitEdit(text);
                }
                cancelEdit();
            });
            spinner.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
        }
    }
}
