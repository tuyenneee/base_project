package com.ivnd.knowledgebase.controller;

import com.ivnd.knowledgebase.facade.PersonFacade;
import com.ivnd.knowledgebase.service.PersonService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by: tuyen.nguyenvan
 * Email: tuyen.nguyen3@ivnd.com.vn
 * Date: 28/05/2024
 */
@Controller
public class TestController {
    private final PersonFacade facade;
    private final PersonService service;

    public TestController(PersonFacade facade, PersonService service) {
        this.facade = facade;
        this.service = service;
    }

    @GetMapping()
    public String exam(Model model) {
        var page = service.findAll(PageRequest.of(0, 100));
        model.addAttribute("person", page);
        return "index";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        var page = service.getOrElseThrow(id);
        model.addAttribute("detail", page);
        return "detail";
    }
}
