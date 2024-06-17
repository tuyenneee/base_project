package com.ivnd.knowledgebase.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseFacade<S extends BaseService<?, ?, ?>> {
    protected final Logger log;
    protected final S service;

    protected BaseFacade(S service) {
        this.log = LoggerFactory.getLogger(getClass());
        this.service = service;
    }
}
