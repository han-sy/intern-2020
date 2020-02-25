/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file handlebarsHelper.js
 */
//기능 사용여부 체크 하는 헬퍼함수
Handlebars.registerHelper('isAbleFunction', function (options) {
  if (!(this.functionOn)) {
    return options.fn(this);
  }
  return options.inverse(this);
});

//댓글기능 on인지 체크
Handlebars.registerHelper('isCommentAble', function (options) {
  if (functionOn.comments) {
    let boardId = getCurrentActiveBoardId();
    if (boardId > 0 || boardId == BOARD_ID.POPULAR || boardId
        == BOARD_ID.MY_POST || boardId == BOARD_ID.MY_REPLY || boardId
        == BOARD_ID.RECENT) {
      return options.fn(this);
    }
  }
  return options.inverse(this);
});

//답글기능 on인지 체크
Handlebars.registerHelper('isReplyAble', function (options) {
  if (functionOn.reply) {
    return options.fn(this);
  }
  return options.inverse(this);
});

//게시글 파일 첨부기능 on인지
Handlebars.registerHelper('isPostFileAttachAble', function (options) {
  if (functionOn.postFileAttach) {
    return options.fn(this);
  }
  return options.inverse(this);
});

//댓글 답글
Handlebars.registerHelper('isCommentFileAttachAble', function (options) {
  if (functionOn.commentFileAttach) {
    return options.fn(this);
  }
  return options.inverse(this);
});

//같은 사용자인지 체크
Handlebars.registerHelper('isSameUser', function (options) {
  let currentUserId = $("#current_user_info").attr("data-id");
  if (this.userId == currentUserId) {
    return options.fn(this);
  }
  return options.inverse(this);
});

//답글인지 체크
Handlebars.registerHelper('isReply', function (options) {
  if (this.type == '답글') {
    return options.fn(this);
  }
  return options.inverse(this);
});

Handlebars.registerHelper('isFirstPage', function (option) {
  if (this.currentPage == 1) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isLastPage', function (option) {
  if (this.currentPage >= this.pageCount) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isFirstRange', function (option) {
  if (this.currentRange == 1) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isLastRange', function (option) {
  if (this.currentRange >= this.rangeCount) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('hasComments', function (option) {
  if (this.commentsCount) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isPopular', function (option) {
  let boardId = parseInt(getCurrentActiveBoardId());
  if (boardId < 0) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isCommentFunction', function () {
  let isCommentFunction = (this.functionID % 2);
  if (isCommentFunction == 0) {
    return "comment_function";
  } else {
    return "";
  }
});

Handlebars.registerHelper('isPostPage', function (option) {
  if (this.pageType == "posts") {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('printFileSize', function () {
  return getFileSize(this.fileSize);
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isRecycle', function (option) {
  if (this.postStatus === POST_STATUS.RECYCLE) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isTempBox', function (option) {
  let boardId = parseInt(getCurrentActiveBoardId());
  if (boardId === BOARD_ID.TEMP_BOX) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isRecycleBin', function (option) {
  let boardId = parseInt(getCurrentActiveBoardId());
  if (boardId === BOARD_ID.RECYCLE) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isTempSaveAble', function (options) {
  if (functionOn.postTempSave) {
    return options.fn(this);
  }
  return options.inverse(this);
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isPostAlarm', function (options) {
  this.registerTime = this.registerTime.substring(0,
      this.registerTime.length - 3);
  if (this.commentId == 0) {
    return options.fn(this); // 게시물 태그 알람
  }
  return options.inverse(this);// 댓글 태그 알람
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isReadAlarm', function (options) {
  if (this.isRead) {
    return options.fn(this); // 읽은 알람
  }
  return options.inverse(this); // 안 읽은 알람
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('hasReferenceCommentId', function (options) {
  if (this.commentReferencedId > 0) {
    return options.fn(this);
  }
  return options.inverse(this);
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isSearchPage', function (options) {
  if (this.pageType === "search") {
    return options.fn(this);
  }
  return options.inverse(this);
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isTemp', function (option) {
  if (this.postStatus === POST_STATUS.TEMP) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});