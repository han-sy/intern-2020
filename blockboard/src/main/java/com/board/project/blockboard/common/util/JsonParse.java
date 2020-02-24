/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file JsonParse.java
 */
package com.board.project.blockboard.common.util;


import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

@Slf4j
public class JsonParse {

  /**
   * 제네릭 리스트 만들기 string 형태의 json을 List<DTO> 로 변경
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
  public static <T> T jsonToDTO(String json, Class<T> tClass) {
    T validationDTO = null;
    ObjectMapper mapper = new ObjectMapper();
    try {
      validationDTO = mapper.readValue(json, tClass);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return validationDTO;
  }

  /**
   * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
   */
  public static Map<String, Object> convertJsonStringToMap(String json) {
    Map<String, Object> map = new HashMap<String,Object>();
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
      map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

  /**
   * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
   */
  public static JSONObject convertMapToJsonString(Map<String, Object> map) {
    JSONObject jsonObject = new JSONObject();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      jsonObject.put(key, value);
    }
    return jsonObject;
  }
}
