/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class PostDTO {

  private int postId;
  private String userId;
  private String userName;
  private int boardId;
  private String boardName;
  private int companyId;
  private String postTitle;
  private String postContent;
  private String postContentUnescapeHtml;
  private String postRegisterTime;
  private String postLastUpdateTime;
  private String postStatus;
  private String thumbnailUrl;
  // postStatus 안의 Key List
  private Boolean isPopular;
  private int commentsCount;
  private int viewCount;
}
