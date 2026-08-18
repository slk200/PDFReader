package controller;

import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by tizzer on 2019/02/06.
 */
public class AboutController {
    public void visit_JavaFX() {
        visitWebPage("https://gluonhq.com/products/javafx/");
    }

    public void visit_Jcob() {
        visitWebPage("https://github.com/freemansoft/jacob-project");
    }

    public void visit_iText() {
        visitWebPage("https://itextpdf.com/");
    }

    public void visit_SQLLite() {
        visitWebPage("https://sqlite.org/index.html");
    }

    public void visit_github() {
        visitWebPage("https://github.com/slk200/PDFReader");
    }

    private void visitWebPage(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
