/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Builder
public class FileDTO {
  private int fileId;
  private int postId;
  private int commentId;
  private String resourceUrl;
  private String originFileName;
  private String storedFileName;
  private long fileSize;
  private String uploadTime;
}
