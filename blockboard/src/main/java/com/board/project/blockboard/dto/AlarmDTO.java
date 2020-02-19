/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file AlarmDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class AlarmDTO {

  private int alarmID;
  private String taggedUserID;
  private int postID;
  private int commentID;
  private String boardName;
  private String alarmContent;
  private String userName;
  private String registerTime;
}
