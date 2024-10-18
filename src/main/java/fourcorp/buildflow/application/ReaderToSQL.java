package fourcorp.buildflow.application;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public abstract class ReaderToSQL {

    public static void readExcelAndGenerateSQL(String excelFilePath, String outputSQLFilePath) {
        try (FileInputStream excelFile = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(excelFile);
             FileWriter sqlFileWriter = new FileWriter(outputSQLFilePath)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String tableName = sheet.getSheetName();

                Iterator<Row> rowIterator = sheet.iterator();
                Row headerRow = rowIterator.hasNext() ? rowIterator.next() : null;

                if (headerRow != null) {
                    StringBuilder insertCommand = new StringBuilder();

                    insertCommand.append("INSERT INTO ").append(tableName).append(" (");
                    Iterator<Cell> cellIterator = headerRow.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell headerCell = cellIterator.next();
                        insertCommand.append(headerCell.getStringCellValue());
                        if (cellIterator.hasNext()) {
                            insertCommand.append(", ");
                        }
                    }
                    insertCommand.append(") VALUES\n");

                    while (rowIterator.hasNext()) {
                        Row currentRow = rowIterator.next();
                        insertCommand.append("(");

                        cellIterator = currentRow.cellIterator();
                        while (cellIterator.hasNext()) {
                            Cell currentCell = cellIterator.next();
                            insertCommand.append(getCellValueAsString(currentCell));
                            if (cellIterator.hasNext()) {
                                insertCommand.append(", ");
                            }
                        }
                        insertCommand.append(")");
                        if (rowIterator.hasNext()) {
                            insertCommand.append(",\n");
                        } else {
                            insertCommand.append(";\n");
                        }
                    }

                    sqlFileWriter.write(insertCommand.toString());
                    sqlFileWriter.write("\n");
                }
            }
            System.out.println("Arquivo SQL gerado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return "'" + cell.getStringCellValue() + "'";
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return "'" + cell.getDateCellValue().toString() + "'";
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "NULL";
        }
    }
}

