package org.tizzer.counttool.cell;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import org.tizzer.counttool.constant.FileType;
import org.tizzer.counttool.constant.ImageSource;

/**
 * Created by tizzer on 2019/1/21.
 */
public class FileTypeTableCell<S> extends TableCell<S, FileType> {

    @Override
    protected void updateItem(FileType item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setAlignment(Pos.CENTER);
            switch (item) {
                case PDF:
                    setGraphic(new ImageView(ImageSource.PDF));
                    break;
                case WORD:
                    setGraphic(new ImageView(ImageSource.WORD));
                    break;
                case PIC:
                    setGraphic(new ImageView(ImageSource.PIC));
                    break;
                case EXCEL:
                    setGraphic(new ImageView(ImageSource.EXCEL));
                    break;
                case PPT:
                    setGraphic(new ImageView(ImageSource.PPT));
                    break;
                case OTHER:
                    setGraphic(new ImageView(ImageSource.OTHER));
                    break;
                default:
            }
        }
    }
}
