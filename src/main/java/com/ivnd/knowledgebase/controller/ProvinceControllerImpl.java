package com.ivnd.knowledgebase.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ivnd.knowledgebase.common.BaseController;
import com.ivnd.knowledgebase.controller.spec.ProvinceController;
import com.ivnd.knowledgebase.facade.ProvinceFacade;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 02/05/2024
 */
@Log4j2
@RestController
public class ProvinceControllerImpl extends BaseController<ProvinceFacade> implements ProvinceController {
    public ProvinceControllerImpl(ProvinceFacade facade) {
        super(facade);
    }

    @Override
    public void create() {
        log.info("ok");
    }

    @Override
    public JsonNode getProvinces() {
        return facade.getProvince();
    }
}
