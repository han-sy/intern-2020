/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.mapper.UserMapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

  @Autowired
  private UserMapper userMapper;
  @Autowired
  private JwtService jwtService;
  private final String HEADER_NAME = "Authorization";

  public boolean loginCheck(UserDTO requestUser, HttpServletResponse response) {
    // 로그인 요청 사용자의 정보를 가져온다.
    UserDTO login_user = userMapper.selectUserByID(requestUser.getUserID());
    String login_userPassword = login_user.getUserPassword();
    String requestPassword = requestUser.getUserPassword();
    String jwtToken = "";

    // 패스워드 검사
    if (StringUtils.equals(login_userPassword, requestPassword)) {
      login_user.setUserPassword(null); // 비밀번호는 JWT 토큰에 담지 않는다.
      jwtToken = jwtService.create(HEADER_NAME, login_user, "user_info");
      Cookie jwtCookie = new Cookie(HEADER_NAME, jwtToken);
      jwtCookie.setHttpOnly(true);
      response.addCookie(jwtCookie);
      return true;
    }
    return false;
  }

  public int selectCompanyIDByUserID(String userID) {

    return userMapper.selectCompanyIDByUserID(userID);
  }

  public String getUserNameByUserID(String userID) {

    return userMapper.selectUserNameByUserID(userID);
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public String getUserTypeByUserID(String userID) {
    String type = userMapper.selectUserTypeByUserID(userID);
    return type;
  }

  public UserDTO getUserInfo(String userID) {
    UserDTO result = userMapper.selectUserByID(userID);
    result.setUserPassword(null);
    return result;
  }
}
