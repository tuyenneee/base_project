package com.ivnd.knowledgebase.service;

import com.ivnd.knowledgebase.common.BaseService;
import com.ivnd.knowledgebase.model.entity.Person;
import com.ivnd.knowledgebase.model.error.ErrorCode;
import com.ivnd.knowledgebase.model.error.ErrorCodeException;
import com.ivnd.knowledgebase.repository.PersonRepository;
import org.springframework.stereotype.Service;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@Service
public class PersonService extends BaseService<Person, Long, PersonRepository> {
    public PersonService(PersonRepository repo) {
        super(repo);
    }

    @Override
    protected ErrorCode errorCodeNotFoundEntity() {
        return ErrorCode.COMMON_001_ENTITY_NOT_FOUND;
    }
}
