/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file JwtService.java
 */
package com.board.project.blockboard.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service("jwtService")
public class JwtService {

  private final String SALT = "blockboard"; // Secret Key
  private final String HEADER_NAME = "Authorization";

  public <T> String create(String key, T data, String subject) {
    String issure = "BlockBoard";
    Date exDate = new Date(System.currentTimeMillis() + (60000 * 60 * 24));
    String jwt = Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setHeaderParam("regDate", System.currentTimeMillis())
        .setSubject(subject)
        .claim(key, data)
        .signWith(SignatureAlgorithm.HS256, this.generateKey())
        .setIssuedAt(new Date())
        .setExpiration(exDate)
        .setIssuer(issure)
        .compact();
    return jwt;
  }

  private byte[] generateKey() {
    byte[] key = null;
    try {
      key = SALT.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      if (log.isInfoEnabled()) {
        e.printStackTrace();
      } else {
        log.error("Making JWT Key Error {}", e.getMessage());
      }
    }
    return key;
  }

  public boolean isUsable(String jwt) {
    try {
      Jws<Claims> claims = Jwts.parser()
          .setSigningKey(this.generateKey())
          .parseClaimsJws(jwt);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Map<String, Object> get() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .currentRequestAttributes()).getRequest();
    Cookie[] getCookie = request.getCookies();
    String jwt = null;
    for (Cookie c : getCookie) {
      if (StringUtils.equals(c.getName(), HEADER_NAME)) {
        jwt = c.getValue();
      }
    }
    Jws<Claims> claims = null;
    try {
      claims = Jwts.parser()
          .setSigningKey(SALT.getBytes("UTF-8"))
          .parseClaimsJws(jwt);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (Map<String, Object>) claims.getBody().get(HEADER_NAME);
  }

  // Interceptor 의 request 에 JWT 에서 가져온 유저정보를 넣어준다.
  public void setUserDataToRequest(HttpServletRequest request) {
    Map<String, Object> jwtClaims = this.get();
    request.setAttribute("userId", jwtClaims.get("userId").toString());
    request.setAttribute("userName", jwtClaims.get("userName").toString());
    request.setAttribute("userType", jwtClaims.get("userType").toString());
    request.setAttribute("companyId", Integer.parseInt(jwtClaims.get("companyId").toString()));
  }
}
