/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionDTO.java
 */
package com.board.project.blockboard.dto;

import java.util.List;
import lombok.Data;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;

@Alias("FunctionDTO")
@Data
public class FunctionDTO {

  private int functionID;
  private boolean functionOn;
  private String functionName;
  private String functionData;
}
