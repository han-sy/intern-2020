/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.UserDTO;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

  UserDTO selectUserByID(String userID);

  void insertUser(UserDTO user);

  String selectUserNameByUserID(String userID);

  String selectUserTypeByUserID(String userID);

  void updateUserImage(Map<String, Object> userData);
}
