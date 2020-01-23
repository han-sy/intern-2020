package com.board.project.blockboard.common.exception;

public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = -223803020312321L;

    public UnauthorizedException() {
        super("계정 권한이 유효하지 않습니다.\n다시 로그인을 해주세요.");
    }
}
