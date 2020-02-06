package com.board.project.blockboard.common.util;

public class HTMLTagUtils {

  public static String HTMLtoString(String content) {
    return content.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "")
        .replaceAll("&lt;", "<")
        .replaceAll("&gt;", ">")
        .replaceAll("&quot;", "\"")
        .replaceAll("&amp;", "&");
  }
}
