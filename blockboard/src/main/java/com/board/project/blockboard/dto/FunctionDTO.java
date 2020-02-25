/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionDTO.java
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
public class FunctionDTO {

  private int functionId;
  private boolean functionOn;
  private String functionName;
  private String functionData;
}
