/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentVaildation.java
 */
package com.board.project.blockboard.common.validation;


import com.board.project.blockboard.common.exception.BoardValidException;
import com.board.project.blockboard.common.exception.CommentValidException;
import com.board.project.blockboard.service.CommentService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentValidation {

  @Autowired
  private CommentService commentService;
  @SneakyThrows
  public void checkExistedComment(int commentId) {
    boolean isValid = commentService.isExistComment(commentId);
    if (!isValid) {
      throw new CommentValidException("이미 존재하지 않는 댓글입니다.");
    }
  }

}
