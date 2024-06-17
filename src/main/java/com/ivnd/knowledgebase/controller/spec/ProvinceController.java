package com.ivnd.knowledgebase.controller.spec;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 02/05/2024
 */
@RequestMapping("/api/v1/province")
public interface ProvinceController {
    void create();

    @GetMapping()
    JsonNode getProvinces();
}
