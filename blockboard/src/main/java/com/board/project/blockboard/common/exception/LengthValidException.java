package com.board.project.blockboard.common.exception;

public class LengthValidException extends Exception {

  public static int ERROR_CODE;

  public LengthValidException() {
    super();
    ERROR_CODE = 400;
  }

  public LengthValidException(String msg) {
    this(msg, 400);
  }

  public LengthValidException(String msg, int errorCode) {
    super(msg);
    ERROR_CODE = errorCode;
  }

  public int getErrorCode() {
    return ERROR_CODE;
  }
}
