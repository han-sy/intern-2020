package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.common.util.JsonParse;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.PostMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
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

  public boolean isTempSavedPost(int postid, HttpServletResponse response) {
    PostDTO post = postMapper.selectPostByPostID(postid);
    Gson gson = new Gson();
    PostDTO postStatus = JsonParse.jsonToDTO(gson.toJson(post.getPostStatus()), PostDTO.class);

    if (postStatus.getIsTemp()) {
      return true;
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
}
