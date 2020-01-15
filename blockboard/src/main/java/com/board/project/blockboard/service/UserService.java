package com.board.project.blockboard.service;

import com.board.project.blockboard.mapper.UserMapper;
import com.board.project.blockboard.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserService {
    private UserMapper userMapper;

    @Autowired
    UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    Logger logger = LoggerFactory.getLogger(getClass());

    public boolean loginCheck(HttpServletRequest request, HttpSession session) {
        // 로그인 요청 사용자의 정보를 가져온다.
        UserDTO login_user = userMapper.getUser(request.getParameter("user_id"));

        // id가 있으면 로그인(추후에 password까지 검증)
        if(login_user != null) {
            if(login_user.getUser_pwd().equals(request.getParameter("user_pwd"))) {
                return true;
            }
        }
        return false;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

}
