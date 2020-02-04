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

  public UserDTO selectUserByID(String userID);

  public int selectCompanyIDByUserID(String userID);

  public String selectUserNameByUserID(String userID);

  String selectUserTypeByUserID(String userID);
}
