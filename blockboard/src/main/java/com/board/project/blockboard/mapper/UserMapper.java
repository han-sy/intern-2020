package com.board.project.blockboard.mapper;

import com.board.project.blockboard.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    List<User> allUser();
}
