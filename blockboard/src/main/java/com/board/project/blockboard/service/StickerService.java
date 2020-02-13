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

  private final Map<String, Object> cache_stickerList = new HashMap<>();

  private final ArrayList<String> cache_groupNames = new ArrayList<>();

  long start, end;

  public JSONObject getStickers(HttpServletRequest request) {
    start = System.nanoTime();
    if (cache_stickerList.isEmpty() || this.isModify()) {
      JSONArray stickers = new JSONArray();
      JSONArray groups = new JSONArray();
      ArrayList<String> groupNames = new ArrayList<>();
      int count = -1;
      // 하위 디렉토리
      for (File info : Objects.requireNonNull(new File(STICKER_PATH).listFiles())) {
        if (info.isDirectory()) {
          String newPath = STICKER_PATH + "/" + info.getName();
          JSONObject group = new JSONObject();
          JSONObject position = new JSONObject();
          position.put("x", (count--) * 21);
          position.put("y", 0);
          group.put("groupName", info.getName());
          groupNames.add(info.getName());
          group.put("position", position);
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

      JSONObject result = new JSONObject();
      result.put("groups", groups);
      result.put("items", stickers);
      cache_stickerList.putAll(result);
      cache_groupNames.addAll(groupNames);
      end = System.nanoTime();
      log.info("getStickers: " + (end - start) / 1000000.0);
      return result;
    }
    end = System.nanoTime();
    log.info("getStickers: " + (end - start) / 1000000.0);
    return JsonParse.getJsonStringFromMap(cache_stickerList);
  }

  public byte[] getSticker(String dirName, String fileName) throws IOException {
    start = System.nanoTime();
    String requestPath = STICKER_PATH + "/" + dirName + "/" + fileName;
    InputStream in = new FileInputStream(requestPath);
    byte[] imageByteArray = IOUtils.toByteArray(in);
    in.close();
    end = System.nanoTime();
    return imageByteArray;
  }

  public boolean isModify() {
    int directoryCount = 0;

    for (File info : Objects.requireNonNull(new File(STICKER_PATH).listFiles())) {
      if(info.isDirectory()) {
        directoryCount++;
        if (!cache_groupNames.contains(info.getName())) {
          cache_groupNames.clear();
          cache_stickerList.clear();
          return true;
        }
      }
    }
    if (directoryCount != cache_groupNames.size()) {
      return true;
    }
    return false;
  }
}
