package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.UserService;
import com.board.project.blockboard.util.AES256Util;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    private String key = "slgi3ibu5phi8euf";
    private String token = "server";
    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/loginCheck")
    public String loginCheck(@ModelAttribute UserDTO requestUser, HttpServletResponse response) throws UnsupportedEncodingException {
        // 로그인 Validation
        boolean result = userService.loginCheck(requestUser);
        logger.info(requestUser.getUserID() + requestUser.getUserPassword());
        // 로그인 성공시 클라이언트에게 생성한 쿠키 전달
        if(result) {
            // 암호화 과정
            AES256Util aes256 = new AES256Util(key);
            URLCodec codec = new URLCodec();
            String mergeToken = requestUser.getUserID() + token;

            try {
                String encrypt = codec.encode(aes256.aesEncode(mergeToken));
                Cookie sessionCookie = new Cookie("sessionID", encrypt); // 클라이언트에게 전달할 쿠키 생성
                Cookie userIDCookie = new Cookie("userID", requestUser.getUserID()); // userID 쿠키 생성
                sessionCookie.setMaxAge(60*60);
                userIDCookie.setMaxAge(60*60);
                response.addCookie(sessionCookie);
                response.addCookie(userIDCookie);
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/";
            }

        }
        return "redirect:/";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletResponse response) {
        // s_id 쿠키 새로 생성해서 시간 0으로 설정
        Cookie kc = new Cookie("sessionID", null);
        kc.setMaxAge(0);
        response.addCookie(kc);
        return "redirect:/";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login(HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] getCookie = request.getCookies();
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        // 클라이언트가 보낸 쿠키가 서버가 생성해준건지 검사 (복호화 과정)
        if(getCookie != null){
            for(int i=0; i<getCookie.length; i++){
                Cookie c = getCookie[i];
                String name = c.getName();
                String value = c.getValue();

                if(name.equals("sessionID")) {
                    String decode = null;
                    try {
                        decode = aes256.aesDecode(codec.decode(value));
                        logger.info(decode);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    //서버가 만들어준 쿠키면
                    if(decode.substring(decode.length()-6,decode.length()).equals("server")) {
                        return "redirect:/board";
                    }
                }
            }
        }

        return "login";
    }
}
