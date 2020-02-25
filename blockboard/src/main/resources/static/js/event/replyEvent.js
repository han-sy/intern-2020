/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file replyEvent.js
 */

//답글달기 버튼
$(document).on('click', '.reply_btn', function () {
  var referenceCommentContainer = getReferenceCommentContainer.call(this);
  var referenceUserName = getReferenceUserName.call(this);
  var referenceUserId = getReferenceUserId.call(this);
  var replyInputContainerID = getReplyInputContainerID(referenceCommentContainer);

  replyFormClear(); //답글창 하나만 유지하기위해 다 클리어

  getCommentInputHtml("답글", "입력",
      `<a class="mentions_tag name" style="cursor:pointer; text-decoration: none;" href="javascript:void(0)"`
      + ` data-id="${referenceUserId}"><strong>@${referenceUserName}</strong></a>&nbsp;`,
      "#" + replyInputContainerID, "btn_openReply", "is_reply_input", "replyText");

  fileFormClear();
  moveToInputID(replyInputContainerID);
});

//답글 입력 버튼
$(document).on('click', '.btn_openReply', function () {
  var editorName = $(this).closest(".commentHtml").find('textarea').attr('id');
  var postId = getPostIdInPost();
  var boardId = getCurrentActiveBoardId();
  var commentText = CKEDITOR.instances[editorName].getData();
  var commentReferencedId = getCommentReferencedIdInReferenceCommentContainer.call(this);
  if (commentText == "") {
    alert("내용을 입력하세요.");
    return;
  }
  $(function () {
    insertReply(boardId, postId, commentText, commentReferencedId, editorName);
  });
});


//답글 취소버튼
$(document).on('click', '.btn_close_cmt_input', function () {
  var referenceCommentContainer = $(this).closest(".referenceCommentContainer");
  var replyInputContainer = $(
      "#reply_input_container" + referenceCommentContainer.attr("data-id"));
  replyInputContainer.html("");
  $('#comment-alarm-modal').modal('hide');
});

/**
 * 답글보기 버튼
 */
$(document).on('click', '.open_reply_list_btn', function () {
  var commentId = getCommentIdInCommentContainer.call(this);
  var boardId = getBoardIdInPost();
  var postId = getPostIdInPost();
  var switchText = $(this);
  switchReplyListOpenAndClose(switchText,boardId,postId,commentId);
});

/**
 * 답글더보기 버튼
 */
$(document).on('click', '.more-replies-btn', function () {
  var boardId = getBoardIdInPost();
  var postId = getPostIdInPost();
  var commentReferencedId = getCommentReferencedIdInReferenceCommentContainer.call(this);
  var printedRepliesCount =  getCountPrintedReplies();
  getReplyList(boardId, postId, commentReferencedId,printedRepliesCount,getReplyListUI);
});


function switchReplyListOpenAndClose(switchText,boardId,postId,commentId) {
  if(switchText.html() =="답글 보기"){
    $(".open_reply_list_btn").html("답글 보기");
    clearAllReplyContainer();
    var printedRepliesCount =  getCountPrintedReplies();
    console.log("!!!!printedRepliesCount : "+printedRepliesCount);
    getReplyList(boardId, postId, commentId,printedRepliesCount,getReplyListUI);
    switchText.html("답글 닫기");
  }else if("답글 닫기"){
    clearReplyContainer(commentId);
    switchText.html("답글 보기");
  }else{
    changedDataError();
  }
}

function clearAllReplyContainer() {
  $(".replyContainer").html("");
}

function clearReplyContainer(commentId) {
  $("#reply_container" + commentId).html("");
}