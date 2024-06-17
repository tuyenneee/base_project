package com.ivnd.knowledgebase.controller.spec;

import com.ivnd.knowledgebase.model.request.PersonRequest;
import com.ivnd.knowledgebase.model.response.PersonResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@RequestMapping("/api/v1/person")
public interface PersonController {
    @PostMapping()
    void create(@RequestBody PersonRequest request);

    @PutMapping("/{id}")
    void update(@PathVariable Long id, @RequestBody PersonRequest request);

    @GetMapping("/{id}")
    PersonResponse getById(@PathVariable Long id);

    @GetMapping()
    Page<PersonResponse> getPage(@ModelAttribute PersonRequest request);

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Long id);
}
