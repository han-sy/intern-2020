//댓글 리스트 받아오기
function getCommentList(data) {
  $(".comment_list_container").html("");
  var commentHtml = "";

  //console.log(${userID}+"------"+$("#current_user_id").text())

  $.each(data, function (key, value) {
    commentHtml += "<hr><div id=comment"+value.commentID+"><div>";
    commentHtml += ("<p class=user><span class=name>" + value.userName + "</strong> <span class=date> " + value.commentRegisterTime + "</span></p>");
    commentHtml += ("<p class =comment_area>" + value.commentContent + "</p></div>");
    commentHtml += "<div class=btn>";
    if ($('#functionAble2').attr("value") == "on") {
      commentHtml += "<button type=button class=_no_print>답글</button>";
    }
    if (value.userID == $("#current_user_id").text()) {
      commentHtml += "<button type=button id = edit_comment>수정</button>";
      commentHtml += "<button type=button id = delete_comment>삭제</button>";
    }
    commentHtml += "</div></div>";
  });


  $(".comment_list_container").append(commentHtml);
}

//댓글 inputform 받아오기
function getCommentInputHtml() {
  var commentInputHtml = "";
  commentInputHtml += "<br><div style='width: 100%' class=commentHtml>";
  commentInputHtml += "<textarea style='width: 1100px' id=commentText placeholder = '댓글을 입력하세요' name=commentTxt ></textarea>";
  commentInputHtml += "<div><button id=btn_openComment onclick = javascript:clickSendCommentBtn()>입력</button></div>";
  commentInputHtml += "</div>";
  $(".comment_input_container").append(commentInputHtml + "</div>");
}


function getCommentAllContents(postID, boardID) {
  console.log("conmment ajax 함수 호출");
  $.ajax({
    type: 'GET',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments",
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      getCommentList(data);
      getCommentInputHtml();
    }
  });
}

function updateComment(boardID, postID, commentText) {
  $.ajax({
    type: 'POST',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments",
    data: { commentContent: commentText },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      getCommentList(data);
    }
  });
}

function deleteCommentByCommentID(postID,boardID,commentID){
  $.ajax({
    type: 'DELETE',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments/"+commentID,
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      getCommentList(data);
    }
  });
}