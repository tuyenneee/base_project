package com.ivnd.knowledgebase.controller;

import com.ivnd.knowledgebase.anotation.Loggable;
import com.ivnd.knowledgebase.common.BaseController;
import com.ivnd.knowledgebase.controller.spec.PersonController;
import com.ivnd.knowledgebase.facade.PersonFacade;
import com.ivnd.knowledgebase.model.request.PersonRequest;
import com.ivnd.knowledgebase.model.response.PersonResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@RestController
public class PersonControllerImpl extends BaseController<PersonFacade> implements PersonController {
    public PersonControllerImpl(PersonFacade facade) {
        super(facade);
    }

    @Override
    public void create(PersonRequest request) {
        facade.create(request);
    }

    @Override
    public void update(Long id, PersonRequest request) {
        facade.update(id, request);
    }

    @Override
    @Loggable
    public PersonResponse getById(Long id) {
        return facade.getById(id);
    }

    @Override
    @Loggable
    public Page<PersonResponse> getPage(PersonRequest request) {
        return facade.getPage(request);
    }

    @Override
    public void deleteById(Long id) {
        //TODO
    }
}
