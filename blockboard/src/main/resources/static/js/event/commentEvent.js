/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentEvent.js
 */
const COMMENT_PREFIX = {
  length: 7
};

//댓글 입력버튼 누를때
$(document).on('click', '.btn_open_comment', function () {
  var postID = getPostIDInPost();
  var boardID = getBoardIDInPost();
  var commentText = CKEDITOR.instances['commentText'].getData();
  if (commentText == "") {
    alert("내용을 입력하세요.");
    return;
  }
  insertComment(boardID, postID, commentText);
});

//댓글삭제버튼 누를때
//삭제클릭한곳의 부모의 div의 id뽑게했음
$(document).on('click', '#delete_comment', function () {
  var postID = getPostIDInPost();
  var boardID = getCurrentBoardID();
  var commentID = getCommentIDInCommentContainer.call(this);
  var isAcceptance = confirm("선택한 댓글 or 답글을 정말 삭제하시겠습니까?");
  if (isAcceptance) {
    deleteCommentByCommentID(postID, boardID, commentID);
  }
});

//수정 버튼 눌렀을때
$(document).on('click', '#edit_comment', function () {
  var postID = getPostIDInPost();
  var boardID = getBoardIDInPost();
  var commentID = getCommentIDInCommentContainer.call(this);
  editCommentByCommentID(postID, boardID, commentID);
});

//댓글수정후 수정하기 버튼 눌렀을때
$(document).on('click', '.btn_edit_comment_complete', function () {
  var newComment = $(this).closest(".commentHtml").find(
      '#commentText').val().replace(/\n/g,
      "<br>");
  var commentID = getCommentIDInCommentContainer.call(this);
  console.log("commentID");
  var postID = getPostIDInPost();
  var boardID = getBoardIDInPost();
  console.log(postID + "<-pid bid->" + boardID);
  var isAcceptance = confirm("댓글을 수정 하시겠습니까?");
  if (isAcceptance) {
    if (functionOn.commentFileAttach) {
      updateIDToFiles("", commentID);
    }

    editComment(postID, boardID, commentID, newComment);
  }
});

//댓글달기 버튼
$(document).on('click', '.comment_btn', function () {
  var offset = $(".comment_input_container").offset().top - $(window).height()
      / 2;
  $(window).scrollTop(offset);
});



//답글달기 버튼
$(document).on('click', '.reply_btn', function () {
  console.log("답글 창 생성");
  var referenceCommentContainer = getReferenceCommentContainer.call(this);
  var referenceUserName = getReferenceUserName.call(this);
  var referenceUserID = getReferenceUserID.call(this);
  var replyInputContainerID = getReplyInputContainerID(referenceCommentContainer);

  replyFormClear(); //답글창 하나만 유지하기위해 다 클리어

  getCommentInputHtml("답글", "입력",
      `<a class="mentions_tag name" style="cursor:pointer; text-decoration: none;" href="javascript:void(0)"`
      + ` data-id="${referenceUserID}"><strong>@${referenceUserName}</strong></a>&nbsp;`,
      "#" + inputID, "btn_openReply", "is_reply_input");

  fileFormClear();
  moveToInputID(replyInputContainerID);
});

//답글 입력 버튼
$(document).on('click', '.btn_openReply', function () {
  var postID = getPostIDInPost();
  var boardID = getCurrentBoardID();
  var commentText = CKEDITOR.instances['commentText'].getData();
  var commentReferencedID = getCommentReferencedIDInReplyContainer.call(this);
  if (commentText == "") {
    alert("내용을 입력하세요.");
    return;
  }
  $(function () {
    insertReply(boardID, postID, commentText, commentReferencedID);
  });
});

//답글 취소버튼
$(document).on('click', '.btn_close_cmt_input', function () {
  var referenceCommentContainer = $(this).closest(".referenceCommentContainer");
  var replyInputContainer = $(
      "#reply_input_container" + referenceCommentContainer.attr("data-id"));
  replyInputContainer.html("");
});



$(document).on('click', '.open_attached_file_list', function () {
  var commentID = getCommentIDInCommentContainer.call(this);
  var fileContainer = getFileContainerInLocalCommentContainer.call(this);
  var switchText = $(this);
  switchAttachFileOpenAndClose(switchText, commentID, fileContainer);
});


/**
 * 파일첨부 수정 버튼 클릭시
 */
$(document).on('click', '.open_edit_file_form_btn', function () {
  var commentID = getCommentIDInCommentContainer.call(this);
  console.log("commentID :" + commentID);
  fileFormClear();
  openFileAttachForm("", commentID, $(this).closest(".commentHtml"));
});

$(document).on('click', '.open_reply_list_btn'), function () {
  var commentID = getCommentIDInCommentContainer.call(this);
  var boardID = getBoardIDInPost();
  var postID = getPostIDInPost();
  getReplyList(boardID, postID, commentID, getReplyListUI);
}

function moveToInputID(inputID) {
  var offset = $("#" + inputID).offset().top - $(window).height() / 2;
  $(window).scrollTop(offset);
}

function switchAttachFileOpenAndClose(switchText, commentID, fileContainer) {
  if (switchText.html() == "첨부파일 보기") {
    getFileList(0, commentID, fileContainer, updateFileListInCommentUI);
    switchText.html("첨부파일 닫기");
  } else if ("첨부파일 닫기") {
    fileContainer.html("");
    switchText.html("첨부파일 보기");
  } else {
    changedDataError();
  }
}