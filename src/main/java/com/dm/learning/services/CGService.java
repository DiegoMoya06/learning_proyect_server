package com.dm.learning.services;

import java.io.File;
import java.io.IOException;

public interface CGService {

    String sendChatRequestWithFile(File file) throws IOException;

    String sendChatRequest(String userMessage);

    String uploadFile(File file);
}
