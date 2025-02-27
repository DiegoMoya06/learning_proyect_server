package com.dm.learning.services;

import java.io.File;
import java.io.IOException;

public interface DSService {
    String sendChatRequestWithFile(File file) throws IOException;
}
