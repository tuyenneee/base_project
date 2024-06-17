package com.ivnd.knowledgebase.common;

import com.ivnd.knowledgebase.model.error.ErrorCode;
import com.ivnd.knowledgebase.model.error.ErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class BaseService<E, ID, R extends BaseRepository<E, ID>> {
    protected final R repo;
    protected final Logger log;

    protected BaseService(R repo) {
        this.log = LoggerFactory.getLogger(getClass());
        this.repo = repo;
    }

    public E save(E entity) {
        repo.save(entity);
        return entity;
    }

    public List<E> saveAll(List<E> entities) {
        repo.saveAll(entities);
        return entities;
    }

    public Optional<E> get(ID id) {
        return repo.findById(id);
    }

    public E getOrElseThrow(ID id) {
        return get(id).orElseThrow(() -> new ErrorCodeException(errorCodeNotFoundEntity()));
    }

    public void delete(E entity) {
        repo.delete(entity);
    }

    public void deleteIfExist(ID id) {
        Optional<E> optional = get(id);
        optional.ifPresent(this::delete);
    }

    public void update(E entity, Consumer<E> fieldConsumer) {
        fieldConsumer.accept(entity);
        this.save(entity);
    }

    public void updateOnField(ID id, Consumer<E> fieldConsumer) {
        E entity = getOrElseThrow(id);
        this.update(entity, fieldConsumer);
    }

    public Page<E> query(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public List<E> findAll(Pageable pageable) {
        List<E> result = new LinkedList<>();

        Page<E> page = query(pageable);
        while (page.hasContent()) {
            result.addAll(page.getContent());
            if (result.size() >= page.getTotalElements()) break;
            var nextPageable = page.getPageable().next();
            page = query(nextPageable);
        }
        return result;
    }

    public List<E> findAll(Integer offset, Integer limit) {
        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        Pageable pageable = PageRequest.of(offset, limit);
        return findAll(pageable);
    }

    protected abstract ErrorCode errorCodeNotFoundEntity();
}
