package com.board.project.blockboard.service;

import com.board.project.blockboard.mapper.UserMapper;
import com.board.project.blockboard.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public boolean loginCheck(UserDTO requestUser) {
        // 로그인 요청 사용자의 정보를 가져온다.
        UserDTO login_user = userMapper.getUserByID(requestUser.getUserID());

        // id에 해당하는 유저가 있으면 패스워드 검사
        if(login_user != null) {
            if(login_user.getUserPassword().equals(requestUser.getUserPassword())) {
                return true;
            }
        }
        return false;
    }

    public int selectCompanyIDByUserID(String userID) {
        return userMapper.selectCompanyIDByUserID(userID);
    }
}
