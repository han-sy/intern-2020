/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file UserValidException.java
 */
package com.board.project.blockboard.common.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class UserValidException extends Exception {

  public UserValidException() {
    super();
  }

  public UserValidException(String msg) {
    super(msg);
  }

  public void sendError(HttpServletResponse response, String msg) {
    try {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, msg);
      this.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
