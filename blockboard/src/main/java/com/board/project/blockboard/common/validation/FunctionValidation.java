/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionValidation.java
 */
package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.common.exception.FunctionValidException;
import com.board.project.blockboard.service.FunctionService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FunctionValidation {

  @Autowired
  FunctionService functionService;

  public boolean isFunctionOn(int companyID, int functionID, HttpServletResponse response) {
    boolean isValid = functionService.getFunctionStatus(companyID, functionID);
    try {
      if (!isValid) {
        throw new FunctionValidException("존재하지 않는 기능 권한에 접근하였습니다.");
      }
    } catch (FunctionValidException fve) {
      fve.sendError(response, "고객사에서 제공하지 않는 기능에 접근하였습니다. 로그인 화면으로 돌아갑니다.");
    } finally {
      return isValid;
    }
  }

  public boolean isFunctionOn(int companyID, int postFunctionID, int commentFunctionID,
      HttpServletResponse response) {
    boolean isPostFunctionValid = functionService.getFunctionStatus(companyID, postFunctionID);
    boolean isCommentFunctionValid = functionService.getFunctionStatus(companyID, commentFunctionID);
    boolean isValid = (isCommentFunctionValid || isPostFunctionValid);
    try {
      if (!(isValid)) {
        throw new FunctionValidException("존재하지 않는 기능 권한에 접근하였습니다.");
      }
    } catch (FunctionValidException fve) {
      fve.sendError(response, "고객사에서 제공하지 않는 기능에 접근하였습니다. 로그인 화면으로 돌아갑니다.");
    } finally {
      return isValid;
    }

  }
}
