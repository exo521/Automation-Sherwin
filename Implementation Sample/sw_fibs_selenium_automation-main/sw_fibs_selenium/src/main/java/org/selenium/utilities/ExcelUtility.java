package org.selenium.utilities;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelUtility {

    public static XSSFWorkbook ExcelBook;
    public static XSSFSheet ExcelSheet;
    public static XSSFRow row;
    public static XSSFCell cell;


    // Constructor to set excel path
    public ExcelUtility(String path, String sheetName) throws Exception {

        try {
            FileInputStream ExcelFile = new FileInputStream(path);
            ExcelBook = new XSSFWorkbook(ExcelFile);
            ExcelSheet = ExcelBook.getSheet(sheetName);

        } catch (Exception e) {
            throw (e);
        }
    }

    //Method to get row count
    public int getRowCount() {
        int rowCount = 0;
        try {
            rowCount = ExcelSheet.getPhysicalNumberOfRows();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowCount;

    }

    //Method to get column count
    public int getColCount() {
        int colCount = 0;
        try {
            colCount = ExcelSheet.getRow(0).getPhysicalNumberOfCells();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colCount;
    }

    // Method to get the String value from a cell in an excel
    public String getCellDataString(int rowNum, int ColNum) {
        String cellData;
        try {
            cell = ExcelSheet.getRow(rowNum).getCell(ColNum);
            cellData = cell.getStringCellValue();

        } catch (Exception e) {
            throw (e);
        }
        return cellData;
    }

    // Method to get the Numeric value from a cell in an excel
    public int getCellDataNumeric(int rowNum, int ColNum) {

        try {
            cell = ExcelSheet.getRow(rowNum).getCell(ColNum);
            int cellData = (int) cell.getNumericCellValue();
            return cellData;
        } catch (Exception e) {
            throw (e);
        }
    }

    public String getCellData(int rowNum, int ColNum) {
        String cellData;
        try {
            cell = ExcelSheet.getRow(rowNum).getCell(ColNum, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            //cell= ExcelSheet.getRow(rowNum).getCell(ColNum);
            if (cell.getCellType() == CellType.NUMERIC) {

                DataFormatter dataFormatter = new DataFormatter();
                cellData = dataFormatter.formatCellValue(cell);
                return cellData;
            } else if (cell == null || cell.getCellType() == CellType.BLANK) {
                cellData = cell.getStringCellValue();
                return "";
            }
//            else if(cell.getCellType()==null){
//                return "";
//            }
            else if (cell.getCellType() == CellType.STRING) {
                cellData = cell.getStringCellValue();
                return cellData;
            }

        } catch (Exception e) {
            throw (e);
        }
        return null;
    }

    // Method to set the value in a cell in an excel
    public void setCellData(String Result, int RowNum, int ColNum) throws Exception {
        try {
            ExcelSheet.createRow(RowNum).createCell(ColNum).setCellValue(Result);
            FileOutputStream fileOut = new FileOutputStream(
                    System.getProperty("user.dir") + "\\Excel\\Output Data.xlsx");
            ExcelBook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            throw (e);
        }
    }

}