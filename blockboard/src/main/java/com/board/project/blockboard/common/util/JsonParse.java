/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file JsonParse.java
 */
package com.board.project.blockboard.common.util;


import com.board.project.blockboard.dto.FunctionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonParse {

  /**
   * string 형태의 json을 List<FunctionDTO> 로 변경
   */
  public static List<FunctionDTO> jsonToFunctionDTOList(String json) {
    ObjectMapper mapper = new ObjectMapper();
    List<FunctionDTO> functionDTOList =null;
    try{
      functionDTOList = Arrays.asList(mapper.readValue(json, FunctionDTO[].class));
    }catch (IOException e){
      e.printStackTrace();
    }
    return functionDTOList;
  }


}
