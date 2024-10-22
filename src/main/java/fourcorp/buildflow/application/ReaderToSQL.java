package fourcorp.buildflow.application;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReaderToSQL {
    private static String excelFile;
    private static String outputFile;

    public static void readExcelAndGenerateSQL(String excelFilePath, String outputSQLFilePath) {
        excelFile = excelFilePath;
        outputFile = outputSQLFilePath;

        try (FileInputStream excelFile = new FileInputStream(excelFilePath); FileWriter sqlFileWriter = new FileWriter(outputSQLFilePath)) {
            Workbook workbook = new XSSFWorkbook(excelFile);

            readProductFamily(workbook, sqlFileWriter);
            sqlFileWriter.write("\n");
            readProduct(workbook, sqlFileWriter);
            sqlFileWriter.write("\n");
            readCostumer(workbook, sqlFileWriter);
            sqlFileWriter.write("\n");
            readOrder(workbook, sqlFileWriter);
            sqlFileWriter.write("\n");
            insertProductionOrder(workbook, sqlFileWriter);
            sqlFileWriter.write("\n");
            insertBOO(workbook, sqlFileWriter);
            sqlFileWriter.close();
        } catch (IOException e) {
            System.out.println("Error reading Excel file: " + e.getMessage());
        }
    }

    private static void readProductFamily(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("ProductFamily");
        if (sheet == null) {
            System.out.println("Sheet 'ProductFamily' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String id = getCellValue(row.getCell(0));
            String name = getCellValue(row.getCell(1));

            String sqlInsert = String.format("INSERT INTO Product_Family (Family_ID, Name) VALUES ('%s', '%s');\n",
                    id, name.replace("'", "''"));

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [Product Family]: " + e.getMessage());
            }
        }
    }

    private static void readProduct(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("Products");
        if (sheet == null) {
            System.out.println("Sheet 'Products' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String id = getCellValue(row.getCell(0));
            String name = getCellValue(row.getCell(1));
            String desc = getCellValue(row.getCell(2));
            String fam = "'" + getCellValue(row.getCell(3)) + "'";
            if (getCellValue(row.getCell(3)).isEmpty()) {
                fam = "NULL";
            }

            String sqlInsert = String.format("INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID) VALUES ('%s', '%s', '%s', %s);\n",
                    id, name, desc, fam);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [Products]: " + e.getMessage());
            }
        }
    }

    private static void readCostumer(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("Clients");
        if (sheet == null) {
            System.out.println("Sheet 'Clients' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String name = getCellValue(row.getCell(1));
            String vatin = getCellValue(row.getCell(2));
            String address = getCellValue(row.getCell(3));
            String zip = getCellValue(row.getCell(4));
            String town = getCellValue(row.getCell(5));
            String country = getCellValue(row.getCell(6));
            String mail = "'" + getCellValue(row.getCell(7)) + "'";
            if (getCellValue(row.getCell(7)).isEmpty()) {
                mail = "NULL";
            }
            String phone = "'" + getCellValue(row.getCell(8)) + "'";
            if (getCellValue(row.getCell(8)).isEmpty()) {
                phone = "NULL";
            }


            String sqlInsert = String.format("INSERT INTO Costumer (VAT, Name, Address, \"Zip-Code\", City, Country, Email, Phone) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %s, %s);\n",
                    vatin, name, address, zip, town, country, mail, phone);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [Costumer]: " + e.getMessage());
            }
        }
    }

    private static void readOrder(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("Orders");
        if (sheet == null) {
            System.out.println("Sheet 'Order' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String id = getCellValue(row.getCell(0));
            String CId = getCellValue(row.getCell(1));
            String vat = searchVAT(CId, workbook);
            String orderDate = getCellValue(row.getCell(4));
            String deliveryDate = getCellValue(row.getCell(5));

            String sqlInsert = String.format("INSERT INTO \"Order\" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('%s', %s, %s, '%s');\n",
                    id, orderDate, deliveryDate, vat);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [Costumer]: " + e.getMessage());
            }
        }
    }

    private static void insertProductionOrder(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("Orders");
        if (sheet == null) {
            System.out.println("Sheet 'Order' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String product = getCellValue(row.getCell(2));
            String ord = getCellValue(row.getCell(0));
            int qntty = Integer.parseInt(getCellValue(row.getCell(3)));


            String sqlInsert = String.format("INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('%s', '%s', %d);\n",
                    product, ord, qntty);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [ProductionOrder]: " + e.getMessage());
            }
        }
    }

    private static void insertBOO(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("Orders");
        if (sheet == null) {
            System.out.println("Sheet 'Order' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String product = getCellValue(row.getCell(2));
            String ord = getCellValue(row.getCell(0));
            int qntty = Integer.parseInt(getCellValue(row.getCell(3)));


            String sqlInsert = String.format("INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('%s', '%s', %d);\n",
                    product, ord, qntty);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [ProductionOrder]: " + e.getMessage());
            }
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return "TO_DATE('" + sdf.format(date) + "'" + ", 'YYYY-MM-DD')";
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private static String searchVAT(String clientID, Workbook workbook) {
        Sheet sheet = workbook.getSheet("Clients");
        if (sheet == null) {
            System.out.println("Sheet 'Clients' does not exist in the Excel file.");
            return null;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String id = getCellValue(row.getCell(0));
            if (id.equals(clientID)) {
                return getCellValue(row.getCell(2));
            }
        }
        return null;
    }
}
