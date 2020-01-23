/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    JwtInterceptor.java
 */
package com.board.project.blockboard.common.interceptor;

import com.board.project.blockboard.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
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
            log.info("interceptor에서 true");
            return true;
        } else {
            log.info("interceptor에서 false");
            log.info("redirectContent= " + request.getContextPath());
            log.info("redirectServlet= " + request.getServletPath());
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
    }
}