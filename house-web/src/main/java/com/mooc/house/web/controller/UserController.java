package com.mooc.house.web.controller;

import com.mooc.house.biz.service.UserService;
import com.mooc.house.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("msg")
    public List<User> accountsRegister() {
        return userService.getUsers();
    }

    @RequestMapping("index")
    public String index() {
        return "homepage/index";
    }
}
