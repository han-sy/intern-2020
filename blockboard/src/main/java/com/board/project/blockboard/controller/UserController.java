package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    final String HEADER_NAME = "Authorization";
    /**
     * 로그인 검증
     * @param requestUser   로그인을 요청한 User의 정보
     * @param response      쿠키를 담은 객체
     * @return              로그인 메인화면으로 redirect
     */
    @PostMapping("/login")
    public String loginCheck(@ModelAttribute UserDTO requestUser, HttpServletResponse response) {
        boolean isValid = userService.loginCheck(requestUser, response);
        if(isValid)
            return "redirect:/boards";
        else
            return "redirect:/";
    }

    /**
     * 로그아웃
     * @param response      유효기간이 0인 쿠키를 담은 객체
     * @return              로그인 메인화면으로 redirect
     */
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie c = new Cookie("Authorization", null);
        c.setMaxAge(0);
        response.addCookie(c);
        return "redirect:/";
    }

    /**
     * 로그인 메인 화면
     * @param request       쿠키 조회하기 위한 객체
     * @return              server가 만들어 준 쿠키가 있다면 -> /boards redirect
     *                      없다면 -> 로그인 화면 띄운다.
     */
    @GetMapping("/")
    public String login(HttpServletRequest request) {
        // 이미 JWT 토큰을 가지고 있으면 로그인 생략 후 메인화면으로 이동
        Cookie[] getCookie = request.getCookies();
        if (getCookie != null) {
            for (Cookie c : getCookie) {
                if (c.getName().equals(HEADER_NAME)) {
                    if (jwtService.isUsable(c.getValue()))
                        return "redirect:/boards";
                    else
                        return "login";
                }
            }
        }
    return "login";
    }
}
