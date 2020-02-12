/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

  UserDTO selectUserByID(String userID);

  int selectCompanyIDByUserID(String userID);

  String selectUserNameByUserID(String userID);

  String selectUserTypeByUserID(String userID);
}
