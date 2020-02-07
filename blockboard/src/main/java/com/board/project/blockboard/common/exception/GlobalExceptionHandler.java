package com.board.project.blockboard.common.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NullPointerException.class)
  protected void NullPointerError(HttpServletResponse response, Exception ee) {
    try {
      response.sendError(HttpServletResponse.SC_CONFLICT, "NULL Exception");
      ee.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected void IllegalArgumentError(HttpServletResponse response) {
    try {
      response.sendError(HttpServletResponse.SC_CONFLICT, "유효하지 않은 변수를 요청하였습니다.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
