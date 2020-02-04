/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UnauthorizedException.java
 */
package com.board.project.blockboard.common.exception;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException() {
    super("계정 권한이 유효하지 않습니다.\n다시 로그인을 해주세요.");
  }
}
