package com.example;

import javax.swing.*;
import java.io.*;
import java.util.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;


public class Business {
    private String name;
    private String address;
    private String website;
    private String phoneNumber;

    // Getters e Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}


class BusinessList {
    private List<Business> businessList = new ArrayList<>();

    public void addBusiness(Business business) {
        businessList.add(business);
    }

    public void saveToExcel(String filename) throws IOException {
        String fileLocation = openFileDialog();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Businesses");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Address");
        headerRow.createCell(2).setCellValue("Website");
        headerRow.createCell(3).setCellValue("Phone Number");

        int rowNum = 1;
        for (Business business : businessList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(business.getName());
            row.createCell(1).setCellValue(business.getAddress());
            row.createCell(2).setCellValue(business.getWebsite());
            row.createCell(3).setCellValue(business.getPhoneNumber());
        }

        try (FileOutputStream fileOut = new FileOutputStream(fileLocation + "/" + filename + ".xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    public void saveToCSV(String filename) throws IOException {
        String fileLocation = openFileDialog();
        try (PrintWriter writer = new PrintWriter(new File(fileLocation + "/" + filename + ".csv"))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Name");
            sb.append(';');
            sb.append("Address");
            sb.append(';');
            sb.append("Website");
            sb.append(';');
            sb.append("Phone Number");
            sb.append('\n');

            for (Business business : businessList) {
                sb.append(business.getName());
                sb.append(';');
                sb.append(business.getAddress());
                sb.append(';');
                sb.append(business.getWebsite());
                sb.append(';');
                sb.append(business.getPhoneNumber());
                sb.append('\n');
            }

            writer.write(sb.toString());
        }
    }

    private String openFileDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return "";
        }
    }
}
