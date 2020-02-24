/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file GlobalExceptionHandler.java
 */
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
  protected void NullPointerError(HttpServletResponse response, Exception e) {
    try {
      response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
      e.printStackTrace();
    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected void IllegalArgumentError(HttpServletResponse response, Exception e) {
    try {
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }

  @ExceptionHandler(InputValidException.class)
  protected void InputValidError(HttpServletResponse response, Exception e) {
    try {
      response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
      e.printStackTrace();
    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }

  @ExceptionHandler(UserValidException.class)
  protected void userValidError(HttpServletResponse response, Exception e) {
    try {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
      e.printStackTrace();
    } catch (IOException ie) {
      ie.printStackTrace();
    }
  }
}
