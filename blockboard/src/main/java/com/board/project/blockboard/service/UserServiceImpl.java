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
        // 로그인 요청 사용자의 정보를 가져온다.

        UserDTO login_user = userMapper.selectUser(request.getParameter("userID"));
        Logger logger = LoggerFactory.getLogger(getClass());

        // id가 있으면 로그인(추후에 password까지 검증)
        if(login_user != null) {
            logger.info(request.getParameter("userPassword"));
            if(login_user.getUserPassword().equals(request.getParameter("userPassword"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @Override
    public boolean findSession(String hostname){
        return false;
    }

}
