/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file JsonParse.java
 */
package com.board.project.blockboard.common.util;


import com.board.project.blockboard.dto.FunctionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonParse {

  /**
   * 제네릭 리스트 만들기
   * string 형태의 json을 List<DTO> 로 변경
   */
  public static <T> List<T> jsonToDTOList(String json, Class<FunctionDTO[]> tClass) {
    List<T> objectDTOList = null;
    ObjectMapper mapper = new ObjectMapper();
    try {
      objectDTOList = (List<T>) Arrays.asList(mapper.readValue(json, tClass));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return objectDTOList;
  }

  /**
   * string 형태의 json을 DTO 로 변경
   */
  public static <T> T jsonToDTO(String json,Class<T> tClass) {
    T validationDTO = null;
    ObjectMapper mapper = new ObjectMapper();
    try {
      validationDTO = mapper.readValue(json, tClass);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return validationDTO;
  }


}
