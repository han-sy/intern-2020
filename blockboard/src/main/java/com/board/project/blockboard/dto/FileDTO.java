package com.board.project.blockboard.dto;

import lombok.Data;

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileDTO.java
 */
@Data
public class FileDTO {
  private int fileID;
  private int postID;
  private String fileName;
  private String fileOriginalName;
  private String fileUrl;
}
