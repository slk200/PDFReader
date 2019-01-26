package org.tizzer.counttool.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * Created by tizzer on 2019/1/21.
 */
class Office2PDFHandler {
    private static final Integer WORD_TO_PDF_OPERAND = 17;
    private static final Integer PPT_TO_PDF_OPERAND = 32;
    private static final Integer EXCEL_TO_PDF_OPERAND = 0;

    /**
     * word转为pdf
     *
     * @param inFile
     * @param outFile
     */
    static void word2pdf(String inFile, String outFile) {
        ActiveXComponent activeXComponent = null;
        Dispatch wps = null;
        try {
            ComThread.InitSTA();
            activeXComponent = new ActiveXComponent("KWPS.Application");
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (wps != null) {
                Dispatch.call(wps, "Close", false);
            }
            if (activeXComponent != null) {
                activeXComponent.invoke("Quit", 0);
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
        ActiveXComponent activeXComponent = null;
        Dispatch wpp = null;
        try {
            ComThread.InitSTA();
            activeXComponent = new ActiveXComponent("KWPP.Application");
            Dispatch kwpp = activeXComponent.getProperty("Presentations").toDispatch();
            /*
             * call
             * param 4: ReadOnly
             * param 5: Untitled:the pointed file isTitled
             * param 6: WithWindow:the pointed file isVisible
             * */
            wpp = Dispatch.call(kwpp, "Open", inFile, true, true, false).toDispatch();
            Dispatch.call(wpp, "SaveAs", outFile, PPT_TO_PDF_OPERAND);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (wpp != null) {
                Dispatch.call(wpp, "Close");
            }
            if (activeXComponent != null) {
                activeXComponent.invoke("Quit");
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
        ActiveXComponent activeXComponent = null;
        Dispatch et = null;
        try {
            ComThread.InitSTA();
            activeXComponent = new ActiveXComponent("KET.Application");
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (et != null) {
                Dispatch.call(et, "Close", new Variant(false));
            }
            if (activeXComponent != null) {
                activeXComponent.invoke("Quit", new Variant[]{});
            }
            ComThread.Release();
        }

    }
}
