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
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
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
    UserDTO login_user = userMapper.selectUserByID(requestUser.getUserId());
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

  public String getUserNameByUserId(String userId) {
    return userMapper.selectUserNameByUserId(userId);
  }

  public UserDTO insertUser(HttpServletRequest request, UserDTO user) {
    user.setCompanyId(Integer.parseInt(request.getAttribute("companyId").toString()));
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
    if (korean.matcher(user.getUserId()).find()) {
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
    return userMapper.selectUserByUserIdAndCompanyId(user) != null;
  }

  public List<UserDTO> selectUsersByCompanyId(int companyId) {
    return userMapper.selectUsersByCompanyId(companyId);
  }

  public UserDTO selectUserByUserIdAndCompanyId(String userId, int companyId) {
    UserDTO user = new UserDTO(userId, companyId);
    return userMapper.selectUserByUserIdAndCompanyId(user);
  }

  public int countUsersByCompanyId(HttpServletRequest request) {
    int companyId = Integer.parseInt(request.getAttribute("companyId").toString());
    return userMapper.countUsersByCompanyId(companyId);
  }

  /**
   * @param userId
   * @return
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public String getUserTypeByUserId(String userId) {
    String type = userMapper.selectUserTypeByUserId(userId);
    return type;
  }

  /**
   * 유저 이미지 업로드 & 디비와 매핑
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public void updateUserImage(MultipartHttpServletRequest multipartRequest, String userId) {
    Iterator<String> itr = multipartRequest.getFileNames();
    String url = "";
    String thumbnailUrl = "";
    while (itr.hasNext()) {
      MultipartFile mpf = multipartRequest.getFile(itr.next());

      String originFileName = mpf.getOriginalFilename(); //파일명
      String storedFileName = userId + originFileName.substring(originFileName.indexOf("."));
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

      UserDTO userData = new UserDTO(userId, url, storedFileName, thumbnailUrl, storedFileName);
      userMapper.updateUserImage(userData);
    }
  }
}
