package com.board.project.blockboard.common.util;
/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file JsonParse.java
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class JsonParse {

  /**
   * string 형태의 json을 ArrayList<Map<String,String>> 으로 변경Z
   */
  public static ArrayList<Map<String, String>> stringToMapArrayList(String stringData) {
    Gson gson = new Gson();
    Type type = new TypeToken<ArrayList<Map<String, String>>>() {
    }.getType();
    ArrayList<Map<String, String>> resultListMap = gson.fromJson(stringData, type);
    return resultListMap;
  }
}
