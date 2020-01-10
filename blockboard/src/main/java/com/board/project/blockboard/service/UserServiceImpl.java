package com.board.project.blockboard.service;

import com.board.project.blockboard.mapper.UserMapper;
import com.board.project.blockboard.dto.UserDTO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private UserMapper userMapper;

    @Autowired
    UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> allUser() {
        return userMapper.allUser();
    }

    @Override
    public boolean loginCheck(HttpServletRequest request, HttpSession session) {
        // 로그인 체크 기능 구현예정
        UserDTO login_user;

        // id가 있으면 로그인(임시, 원래 password까지 검증)
        if(userMapper.selectUser(request.getParameter("user_id")) != null) {
            return true;
        } else return false;
    }

}
