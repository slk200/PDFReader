package org.tizzer.pdfreader.constants;

import javax.swing.*;
import java.awt.*;

public interface SystemConstants {
    //table's title
    String[] TABLE_TITLE = {"文件名", "页数"};

    String TITLE = "文档页数统计";
    String FILE = "文件 (F)";
    String SETTING = "设置 (S)";
    String EXIT = "退出 (E)";
    String SCAN = "浏览";
    String PREFERENCE = "首选项";
    String CONFIRM = "确定";
    String ANALYSIS = "分析";
    String ANALYSING = "分析中...";
    String CALC = "计算器";
    String COMMAND = "calc";
    String WORD = "文档数：";
    String PPT = "演示数：";
    String EXCEL = "表格数：";
    String PDF = "PDF数：";
    String PAGE = "总页数：";
    String CONSOLE = "控制台";
    String NO_ERROR = "当前没有无法转换的文件";
    String EXIST_ERROR = "当前存在无法转换的文件";
    String LOC_FILE = "查看文件所在目录";
    String DIALOG_TITLE = "无法转换的文件目录";
    String TEXT_HTML = "text/html";

    ImageIcon _imgprop = new ImageIcon("image/prop.png");
    ImageIcon _imgsale = new ImageIcon("image/sale.png");
    ImageIcon _imgtaobao = new ImageIcon("image/taobao.jpg");
    ImageIcon _imgfolder = new ImageIcon("image/folder.png");
    ImageIcon _imgstart = new ImageIcon("image/start.png");
    ImageIcon _imgerroroff = new ImageIcon("image/error_off.png");
    ImageIcon _imgerroron = new ImageIcon("image/error_on.png");
    ImageIcon _imglocfileoff = new ImageIcon("image/locfile_off.png");
    ImageIcon _imglocfileon = new ImageIcon("image/locfile_on.png");
    ImageIcon _imgword = new ImageIcon("image/word.png");
    ImageIcon _imgppt = new ImageIcon("image/ppt.png");
    ImageIcon _imgexcel = new ImageIcon("image/excel.png");
    ImageIcon _imgpdf = new ImageIcon("image/pdf.png");
    ImageIcon _imgpage = new ImageIcon("image/page.png");
    ImageIcon _imgcalc = new ImageIcon("image/calc.png");

    Color _fieldcolor = new Color(232, 232, 232);

    Font defaultFont = new Font("Microsoft YaHei", Font.PLAIN, 12);
}
