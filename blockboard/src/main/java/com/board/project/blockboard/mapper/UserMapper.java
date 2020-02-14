/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.UserDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

  List<UserDTO> selectUsersByCompanyID(int companyID);

  UserDTO selectUserByID(String userID);

  String selectUserNameByUserID(String userID);

  String selectUserTypeByUserID(String userID);
}
