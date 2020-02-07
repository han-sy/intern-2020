/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserDTO.java
 */
package com.board.project.blockboard.dto;

import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.ibatis.type.Alias;

@Slf4j
@Getter
@Setter
public class UserDTO {

  private String userID;
  private int companyID;
  private String userName;
  private String userPassword;
  private String userType;

  public UserDTO(){
    log.info("0000UserDTO");
  }

  public UserDTO(String userID, int companyID, String userName,
      String userType) {
    log.info("1111UserDTO");
    this.userID = userID;
    this.companyID = companyID;
    this.userName = userName;
    this.userType = userType;
  }

  public UserDTO(HttpServletRequest request) {
    log.info("2222UserDTO");
    this.userID = request.getAttribute("userID").toString();
    this.userName = request.getAttribute("userName").toString();
    this.userType = request.getAttribute("userType").toString();
    this.companyID = Integer.parseInt(request.getAttribute("companyID").toString());
  }
}
