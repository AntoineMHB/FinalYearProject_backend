package com.antoine.springJwt.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

        public void generateAndSaveReceipt(String email, double amount, String txRef) throws IOException {
        String content = """
            Receipt for Transaction
            -----------------------
            Email: %s
            Amount Paid: %.2f
            Transaction Ref: %s
            Date: %s
            """.formatted(email, amount, txRef, LocalDate.now());

        File dir = new File("receipts");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, txRef + ".pdf");

        // Use iText or simple PDF creation
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream stream = new PDPageContentStream(document, page);
        stream.beginText();
        stream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        stream.setLeading(20f);
        stream.newLineAtOffset(50, 700);

        for (String line : content.split("\n")) {
            stream.showText(line);
            stream.newLine();
        }

        stream.endText();
        stream.close();
        document.save(file);
        document.close();
    }
    
}
