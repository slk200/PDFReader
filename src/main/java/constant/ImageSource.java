package constant;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Created by tizzer on 2019/1/21.
 */
public interface ImageSource {
    Image LOGO = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/logo@256px.png")));

    Image NO_TRADE = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/paint2.png")));
    Image NO_EXTRA = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/paint1.png")));

    Image STATE_FAILED = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/failed@16px.png")));
    Image STATE_READY = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/ready@16px.png")));
    Image STATE_DONE = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/done@16px.png")));
    Image STATE_NO_NEED = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/no_need@16px.png")));

    Image OFFICE_DOC = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/office_doc@16px.png")));
    Image OFFICE_ELS = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/office_els@16px.png")));
    Image OFFICE_PPT = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/office_ppt@16px.png")));
    Image OFFICE_PDF = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/office_pdf@16px.png")));
    Image OFFICE_PIC = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/office_image@16px.png")));
    Image OFFICE_OTHER = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/office_other@16px.png")));

    Image CONVERT = new Image(Objects.requireNonNull(ImageSource.class.getResourceAsStream("/image/convert@64px.png")));
}
