//댓글 리스트 받아오기
function getCommentList(data) {
  $(".comment_list_container").html("");
  var commentHtml = "";
  commentHtml += "<hr><div><div><table class='table'><h4><strong>${userName}</strong></h4>";
  commentHtml += "${commentContent}<tr><td></td></tr></table></div></div>";
  $.template("commentListTmpl", commentHtml);
  $.tmpl("commentListTmpl", data).appendTo(".comment_list_container");
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
      //TODO Template 적용예정
      getCommentList(data);
      getCommentInputHtml();
    }
  });
}

function updateComment(boardID,postID,commentText) {
  $.ajax({
    type: 'POST',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments",
    data: {commentContent: commentText},
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      //TODO Template 적용예정
      getCommentList(data);
    }
  });
}