package com.ipear.web.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RenderController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/app")
    public String app() {
        return "app";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/history")
    public String history() {
        return "history";
    }

    @RequestMapping("/train")
    public String train() {
        return "train";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/changePassword")
    public String changePassword() {
        return "changePassword";
    }

}
