/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.UserDTO;
import java.util.Map;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

  List<UserDTO> selectUsersByCompanyId(int companyId);

  void insertUser(UserDTO user);

  UserDTO selectUserByID(String userId);

  UserDTO selectUserByUserIdAndCompanyId(UserDTO user);

  String selectUserNameByUserId(String userId);

  String selectUserTypeByUserId(String userId);

  void updateUserImage(UserDTO userData);
}
