package com.ivnd.knowledgebase.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController<F> {
    protected final Logger log;
    protected final F facade;

    protected BaseController(F facade) {
        this.log = LoggerFactory.getLogger(getClass());
        this.facade = facade;
    }
}
