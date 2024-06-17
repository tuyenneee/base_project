package com.ivnd.knowledgebase.service;

import com.ivnd.knowledgebase.common.BaseService;
import com.ivnd.knowledgebase.model.entity.Province;
import com.ivnd.knowledgebase.model.error.ErrorCode;
import com.ivnd.knowledgebase.repository.ProvinceRepository;
import org.springframework.stereotype.Service;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 02/05/2024
 */
@Service
public class ProvinceService extends BaseService<Province, Long, ProvinceRepository> {
    public ProvinceService(ProvinceRepository repo) {
        super(repo);
    }

    @Override
    protected ErrorCode errorCodeNotFoundEntity() {
        return ErrorCode.COMMON_001_ENTITY_NOT_FOUND;
    }
}
