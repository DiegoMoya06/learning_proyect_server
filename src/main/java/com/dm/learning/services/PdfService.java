package com.dm.learning.services;

import java.io.File;
import java.io.IOException;

public interface PdfService {
    String extractTextFromPdf(File file) throws IOException;
}
