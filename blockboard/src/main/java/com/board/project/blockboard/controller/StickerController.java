package com.board.project.blockboard.controller;

import com.board.project.blockboard.service.StickerService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private StickerService stickerService;

  @GetMapping("/sticker/{pageNum}")
  public JSONObject getStickers(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
    return stickerService.getStickers(request, pageNum);
  }

  @GetMapping(value = "/sticker/{directory}/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
  public byte[] getSticker(@PathVariable("directory") String dirName,
      @PathVariable("fileName") String fileName) throws IOException {
    return stickerService.getSticker(dirName, fileName);
  }
}