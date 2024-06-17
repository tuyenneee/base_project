package com.ivnd.knowledgebase.facade;

import com.ivnd.knowledgebase.common.BaseFacade;
import com.ivnd.knowledgebase.anotation.Facade;
import com.ivnd.knowledgebase.model.entity.Person;
import com.ivnd.knowledgebase.model.request.PersonRequest;
import com.ivnd.knowledgebase.model.response.PersonResponse;
import com.ivnd.knowledgebase.service.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@Facade
public class PersonFacade extends BaseFacade<PersonService> {
    public PersonFacade(PersonService service) {
        super(service);
    }

    public void create(PersonRequest request) {
        var person = new Person();
        person.setFullName(request.getFullName())
                .setAge(request.getAge())
                .setGender(request.getGender())
                .setPhone(request.getPhone())
                .setIdentityNo(request.getIdentityNo());
        service.save(person);
    }

    public Page<PersonResponse> getPage(PersonRequest request) {
        var pageable = PageRequest.of(request.getPage(), request.getSize());
        var page = service.query(pageable);
        return page.map(PersonResponse::of);
    }

    public PersonResponse getById(Long id) {
        var person = service.getOrElseThrow(id);
        return PersonResponse.of(person);
    }

    public void update(Long id, PersonRequest request) {
        service.updateOnField(id, person -> {
            person.setFullName(request.getFullName());
            person.setAge(request.getAge());
            person.setGender(request.getGender());
            person.setPhone(request.getPhone());
            person.setIdentityNo(request.getIdentityNo());
        });
    }
}
