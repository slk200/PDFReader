package org.tizzer.counttool.constant;

import javafx.scene.image.Image;

/**
 * Created by tizzer on 2019/1/21.
 */
public interface ImageSource {
    Image LOGO = new Image(ImageSource.class.getResourceAsStream("/image/logo@256px.png"));

    Image NOTRADE = new Image(ImageSource.class.getResourceAsStream("/image/paint2.png"));
    Image NOEXTRA = new Image(ImageSource.class.getResourceAsStream("/image/paint1.png"));

    Image FAILED = new Image(ImageSource.class.getResourceAsStream("/image/failed@16px.png"));
    Image READY = new Image(ImageSource.class.getResourceAsStream("/image/ready@16px.png"));
    Image DONE = new Image(ImageSource.class.getResourceAsStream("/image/done@16px.png"));
    Image NONEED = new Image(ImageSource.class.getResourceAsStream("/image/noneed@16px.png"));

    Image WORD = new Image(ImageSource.class.getResourceAsStream("/image/word@16px.png"));
    Image EXCEL = new Image(ImageSource.class.getResourceAsStream("/image/excel@16px.png"));
    Image PPT = new Image(ImageSource.class.getResourceAsStream("/image/ppt@16px.png"));
    Image PDF = new Image(ImageSource.class.getResourceAsStream("/image/pdf@16px.png"));
    Image PIC = new Image(ImageSource.class.getResourceAsStream("/image/pic@16px.png"));
    Image OTHER = new Image(ImageSource.class.getResourceAsStream("/image/other@16px.png"));

    Image PAUSE = new Image(ImageSource.class.getResourceAsStream("/image/pause@16px.png"));
    Image PLAY = new Image(ImageSource.class.getResourceAsStream("/image/play@16px.png"));
}
