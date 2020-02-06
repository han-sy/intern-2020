/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias("PostDTO")
@Data
@NoArgsConstructor
public class PostDTO {

  private int postID;
  private String userID;
  private String userName;
  private int boardID;
  private String boardName;
  private int companyID;
  private String postTitle;
  private String postContent;
  private String postContentExceptHTMLTag;
  private String postRegisterTime;
  private String postLastUpdateTime;
  private Object postStatus;
  // postStatus 안의 Key List
  private Boolean isTemp;
  private Boolean isTrash;
  private int originalBoardID;
}
