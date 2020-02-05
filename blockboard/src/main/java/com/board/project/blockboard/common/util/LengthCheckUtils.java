package com.board.project.blockboard.common.util;

import com.board.project.blockboard.common.exception.LengthValidException;
import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.dto.PostDTO;

public class LengthCheckUtils {

  static final int POST_TITLE_LIMIT_LENGTH = 150;   // 게시글 제목 글자수 제한
  static final int POST_CONTENT_LIMIT_LENGTH = 4000; // 게시글 내용 글자수 제한
  static final int COMMENT_LIMIT_LENGTH = 4000;      // 댓글 내용 글자수 제한

  public static Boolean isValid(PostDTO post) {
    try {
      if (post.getPostTitle().getBytes().length > POST_TITLE_LIMIT_LENGTH) {
        throw new LengthValidException("게시글 제목 글자수를 초과하였습니다.");
      }
      if (post.getPostContent().getBytes().length > POST_CONTENT_LIMIT_LENGTH) {
        throw new LengthValidException("게시글 내용 글자수를 초과하였습니다.");
      }
    } catch (LengthValidException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static Boolean isValid(CommentDTO comment) {
    try {
      if (comment.getCommentContent().getBytes().length > COMMENT_LIMIT_LENGTH) {
        throw new LengthValidException("댓글 글자수를 초과하였습니다.");
      }
    } catch (LengthValidException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
