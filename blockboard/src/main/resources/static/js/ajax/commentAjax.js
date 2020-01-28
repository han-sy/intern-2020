//댓글 출력
function UpdateCommentListUI(data) {
  $(".comment_list_container").html("");
  var commentHtml = "";

  //console.log(${userID}+"------"+$("#current_user_id").text())

  $.each(data, function (key, value) {
    commentHtml += ("<hr><div class =referenceCommentContainer data-id="+value.commentID+"><div class = commentContainer id=comment" + value.commentID + "><div>");
    commentHtml += ("<p class=user><span class=name>" + value.userName + "</span></strong> <span class=date> " + value.commentRegisterTime + "</span></p>");
    commentHtml += ("<p class =comment_area id=translate_area>" + value.commentContent + "</p></div>");
    commentHtml += "<div class=btn>";
    if ($('#functionAble2').attr("value") == "on") {
      commentHtml += "<button type=button class=replyBtn>답글</button>";
    }
    if (value.userID == $("#current_user_id").text()) {
      commentHtml += "<button type=button id = edit_comment>수정</button>";
      commentHtml += "<button type=button id = delete_comment>삭제</button>";
    }
    commentHtml += "</div>"
    if ($('#functionAble2').attr("value") == "on") {
      commentHtml += "</div><div style ='padding: 5px 1px 3px 20px;' class=replyContainer id=reply_container" + value.commentID + " ></div>"
      commentHtml += "<div style ='padding: 5px 1px 3px 20px;' class=replyContainer id=reply_input_container" + value.commentID + " ></div>"
    }
    commentHtml += "</div></div>";
  });
  //답글 추가
  if ($('#functionAble2').attr("value") == "on") {
    getAllReplyList(data);
  }
  $(".comment_list_container").append(commentHtml);
}



//댓글 inputform 받아오기
function getCommentInputHtml(type,buttonName,tag,className) {
  var commentInputHtml = "";
  commentInputHtml += "<br><div style='width: 100%' class=commentHtml>";
  commentInputHtml += ("<strong class=tag style='cursor:pointer'>"+tag+"</strong>");
  commentInputHtml += ("<textarea style='width: 1100px' id=commentText placeholder = '"+type+"을 입력하세요' name=commentTxt ></textarea>");
  commentInputHtml += ("<div><button id=btn_openComment onclick = javascript:clickSendCommentBtn()>"+buttonName+"</button>");
  if(type=="답글"){
    commentInputHtml += ("<button id=btn_close_cmt_input >취소</button>");
  }
  commentInputHtml += "</div></div>";
  $(className).append(commentInputHtml + "</div>");
}

//댓글 컨텐츠 모두 불러오기
function getCommentAllContents(data) {
  UpdateCommentListUI(data);
  getCommentInputHtml("댓글","입력","",".comment_input_container");
}

//댓글리스트 받아오기
function getCommentList(boardID, postID, successFunction) {
  console.log("!!!boardID : " + boardID + ",postID : " + postID);
  $.ajax({
    type: 'GET',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments",
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      successFunction(data);
    }
  });
}

//댓글 추가
function updateComment(boardID, postID, commentText) {
  $.ajax({
    type: 'POST',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments",
    data: { boardID: boardID, postID: postID, commentContent: commentText },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      getCommentList(boardID, postID, UpdateCommentListUI);//성공하면 댓글목록 갱신
      $('#commentText').val("");
    }
  });
}

//댓글삭제
function deleteCommentByCommentID(postID, boardID, commentID) {
  $.ajax({
    type: 'DELETE',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments/" + commentID,
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      getCommentList(boardID, postID, UpdateCommentListUI);//성공하면 댓글목록 갱신
    }
  });
}

//댓글수정모드
function EditCommentByCommentID(postID, boardID, commentID) {
  var oldText = $('#comment' + commentID).children().children("#translate_area").html();
  //$('#comment'+commentID).html("!!");
  var commentInputHtml = "";
  commentInputHtml += "<br><div style='width: 100%' class=commentHtml>";
  commentInputHtml += ("<textarea style='width: 1100px' id=commentText placeholder ='댓글을 입력하세요' name=commentTxt >" + oldText + "</textarea>");
  commentInputHtml += "<div><button id=btn_edit_comment_complete >수정하기</button></div>";
  commentInputHtml += "</div>";
  $('#comment' + commentID).html(commentInputHtml + "</div>");
  //$('#comment'+commentID).html();
}

//댓글 수정
function editComment(postID, boardID, commentID, newComment) {
  $.ajax({
    type: 'PUT',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments/" + commentID,
    data: { newComment, newComment },
    error: function () {  //통신 실패시
      alert('통신실패!수정');
    },
    success: function (data) {
      getCommentList(boardID, postID, UpdateCommentListUI);//성공하면 댓글목록 갱신
    }
  });
}

//답글 ui 구성
function getReplyListUI(commentID,data) {
    //console.log("댓글"+commentID);
  $("#reply_container"+commentID).html("");
  var commentHtml = "";

  //console.log(${userID}+"------"+$("#current_user_id").text())

  $.each(data, function (key, value) {
    console.log("답글"+value.commentID);
    commentHtml += "<div class = commentContainer id=comment" + value.commentID + "><div>";
    commentHtml += ("<p class=user>└<span class=name>" + value.userName + "</span></strong> <span class=date> " + value.commentRegisterTime + "</span></p>");
    commentHtml += ("<p class =comment_area id=translate_area>" + value.commentContent + "</p></div>");
    commentHtml += "<div class=btn>";
    if ($('#functionAble2').attr("value") == "on") {
      commentHtml += "<button type=button class=replyBtn>답글</button>";
    }
    if (value.userID == $("#current_user_id").text()) {
      commentHtml += "<button type=button id = edit_comment>수정</button>";
      commentHtml += "<button type=button id = delete_comment>삭제</button>";
    }
    commentHtml += "</div></div>";

  });
  $("#reply_container"+commentID).append(commentHtml);
}

//답글전체 받아오기
function getAllReplyList(data) {
  $.each(data, function (key, value) {
    getReplyList(value.boardID, value.postID, value.commentID, getReplyListUI);
  });
}

//답글리스트 받아오기
function getReplyList(boardID, postID, commentID, successFunction) {
  $.ajax({
    type: 'GET',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments/" + commentID,
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      successFunction(commentID,data);
    }
  });
}