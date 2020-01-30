//기능 사용여부 체크 하는 헬퍼함수
Handlebars.registerHelper('isAbleFunction', function(options) {
    if (this.companyID == 0) {
        return options.fn(this); //OFF else 실행행
    }
    return options.inverse(this);// ON
});

Handlebars.registerHelper('isReplyAble', function(options) {
    if ($('#functionAble2').attr("value") == "on") {
        return options.fn(this); //true
    }
    return options.inverse(this);//false
});

Handlebars.registerHelper('isSameUser', function(options) {
    if (this.userID = $("#current_user_id").text()) {
        return options.fn(this); //true
    }
    return options.inverse(this);//false
});