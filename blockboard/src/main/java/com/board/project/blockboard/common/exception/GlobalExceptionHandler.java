/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file GlobalExceptionHandler.java
 */
package com.board.project.blockboard.common.exception;

import com.board.project.blockboard.dto.FunctionDTO;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NullPointerException.class)
  @ResponseBody
  protected ResponseEntity<?> NullPointerError(HttpServletResponse response, Exception e) {
    return new ResponseEntity<>(e, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseBody
  protected ResponseEntity<?> IllegalArgumentError(HttpServletResponse response, Exception e) {
    return new ResponseEntity<>(e, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(InputValidException.class)
  @ResponseBody
  protected ResponseEntity<?> InputValidError(HttpServletResponse response, Exception e) {
    return new ResponseEntity<>(e, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserValidException.class)
  @ResponseBody
  protected ResponseEntity<?> userValidError(Exception e) {
    return new ResponseEntity<>(e, HttpStatus.UNAUTHORIZED);
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @ExceptionHandler(FunctionValidException.class)
  @ResponseBody
  protected ResponseEntity<?> FunctionValidError(Exception e) {
    return new ResponseEntity<>(e, HttpStatus.FORBIDDEN);
  }
  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @ExceptionHandler(FileValidException.class)
  @ResponseBody
  protected ResponseEntity<?> FileValidException(Exception e) {

    return new ResponseEntity<>(e, HttpStatus.CONFLICT);
  }
  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @ExceptionHandler(AuthorityValidException.class)
  @ResponseBody
  protected ResponseEntity<?> AuthorityValidException(Exception e) {

    return new ResponseEntity<>(e, HttpStatus.FORBIDDEN);
  }

}