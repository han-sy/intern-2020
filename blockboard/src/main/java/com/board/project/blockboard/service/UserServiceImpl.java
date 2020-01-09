package com.board.project.blockboard.service;

import com.board.project.blockboard.mapper.UserMapper;
import com.board.project.blockboard.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private UserMapper userMapper;

    @Autowired
    UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> allUser() {
        return userMapper.allUser();
    }

}
