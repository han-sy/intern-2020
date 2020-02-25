/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ViewRecordsDTO.java
 */
package com.board.project.blockboard.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ViewRecordDTO {

  private int postId;
  private String userId;
  private String userName;
  private String thumbnailUrl;
}
