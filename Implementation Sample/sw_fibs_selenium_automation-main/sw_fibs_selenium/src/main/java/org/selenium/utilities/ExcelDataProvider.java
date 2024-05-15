package org.selenium.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelDataProvider {


    public static Object[][] testData(String excelPath, String sheetName) {

        ExcelUtility excel = null;
        try {
            excel = new ExcelUtility(excelPath, sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int rowCount = excel.getRowCount();
        int colCount = excel.getColCount();

        Object data[][] = new Object[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                String cellData = excel.getCellData(i, j);
                data[i - 1][j] = cellData;
            }
        }
        return data;

    }

    public static Map<Integer, Map<String, String>> excelDataMap(String excelPath, String sheetName) {
        Map<Integer, Map<String, String>> excelData = new HashMap<Integer, Map<String, String>>();
        ExcelUtility excel = null;
        List<String> headerList = new ArrayList<String>();

        Map<String, String> rowMap = null;

        try {
            excel = new ExcelUtility(excelPath, sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int rowCount = excel.getRowCount();
        int colCount = excel.getColCount();
        for (int i = 0; i < rowCount; i++) {
            rowMap = new HashMap<String, String>();
            for (int j = 0; j < colCount; j++) {
                String cellData = excel.getCellData(i, j);
                //System.out.print(cellData + " | ");
                if (i == 0)
                    headerList.add(cellData.trim());
                else
                    // rowMap.put(headerList.get(j), cellData==null?"":cellData.trim());
                    rowMap.put(headerList.get(j), cellData);
                //data[j][1] = colmap;
            }
            excelData.put(i, rowMap);

        }
        return excelData;
    }// close excel after return

}