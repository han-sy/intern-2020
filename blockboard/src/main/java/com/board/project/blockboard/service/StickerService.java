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

  private final int MAX_GROUP_PER_PAGE = 6;

  long start, end;

  public JSONObject getStickers(HttpServletRequest request, int pageNum) {
    Map<String, Object> stickerList = new HashMap<>();

    log.info("요청온 페이지 = " + pageNum);
    start = System.nanoTime();
    int groupIndex = -1;
    int totalGroupCount = 0;

    JSONArray stickers = new JSONArray();
    JSONArray groups = new JSONArray();

    int count = -1;
    // 하위 디렉토리
    for (File info : Objects.requireNonNull(new File(STICKER_PATH).listFiles())) {
      if (info.isDirectory()) {
        totalGroupCount++;
        groupIndex++;
        log.info("index = " + groupIndex);
        if (groupIndex < (pageNum - 1) * MAX_GROUP_PER_PAGE) {
          log.info("다음꺼 검사");
          continue;
        }
        if (groupIndex >= pageNum * MAX_GROUP_PER_PAGE) {
          log.info("여기까지");
          break;
        }
        log.info("스티커 저장 시작! + " + groupIndex);
        String newPath = STICKER_PATH + "/" + info.getName();
        JSONObject group = new JSONObject();
        JSONObject position = new JSONObject();
        position.put("x", (count--) * 25);
        position.put("y", 0);
        group.put("groupName", info.getName());
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
    log.info("총 디렉토리 수 = " + totalGroupCount);
    JSONObject result = new JSONObject();
    result.put("groups", groups);
    result.put("items", stickers);
    result.put("totalGroupCount", totalGroupCount);
    stickerList.putAll(result);
    end = System.nanoTime();
    log.info("getStickers: " + (end - start) / 1000000.0);
    log.info(result.toJSONString());
    return result;
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

}
