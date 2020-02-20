/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ViewRecordsDTO.java
 */
package com.board.project.blockboard.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewRecordDTO {
  private int postID;
  private String userID;
}
