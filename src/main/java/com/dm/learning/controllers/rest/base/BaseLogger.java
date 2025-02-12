package com.dm.learning.controllers.rest.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extend if a logger is needed
 */
public abstract class BaseLogger {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
}
