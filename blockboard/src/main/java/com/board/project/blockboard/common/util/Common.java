package com.board.project.blockboard.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file Time.java
 */
public class Common {

  public static String getTime() {
    long time = System.currentTimeMillis();
    SimpleDateFormat dayTime = new SimpleDateFormat("HHmmssSSS");
    String strTime = dayTime.format(new Date(time));
    return strTime;
  }

  public static String getNewUUID() {
    UUID uuid = UUID.randomUUID();
    String prefix = uuid.toString().replaceAll("-", "_");
    return prefix;

  }

  public static String getFileName(String originalFileName) {
    int index = originalFileName.lastIndexOf(".");
    String fileName = originalFileName.substring(0, index);
    return fileName;
  }

  public static String getFileExt(String originalFileName) {
    int index = originalFileName.lastIndexOf(".");
    String fileExt = originalFileName.substring(index + 1);
    return fileExt;
  }
}
