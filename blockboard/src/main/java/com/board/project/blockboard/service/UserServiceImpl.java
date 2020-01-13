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
        // 로그인 요청 사용자의 정보를 가져온다.
        UserDTO login_user = userMapper.selectUser(request.getParameter("user_id"));

        // id가 있으면 로그인(추후에 password까지 검증)
        if(login_user != null) {
            if(login_user.getUser_pwd() != request.getParameter("user_pwd")) {
                session.setAttribute("USER", login_user.getUser_id());
                session.setAttribute("COMPANY", login_user.getCom_id());
                return true;
            }
        }
        return false;
    }

}
