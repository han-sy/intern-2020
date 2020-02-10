/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class FunctionDTO {

  private int functionID;
  private boolean functionOn;
  private String functionName;
  private String functionData;
}
