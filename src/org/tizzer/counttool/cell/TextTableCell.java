package org.tizzer.counttool.cell;

import javafx.geometry.Pos;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;

/**
 * Created by tizzer on 2019/1/21.
 */
public class TextTableCell<S> extends TableCell<S, String> {

    private Pos pos;

    public TextTableCell() {
        this(Pos.CENTER_LEFT);
    }

    public TextTableCell(Pos pos) {
        this.pos = pos;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setAlignment(pos);
            setText(item);
            setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
            setTooltip(new Tooltip(item));
        }
    }
}
