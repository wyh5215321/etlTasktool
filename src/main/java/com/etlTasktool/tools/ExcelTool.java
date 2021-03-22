package com.etlTasktool.tools;



import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTool {
    public final static String PATH = "C:\\Users\\xm\\Desktop\\模板数据抽取结果 - 副本.xlsx";

    /**
     * 定位单元格位置
     * @param taskName
     * @return
     * @throws IOException
     */
    public static int[] locate(String taskName) throws IOException {
        File xlsFile = new File(PATH);
        // 获得工作簿
        Workbook workbook = WorkbookFactory.create(xlsFile);
        int[] res = new int[2];
        // 遍历工作表
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();

        for (int i=1;i<= rows; ++i) {
            Row r = sheet.getRow(i - 1);
            String content = r.getCell(0).getStringCellValue();
            int col = getIndex(content, taskName);
            if (col != 0) {
                res[1] = col;
                res[0] = i - 1;
                workbook.close();
                return res;
            }
        }
        workbook.close();
        return null;
    }

    public static void write(int row, int col, String cellValue,String errorValue) throws IOException {
        FileInputStream fs=new FileInputStream(PATH);
//        POIFSFileSystem ps=new POIFSFileSystem(fs);
        // 获得工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        // 遍历工作表
        Sheet sheet = workbook.getSheetAt(0);

        FileOutputStream fos = null;
        try {
            Row r = sheet.getRow(row);
             fos = new FileOutputStream(PATH);
            r.getCell(col).setCellValue(cellValue);
            if (!errorValue.equals("0")) {
                Row r1=sheet.getRow(row);
                r1=sheet.getRow(row);
                r1.createCell(3).setCellValue(errorValue);
            }
            fos.flush();
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            fos.close();
        }
    }

    public static void saveExcel(String cellValue, String errorValue, String taskName) throws IOException {
        int[] res = locate(taskName);
        if (res != null && res[1] != 0) {
            write(res[0],res[1],cellValue,errorValue);
        }
    }

    public static int getIndex(String cellValue, String taskName) {

        if (taskName.contains("sum")) {
            taskName = taskName.replace("sum", "");
            if (cellValue.toLowerCase().contains(taskName.toLowerCase())) {
                return 2;
            }
        }

        if (taskName.contains("汇总")) {
            taskName = taskName.substring(0, 7);
            if (cellValue.toLowerCase().contains(taskName.toLowerCase())) {
                return 2;
            }
            if (cellValue.toLowerCase().contains(taskName.toLowerCase().substring(0,4))) {
                return 2;
            }
        }
        if (cellValue.toLowerCase().contains(taskName.toLowerCase())) {
            return 1;
        }
//        先判断是否cellvalue是否包含了taskname
        if (!taskName.contains("报表")&&!taskName.contains("代码")&&cellValue.toLowerCase().contains(taskName.toLowerCase().substring(0,6))) {
            return 1;
        }

        if (!taskName.contains("报表")&&!taskName.contains("代码")&&cellValue.toLowerCase().contains(taskName.toLowerCase().substring(0,4))) {
            return 1;
        }


        return 0;
    }
}
