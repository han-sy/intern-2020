/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file validateAuthority.java
 */
package com.board.project.blockboard.common.validation;


import com.board.project.blockboard.common.exception.UserValidException;
import com.board.project.blockboard.dto.UserDTO;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorityValidation {

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


}
