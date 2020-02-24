/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ViewRecordsDTO.java
 */
package com.board.project.blockboard.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ViewRecordDTO {
  @NonNull private int postId;
  @NonNull private String userId;
  private String userName;
  private String thumbnailUrl;
}
