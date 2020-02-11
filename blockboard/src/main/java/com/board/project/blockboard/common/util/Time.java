package com.board.project.blockboard.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file Time.java
 */
public class Time {
  public static String getTime(){
    long time = System.currentTimeMillis();
    SimpleDateFormat dayTime = new SimpleDateFormat("HHmmssSSS");
    String strTime = dayTime.format(new Date(time));
    return strTime;
  }
}
