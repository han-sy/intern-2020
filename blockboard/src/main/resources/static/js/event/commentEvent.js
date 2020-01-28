
//댓글 추가버튼 누를때
/*$(document).on('click', '#btn_openComment', function () {
  alert(postID+","+boardID+":"+commentText);
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  var commentText = $(this).closest(".commentHtml").find(".commentText").val();
  if (commentText == "") {
      alert("내용을 입력하세요.");
      return;
  }
  $(function () {
      updateComment(boardID, postID, commentText);
  });
})*/
function clickSendCommentBtn() {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  var commentText = $('#commentText').val();

  if (commentText == "") {
    alert("내용을 입력하세요.");
    return;
  }
  $(function () {
    updateComment(boardID, postID, commentText);
  });
  $('#config_container').html("");
}

//댓글삭제버튼 누를때
//삭제클릭한곳의 부모의 div의 id뽑게했음
$(document).on('click', '#delete_comment', function () {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();

  var deleteDivID = $(this).parents("div").parents("div").attr("id");
  var commentID = deleteDivID.substring(7);

  $(function () {
    deleteCommentByCommentID(postID, boardID, commentID);
  });
})

//수정 버튼 눌렀을때
$(document).on('click', '#edit_comment', function () {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  var editDivID = $(this).parents("div").parents("div").attr("id");
  var commentID = editDivID.substring(7);

  $(function () {
    EditCommentByCommentID(postID, boardID, commentID);
  });
})

//댓글수정후 수정하기 버튼 눌렀을때
$(document).on('click', '#btn_edit_comment_complete', function () {
  commentDiv = $(this).parents("div").parents("div");
  var newComment = commentDiv.children('#commentText').val();
  var commentID = commentDiv.parents("div").attr("id").substring(7);
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  $(function () {
    editComment(postID, boardID, commentID, newComment);
  });
})


//답글달기 버튼
$(document).on('click', '.replyBtn', function () {
    var referenceCommentContainer = $(this).closest(".referenceCommentContainer");
    var referenceUserName = $(this).closest(".commentContainer").find(".name").html();
    var referenceUserID = $(this).closest(".commentContainer").find(".name").attr("data-id");
    var inputID = referenceCommentContainer.find("#reply_input_container"+referenceCommentContainer.attr("data-id")).attr("id");
    getCommentInputHtml("답글","입력","To <strong class =tag style ='cursor:pointer;' data-id="+referenceUserID+" >"+referenceUserName+"</strong>","#"+inputID);
})


$(document).on('click', '.btn_close_cmt_input', function () {
    var referenceCommentContainer = $(this).closest(".referenceCommentContainer");
    var replyInputContainer = $(this).closest(".replyContainer");
    replyInputContainer.html("");
})