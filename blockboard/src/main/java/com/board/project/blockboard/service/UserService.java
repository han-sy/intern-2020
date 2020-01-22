package com.board.project.blockboard.service;

import com.board.project.blockboard.mapper.UserMapper;
import com.board.project.blockboard.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtService jwtService;
    private final String HEADER_NAME = "Authorization";

    public boolean loginCheck(UserDTO requestUser, HttpServletResponse response) {
        // 로그인 요청 사용자의 정보를 가져온다.
        UserDTO login_user = userMapper.selectUserByID(requestUser.getUserID());
        String login_userPassword = login_user.getUserPassword();
        String requestPassword = requestUser.getUserPassword();
        String jwtToken = "";

        // 패스워드 검사
        if(login_userPassword.equals(requestPassword)) {
            login_user.setUserPassword(null); // 비밀번호는 JWT 토큰에 담지 않는다.
            jwtToken = jwtService.create(HEADER_NAME, login_user, "user_info");
            Cookie jwtCookie = new Cookie(HEADER_NAME, jwtToken);
            jwtCookie.setMaxAge(60 * 3);
            response.addCookie(jwtCookie);
            return true;
        }
        return false;
    }

    public int selectCompanyIDByUserID(String userID) {
        return userMapper.selectCompanyIDByUserID(userID);
    }
}
