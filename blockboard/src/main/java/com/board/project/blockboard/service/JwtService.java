/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    JwtService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service("jwtService")
public class JwtService {
    private final String SALT = "blockboard"; // Secret Key
    private final String HEADER_NAME = "Authorization";

    public <T> String create(String key, T data, String subject) {
        String issure = "BlockBoard";
        Date exDate = new Date(System.currentTimeMillis() + (60000 * 60 * 24)); // 토큰 만료 시간 10분
        String jwt = Jwts.builder()
                .setHeaderParam("typ","JWT")
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
            if(log.isInfoEnabled()) {
                e.printStackTrace();
            }else {
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

    public Map<String, Object> get(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Cookie[] getCookie = request.getCookies();
        String jwt = null;
        for(Cookie c : getCookie) {
            if(StringUtils.equals(c.getName(),HEADER_NAME))
                jwt = c.getValue();
        }
        Jws<Claims> claims = null;
            try {
            claims = Jwts.parser()
                    .setSigningKey(SALT.getBytes("UTF-8"))
                    .parseClaimsJws(jwt);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> value = (LinkedHashMap<String, Object>)claims.getBody().get(key);
        return value;
    }

    public String getUserId() {
        return this.get(HEADER_NAME).get("userID").toString();
    }
    public int getCompanyId() {
        return Integer.parseInt(this.get(HEADER_NAME).get("companyID").toString());
    }
    public String getUserType() {
        return this.get(HEADER_NAME).get("userType").toString();
    }
    public String getUserName() {
        return this.get(HEADER_NAME).get("userName").toString();
    }
}
