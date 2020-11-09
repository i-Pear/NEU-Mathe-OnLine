package com.ipear.web.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RenderController {

    @RequestMapping("/")
    public String index() {
        return "app";
    }

    @RequestMapping("/train")
    public String train() {
        return "train";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

}
