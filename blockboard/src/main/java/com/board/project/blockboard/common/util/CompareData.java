/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CompareDTO.java
 */
package com.board.project.blockboard.common.util;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.dto.FunctionDTO;



public class CompareData {

  /**
   * functionDTO
   * ON OFF 비교
   */
  public static int compareFunctionOnOff(FunctionDTO oldFunction, FunctionDTO newFunction){
    if(!oldFunction.isFunctionOn() && newFunction.isFunctionOn())
      return ConstantData.OFF_TO_ON;
    else if(oldFunction.isFunctionOn() && !newFunction.isFunctionOn())
      return ConstantData.ON_TO_OFF;
    else
      return ConstantData.NO_CHANGE;
  }

}
