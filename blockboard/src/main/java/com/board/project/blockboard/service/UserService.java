/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.mapper.UserMapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    UserDTO login_user = userMapper.selectUserByID(requestUser.getUserID());
    String login_userPassword = login_user.getUserPassword();
    String requestPassword = requestUser.getUserPassword();
    String jwtToken = "";

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

  public String getUserNameByUserID(String userID) {
    return userMapper.selectUserNameByUserID(userID);
  }

  public UserDTO insertUser(HttpServletRequest request, UserDTO user) {
    user.setCompanyID(Integer.parseInt(request.getAttribute("companyID").toString()));
    user.setUserType("일반");
    userMapper.insertUser(user);
    return user;
  }
  /**
   * @param userID
   * @return
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public String getUserTypeByUserID(String userID) {
    String type = userMapper.selectUserTypeByUserID(userID);
    return type;
  }
}
