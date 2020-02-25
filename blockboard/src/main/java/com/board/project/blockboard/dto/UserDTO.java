/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserDTO.java
 */
package com.board.project.blockboard.dto;

import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {

  private String userId;
  private int companyId;
  private String companyName;
  private String userName;
  private String userPassword;
  private String userType;
  private String imageUrl;
  private String imageFileName;
  private String thumbnailUrl;
  private String thumbnailFileName;

  public UserDTO(String userId, int companyId, String userName,
      String userType) {
    this.userId = userId;
    this.companyId = companyId;
    this.userName = userName;
    this.userType = userType;
  }

  public UserDTO(HttpServletRequest request) {
    this.userId = request.getAttribute("userId").toString();
    this.userName = request.getAttribute("userName").toString();
    this.userType = request.getAttribute("userType").toString();
    this.companyId = Integer.parseInt(request.getAttribute("companyId").toString());
  }

  public UserDTO(String userId, int companyId) {
    this.userId = userId;
    this.companyId = companyId;
  }

  public UserDTO(String userId, String imageUrl, String imageFileName, String thumbnailUrl,
      String thumbnailFileName) {
    this.userId = userId;
    this.imageUrl = imageUrl;
    this.imageFileName = imageFileName;
    this.thumbnailUrl = thumbnailUrl;
    this.thumbnailFileName = thumbnailFileName;
  }
}
