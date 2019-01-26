package org.tizzer.counttool.cell;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;

/**
 * Created by tizzer on 2019/01/25.
 */
public class IntegerTableCell<S> extends TableCell<S, Integer> {

    private Spinner<Integer> spinner;

    @Override
    public void startEdit() {
        super.startEdit();
        createSpinner();
        spinner.getEditor().setText(String.valueOf(getItem()));
        setText(null);
        setGraphic(spinner);
        spinner.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(String.valueOf(getItem()));
        setGraphic(null);
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText(null);
            setGraphic(null);
        } else {
            createSpinner();
            setText(String.valueOf(item));
        }
    }


    private void createSpinner() {
        if (spinner == null) {
            spinner = new Spinner<>();
            spinner.setEditable(true);
            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,
                    1, 1));
            spinner.getEditor().setOnAction(event -> {
                String text = spinner.getEditor().getText().trim();
                if (text.isEmpty() || !text.matches("^[1-9]\\d*")) {
                    spinner.getEditor().setText("1");
                    commitEdit(1);
                } else {
                    commitEdit(Integer.valueOf(text));
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
