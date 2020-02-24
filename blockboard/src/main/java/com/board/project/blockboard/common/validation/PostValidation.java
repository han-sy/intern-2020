/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostValidation.java
 */
package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.common.exception.UserValidException;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.dto.UserDTO;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostValidation {

  public enum searchOptions {title, writer, content, titleAndContent}

  public static void validateTempPost(PostDTO post) {
    isExistPost(post);
    if (!StringUtils.equals(post.getPostStatus(), "temp")) {
      throw new NullPointerException("임시 저장 게시글이 아닙니다.");
    }
  }

  @SneakyThrows
  public static void validateDelete(PostDTO post, UserDTO user) {
    isExistPost(post);
    if(!StringUtils.equals(post.getUserId(), user.getUserId())) {
      throw new UserValidException("게시물 변경 권한이 없습니다.");
    }
  }
  public static void isExistPost(PostDTO post) {
    if (post == null) {
      throw new NullPointerException("요청한 게시물을 찾을 수 없습니다.");
    }
  }

  public static void isValidSearch(String option, String keyword) {
    searchOptions.valueOf(option);
    if (StringUtils.equals(keyword.trim(), "")) {
      throw new NullPointerException("검색 값을 입력해주세요.");
    }
  }

  @SneakyThrows
  public static void isValidChange(PostDTO post, UserDTO user) {
    isExistPost(post);
    if (!StringUtils.equals(post.getUserId(), user.getUserId())) {
      throw new UserValidException("게시물 변경 권한이 없습니다.");
    }
  }

  public static void isValidRestore(PostDTO post, UserDTO user) {
    isValidChange(post ,user);
    if (!StringUtils.equals(post.getPostStatus(), "recycle")) {
      throw new NullPointerException("휴지통에 존재하지 않습니다.");
    }
  }
}
