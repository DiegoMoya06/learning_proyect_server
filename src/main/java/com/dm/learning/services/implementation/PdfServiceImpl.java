package com.dm.learning.services.implementation;

import com.dm.learning.services.PdfService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfServiceImpl implements PdfService {
    @Override
    public String extractTextFromPdf(File file) throws IOException {
        PDDocument document = Loader.loadPDF(file);

        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text = pdfTextStripper.getText(document);
System.out.println(text);
        document.close();
        return text;
    }
}
