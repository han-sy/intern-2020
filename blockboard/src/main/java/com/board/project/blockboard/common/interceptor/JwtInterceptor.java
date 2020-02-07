/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file JwtInterceptor.java
 */
package com.board.project.blockboard.common.interceptor;

import com.board.project.blockboard.common.util.CookieUtils;
import com.board.project.blockboard.service.JwtService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

  private static final String HEADER_AUTH = "Authorization";

  @Autowired
  private JwtService jwtService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)
      throws Exception {
    String token = CookieUtils.getCookie(request, HEADER_AUTH);

    // JWT Token 만료 검사 + 유저 일치검사
    if (token != null && jwtService.isUsable(token)) {
      jwtService.setUserDataToRequest(request);
      return true;
    } else {
      if (isAjaxRequest(request)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      } else {
        response.sendRedirect(request.getContextPath() + "/login");
      }
      return false;
    }

  }

  private boolean isAjaxRequest(HttpServletRequest request) {
    String ajaxHeader = "x-requested-with";
    return StringUtils.equals("XMLHttpRequest", request.getHeader(ajaxHeader));
  }
}