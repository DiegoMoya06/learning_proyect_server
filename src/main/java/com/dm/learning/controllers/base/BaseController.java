package com.dm.learning.controllers.base;

import com.dm.learning.services.base.BaseService;
import com.dm.learning.utils.BaseLogger;

public abstract class BaseController<S extends BaseService> extends BaseLogger {

    protected final S service;

    protected BaseController(S service) {
        this.service = service;
    }
}
