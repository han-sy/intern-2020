/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    JwtInterceptor.java
 */
package com.board.project.blockboard.common.interceptor;

import com.board.project.blockboard.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

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
            return true;
        } else {
            if(isAjaxRequest(request))
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            else
                response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxHeader = "x-requested-with";
        return StringUtils.equals("XMLHttpRequest", request.getHeader(ajaxHeader));
    }
}