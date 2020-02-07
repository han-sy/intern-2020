/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserDTO.java
 */
package com.board.project.blockboard.dto;

import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import org.apache.catalina.User;
import org.apache.ibatis.type.Alias;

@Alias("UserDTO")
@Data
public class UserDTO {

  private String userID;
  private int companyID;
  private String userName;
  private String userPassword;
  private String userType;

  public UserDTO(HttpServletRequest request) {
    this.userID = request.getAttribute("userID").toString();
    this.userName = request.getAttribute("userName").toString();
    this.userType = request.getAttribute("userType").toString();
    this.companyID = Integer.parseInt(request.getAttribute("companyID").toString());
  }
}
