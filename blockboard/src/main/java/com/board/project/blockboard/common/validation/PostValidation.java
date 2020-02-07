/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostValidation.java
 */
package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.common.util.JsonParse;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.PostMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostValidation {

  @Autowired
  private PostMapper postMapper;

  public enum searchOptions {title, writer, content, titleAndContent}

  public boolean isTempSavedPost(PostDTO post, HttpServletResponse response) {
    PostDTO setStatusPost = JsonParse.setPostStatusFromJsonString(post);
    if (setStatusPost.getIsTemp()) {
      if (!setStatusPost.getIsTrash()) {
        return true;
      }
    }
    try {
      response.sendError(HttpServletResponse.SC_CONFLICT, "이미 저장된 게시물입니다.");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean isExistPost(int postID, HttpServletResponse response) {
    PostDTO post = postMapper.selectPostByPostID(postID);
    if (post != null) {
      return true;
    }
    try {
      response.sendError(HttpServletResponse.SC_CONFLICT, "요청한 게시물을 찾을 수 없습니다.");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean isExistPost(PostDTO post, HttpServletResponse response) {
    if (post != null) {
      return true;
    }
    try {
      response.sendError(HttpServletResponse.SC_CONFLICT, "요청한 게시물을 찾을 수 없습니다.");
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

  public boolean isValidDelete(int boardID, PostDTO post, HttpServletResponse response) {
    try {
      if (post == null) {
        response.sendError(HttpServletResponse.SC_CONFLICT, "존재하지 않은 게시물입니다.");
        return false;
      }
      if (post.getBoardID() != boardID) {
        response.sendError(HttpServletResponse.SC_CONFLICT, "현재 게시판에 없는 글입니다.");
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public boolean isInTrashBox(PostDTO post, HttpServletResponse response) {
    try {
      JsonParse.setPostStatusFromJsonString(post);
      if (!post.getIsTrash()) {
        response.sendError(HttpServletResponse.SC_CONFLICT, "휴지통에 존재하지 않습니다.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }
}
