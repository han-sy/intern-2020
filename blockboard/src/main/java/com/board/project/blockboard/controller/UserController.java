/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    UserController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.common.util.CookieUtils;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            return "redirect:/login";
    }

    /**
     * 로그아웃
     * @param response      유효기간이 0인 쿠키를 담은 객체
     * @return              로그인 메인화면으로 redirect
     */
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie c = new Cookie(HEADER_NAME, null);
        c.setMaxAge(0);
        response.addCookie(c);
        return "redirect:/login";
    }

    /**
     * 로그인 메인 화면
     * @param request       쿠키 조회하기 위한 객체
     * @return              server가 만들어 준 쿠키가 있다면 -> /boards redirect
     *                      없다면 -> 로그인 화면 띄운다.
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        // 이미 JWT 토큰을 가지고 있으면 로그인 생략 후 메인화면으로 이동
        CookieUtils cookieUtils = new CookieUtils();
        String token = cookieUtils.getCookie(request,HEADER_NAME);

        if(jwtService.isUsable(token))
            return "redirect:/boards";
        else
            return "login";
    }
}
