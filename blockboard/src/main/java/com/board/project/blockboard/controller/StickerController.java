package com.board.project.blockboard.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file StickerController.java
 */
@Slf4j
@RestController
public class StickerController {

  private final String STICKER_PATH = "/home1/irteam/storage/sticker";

  @GetMapping("/sticker")
  public JSONObject getStickers(HttpServletRequest request) {
    JSONArray stickers = new JSONArray();
    JSONArray groups = new JSONArray();
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
        group.put("position", position);
        for (File sub_info : Objects.requireNonNull(new File(newPath).listFiles())) {
          if (sub_info.isFile() && !sub_info.getName().startsWith("nav")) {
            JSONObject sticker = new JSONObject();
            sticker.put("groupName", info.getName());
            sticker.put("id", sub_info.getName());
            sticker.put("src", request.getContextPath() + "/sticker/" + info.getName() + "/" + sub_info.getName());
            stickers.add(sticker);
          } else if (sub_info.getName().startsWith("nav")) {
            group.put("navsrc", request.getContextPath() + "/sticker/" + info.getName() + "/" + sub_info.getName());
          }
        }
        groups.add(group);
      }
    }
    JSONObject result = new JSONObject();
    result.put("groups", groups);
    result.put("items", stickers);
    log.info(result.toJSONString());
    log.info(stickers.toJSONString());
    return result;
  }

  @GetMapping(value = "/sticker/{directory}/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
  public byte[] getSticker(@PathVariable("directory") String dirName,
      @PathVariable("fileName") String fileName) throws IOException {
    String requestPath = STICKER_PATH + "/" + dirName + "/" + fileName;
    InputStream in = new FileInputStream(requestPath);
    byte[] imageByteArray = IOUtils.toByteArray(in);
    in.close();
    return imageByteArray;
  }
}