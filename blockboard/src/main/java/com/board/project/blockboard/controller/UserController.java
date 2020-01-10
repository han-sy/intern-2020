package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/test")
    public String second(HttpServletRequest requset, Model model) {
        List<UserDTO> list = userService.allUser();
        String id = requset.getParameter("id");
        String pwd = requset.getParameter("pwd");

        model.addAttribute("user_id",list.get(0).getUser_id());
        model.addAttribute("com_id",list.get(0).getCom_id());
        model.addAttribute("user_type",list.get(0).getUser_type());
        model.addAttribute("id", id);
        model.addAttribute("pwd", pwd);

        return "test";
    }

    @RequestMapping("/loginCheck")
    public String loginCheck(UserDTO user, HttpSession session) {
        boolean result = userService.loginCheck(user, session);
        if(result == true){ // 로그인 성공
            // board.jsp로 이동
            return "board";
        } else {            // 로그인 실패
            // 로그인 화면으로 이동
            return "login";
        }
    }
    @RequestMapping("/")
    public String login() {
        return "login";
    }
}
