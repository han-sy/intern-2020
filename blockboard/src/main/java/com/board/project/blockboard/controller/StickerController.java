/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file StickerController.java
 */
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

@Slf4j
@RestController()
public class StickerController {

  @Autowired
  private StickerService stickerService;

  @GetMapping("/sticker/{pageNum}")
  public JSONObject getNavigationItemAndStickerAtFirst(HttpServletRequest request,
      @PathVariable int pageNum) {
    return stickerService.getNavigationItemAndStickerAtFirst(request, pageNum);
  }

  @GetMapping("/sticker/groups/{groupName}")
  public JSONObject getStickerByGroupNameInPage(HttpServletRequest request,
      @PathVariable String groupName) {
    return stickerService.getStickersByGroupName(request, groupName);
  }

  @GetMapping(value = "/sticker/{groupName}/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
  public byte[] getSticker(@PathVariable String groupName,
      @PathVariable String fileName) throws IOException {
    return stickerService.getSticker(groupName, fileName);
  }
}