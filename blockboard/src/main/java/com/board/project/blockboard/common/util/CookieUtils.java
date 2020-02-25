/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file CookieUtils.java
 */
package com.board.project.blockboard.common.util;

import java.net.URLDecoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.StringUtils;

public class CookieUtils {

  @SneakyThrows
  public static String getCookie(HttpServletRequest request, String cookieName) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }
    String token = "";
    for (Cookie cookie : cookies) {
      if (StringUtils.equals(cookie.getName(), cookieName)) {
        token = URLDecoder.decode(cookie.getValue(), "UTF-8");
        break;
      }
    }
    return token;
  }

  @SneakyThrows
  public static String expireCookie(HttpServletRequest request, String cookieName,
      HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }
    String token = "";
    for (Cookie cookie : cookies) {
      if (StringUtils.equals(cookie.getName(), cookieName)) {
        cookie.setPath("/");
        cookie.setValue("");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        break;
      }
    }
    return token;
  }
}
