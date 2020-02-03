/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file CookieUtils.java
 */
package com.board.project.blockboard.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.StringUtils;

public class CookieUtils {

  public static String getCookie(HttpServletRequest request, String cookieName) {
    Cookie[] getCookie = request.getCookies();
    String token = null;
    if(getCookie != null) {
      for (Cookie c : getCookie) {
        if (StringUtils.equals(c.getName(), cookieName))
          token = c.getValue();
      }
    }
    return token;
  }
}
