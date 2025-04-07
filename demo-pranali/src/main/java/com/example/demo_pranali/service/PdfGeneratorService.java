
package com.example.demo_pranali.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    private String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private Image loadLogo() throws IOException, BadElementException {
        URL imageUrl = getClass().getClassLoader().getResource("pic.jpg");
        if (imageUrl != null) {
            Image logo = Image.getInstance(imageUrl);
            logo.scaleToFit(80, 80);
            logo.setAlignment(Image.ALIGN_LEFT);
            logo.setAlignment(Image.ALIGN_CENTER);
            return logo;
        } else {
            System.err.println("❌ pic.jpg not found in resources!");
            return null;
        }
    }

    public byte[] generateBonafideCertificate(String studentName, String prnNo, String documentType) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Image logo = loadLogo();
            if (logo != null) document.add(logo);

            Paragraph header = new Paragraph("📘 College of Engineering\nCity, State\n\nBONAFIDE CERTIFICATE\n\n",
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph datePara = new Paragraph("Date: " + getCurrentDate() + "\n\n",
                    new Font(Font.FontFamily.HELVETICA, 12));
            datePara.setAlignment(Element.ALIGN_RIGHT);
            document.add(datePara);

            Paragraph content = new Paragraph(
                    "This is to certify that Ms./Mr. " + studentName +
                            " (PRN: " + prnNo + ") is a bonafide student of this institution.\n" +
                            "This certificate is issued upon the student's request for the purpose of: " + documentType + ".\n\n",
                    new Font(Font.FontFamily.HELVETICA, 12));
            content.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(content);

            Paragraph footer = new Paragraph("Signature\n(Authorized Signatory)",
                    new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(50);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    public byte[] generateLeavingCertificate(String studentName, String prnNo, String course, String reason) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Image logo = loadLogo();
            if (logo != null) document.add(logo);

            Paragraph header = new Paragraph("College of Engineering\nCity, State\n\nLEAVING CERTIFICATE\n\n",
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph datePara = new Paragraph("Date: " + getCurrentDate() + "\n\n",
                    new Font(Font.FontFamily.HELVETICA, 12));
            datePara.setAlignment(Element.ALIGN_RIGHT);
            document.add(datePara);

            Paragraph content = new Paragraph(
                    "This is to certify that Ms./Mr. " + studentName + " (PRN: " + prnNo + ") has been a student of " +
                            course + " course. The student is leaving the institution due to: " + reason + ".\n\n",
                    new Font(Font.FontFamily.HELVETICA, 12));
            content.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(content);

            Paragraph footer = new Paragraph("Principal’s Signature",
                    new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(50);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    public byte[] generateIdCard(String studentName, String prnNo, String course, String photoPath) {
        Document document = new Document(new Rectangle(250, 350)); // ID card size
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Image logo = loadLogo();
            if (logo != null) document.add(logo);

            Paragraph college = new Paragraph("College of Engineering\nID CARD\n\n",
                    new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            college.setAlignment(Element.ALIGN_CENTER);
            document.add(college);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            // Student photo
            try {
                Image photo = Image.getInstance(photoPath);
                photo.scaleToFit(80, 100);
                PdfPCell photoCell = new PdfPCell(photo, true);
                photoCell.setBorder(Rectangle.NO_BORDER);
                table.addCell(photoCell);
            } catch (Exception e) {
                PdfPCell photoCell = new PdfPCell(new Phrase("Photo Not Available"));
                photoCell.setBorder(Rectangle.NO_BORDER);
                table.addCell(photoCell);
            }

            // Info
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorder(Rectangle.NO_BORDER);
            infoCell.addElement(new Paragraph("Name: " + studentName));
            infoCell.addElement(new Paragraph("PRN: " + prnNo));
            infoCell.addElement(new Paragraph("Course: " + course));
            table.addCell(infoCell);

            document.add(table);

            Paragraph valid = new Paragraph("\nValid for Academic Year: 2024-2025",
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
            valid.setAlignment(Element.ALIGN_CENTER);
            document.add(valid);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

//    public byte[] generateHallTicket(String studentName, String prnNo, String course, String[] subjects) {
//        Document document = new Document();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        try {
//            PdfWriter.getInstance(document, outputStream);
//            document.open();
//
//            Image logo = loadLogo();
//            if (logo != null) document.add(logo);
//
//            Paragraph header = new Paragraph("College of Engineering\nCity, State\n\nHALL TICKET\n\n",
//                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
//            header.setAlignment(Element.ALIGN_CENTER);
//            document.add(header);
//
//            Paragraph info = new Paragraph("Name: " + studentName + "\nPRN: " + prnNo + "\nCourse: " + course + "\nDate: " + getCurrentDate() + "\n\n",
//                    new Font(Font.FontFamily.HELVETICA, 12));
//            info.setAlignment(Element.ALIGN_LEFT);
//            document.add(info);
//
//            Paragraph subHeader = new Paragraph("Subjects:", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
//            subHeader.setSpacingBefore(10);
//            document.add(subHeader);
//
//            com.itextpdf.text.List subjectList = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED);
//            for (String subject : subjects) {
//                subjectList.add(new ListItem(subject));
//            }
//            document.add(subjectList);
//
//            Paragraph sign = new Paragraph("\nSignature\n(Exam Controller)",
//                    new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
//            sign.setAlignment(Element.ALIGN_RIGHT);
//            sign.setSpacingBefore(30);
//            document.add(sign);
//
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return outputStream.toByteArray();
//    }

    public byte[] generateHallTicket(String studentName, String prnNo, String course, String[] subjects) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Image logo = loadLogo();
            if (logo != null) document.add(logo);

            // Institute Header
            Paragraph instituteHeader = new Paragraph("Pune Institute of Computer Technology, Pune\n\n",
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            instituteHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(instituteHeader);

            Paragraph mainHeader = new Paragraph("HALL TICKET\n\n",
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            mainHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(mainHeader);

            Paragraph info = new Paragraph("Name: " + studentName + "\nPRN: " + prnNo + "\nCourse: " + course + "\nDate: " + getCurrentDate() + "\n\n",
                    new Font(Font.FontFamily.HELVETICA, 12));
            info.setAlignment(Element.ALIGN_LEFT);
            document.add(info);

            Paragraph subHeader = new Paragraph("Subjects:", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            subHeader.setSpacingBefore(10);
            document.add(subHeader);

            // Table with Subject and Date
            PdfPTable table = new PdfPTable(3); // 3 columns: Sr No., Subject, Date
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{1f, 4f, 3f});

            // Table Header
            table.addCell("Sr No.");
            table.addCell("Subject Name");
            table.addCell("Date");

            LocalDate examDate = LocalDate.now().plusDays(1); // start from tomorrow

            for (int i = 0; i < subjects.length; i++) {
                table.addCell(String.valueOf(i + 1));
                table.addCell(subjects[i]);
                table.addCell(examDate.plusDays(i).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }

            document.add(table);

            Paragraph sign = new Paragraph("\nSignature\n(Exam Controller)",
                    new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
            sign.setAlignment(Element.ALIGN_RIGHT);
            sign.setSpacingBefore(30);
            document.add(sign);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }



}

