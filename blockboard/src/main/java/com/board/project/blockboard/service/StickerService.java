package com.board.project.blockboard.service;

import com.board.project.blockboard.common.util.JsonParse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file StickerService.java
 */

@Slf4j
@Service
public class StickerService {

  private final String STICKER_PATH = "/home1/irteam/storage/sticker";

  private final Map<String, Object> cache_stickerList = new HashMap<>();  // 모든 스티커 Json

  private final ArrayList<String> cache_groupNames = new ArrayList<>(); // 모든 그룹 이름

  public JSONObject getStickers(HttpServletRequest request) {
    if (cache_stickerList.isEmpty() || this.isModify()) {
      JSONArray stickers = new JSONArray(); // 모든 스티커
      JSONArray groups = new JSONArray(); // 모든 스티커 그룹
      ArrayList<String> groupNames = new ArrayList<>();
      int count = -1;
      // 하위 디렉토리
      for (File info : Objects.requireNonNull(new File(STICKER_PATH).listFiles())) {
        if (info.isDirectory()) {
          String newPath = STICKER_PATH + "/" + info.getName();
          JSONObject group = new JSONObject();  // 스티커 그룹 정보
          JSONObject position = new JSONObject(); // 각 그룹이 view 에서 표시될 위치
          position.put("x", (count--) * 21);
          position.put("y", 0);
          group.put("groupName", info.getName());
          group.put("position", position);
          groupNames.add(info.getName());
          for (File sub_info : Objects.requireNonNull(new File(newPath).listFiles())) {
            if (sub_info.isFile() && !sub_info.getName().startsWith("nav")) {
              JSONObject sticker = new JSONObject();
              sticker.put("groupName", info.getName());
              sticker.put("id", sub_info.getName());
              sticker.put("src",
                  request.getContextPath() + "/sticker/" + info.getName() + "/" + sub_info
                      .getName());
              stickers.add(sticker);
            } else if (sub_info.getName().startsWith("nav")) {
              group.put("navsrc",
                  request.getContextPath() + "/sticker/" + info.getName() + "/" + sub_info
                      .getName());
            }
          }
          groups.add(group);
        }
      }

      JSONObject result = new JSONObject(); // CKEditor Plugin 으로 보낼 JSON
      result.put("groups", groups);
      result.put("items", stickers);
      cache_stickerList.putAll(result);
      cache_groupNames.addAll(groupNames);
      return result;
    }
    return JsonParse.getJsonStringFromMap(cache_stickerList);
  }

  public byte[] getSticker(String dirName, String fileName) throws IOException {
    String requestPath = STICKER_PATH + "/" + dirName + "/" + fileName;
    InputStream in = new FileInputStream(requestPath);
    byte[] imageByteArray = IOUtils.toByteArray(in);
    in.close();
    return imageByteArray;
  }

  // 스티커 폴더의 변경있으면 cache 초기화
  public boolean isModify() {
    int directoryCount = 0;

    for (File info : Objects.requireNonNull(new File(STICKER_PATH).listFiles())) {
      if (info.isDirectory()) {
        directoryCount++;
        if (!cache_groupNames.contains(info.getName())) {
          cache_groupNames.clear();
          cache_stickerList.clear();
          return true;
        }
      }
    }
    if (directoryCount != cache_groupNames.size()) {
      cache_groupNames.clear();
      cache_stickerList.clear();
      return true;
    }
    return false;
  }
}
