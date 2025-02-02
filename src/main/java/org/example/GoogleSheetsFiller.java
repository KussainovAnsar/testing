package org.example;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GoogleSheetsFiller {
    private static final String FILE_NAME = "EmployeeData.xlsx";
    private static final String[] COLUMNS = {"ID", "Name", "Birth Date", "Age", "Email", "Phone", "Salary (USD)"};
    private static final int ROW_COUNT = 30;

    public static void main(String[] args) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employees");
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle currencyStyle = createCurrencyStyle(workbook);
        CellStyle dateStyle = createDateStyle(workbook);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < COLUMNS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(COLUMNS[i]);
            cell.setCellStyle(headerStyle);
        }

        Random random = new Random();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i <= ROW_COUNT; i++) {
            Row row = sheet.createRow(i);

            int age = 20 + random.nextInt(40); // Возраст от 20 до 59
            double salary = 40000 + (random.nextDouble() * 60000); // Зарплата от 40k до 100k
            Date birthDate = new Date(System.currentTimeMillis() - (long) age * 365 * 24 * 60 * 60 * 1000);

            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue(generateRandomName());
            Cell birthDateCell = row.createCell(2);
            birthDateCell.setCellValue(birthDate);
            birthDateCell.setCellStyle(dateStyle);
            row.createCell(3).setCellValue(age);
            row.createCell(4).setCellValue("user" + i + "@example.com");
            row.createCell(5).setCellValue(generatePhoneNumber());
            Cell salaryCell = row.createCell(6);
            salaryCell.setCellValue(salary);
            salaryCell.setCellStyle(currencyStyle);
        }

        Row summaryRow = sheet.createRow(ROW_COUNT + 2);
        summaryRow.createCell(0).setCellValue("Average Age:");
        summaryRow.createCell(1).setCellFormula("AVERAGE(D2:D" + ROW_COUNT + ")");
        summaryRow.createCell(3).setCellValue("Total Salary:");
        summaryRow.createCell(4).setCellFormula("SUM(G2:G" + ROW_COUNT + ")");

        for (int i = 0; i < COLUMNS.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
            workbook.write(fileOut);
            System.out.println("File saved as " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomName() {
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona", "George", "Hannah"};
        String[] lastNames = {"Miller", "Davis", "Smith", "Taylor", "Anderson", "Brown", "Wilson", "Martinez"};
        Random random = new Random();
        return firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];
    }

    private static String generatePhoneNumber() {
        Random random = new Random();
        return "+1 " + (100 + random.nextInt(900)) + "-" + (100 + random.nextInt(900)) + "-" + (1000 + random.nextInt(9000));
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private static CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        return style;
    }

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("yyyy-MM-dd"));
        return style;
    }
}
