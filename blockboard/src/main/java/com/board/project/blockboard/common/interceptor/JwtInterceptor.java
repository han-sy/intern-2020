/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file JwtInterceptor.java
 */
package com.board.project.blockboard.common.interceptor;

import com.board.project.blockboard.common.util.CookieUtils;
import com.board.project.blockboard.service.JwtService;
import java.io.IOException;
import java.io.PrintWriter;
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

  private static final String AUTH_HEADER = "Authorization";
  private static final String CSRF_HEADER = "_csrf";
  private static final String CSRF_COOKIE = "CSRF_TOKEN";
  private static final String AJAX_HEADER = "x-requested-with";

  @Autowired
  private JwtService jwtService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)
      throws Exception {
    String token = CookieUtils.getCookie(request, AUTH_HEADER);
    // JWT Token 만료 검사 + 유저 일치검사 + CSRF 검사
    if (token != null && jwtService.isUsable(token)) {
      jwtService.setUserDataToRequest(request);
      checkCsrfToken(request, response);
      return true;
    }
    return sendError(request, response);
  }

  private boolean sendError(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (isAjaxRequest(request)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    } else {
      sendErrorToNotAjax(request, response);
    }
    return false;
  }

  private void sendErrorToNotAjax(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.sendRedirect(request.getContextPath() + "/login");
  }

  private void sendErrorToNotAjaxWithAlertMessage(HttpServletResponse response, String message)
      throws IOException {
    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.println("<script>alert('" + message + "');</script>");
  }

  private boolean isAjaxRequest(HttpServletRequest request) {
    return StringUtils.equals("XMLHttpRequest", request.getHeader(AJAX_HEADER));
  }

  private void checkCsrfToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (request.getMethod().equals("GET")) {
      return;
    }
    String csrfInHeader = request.getHeader(CSRF_HEADER);
    String csrfInCookie = CookieUtils.getCookie(request, CSRF_COOKIE);
    if (!StringUtils.equals(csrfInCookie, csrfInHeader)) {
      sendErrorToNotAjaxWithAlertMessage(response, "CSRF 공격 탐지. 해당 요청을 실행하지 않습니다.");
    }
    CookieUtils.expireCookie(request, CSRF_COOKIE, response);
  }
}