/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file StickerService.java
 */
package com.board.project.blockboard.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings("unchecked")
public class StickerService {

  private final String STICKER_PATH = "/home1/irteam/storage/sticker";

  private static String CONTEXT_PATH;

  private final int MAX_GROUP_PER_PAGE = 6;

  private int startIndex, endIndex, totalDirectoryCount;

  // 해당 페이지의 Navigation Bar Item(=카테고리 목록) 들을 생성하고
  // 그 중 첫 번째 카테고리의 스티커만 불러온다.
  public JSONObject getNavigationItemAndStickerAtFirst(HttpServletRequest request, int pageNum) {
    File[] allStickerDirectory = new File(STICKER_PATH).listFiles();
    assert allStickerDirectory != null;

    CONTEXT_PATH = request.getContextPath();
    setIndexVariable(pageNum, allStickerDirectory);

    return makeNavigationItems(allStickerDirectory);
  }

  private void setIndexVariable(int pageNum, File[] allStickerDirectory) {
    startIndex = (pageNum - 1) * MAX_GROUP_PER_PAGE;
    endIndex = pageNum * MAX_GROUP_PER_PAGE;
    totalDirectoryCount = allStickerDirectory.length;
  }

  // 디렉토리(=groupName)에 들어있는 스티커들을 가져온다.
  public JSONObject getStickersByGroupName(HttpServletRequest request, String groupName) {
    File[] stickerItems = new File(STICKER_PATH + "/" + groupName).listFiles();
    assert stickerItems != null;

    CONTEXT_PATH = request.getContextPath();

    JSONArray stickers = new JSONArray();

    for (File stickerItem : stickerItems) {
      if (!stickerItem.getName().startsWith("nav")) {
        JSONObject sticker = new JSONObject();
        sticker.put("groupName", groupName);
        sticker.put("id", stickerItem.getName());
        sticker.put("src", CONTEXT_PATH + "/sticker/" + groupName + "/" + stickerItem.getName());
        stickers.add(sticker);
      }
    }

    return makeJsonOfStickerGroup(groupName, stickers);
  }

  // 카테고리 아이템 생성
  private JSONObject makeNavigationItems(File[] allDirectory) {
    JSONArray navigationItems = new JSONArray();
    JSONArray firstStickerOfNavigationItems = new JSONArray();

    int navigationIndex = -1;

    for (int i = startIndex; i < endIndex && i < totalDirectoryCount; i++) {
      JSONObject navigationItem = new JSONObject();
      JSONObject uiPosition = new JSONObject();

      uiPosition.put("x", (navigationIndex--) * 25);
      uiPosition.put("y", 0);
      navigationItem.put("groupName", allDirectory[i].getName());
      navigationItem.put("position", uiPosition);

      addNavigationItemWithIcon(allDirectory[i], navigationItems, navigationItem);
      if (i == startIndex) {
        addStickerAtFirst(allDirectory[i], firstStickerOfNavigationItems, navigationItem);
      }

    }
    return makeJsonOfStickerGroups(navigationItems, firstStickerOfNavigationItems);
  }

  // 6개(=MAX_GROUP_PER_PAGE)의 스티커 그룹 Json 생성
  private JSONObject makeJsonOfStickerGroups(JSONArray groups, JSONArray stickers) {
    JSONObject sendDataToEditor = new JSONObject();
    sendDataToEditor.put("groups", groups);
    sendDataToEditor.put("items", stickers);
    sendDataToEditor.put("totalGroupCount", totalDirectoryCount);
    return sendDataToEditor;
  }

  // 1개의 스티커 그룹 Json 생성
  private JSONObject makeJsonOfStickerGroup(String groupName, JSONArray stickers) {
    JSONObject sendDataToEditor = new JSONObject();
    sendDataToEditor.put("groupName", groupName);
    sendDataToEditor.put("items", stickers);
    return sendDataToEditor;
  }

  // Navigation Bar 에 아이콘을 추가한 각 스티커 그룹을 추가
  private void addNavigationItemWithIcon(File directory, JSONArray navigationItems, JSONObject navigationItem) {
    if (!directory.isDirectory()) {
      return;
    }
    String directoryPath = STICKER_PATH + "/" + directory.getName();
    // Navigation Bar 아이콘 파일이름 nav 로 시작한다.
    for (File stickerItem : Objects.requireNonNull(new File(directoryPath).listFiles())) {
      if (stickerItem.getName().startsWith("nav")) {
        navigationItem.put("navIconSrc",
            CONTEXT_PATH + "/sticker/" + directory.getName() + "/" + stickerItem.getName());
        break;
      }
    }
    navigationItems.add(navigationItem);
  }

  // Navigation Items 중 에서 첫번째 카테고리 스티커들을 추가
  private void addStickerAtFirst(File firstDirectory, JSONArray stickers, JSONObject navigationItem) {
    String path = STICKER_PATH + "/" + firstDirectory.getName();

    for (File stickerItem : Objects.requireNonNull(new File(path).listFiles())) {
      JSONObject sticker = new JSONObject();

      String groupName = firstDirectory.getName();
      String stickerName = stickerItem.getName();

      sticker.put("groupName", groupName);
      sticker.put("id", stickerName);
      sticker.put("src", CONTEXT_PATH + "/sticker/" + groupName + "/" + stickerName);
      stickers.add(sticker);

      if (stickerItem.getName().startsWith("nav")) {
        navigationItem.put("navIconSrc", CONTEXT_PATH + "/sticker/" + groupName + "/" + stickerName);
      }
    }
  }

  // 1개의 스티커 파일을 이미지로 반환한다.
  public byte[] getSticker(String groupName, String fileName) throws IOException {
    String requestPath = STICKER_PATH + "/" + groupName + "/" + fileName;
    InputStream in = new FileInputStream(requestPath);
    byte[] imageByteArray = IOUtils.toByteArray(in);
    in.close();
    return imageByteArray;
  }

}
