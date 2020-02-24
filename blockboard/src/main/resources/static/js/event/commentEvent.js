/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentEvent.js
 */


//댓글 입력버튼 누를때
$(document).on('click', '.btn_open_comment', function () {
  var editorName = $(this).closest(".commentHtml").find('textarea').attr('id');
  var postID = getPostIDInPost();
  var boardID = getBoardIDInPost();
  var commentText = CKEDITOR.instances[editorName].getData();
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
  var commentReferencedID = $(this).closest(".referenceCommentContainer").attr(
      "data-id");
  var isAcceptance = confirm("선택한 댓글 or 답글을 정말 삭제하시겠습니까?");
  if (isAcceptance) {
    deleteCommentByCommentID(postID, boardID, commentID,commentReferencedID);
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
  var editorName = $(this).closest(".commentHtml").find('textarea').attr('id');
  var newComment = CKEDITOR.instances[editorName].getData();
  var commentID = getCommentIDInCommentContainer.call(this);
  var postID = getPostIDInPost();
  var boardID = getBoardIDInPost();
  var isAcceptance = confirm("댓글을 수정 하시겠습니까?");
  if (isAcceptance) {
    if (functionOn.commentFileAttach) {
      updateIDToFiles("comment","", commentID);
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

/**
 * 첨부파일보기 버튼
 */
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
  fileFormClear();
  openFileAttachForm("", commentID, $(this).closest(".commentHtml"));
});


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


