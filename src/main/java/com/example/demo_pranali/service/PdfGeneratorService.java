package com.example.demo_pranali.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    public byte[] generateBonafideCertificate(String studentName, String prnNo, String documentType) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // ✅ Load image from resources folder
            URL imageUrl = getClass().getClassLoader().getResource("pic.jpg");
            if (imageUrl != null) {
                Image logo = Image.getInstance(imageUrl);
                logo.scaleToFit(80, 80);
                logo.setAlignment(Image.ALIGN_CENTER);
                document.add(logo);
            } else {
                System.err.println("❌ pic.jpg not found in resources!");
            }

//            logo.scaleToFit(80, 80); // Resize if needed
//            logo.setAlignment(Image.ALIGN_CENTER); // or ALIGN_LEFT / ALIGN_RIGHT
//            document.add(logo);

            // Header
            Paragraph header = new Paragraph("📘 College of Engineering\nCity, State\n\nBONAFIDE CERTIFICATE\n\n",
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            // Date
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            Paragraph datePara = new Paragraph("Date: " + currentDate + "\n\n",
                    new Font(Font.FontFamily.HELVETICA, 12));
            datePara.setAlignment(Element.ALIGN_RIGHT);
            document.add(datePara);

            // Body
            Paragraph content = new Paragraph(
                    "This is to certify that Ms./Mr. " + studentName +
                            " (PRN: " + prnNo + ") is a bonafide student of this institution.\n" +
                            "This certificate is issued upon the student's request for the purpose of: " + documentType + ".\n\n",
                    new Font(Font.FontFamily.HELVETICA, 12)
            );
            content.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(content);

            // Footer
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

}
