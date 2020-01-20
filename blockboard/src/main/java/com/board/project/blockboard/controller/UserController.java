package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.UserService;
import com.board.project.blockboard.util.AES256Util;
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
import java.util.StringTokenizer;
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

    private String key = "slgi3ibu5phi8euf";
    private String token = "server";

    @PostMapping("/")
    public String loginCheck(@ModelAttribute UserDTO requestUser, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, EncoderException {

        boolean user_Exist = userService.loginCheck(requestUser);

        if(user_Exist) {
            // 암호화 과정
            AES256Util aes256 = new AES256Util(key);
            URLCodec codec = new URLCodec();
            String userID = requestUser.getUserID(); // userID 가져옴.
            int companyID = userService.selectCompanyIDByUserID(userID); // companyID 가져옴.

            String sessionID = userID + "#" + companyID + "#" + token; // sessionID = {userID}#{companyID}#{serverToken}

            // 로그인 성공시 클라이언트에게 생성한 쿠키 전달
            String encrypt = codec.encode(aes256.aesEncode(sessionID));
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
    public String login(HttpServletRequest request) throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cookie[] getCookie = request.getCookies();
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        // 클라이언트가 보낸 쿠키가 서버가 생성해준건지 검사 (복호화 과정)
        if(getCookie != null){
            for(Cookie c : getCookie){
                if(c.getName().equals("sessionID")) {
                    String decode = null;
                    decode = aes256.aesDecode(codec.decode(c.getValue()));
                    log.info(decode);

                    // token[0]=userID, token[1]=companyID, token[2]=serverToken
                    StringTokenizer tokenizer = new StringTokenizer(decode, "#");
                    String userID = tokenizer.nextToken();
                    int companyID = Integer.parseInt(tokenizer.nextToken());
                    String serverToken = tokenizer.nextToken();

                    // serverToken 검사해서 맞으면 board로 return
                    if(serverToken.equals(token)) {
                        return "redirect:/boards";
                    }
                }
            }
        }

        return "login";
    }
}
