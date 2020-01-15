package com.board.project.blockboard.controller;

import com.board.project.blockboard.service.UserService;
import com.board.project.blockboard.util.AES256Util;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    private String key = "slgi3ibu5phi8euf";
    private String token = "server";
    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/loginCheck")
    public String loginCheck(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, EncoderException {
        // 로그인 Validation
        boolean result = userService.loginCheck(request, session);

        // 로그인 성공시 클라이언트에게 생성한 쿠키 전달
        if(result) {
            // 암호화 과정
            AES256Util aes256 = new AES256Util(key);
            URLCodec codec = new URLCodec();

            String user_id = request.getParameter("user_id") + token;
            String encrypt = "";

            encrypt = codec.encode(aes256.aesEncode(user_id));

<<<<<<< HEAD
            Cookie setCookie = new Cookie("s_id", encrypt); // 클라이언트에게 전달할 쿠키 생성
            setCookie.setMaxAge(60*60);
=======
            Cookie setCookie = new Cookie("session_id", encrypt); // 클라이언트에게 전달할 쿠키 생성
            setCookie.setMaxAge(60*60*24); // 쿠키 유지 시간을 하루로 설정
>>>>>>> 3b3e51fb97ab85c937552f8c52d1503097d4169c
            response.addCookie(setCookie);
        }
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletResponse response) {
        // s_id 쿠키 새로 생성해서 시간 0으로 설정
        Cookie kc = new Cookie("s_id", null);
        kc.setMaxAge(0);
        response.addCookie(kc);
        return "redirect:/";
    }

    @RequestMapping( "/")
    public String login(HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cookie[] getCookie = request.getCookies();
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        // 클라이언트가 보낸 쿠키가 서버가 생성해준건지 검사 (복호화 과정)
        if(getCookie != null){
            for(int i=0; i<getCookie.length; i++){
                Cookie c = getCookie[i];
                String name = c.getName();
                String value = c.getValue();

                if(name.equals("s_id")) {
                    String decode_str = aes256.aesDecode(codec.decode(value));
                    String server_token = decode_str.substring(decode_str.length()-6,decode_str.length());
                    logger.info(server_token);

                    //서버가 만들어준 쿠키면 게시판 화면으로 바로 이동
                    if(server_token.equals(token)) {
                        return "redirect:/board";
                    }
                }
            }
        }
        return "login";
    }
}
