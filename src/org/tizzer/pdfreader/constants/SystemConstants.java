package org.tizzer.pdfreader.constants;

import javax.swing.*;
import java.awt.*;

public interface SystemConstants {
    //table's title
    String[] TABLE_TITLE = {"文件名", "页数"};
    //hot key
    char CH_F = 'F';
    char CH_P = 'P';
    char CH_E = 'E';
    char CH_T = 'T';
    char CH_H = 'H';
    char CH_I = 'I';
    //components' text
    String TITLE = "文档页数统计";

    String FILE = "文件 (" + CH_F + ")";
    String SETTING = "设置 (" + CH_P + ")";
    String EXIT = "退出 (" + CH_E + ")";
    String THEME = "主题 (" + CH_T + ")";
    String DARCULA = "Darcula";
    String SYSTEM = "System";
    String HELP = "帮助 (" + CH_H + ")";
    String GOODS = "宝贝 (" + CH_I + ")";

    String PREFERENCE = "首选项";
    String SET_DEFAULT = "设置默认路径";

    String SCAN = "浏览";
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
    String UNSUPPORTED_FILE = "无法转换的文件目录";

    String GOODS_INFO = "宝贝信息";
    String TEXT_HTML = "text/html";
    //fixed images
    ImageIcon _imgtaobao = new ImageIcon("image/taobao.jpg");
    ImageIcon _imgerroron = new ImageIcon("image/error_on.png");
    ImageIcon _imglocfileon = new ImageIcon("image/locfile_on.png");
    //dark theme images
    ImageIcon _imgprop = new ImageIcon("image/light/prop.png");
    ImageIcon _imgsale = new ImageIcon("image/light/sale.png");
    ImageIcon _imgfolder = new ImageIcon("image/light/folder.png");
    ImageIcon _imgstart = new ImageIcon("image/light/start.png");
    ImageIcon _imgerroroff = new ImageIcon("image/light/error_off.png");
    ImageIcon _imglocfileoff = new ImageIcon("image/light/locfile_off.png");
    ImageIcon _imgword = new ImageIcon("image/light/word.png");
    ImageIcon _imgppt = new ImageIcon("image/light/ppt.png");
    ImageIcon _imgexcel = new ImageIcon("image/light/excel.png");
    ImageIcon _imgpdf = new ImageIcon("image/light/pdf.png");
    ImageIcon _imgpage = new ImageIcon("image/light/page.png");
    ImageIcon _imgcalc = new ImageIcon("image/light/calc.png");
    //light theme images
    ImageIcon _imgpropdark = new ImageIcon("image/dark/prop.png");
    ImageIcon _imgsaledark = new ImageIcon("image/dark/sale.png");
    ImageIcon _imgfolderdark = new ImageIcon("image/dark/folder.png");
    ImageIcon _imgstartdark = new ImageIcon("image/dark/start.png");
    ImageIcon _imgerroroffdark = new ImageIcon("image/dark/error_off.png");
    ImageIcon _imglocfileoffdark = new ImageIcon("image/dark/locfile_off.png");
    ImageIcon _imgworddark = new ImageIcon("image/dark/word.png");
    ImageIcon _imgpptdark = new ImageIcon("image/dark/ppt.png");
    ImageIcon _imgexceldark = new ImageIcon("image/dark/excel.png");
    ImageIcon _imgpdfdark = new ImageIcon("image/dark/pdf.png");
    ImageIcon _imgpagedark = new ImageIcon("image/dark/page.png");
    ImageIcon _imgcalcdark = new ImageIcon("image/dark/calc.png");
    //color
    Color fieldcolor = new Color(232, 232, 232);
    //font
    Font defaultFont = new Font("Microsoft YaHei", Font.PLAIN, 12);
}
