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

Handlebars.registerHelper('isFirstPage', function (option) {
  if (this.currentPage == this.startPage) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isLastPage', function (option) {
  var lastPage = this.rangeCount;
  if (this.currentPage >= this.rangeCount) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});

Handlebars.registerHelper('isLastPage', function (option) {
  var lastPage = this.rangeCount;
  if (this.currentPage >= this.rangeCount) {
    return option.fn(this);
  } else {
    return option.inverse(this);
  }
});