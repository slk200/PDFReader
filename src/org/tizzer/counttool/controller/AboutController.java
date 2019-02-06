package org.tizzer.counttool.controller;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by tizzer on 2019/02/06.
 */
public class AboutController {
    public void visitJavaFX() {
        visitWebPage("https://docs.oracle.com/javase/8/javase-clienttechnologies.htm");
    }

    public void visitJcob() {
        visitWebPage("http://danadler.com/jacob/");
    }

    public void visitiText() {
        visitWebPage("https://itextpdf.com/en");
    }

    private void visitWebPage(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
