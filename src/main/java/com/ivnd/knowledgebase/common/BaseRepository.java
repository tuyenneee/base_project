package com.ivnd.knowledgebase.common;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseRepository<E, ID> extends PagingAndSortingRepository<E, ID> {
}
