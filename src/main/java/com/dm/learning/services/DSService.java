package com.dm.learning.services;

import com.dm.learning.dto.deck.NewAutomaticDeckDto;

import java.io.File;
import java.io.IOException;

public interface DSService {
    NewAutomaticDeckDto sendChatRequestWithFile(File file) throws IOException;
}
