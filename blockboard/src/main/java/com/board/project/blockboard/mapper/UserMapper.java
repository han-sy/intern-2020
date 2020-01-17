package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    public UserDTO getUserByID(String userID);
    public int getUserIDByPostID(int postID);
}
