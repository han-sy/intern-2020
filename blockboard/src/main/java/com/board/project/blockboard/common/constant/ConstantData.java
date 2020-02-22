/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionCompare.java
 */
package com.board.project.blockboard.common.constant;


public class ConstantData {

  public static final int ON_TO_OFF = 1;
  public static final int OFF_TO_ON = 2;
  public static final int NO_CHANGE = 0;
  public static final int POST_PAGE_SIZE = 10;
  public static final int COMMENT_PAGE_SIZE = 5;
  public static final int POST_RANGE_SIZE = 10;
  public static final int COMMENT_RANGE_SIZE = 5;
  public static final int BOARD_MY_POSTS = -1;
  public static final int BOARD_MY_REPLIES = -2;
  public static final int BOARD_TEMP = -3;
  public static final int BOARD_RECYCLE = -4;
  public static final int BOARD_RECENT = -5;
  public static final int BOARD_POPULAR = -6;

  public static final String ATTACH_FILE_PATH = "C:/test";
/*  public static final String AWS_FILE_DIR = "file";
  public static final String AWS_INLINE_DIR = "inline";*/
  public static final String BUCKET_FILE ="block-board";
  public static final String BUCKET_USER = "block-board-user";
  public static final String COLLECTION_ID = "collectionUser";
  public static final String BUCKET_INLINE ="block-board-inline";
  public static final String BUCKET_USER_THUMBNAIL = "block-board-user-thumbnail";

  public static final class FunctionID {

    public static final int COMMENT = 1;
    public static final int REPLY = 2;
    public static final int POST_ATTACH_FILE = 3;
    public static final int COMMENT_ATTACH_FILE =4;
    public static final int POST_INLINE_IMAGE =5;
    public static final int COMMENT_INLINE_IMAGE =6;
    public static final int POST_TEMP_SAVE = 7;
    public static final int POST_STICKER = 9;
    public static final int COMMENT_STICKER = 10;
    public static final int POST_AUTO_TAG = 11;
    public static final int COMMENT_AUTO_TAG = 12;
  }

}
