package fourcorp.dmtbuildflow.application;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            sqlFileWriter.write("\n");
            insertPart(workbook, sqlFileWriter);
            sqlFileWriter.write("\n");
            insertProductPart(workbook, sqlFileWriter);
            sqlFileWriter.write("\n");
            insertTypeWorkstation(workbook, sqlFileWriter);
            sqlFileWriter.write("\n");
            insertOperation(workbook, sqlFileWriter);
            sqlFileWriter.write("\n");
            insertWorkstation(workbook, sqlFileWriter);
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
        Sheet sheet = workbook.getSheet("BOO");
        if (sheet == null) {
            System.out.println("Sheet 'BOO' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String famID = getCellValue(row.getCell(0));
            int opNumber = Integer.parseInt(getCellValue(row.getCell(2)));
            String opID = getCellValue(row.getCell(1));

            String sqlInsert = String.format("INSERT INTO BOO_Operation (BOOOperation_Sequence, Product_FamilyFamily_ID, OperationOperation_ID) VALUES (%d, '%s', '%s');\n",
                    opNumber, famID, opID);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [BOO]: " + e.getMessage());
            }
        }
    }

    private static void insertPart(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("BOM");
        if (sheet == null) {
            System.out.println("Sheet 'BOM' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String partID = getCellValue(row.getCell(1));
            String desc = getCellValue(row.getCell(2));

            String sqlInsert = String.format("INSERT INTO Part (Part_ID, Description) VALUES ('%s', '%s');\n",
                    partID, desc);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [BOM]: " + e.getMessage());
            }
        }
    }

    private static void insertProductPart(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("BOM");
        if (sheet == null) {
            System.out.println("Sheet 'BOM' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String prodID = getCellValue(row.getCell(0));
            String partID = getCellValue(row.getCell(1));
            int quant = Integer.parseInt(getCellValue(row.getCell(3)));

            String sqlInsert = String.format("INSERT INTO Product_Part (ProductProduct_ID, PartPart_ID, Quantity) VALUES ('%s', '%s', %d);\n",
                    prodID, partID, quant);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [BOM]: " + e.getMessage());
            }
        }
    }

    private static void insertTypeWorkstation(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("WorkstationTypes");
        if (sheet == null) {
            System.out.println("Sheet 'WorkstationTypes' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String wtID = getCellValue(row.getCell(0));
            String design = getCellValue(row.getCell(1));

            String sqlInsert = String.format("INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('%s', '%s');\n",
                    wtID, design);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [WorkstationTypes]: " + e.getMessage());
            }
        }
    }

    private static void insertOperation(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet operationsSheet = workbook.getSheet("Operations");
        if (operationsSheet == null) {
            System.out.println("Sheet 'Operations' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= operationsSheet.getLastRowNum(); rowIndex++) {
            Row row = operationsSheet.getRow(rowIndex);
            if (row == null) continue;

            String id = getCellValue(row.getCell(0));
            String design = getCellValue(row.getCell(1));

            List<String> wkTypeIDs = new ArrayList<>();
            for (int colIndex = 2; colIndex < row.getLastCellNum(); colIndex++) {
                String workstationID = getCellValue(row.getCell(colIndex));
                if (!workstationID.isEmpty()) {
                    wkTypeIDs.add(workstationID);
                }
            }

            for (String wkID : wkTypeIDs) {
                String sqlInsert = String.format(
                        "INSERT INTO Operation (Operation_ID, Designation, Type_WorkstationWorkstationType_ID) " +
                                "VALUES ('%s', '%s', '%s');\n",
                        id, design, wkID);

                try {
                    sqlFileWriter.write(sqlInsert);
                } catch (IOException e) {
                    System.out.println("Error writing to SQL file [WorkstationTypes]: " + e.getMessage());
                }
            }
        }
    }

    private static void insertWorkstation(Workbook workbook, FileWriter sqlFileWriter) {
        Sheet sheet = workbook.getSheet("Workstations");
        if (sheet == null) {
            System.out.println("Sheet 'Workstations' does not exist in the Excel file.");
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Starting from 1 to skip header
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            String wtID = getCellValue(row.getCell(0));
            String name = getCellValue(row.getCell(2));
            String desc = getCellValue(row.getCell(3));
            String type = getCellValue(row.getCell(1));

            String sqlInsert = String.format("INSERT INTO Workstation (Workstation_ID, Name, Description, Type_WorkstationWorkstationType_ID) VALUES ('%s', '%s', '%s', '%s');\n",
                    wtID, name, desc, type);

            try {
                sqlFileWriter.write(sqlInsert);
            } catch (IOException e) {
                System.out.println("Error writing to SQL file [Workstations]: " + e.getMessage());
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
}
