/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostValidation.java
 */
package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.dto.UserDTO;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostValidation {

  public enum searchOptions {title, writer, content, titleAndContent}

  public boolean isTempSavedPost(PostDTO post, HttpServletResponse response) {
    try {
      if (StringUtils.equals(post.getPostStatus(), "temp")) {
        return true;
      }
      response.sendError(HttpServletResponse.SC_CONFLICT, "이미 저장된 게시물입니다.");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean isExistPost(PostDTO post, int boardID, HttpServletResponse response) {
    try {
      if (post == null) {
        response.sendError(HttpServletResponse.SC_CONFLICT, "요청한 게시물을 찾을 수 없습니다.");
        return false;
      }
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean isExistPost(PostDTO post, HttpServletResponse response) {
    try {
      if (post == null) {
        response.sendError(HttpServletResponse.SC_CONFLICT, "요청한 게시물을 찾을 수 없습니다.");
        return false;
      }
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean isValidSearch(String option, String keyword, HttpServletResponse response) {
    searchOptions searchOption = searchOptions.valueOf(option);
    if (StringUtils.equals(keyword.trim(), "")) {
      try {
        response.sendError(HttpServletResponse.SC_CONFLICT, "검색 값을 입력해주세요.");
      } catch (IOException e) {
        e.printStackTrace();
      }
      return false;
    }
    return true;
  }

  public boolean isValidChange(PostDTO post, UserDTO user,
      HttpServletResponse response) {
    try {
      if (!StringUtils.equals(post.getUserID(), user.getUserID())) {
        response.sendError(HttpServletResponse.SC_CONFLICT, "게시물 변경 권한이 없습니다.");
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public boolean isValidRestore(PostDTO post, HttpServletResponse response) {
    try {
      if (!StringUtils.equals(post.getPostStatus(), "recycle")) {
        response.sendError(HttpServletResponse.SC_CONFLICT, "휴지통에 존재하지 않습니다.");
      }
    } catch (
        IOException e) {
      e.printStackTrace();
    }
    return true;
  }
}
