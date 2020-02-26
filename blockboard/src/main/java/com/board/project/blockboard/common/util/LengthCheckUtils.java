/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file LengthCheckUtils.java
 */
package com.board.project.blockboard.common.util;

import com.board.project.blockboard.common.exception.InputValidException;
import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.dto.PostDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LengthCheckUtils {

  static final int BOARD_NAME_LIMIT_LENGTH = 150;   //게시판 이름 글자수 제한
  static final int POST_TITLE_LIMIT_LENGTH = 150;   // 게시글 제목 글자수 제한
  static final int POST_CONTENT_LIMIT_LENGTH = 4000; // 게시글 내용 글자수 제한
  static final int COMMENT_LIMIT_LENGTH = 4000;      // 댓글 내용 글자수 제한

  @SneakyThrows
  public static void validateBoardName(BoardDTO board){
    if(board.getBoardName().getBytes().length>BOARD_NAME_LIMIT_LENGTH){
      throw new InputValidException("게시판 이름 글자수를 초과하였습니다.");
    }
  }
  @SneakyThrows
  public static void validatePostData(PostDTO post) {
    if (post.getPostTitle().getBytes().length > POST_TITLE_LIMIT_LENGTH) {
      throw new InputValidException("게시글 제목 글자수를 초과하였습니다.");
    }
    if (post.getPostContent().getBytes().length > POST_CONTENT_LIMIT_LENGTH) {
      throw new InputValidException("게시글 내용 글자수를 초과하였습니다.");
    }
  }

  @SneakyThrows
  public static void validCommentData(CommentDTO comment) {
    if (comment.getCommentContent().getBytes().length > COMMENT_LIMIT_LENGTH) {
      throw new InputValidException("댓글 글자수를 초과하였습니다.");
    }
  }
}
