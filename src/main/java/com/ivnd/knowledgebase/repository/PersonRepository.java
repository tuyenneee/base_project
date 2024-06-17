package com.ivnd.knowledgebase.repository;

import com.ivnd.knowledgebase.common.BaseRepository;
import com.ivnd.knowledgebase.model.entity.Person;
import org.springframework.stereotype.Repository;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@Repository
public interface PersonRepository extends BaseRepository<Person, Long> {
}
