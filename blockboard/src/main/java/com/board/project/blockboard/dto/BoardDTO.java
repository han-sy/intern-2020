/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file BoardDTO.java
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
public class BoardDTO {

  private int boardId;
  private int companyId;
  private String boardName;
}
