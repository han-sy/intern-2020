/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file JwtInterceptor.java
 */
package com.board.project.blockboard.common.interceptor;

import com.board.project.blockboard.common.util.CookieUtils;
import com.board.project.blockboard.common.util.JsonParse;
import com.board.project.blockboard.common.validation.AuthorityValidation;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.JwtService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

  private static final String HEADER_AUTH = "Authorization";

  @Autowired
  private JwtService jwtService;

  private AuthorityValidation validateAuthority;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)
      throws Exception {
    String token = CookieUtils.getCookie(request, HEADER_AUTH);
    validateAuthority = new AuthorityValidation();

    // JWT Token 만료 검사 + 유저 일치검사
    if (token != null && jwtService.isUsable(token)) {
      String userDataJson = request.getParameter("userData");
      if (userDataJson != null) {
        UserDTO userData = JsonParse.jsonToDTO(userDataJson,UserDTO.class);
        if (validateAuthority.isValidateUserData(userData, jwtService.getUserDTO(), response)) {
          return true;
        }
        return false;
      }
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

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    if(handler == null)
      log.info("반환하는 값이 null입니다.");
  }

  private boolean isAjaxRequest(HttpServletRequest request) {
    String ajaxHeader = "x-requested-with";
    return StringUtils.equals("XMLHttpRequest", request.getHeader(ajaxHeader));
  }
}