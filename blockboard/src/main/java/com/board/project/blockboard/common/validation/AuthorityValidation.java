/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file validateAuthority.java
 */
package com.board.project.blockboard.common.validation;


import com.board.project.blockboard.common.exception.UserValidException;
import com.board.project.blockboard.dto.FileDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.FileService;
import com.board.project.blockboard.service.FunctionService;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorityValidation {

  @Autowired
  private FunctionService functionService;
  @Autowired
  private FileService fileService;

  public static boolean isValidateUserData(UserDTO frontEndUserData, UserDTO validatedUserData,
      HttpServletResponse response) {
    boolean isValid = frontEndUserData.equals(validatedUserData);
    try {
      if (!isValid) {
        throw new UserValidException("로그인된 유저가 변경되었습니다.");
      }
    } catch (UserValidException uve) {
      uve.sendError(response, "로그인된 유저가 변경되었습니다. 로그인화면으로 돌아갑니다");
    } finally {
      return isValid;
    }

  }

  public static boolean isAdmin(UserDTO frontEndUserData, HttpServletResponse response) {
    boolean isValid = frontEndUserData.getUserType().equals("관리자");
    try {
      if (!isValid) {
        throw new UserValidException("관리자권한에 접근하였습니다.");
      }
    } catch (UserValidException uve) {
      uve.sendError(response, "관리자권한에 접근하였습니다. 로그인화면으로 돌아갑니다.");
    } finally {
      return isValid;
    }
  }

  public boolean isWriter(FileDTO fileData,UserDTO userData,HttpServletResponse response){
    String writerUserId = fileService.getFileWriterUserId(fileData);
    boolean isValid = writerUserId.equals(userData.getUserId());
    try {
      if(!isValid){
        throw new UserValidException();
      }
    } catch (UserValidException e) {
      e.sendError(response,"다른 작성자의 작성물에 접근하였습니다. 로그인 화면으로 돌아갑니다.");
      e.printStackTrace();
    }finally {
      return isValid;
    }

  }






}
