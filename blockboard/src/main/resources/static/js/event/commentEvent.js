/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentEvent.js
 */


//댓글 입력버튼 누를때
$(document).on('click', '.btn_open_comment', function () {
  var editorName = $(this).closest(".commentHtml").find('textarea').attr('id');
  var postId = getPostIdInPost();
  var boardId = getBoardIdInPost();
  var commentText = CKEDITOR.instances[editorName].getData();
  if (commentText == "") {
    alert("내용을 입력하세요.");
    return;
  }
  insertComment(boardId, postId, commentText);

});

//댓글삭제버튼 누를때
//삭제클릭한곳의 부모의 div의 id뽑게했음
$(document).on('click', '#delete_comment', function () {
  var postId = getPostIdInPost();
  var boardId = getCurrentActiveBoardId();
  var commentId = getCommentIdInCommentContainer.call(this);
  var commentReferencedId = $(this).closest(".referenceCommentContainer").attr(
      "data-id");
  var isAcceptance = confirm("선택한 댓글 or 답글을 정말 삭제하시겠습니까?");
  if (isAcceptance) {
    deleteCommentByCommentId(postId, boardId, commentId,commentReferencedId);
  }
});

//수정 버튼 눌렀을때
$(document).on('click', '#edit_comment', function () {
  var postId = getPostIdInPost();
  var boardId = getBoardIdInPost();
  var commentId = getCommentIdInCommentContainer.call(this);
  editCommentByCommentId(postId, boardId, commentId);
});

//댓글수정후 수정하기 버튼 눌렀을때
$(document).on('click', '.btn_edit_comment_complete', function () {
  var editorName = $(this).closest(".commentHtml").find('textarea').attr('id');
  var newComment = CKEDITOR.instances[editorName].getData();
  var commentId = getCommentIdInCommentContainer.call(this);
  var postId = getPostIdInPost();
  var boardId = getBoardIdInPost();
  var isAcceptance = confirm("댓글을 수정 하시겠습니까?");
  if (isAcceptance) {
    if (functionOn.commentFileAttach) {
      updateIDToFiles("comment","", commentId);
    }
    editComment(postId, boardId, commentId, newComment);
  }
});

//댓글달기 버튼
$(document).on('click', '.comment_btn', function () {
  var offset = $(".comment_input_container").offset().top - $(window).height()
      / 2;
  $(window).scrollTop(offset);
});

/**
 * 첨부파일보기 버튼
 */
$(document).on('click', '.open_attached_file_list', function () {
  var commentId = getCommentIdInCommentContainer.call(this);
  var fileContainer = getFileContainerInLocalCommentContainer.call(this);
  var switchText = $(this);
  switchAttachFileOpenAndClose(switchText, commentId, fileContainer);
});


/**
 * 파일첨부 수정 버튼 클릭시
 */
$(document).on('click', '.open_edit_file_form_btn', function () {
  var commentId = getCommentIdInCommentContainer.call(this);
  fileFormClear();
  openFileAttachForm("", commentId, $(this).closest(".commentHtml"));
});


function moveToInputID(inputID) {
  var offset = $("#" + inputID).offset().top - $(window).height() / 2;
  $(window).scrollTop(offset);
}

function switchAttachFileOpenAndClose(switchText, commentId, fileContainer) {
  if (switchText.html() == "첨부파일 보기") {
    getFileList(0, commentId, fileContainer, updateFileListInCommentUI);
    switchText.html("첨부파일 닫기");
  } else if ("첨부파일 닫기") {
    fileContainer.html("");
    switchText.html("첨부파일 보기");
  } else {
    changedDataError();
  }
}


