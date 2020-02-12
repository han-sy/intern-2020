/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file LengthCheckUtils.java
 */
package com.board.project.blockboard.common.util;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.dto.PostDTO;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LengthCheckUtils {

  static final int POST_TITLE_LIMIT_LENGTH = 150;   // 게시글 제목 글자수 제한
  static final int POST_CONTENT_LIMIT_LENGTH = 4000; // 게시글 내용 글자수 제한
  static final int COMMENT_LIMIT_LENGTH = 4000;      // 댓글 내용 글자수 제한

  public static Boolean isValid(PostDTO post, HttpServletResponse response) {
    try {
      if (post.getPostTitle().getBytes().length > POST_TITLE_LIMIT_LENGTH) {
        log.info("게시글 제목 글자수를 초과하였습니다.");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return false;
      }
      if (post.getPostContent().getBytes().length > POST_CONTENT_LIMIT_LENGTH) {
        log.info("게시글 내용 글자수를 초과하였습니다.");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public static Boolean isValid(CommentDTO comment, HttpServletResponse response) {
    try {
      if (comment.getCommentContent().getBytes().length > COMMENT_LIMIT_LENGTH) {
        log.info("댓글 글자수를 초과하였습니다.");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }
}
