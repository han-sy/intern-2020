package com.board.project.blockboard.common.interceptor;

import com.board.project.blockboard.common.exception.UnauthorizedException;
import com.board.project.blockboard.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final String HEADER_AUTH = "Authorization";

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        Cookie[] getCookie = request.getCookies();
        String token = null;
        for (Cookie c : getCookie) {
            if (c.getName().equals(HEADER_AUTH))
                token = c.getValue();
        }
        if (token != null && jwtService.isUsable(token)) {
            return true;
        } else {
            return true;
        }
    }
}