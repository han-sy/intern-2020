package com.board.project.blockboard.controller;

import com.board.project.blockboard.model.User;
import com.board.project.blockboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    @ResponseBody
    public List<User> allUser() {
        return userService.allUser();
    }

    @RequestMapping("/test")
    public String second(Model model) {
        List<User> list = userService.allUser();

        model.addAttribute("id",list.get(0).getId());
        model.addAttribute("name",list.get(0).getName());

        return "test";
    }
}
