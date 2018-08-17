package org.tizzer.pdfreader.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

class Office2PDF {
    private static final Integer WORD_TO_PDF_OPERAND = 17;
    private static final Integer PPT_TO_PDF_OPERAND = 32;
    private static final Integer EXCEL_TO_PDF_OPERAND = 0;

    /**
     * word转pdf
     *
     * @param srcFilePath
     * @param pdfFilePath
     */
    static void word2pdf(String srcFilePath, String pdfFilePath) {
        ActiveXComponent activeXComponent = null;
        Dispatch doc = null;
        try {
            ComThread.InitSTA();
            activeXComponent = new ActiveXComponent("Word.Application");
            activeXComponent.setProperty("Visible", false);
            Dispatch docs = activeXComponent.getProperty("Documents").toDispatch();
            Object[] obj = new Object[]{
                    srcFilePath,
                    new Variant(false),
                    new Variant(false),//是否只读
                    new Variant(false),
                    new Variant("pwd")
            };
            doc = Dispatch.invoke(docs, "Open", Dispatch.Method, obj, new int[1]).toDispatch();
//          Dispatch.put(doc, "Compatibility", false);  //兼容性检查,为特定值false不正确
            Dispatch.put(doc, "RemovePersonalInformation", false);
            Dispatch.call(doc, "ExportAsFixedFormat", pdfFilePath, WORD_TO_PDF_OPERAND);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc != null) {
                Dispatch.call(doc, "Close", false);
            }
            if (activeXComponent != null) {
                activeXComponent.invoke("Quit", 0);
            }
            ComThread.Release();
        }
    }

    /**
     * ppt转pdf
     *
     * @param srcFilePath
     * @param pdfFilePath
     */
    static void ppt2pdf(String srcFilePath, String pdfFilePath) {
        ActiveXComponent activeXComponent = null;
        Dispatch ppt = null;
        try {
            ComThread.InitSTA();
            activeXComponent = new ActiveXComponent("Powerpoint.Application");
            Dispatch ppts = activeXComponent.getProperty("Presentations").toDispatch();
            /*
             * call
             * param 4: ReadOnly
             * param 5: Untitled指定文件是否有标题
             * param 6: WithWindow指定文件是否可见
             * */
            ppt = Dispatch.call(ppts, "Open", srcFilePath, true, true, false).toDispatch();
            Dispatch.call(ppt, "SaveAs", pdfFilePath, PPT_TO_PDF_OPERAND);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ppt != null) {
                Dispatch.call(ppt, "Close");
            }
            if (activeXComponent != null) {
                activeXComponent.invoke("Quit");
            }
            ComThread.Release();
        }
    }

    /**
     * excel转pdf
     *
     * @param inFilePath
     * @param outFilePath
     */
    static void excel2pdf(String inFilePath, String outFilePath) {
        ActiveXComponent activeXComponent = null;
        Dispatch excel = null;
        try {
            ComThread.InitSTA();
            activeXComponent = new ActiveXComponent("Excel.Application");
            activeXComponent.setProperty("Visible", new Variant(false));
            activeXComponent.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            Dispatch excels = activeXComponent.getProperty("Workbooks").toDispatch();

            Object[] obj = new Object[]{
                    inFilePath,
                    new Variant(false),
                    new Variant(false)
            };
            excel = Dispatch.invoke(excels, "Open", Dispatch.Method, obj, new int[9]).toDispatch();

            // 转换格式
            Object[] obj2 = new Object[]{
                    new Variant(EXCEL_TO_PDF_OPERAND), // PDF格式=0
                    outFilePath,
                    new Variant(0)  //0=标准 (生成的PDF图片不会变模糊) ; 1=最小文件
            };
            Dispatch.invoke(excel, "ExportAsFixedFormat", Dispatch.Method, obj2, new int[1]);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excel != null) {
                Dispatch.call(excel, "Close", new Variant(false));
            }
            if (activeXComponent != null) {
                activeXComponent.invoke("Quit", new Variant[]{});
            }
            ComThread.Release();
        }

    }
}
