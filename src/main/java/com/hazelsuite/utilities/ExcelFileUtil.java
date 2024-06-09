package com.hazelsuite.utilities;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelFileUtil {
    public static Map<String, String> readExcelColumns(String filePath, String sheetName, int valueColumnIndex) {
        Map<String, String> data = new LinkedHashMap<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheet(sheetName);

            int rows = sheet.getPhysicalNumberOfRows();

            for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row != null) {
                    Cell keyCell = row.getCell(0);
                    Cell valueCell = row.getCell(valueColumnIndex);

                    if (keyCell != null && valueCell != null) {
                        String key = keyCell.getStringCellValue();
                        String value = getCellValueAsString(valueCell);
                        data.put(key, value);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static String getCellValueAsString(Cell cell) {
        String cellValue;
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                double numericValue = cell.getNumericCellValue();
                long longValue = (long) numericValue;

                // Check if the numeric value is an integer
                if (numericValue == longValue) {
                    cellValue = String.valueOf(longValue);
                } else {
                    cellValue = String.valueOf(numericValue);
                }
                break;
            default:
                cellValue = "";
        }
        return cellValue;
    }

}
