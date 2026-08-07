package org.tizzer.counttool.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * Created by tizzer on 2019/1/21.
 *
 * <p>Office转PDF转换器：优先调用WPS Office的COM组件，
 * 若未找到WPS组件，则自动回退到Microsoft Office的COM组件。</p>
 */
class Office2PDFHandler {
    private static final Integer WORD_TO_PDF_OPERAND = 17;
    private static final Integer PPT_TO_PDF_OPERAND = 32;
    private static final Integer EXCEL_TO_PDF_OPERAND = 0;

    //WPS Office COM组件ProgID
    private static final String WPS_WORD_PROGID = "KWPS.Application";
    private static final String WPS_PPT_PROGID = "KWPP.Application";
    private static final String WPS_EXCEL_PROGID = "KET.Application";

    //Microsoft Office COM组件ProgID（未找到WPS时回退使用）
    private static final String MS_WORD_PROGID = "Word.Application";
    private static final String MS_PPT_PROGID = "PowerPoint.Application";
    private static final String MS_EXCEL_PROGID = "Excel.Application";

    /**
     * word转为pdf
     *
     * @param inFile
     * @param outFile
     */
    static void word2pdf(String inFile, String outFile) {
        try {
            convertWord(WPS_WORD_PROGID, inFile, outFile);
        } catch (ComFailException e) {
            //未找到WPS组件，回退到Microsoft Word
            try {
                convertWord(MS_WORD_PROGID, inFile, outFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void convertWord(String progId, String inFile, String outFile) throws Exception {
        ActiveXComponent activeXComponent = null;
        Dispatch wps = null;
        try {
            ComThread.InitSTA();
            activeXComponent = new ActiveXComponent(progId);
            activeXComponent.setProperty("Visible", false);
            Dispatch kwps = activeXComponent.getProperty("Documents").toDispatch();
            Object[] obj = new Object[]{
                    inFile,
                    new Variant(false),
                    new Variant(false),//is read only
                    new Variant(false),
                    new Variant("pwd")
            };
            wps = Dispatch.invoke(kwps, "Open", Dispatch.Method, obj, new int[1]).toDispatch();
            Dispatch.put(wps, "RemovePersonalInformation", false);
            Dispatch.call(wps, "ExportAsFixedFormat", outFile, WORD_TO_PDF_OPERAND);
        } finally {
            try {
                if (wps != null) {
                    Dispatch.call(wps, "Close", false);
                }
                if (activeXComponent != null) {
                    activeXComponent.invoke("Quit", 0);
                }
            } catch (Exception ignore) {
                //忽略清理时的异常，避免掩盖主流程异常
            }
            ComThread.Release();
        }
    }

    /**
     * ppt转为pdf
     *
     * @param inFile
     * @param outFile
     */
    static void ppt2pdf(String inFile, String outFile) {
        try {
            convertPpt(WPS_PPT_PROGID, inFile, outFile);
        } catch (ComFailException e) {
            //未找到WPS组件，回退到Microsoft PowerPoint
            try {
                convertPpt(MS_PPT_PROGID, inFile, outFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void convertPpt(String progId, String inFile, String outFile) throws Exception {
        ActiveXComponent activeXComponent = null;
        Dispatch wpp = null;
        try {
            ComThread.InitSTA();
            activeXComponent = new ActiveXComponent(progId);
            Dispatch kwpp = activeXComponent.getProperty("Presentations").toDispatch();
            /*
             * call
             * param 4: ReadOnly
             * param 5: Untitled:the pointed file isTitled
             * param 6: WithWindow:the pointed file isVisible
             * */
            wpp = Dispatch.call(kwpp, "Open", inFile, true, true, false).toDispatch();
            Dispatch.call(wpp, "SaveAs", outFile, PPT_TO_PDF_OPERAND);
        } finally {
            try {
                if (wpp != null) {
                    Dispatch.call(wpp, "Close");
                }
                if (activeXComponent != null) {
                    activeXComponent.invoke("Quit");
                }
            } catch (Exception ignore) {
                //忽略清理时的异常，避免掩盖主流程异常
            }
            ComThread.Release();
        }
    }

    /**
     * excel转为pdf
     *
     * @param inFile
     * @param outFile
     */
    static void excel2pdf(String inFile, String outFile) {
        try {
            convertExcel(WPS_EXCEL_PROGID, inFile, outFile);
        } catch (ComFailException e) {
            //未找到WPS组件，回退到Microsoft Excel
            try {
                convertExcel(MS_EXCEL_PROGID, inFile, outFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void convertExcel(String progId, String inFile, String outFile) throws Exception {
        ActiveXComponent activeXComponent = null;
        Dispatch et = null;
        try {
            ComThread.InitSTA();
            activeXComponent = new ActiveXComponent(progId);
            activeXComponent.setProperty("Visible", new Variant(false));
            activeXComponent.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            Dispatch ket = activeXComponent.getProperty("Workbooks").toDispatch();

            Object[] obj = new Object[]{
                    inFile,
                    new Variant(false),
                    new Variant(false)
            };
            et = Dispatch.invoke(ket, "Open", Dispatch.Method, obj, new int[9]).toDispatch();
            // trans form
            Object[] obj2 = new Object[]{
                    new Variant(EXCEL_TO_PDF_OPERAND), // PDF=0
                    outFile,
                    new Variant(0)  //0=standard (生成的PDF图片不会变模糊) ; 1=最小文件
            };
            Dispatch.invoke(et, "ExportAsFixedFormat", Dispatch.Method, obj2, new int[1]);
        } finally {
            try {
                if (et != null) {
                    Dispatch.call(et, "Close", new Variant(false));
                }
                if (activeXComponent != null) {
                    activeXComponent.invoke("Quit", new Variant[]{});
                }
            } catch (Exception ignore) {
                //忽略清理时的异常，避免掩盖主流程异常
            }
            ComThread.Release();
        }
    }
}
