/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserDTO.java
 */
package com.board.project.blockboard.dto;

import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

  private String userID;
  private int companyID;
  private String companyName;
  private String userName;
  private String userPassword;
  private String userType;
  private String imageUrl;
  private String imageFileName;
  private String thumbnailUrl;
  private String thumbnailFileName;

  public UserDTO(String userID, int companyID, String userName,
      String userType) {
    this.userID = userID;
    this.companyID = companyID;
    this.userName = userName;
    this.userType = userType;
  }

  public UserDTO(HttpServletRequest request) {
    this.userID = request.getAttribute("userID").toString();
    this.userName = request.getAttribute("userName").toString();
    this.userType = request.getAttribute("userType").toString();
    this.companyID = Integer.parseInt(request.getAttribute("companyID").toString());
  }

  public UserDTO(String userID, int companyID) {
    this.userID = userID;
    this.companyID = companyID;
  }
}
