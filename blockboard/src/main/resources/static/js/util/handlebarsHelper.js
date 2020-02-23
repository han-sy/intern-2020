/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file handlebarsHelper.js
 */
//기능 사용여부 체크 하는 헬퍼함수
Handlebars.registerHelper('isAbleFunction', function (options) {
  if (!(this.functionOn)) {
    return options.fn(this); //OFF else 실행행
  }
  return options.inverse(this);// ON
});

//댓글기능 on인지 체크
Handlebars.registerHelper('isCommentAble', function (options) {
  var functionOn = new FunctionOn();
  if (functionOn.comment) {
    var boardID = getCurrentBoardID();
    if (boardID > 0 || boardID == BOARD_ID.POPULAR) {
      return options.fn(this);
    } //true
  }
  return options.inverse(this);//false
});

//답글기능 on인지 체크
Handlebars.registerHelper('isReplyAble', function (options) {
  var functionOn = new FunctionOn();
  if (functionOn.reply) {
    return options.fn(this); //true
  }
  return options.inverse(this);//false
});

//게시글 파일 첨부기능 on인지
Handlebars.registerHelper('isPostFileAttachAble', function (options) {
  var functionOn = new FunctionOn();
  if (functionOn.postFileAttach) {
    return options.fn(this); //true
  }
  return options.inverse(this);//false
});

//댓글 답글
Handlebars.registerHelper('isCommentFileAttachAble', function (options) {
  var functionOn = new FunctionOn();
  if (functionOn.commentFileAttach) {
    return options.fn(this); //true
  }
  return options.inverse(this);//false
});

//같은 사용자인지 체크
Handlebars.registerHelper('isSameUser', function (options) {
  var currentUserID = $("#current_user_info").attr("data-id");
  if (this.userID == currentUserID) {
    return options.fn(this); //true
  }
  return options.inverse(this);//false
});

//답글인지 체크
Handlebars.registerHelper('isReply', function (options) {
  if (this.type == '답글') {
    return options.fn(this); //true
  }
  return options.inverse(this);//false
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isTemp', function (option) {
  if (this.postStatus == "temp") {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isFirstPage', function (option) {
  if (this.currentPage == 1) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isLastPage', function (option) {
  var lastPage = this.pageCount;
  if (this.currentPage >= this.pageCount) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isFirstRange', function (option) {
  var firstRange = 1;
  if (this.currentRange == 1) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});
Handlebars.registerHelper('isLastRange', function (option) {
  var lastPage = this.rangeCount;
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
  var boardID = parseInt(getCurrentBoardID());
  if (boardID < 0) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});
Handlebars.registerHelper('isCommentFunction', function (option) {
  var isCommentFunction = (this.functionID%2)
  console.log(this.functionID +" : "+isCommentFunction );
  if (isCommentFunction == 0) {
    return "comment_function";
  } else {
    return "";
  }
});
Handlebars.registerHelper('printFileSize', function (option) {
  return getFileSize(this.fileSize);
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isRecycle', function (option) {
  if (this.postStatus == "recycle") {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isTempBox', function (option) {
  var boardID = parseInt(getCurrentBoardID());
  console.log(boardID);
  if (boardID === BOARD_ID.TEMP_BOX) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isRecycleBin', function (option) {
  var boardID = parseInt(getCurrentBoardID());
  if (boardID === BOARD_ID.RECYCLE) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isTempSaveAble', function (options) {
  var functionOn = new FunctionOn();
  if (functionOn.postTempSave) {
    return options.fn(this); //true
  }
  return options.inverse(this);//false
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isPostAlarm', function (options) {
  this.registerTime = this.registerTime.substring(0, this.registerTime.length - 3);
  if (this.commentID == 0) {
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
  return options.inverse(this);// 안 읽은 알람
});