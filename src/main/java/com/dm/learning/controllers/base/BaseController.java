package com.dm.learning.controllers.base;

import com.dm.learning.services.base.BaseService;
import com.dm.learning.utils.BaseLogger;
import org.springframework.web.bind.annotation.CrossOrigin;

//TODO: check, this will be change when security added
@CrossOrigin(maxAge = 3600)
public abstract class BaseController<S extends BaseService> extends BaseLogger {

    protected final S service;

    protected BaseController(S service) {
        this.service = service;
    }
}
