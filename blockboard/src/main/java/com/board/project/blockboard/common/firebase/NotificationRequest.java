package com.board.project.blockboard.common.firebase;

import lombok.Data;

/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file NotificationRequest.java
 */
@Data
public class NotificationRequest {
  private String token;
  private String title;
  private String message;
}
