/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserService.java
 */
package com.board.project.blockboard.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.board.project.blockboard.common.constant.ConstantData.Bucket;
import com.board.project.blockboard.common.util.Common;
import com.board.project.blockboard.common.util.Thumbnail;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.mapper.UserMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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
  private UserMapper userMapper;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private AmazonS3Service amazonS3Service;

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
    validateUser(user);
    userMapper.insertUser(user);
    return user;
  }

  public void validateUser(UserDTO user) {
    if (isDuplicateUser(user)) {
      throw new IllegalArgumentException("중복된 유저입니다.");
    }
    Pattern korean = Pattern.compile(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    if (korean.matcher(user.getUserID()).find()) {
      throw new IllegalArgumentException("ID에 한글이 포함되어 있습니다.");
    }
    Pattern specialChar = Pattern.compile("[ !@#$%^&*(),.?\\\":{}|<>]");
    if (!specialChar.matcher(user.getUserPassword()).find()) {
      throw new IllegalArgumentException("비밀번호의 최소 1개 특수문자가 포함되어야 합니다.");
    }
    if (user.getUserPassword().length() < 8 || user.getUserPassword().length() > 20) {
      throw new IllegalArgumentException(("비밀번호는 8~20자 사이여야 합니다."));
    }
  }

  public boolean isDuplicateUser(UserDTO user) {
    return userMapper.selectUserByUserIdAndCompanyID(user) != null;
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
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public void updateUserImage(MultipartHttpServletRequest multipartRequest, String userID,
      HttpServletResponse response, HttpServletRequest request)
      throws IOException {
    String uuid = Common.getNewUUID();

    Iterator<String> itr = multipartRequest.getFileNames();
    String url = "";
    String thumbnailUrl = "";
    while (itr.hasNext()) {
      MultipartFile mpf = multipartRequest.getFile(itr.next());

      String originFileName = mpf.getOriginalFilename(); //파일명
      String storedFileName = userID + originFileName.substring(originFileName.indexOf("."));
      //String storedFileName = uuid + "_" + originFileName;
      ObjectMetadata metadata = new ObjectMetadata();
      String fileExt = Common.getFileExt(storedFileName);

      try {
        url = amazonS3Service
            .upload(storedFileName, Bucket.USER, mpf.getInputStream(), metadata);
        InputStream thumbnailInputStream = Thumbnail.makeThumbnail(mpf, storedFileName, fileExt);
        thumbnailUrl = amazonS3Service
            .upload(storedFileName, Bucket.USER_THUMBNAIL, thumbnailInputStream,
                metadata);
        Thumbnail.deleteSubFile(storedFileName);
      } catch (Exception e) {
        e.printStackTrace();
      }

      log.info("url -->" + url);
      log.info("thumbnailUrl -->" + thumbnailUrl);
      //파일 전체 경로
      //log.info("fileName => " + mpf.getName());

      Map<String, Object> userData = new HashMap<String, Object>();
      userData.put("userID", userID);
      userData.put("imageUrl", url);
      userData.put("imageFileName", storedFileName);
      userData.put("thumbnailUrl", thumbnailUrl);
      userData.put("thumbnailFileName", storedFileName);
      userMapper.updateUserImage(userData);
    }
  }
}
