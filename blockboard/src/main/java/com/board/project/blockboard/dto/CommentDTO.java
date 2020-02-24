/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentDTO {

  private int commentID;
  private int postID;
  private int companyID;
  private String userID;
  private String userName;
  private String thumbnailUrl;
  private String commentContent;
  private String commentContentExceptHTMLTag;
  private String commentRegisterTime;
  private int commentReferencedID;
  private int repliesCount;
}
