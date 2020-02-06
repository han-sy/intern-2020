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
  if ($('#functionAble1').attr("value") == "on") {
    if (getCurrentBoardID() > 0)
      return options.fn(this); //true
  }
  return options.inverse(this);//false
});

//답글기능 on인지 체크
Handlebars.registerHelper('isReplyAble', function (options) {
  if ($('#functionAble2').attr("value") == "on") {
    return options.fn(this); //true
  }
  return options.inverse(this);//false
});

//같은 사용자인지 체크
Handlebars.registerHelper('isSameUser', function (options) {
  var currentUserID = $("#current_user_info").attr("data-id");
  console.log(this.userID + "," + currentUserID);
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
  if (this.isTemp == true) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
Handlebars.registerHelper('isTrash', function (option) {
  if (this.isTrash == true) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});