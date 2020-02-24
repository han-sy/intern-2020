/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionValidException.java
 */
package com.board.project.blockboard.common.exception;


import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class FunctionValidException extends Exception {

  public FunctionValidException(String msg) {
    super(msg);
  }
}
