/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file BoardValidation.java
 */
package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.common.exception.BoardValidException;
import com.board.project.blockboard.service.BoardService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardValidation {

  @Autowired
  private BoardService boardService;

  @SneakyThrows
  public void checkExistedBoard(int boardId) {
    boolean isValid = boardService.isExistBoard(boardId);
    if (!isValid) {
      throw new BoardValidException("이미 존재하지 않는 게시판입니다.");
    }
  }
}
