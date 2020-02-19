/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class CommentDTO {

  private int commentID;
  private int postID;
  private int companyID;
  private String userID;
  private String userName;
  private String commentContent;
  private String commentContentExceptHTMLTag;
  private String commentRegisterTime;
  private int commentReferencedID;
}
