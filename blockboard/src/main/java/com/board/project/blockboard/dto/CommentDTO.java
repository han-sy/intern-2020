/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentDTO {

  private int commentId;
  private int postId;
  private int companyId;
  private String userId;
  private String userName;
  private String thumbnailUrl;
  private String commentContent;
  private String commentContentExceptHTMLTag;
  private String commentRegisterTime;
  private int commentReferencedId;
  private int repliesCount;
}
