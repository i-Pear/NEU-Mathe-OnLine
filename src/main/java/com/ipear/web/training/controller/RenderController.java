package com.ipear.web.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RenderController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/train")
    public String train() {
        return "train";
    }

}
