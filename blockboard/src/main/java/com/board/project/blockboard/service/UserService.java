/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserService.java
 */
package com.board.project.blockboard.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.util.Common;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.mapper.FileMapper;
import com.board.project.blockboard.mapper.UserMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Service
public class UserService {

  @Autowired
  private FileMapper fileMapper;
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
  
  public List<UserDTO> selectUsersByCompanyID(int companyID) {
    return userMapper.selectUsersByCompanyID(companyID);
  }

  public UserDTO selectUserByUserIdAndCompanyId(String userID, int companyID) {
    UserDTO user = new UserDTO(userID, companyID);
    return userMapper.selectUserByUserIdAndCompanyID(user);
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
  /**
   * 유저 이미지 업로드 & 디비와 매핑
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public void updateUserImage(MultipartHttpServletRequest multipartRequest, String userID, HttpServletResponse response)
      throws IOException {
    String uuid = Common.getNewUUID();

    Iterator<String> itr = multipartRequest.getFileNames();
    String url = "";
    while (itr.hasNext()) {
      MultipartFile mpf = multipartRequest.getFile(itr.next());

      String originFileName = mpf.getOriginalFilename(); //파일명
      String storedFileName = uuid + "_" + originFileName;
      ObjectMetadata metadata= new ObjectMetadata();
      AWSService awsService = new AWSService();

      url = awsService.upload(storedFileName,mpf.getInputStream(),metadata,ConstantData.AWS_USER_DIR);
      log.info("url -->"+url);
      long fileSize = mpf.getSize();
      //파일 전체 경로
      log.info("fileName => " + mpf.getName());

      Map<String, Object> userData = new HashMap<String,Object>();
      userData.put("userID",userID);
      userData.put("imageUrl",url);
      userData.put("imageFileName",storedFileName);
      userMapper.updateUserImage(userData);
    }
  }
}
