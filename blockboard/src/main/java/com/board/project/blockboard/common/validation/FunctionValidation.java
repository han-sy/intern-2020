/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionValidation.java
 */
package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.common.exception.FunctionValidException;
import com.board.project.blockboard.service.FunctionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class FunctionValidation {

  @Autowired
  FunctionService functionService;

  @SneakyThrows
  public boolean isFunctionOn(int companyId, int functionId) {
    boolean isValid = functionService.isUseFunction(companyId, functionId);
    if (!isValid) {
      throw new FunctionValidException("고객사에서 제공하지 않는 기능에 접근하였습니다. 로그인 화면으로 돌아갑니다.");
    }
    return true;
  }

  @SneakyThrows
  public boolean isFunctionOn(int companyId, int postFunctionId, int commentFunctionId) {
    boolean isPostFunctionValid = functionService.isUseFunction(companyId, postFunctionId);
    boolean isCommentFunctionValid = functionService.isUseFunction(companyId, commentFunctionId);
    boolean isValid = (isCommentFunctionValid || isPostFunctionValid);
    if (!(isValid)) {
      throw new FunctionValidException("고객사에서 제공하지 않는 기능에 접근하였습니다. 로그인 화면으로 돌아갑니다.");
    }
    return true;

  }
}
