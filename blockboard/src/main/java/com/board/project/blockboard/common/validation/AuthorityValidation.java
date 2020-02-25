/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file validateAuthority.java
 */
package com.board.project.blockboard.common.validation;


import com.board.project.blockboard.common.exception.AuthorityValidException;
import com.board.project.blockboard.dto.FileDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.FileService;
import com.board.project.blockboard.service.FunctionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorityValidation {

  @Autowired
  private FileService fileService;

  @SneakyThrows
  public static boolean isValidateUserData(UserDTO frontEndUserData, UserDTO validatedUserData) {
    boolean isValid = frontEndUserData.equals(validatedUserData);
    if (!isValid) {
      throw new AuthorityValidException("로그인된 유저가 변경되었습니다. 로그인화면으로 돌아갑니다");
    }
    return true;

  }

  @SneakyThrows
  public static boolean isAdmin(UserDTO frontEndUserData) {
    boolean isValid = frontEndUserData.getUserType().equals("관리자");
    if (!isValid) {
      throw new AuthorityValidException("관리자권한에 접근하였습니다.");
    }
    return true;
  }

  @SneakyThrows
  public boolean isWriter(FileDTO fileData, UserDTO userData) {
    String writerUserId = fileService.getFileWriterUserId(fileData);
    boolean isValid = writerUserId.equals(userData.getUserId());
    if (!isValid) {
      throw new AuthorityValidException("다른 작성자의 작성물에 접근하였습니다. 로그인 화면으로 돌아갑니다.");
    }
    return true;
  }


}
