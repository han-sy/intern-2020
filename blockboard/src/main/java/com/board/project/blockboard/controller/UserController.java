package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.UserService;
import com.board.project.blockboard.util.AES256Util;
import com.board.project.blockboard.util.SessionTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/")
    public String loginCheck(@ModelAttribute UserDTO requestUser, HttpServletResponse response) {
        SessionTokenizer sessionTokenizer = new SessionTokenizer();
        String key = sessionTokenizer.getKey();
        String token = sessionTokenizer.getToken();

        boolean user_Exist = userService.loginCheck(requestUser);

        if(user_Exist) {
            // 암호화 과정
            AES256Util aes256 = null;
            try {
                aes256 = new AES256Util(key);
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/";
            }
            URLCodec codec = new URLCodec();
            String userID = requestUser.getUserID(); // userID 가져옴.
            int companyID = userService.selectCompanyIDByUserID(userID); // companyID 가져옴.

            String sessionID = userID + "#" + companyID + "#" + token; // sessionID = {userID}#{companyID}#{serverToken}

            // 로그인 성공시 클라이언트에게 생성한 쿠키 전달
            String encrypt = null;
            try {
                encrypt = codec.encode(aes256.aesEncode(sessionID));
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/";
            }
            Cookie sessionCookie = new Cookie("sessionID", encrypt); // 클라이언트에게 전달할 쿠키 생성
            Cookie userIDCookie = new Cookie("userID", requestUser.getUserID()); // userID 쿠키 생성
            sessionCookie.setMaxAge(60*60);
            userIDCookie.setMaxAge(60*60);
            response.addCookie(sessionCookie);
            response.addCookie(userIDCookie);
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // s_id 쿠키 새로 생성해서 시간 0으로 설정
        Cookie kc = new Cookie("sessionID", null);
        kc.setMaxAge(0);
        response.addCookie(kc);
        return "redirect:/";
    }

    @GetMapping("/")
    public String login(HttpServletRequest request) {
        SessionTokenizer session = null;
        try {
            session = new SessionTokenizer(request);
            String serverToken = session.getServerToken();
            // serverToken 검사해서 맞으면 board로 return
            if (serverToken.equals(session.getToken())) {
                return "redirect:/boards";
            }
        } catch (Exception e) {
            session = new SessionTokenizer();
            log.debug(e.getMessage());
            return "login";
        }
        return "login";
    }
}
