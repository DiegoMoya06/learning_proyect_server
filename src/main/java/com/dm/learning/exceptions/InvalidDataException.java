package com.dm.learning.exceptions;

import org.slf4j.helpers.MessageFormatter;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Object... interpolations) {
        super(MessageFormatter.arrayFormat(message, interpolations).getMessage());
    }
}
