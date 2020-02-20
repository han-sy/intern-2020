/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ViewRecordsDTO.java
 */
package com.board.project.blockboard.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ViewRecordDTO {
  @NonNull private int postID;
  @NonNull private String userID;
  private String userName;
  private String thumbnailUrl;
}
