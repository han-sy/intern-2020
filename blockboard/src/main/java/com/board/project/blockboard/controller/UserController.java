package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.UserService;
import com.board.project.blockboard.util.AES256Util;
import com.board.project.blockboard.util.SessionTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 로그인 검증
     * @param requestUser   로그인을 요청한 User의 정보
     * @param response      쿠키를 담은 객체
     * @return              로그인 메인화면으로 redirect
     */
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
            sessionCookie.setMaxAge(60*60);
            response.addCookie(sessionCookie);
        }
        return "redirect:/";
    }

    /**
     * 로그아웃
     * @param response      유효기간이 0인 쿠키를 담은 객체
     * @return              로그인 메인화면으로 redirect
     */
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie kc = new Cookie("sessionID", null);
        kc.setMaxAge(0);
        response.addCookie(kc);
        return "redirect:/";
    }

    /**
     * 로그인 메인 화면
     * @param request       쿠키 조회하기 위한 객체
     * @return              server가 만들어 준 쿠키가 있다면 -> /boards redirect
     *                      없다면 -> 로그인 화면 띄운다.
     */
    @GetMapping("/")
    public String login(HttpServletRequest request) {
        SessionTokenizer session = null;
        try {
            session = new SessionTokenizer(request);
            String serverToken = session.getServerToken();
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
