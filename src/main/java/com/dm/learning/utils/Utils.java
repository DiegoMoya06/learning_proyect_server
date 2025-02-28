package com.dm.learning.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    public static File createMockPdfFile() throws IOException {
        File tempFile = File.createTempFile("mock", ".pdf");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(createMockPdf());
        }
        return tempFile;
    }

    public static byte[] createMockPdf() {
        String pdfContent = "%PDF-1.7\n" +
                "1 0 obj\n" +
                "<< /Type /Catalog /Pages 2 0 R >>\n" +
                "endobj\n" +
                "2 0 obj\n" +
                "<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n" +
                "endobj\n" +
                "3 0 obj\n" +
                "<< /Type /Page /Parent 2 0 R /Contents 4 0 R >>\n" +
                "endobj\n" +
                "4 0 obj\n" +
                "<< /Length 44 >>\n" +
                "stream\n" +
                "BT /F1 12 Tf 100 700 Td (Hello, this is a mock PDF!) Tj ET\n" +
                "endstream\n" +
                "endobj\n" +
                "xref\n" +
                "0 5\n" +
                "0000000000 65535 f \n" +
                "0000000010 00000 n \n" +
                "0000000079 00000 n \n" +
                "0000000178 00000 n \n" +
                "0000000301 00000 n \n" +
                "trailer\n" +
                "<< /Size 5 /Root 1 0 R >>\n" +
                "startxref\n" +
                "394\n" +
                "%%EOF";

        return pdfContent.getBytes();
    }
}
