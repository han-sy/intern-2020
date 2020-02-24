/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file AlarmDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class AlarmDTO {

  private int alarmID;
  private String taggedUserId;
  private int postId;
  private int commentId;
  private Boolean isRead;
  private String boardName;
  private String alarmContent;
  private String userName;
  private String registerTime;
}
