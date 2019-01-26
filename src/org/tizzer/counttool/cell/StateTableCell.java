package org.tizzer.counttool.cell;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import org.tizzer.counttool.constant.ImageSource;
import org.tizzer.counttool.constant.Signal;

/**
 * Created by tizzer on 2019/1/21.
 */
public class StateTableCell<S> extends TableCell<S, Signal> {

    @Override
    protected void updateItem(Signal item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setAlignment(Pos.CENTER);
            switch (item) {
                case READY:
                    setGraphic(new ImageView(ImageSource.READY));
                    setText("等待转换");
                    break;
                case NO_NEED:
                case ONLYCOUNT:
                    setGraphic(new ImageView(ImageSource.NONEED));
                    setText("无需转换");
                    break;
                case PENDINGANDCOUNT:
                    setGraphic(new ProgressBar());
                    setText("正在转换");
                    break;
                case DONE:
                    setGraphic(new ImageView(ImageSource.DONE));
                    setText("转换完成");
                    break;
                case FAILED:
                    setGraphic(new ImageView(ImageSource.FAILED));
                    setText("转换失败");
                    break;
                default:
            }
        }
    }
}
