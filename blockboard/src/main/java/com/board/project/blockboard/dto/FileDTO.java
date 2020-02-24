package com.board.project.blockboard.dto;

import lombok.Data;

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileDTO.java
 */
@Data
public class FileDTO {
  private int fileID;
  private int postId;
  private int commentId;
  private String resourceUrl;
  private String originFileName;
  private String storedFileName;
  private long fileSize;
  private String uploadTime;
}
