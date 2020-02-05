/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("FunctionDTO")
@Data
public class FunctionDTO {

  int functionID;
  boolean functionOn;
  String functionName;
  String functionData;
}
