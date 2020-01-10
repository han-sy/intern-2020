package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/loginCheck")
    public String loginCheck(HttpServletRequest request, HttpSession session) {
        // 로그인 성공 Check
        boolean result = userService.loginCheck(request, session);
        System.out.println(result);
        return "login";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 종료
        return "login";
    }

    @RequestMapping("/")
    public String login() {
        return "login";
    }
}
