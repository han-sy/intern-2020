/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionCompare.java
 */
package com.board.project.blockboard.common.constant;


public class ConstantData {

  public static final int NO_CHANGE = 0;


  public static final int BOARD_MY_POSTS = -1;
  public static final int BOARD_MY_REPLIES = -2;
  public static final int BOARD_TEMP = -3;
  public static final int BOARD_RECYCLE = -4;
  public static final int BOARD_RECENT = -5;
  public static final int BOARD_POPULAR = -6;

  public static final int ALARM_COUNT_PER_PAGE = 6;

  public static final class EditorName {

    public static final String POST_EDITOR = "editor";
    public static final String COMMENT_EDITOR = "commentText";
    public static final String REPLY_EDITOR = "replyText";
  }

  public static final class PostStatus {

    public static final String NORMAL = "normal";
    public static final String TEMP = "temp";
    public static final String RECYCLE = "recycle";
  }

  public static final class FunctionAble {

    public static final int ON_TO_OFF = 1;
    public static final int OFF_TO_ON = 2;
  }

  public static final class PageSize {

    public static final int REPLY = 5;
    public static final int POST = 10;
    public static final int COMMENT = 5;
    public static final int VIEW_RECORDS = 10;
  }

  public static final class RangeSize {

    public static final int POST = 10;
    public static final int COMMENT = 5;
  }

  public static final class Bucket {

    public static final String FILE = "block-board";
    public static final String USER = "block-board-user";
    public static final String INLINE = "block-board-inline";
    public static final String USER_THUMBNAIL = "block-board-user-thumbnail";
  }

  public static final class FunctionID {

    public static final int COMMENT = 1;
    public static final int REPLY = 2;
    public static final int POST_ATTACH_FILE = 3;
    public static final int COMMENT_ATTACH_FILE = 4;
    public static final int POST_INLINE_IMAGE = 5;
    public static final int COMMENT_INLINE_IMAGE = 6;
    public static final int POST_TEMP_SAVE = 7;
    public static final int POST_STICKER = 9;
    public static final int COMMENT_STICKER = 10;
    public static final int POST_AUTO_TAG = 11;
    public static final int COMMENT_AUTO_TAG = 12;
  }

}
