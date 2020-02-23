/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentEvent.js
 */
const COMMENT_PREFIX = {
  length: 7
};

//댓글 입력버튼 누를때
$(document).on('click', '.btn_openComment', function () {
  var editorName = $(this).closest(".commentHtml").find('textarea').attr('id');
  var postID = $("#postID").html();
  var boardID = $("#boardIdInPost").html();
  var commentText = CKEDITOR.instances[editorName].getData();
  if (commentText == "") {
    alert("내용을 입력하세요.");
    return;
  }
  $(function () {
    insertComment(boardID, postID, commentText);
  });

});

//댓글삭제버튼 누를때
//삭제클릭한곳의 부모의 div의 id뽑게했음
$(document).on('click', '#delete_comment', function () {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();

  var deleteDivID = $(this).closest(".commentContainer").attr("id");
  var commentID = deleteDivID.substring(COMMENT_PREFIX.length);
  var isAcceptance = confirm("선택한 댓글 or 답글을 정말 삭제하시겠습니까?");
  if (isAcceptance) {
    $(function () {
      deleteCommentByCommentID(postID, boardID, commentID);
    });
  }
});

//수정 버튼 눌렀을때
$(document).on('click', '#edit_comment', function () {
  var postID = $("#postID").html();
  var boardID = $('#boardIdInPost').html();
  var editDivID = $(this).closest(".commentContainer").attr("id");
  var commentID = editDivID.substring(COMMENT_PREFIX.length);
  $(function () {
    editCommentByCommentID(postID, boardID, commentID);
  });
});

//댓글수정후 수정하기 버튼 눌렀을때
$(document).on('click', '.btn_edit_comment_complete', function () {
  var newComment = $(this).closest(".commentHtml").find('#commentText').val().replace(/\n/g,
      "<br>");
  var commentID = $(this).closest(".commentContainer").attr("id").substring(COMMENT_PREFIX.length);
  console.log("commentID");
  var postID = $("#postID").html();
  var boardID = $('#boardIdInPost').html();
  console.log(postID+"<-pid bid->"+boardID);
  var isAcceptance = confirm("댓글을 수정 하시겠습니까?");
  if (isAcceptance) {
    $(function () {
      var functionOn = new FunctionOn();
      if (functionOn.commentFileAttach) {
        updateIDToFiles("",commentID);
      }

      editComment(postID, boardID, commentID, newComment);
    });
  }
});

//댓글달기 버튼
$(document).on('click', '.commentBtn', function () {
  var offset = $(".comment_input_container").offset().top - $(window).height()
      / 2;
  $(window).scrollTop(offset);
});

//답글달기 버튼
$(document).on('click', '.replyBtn', function () {
  console.log("답글 창 생성");
  var referenceCommentContainer = $(this).closest(".referenceCommentContainer");
  var referenceUserName = $(this).closest(".commentContainer").find(
      ".name").html();
  var referenceUserID = $(this).closest(".commentContainer").find(".name").attr(
      "data-id");
  var inputID = referenceCommentContainer.find(
      "#reply_input_container" + referenceCommentContainer.attr(
      "data-id")).attr("id");

  replyFormClear(); //답글창 하나만 유지하기위해 다 클리어

  getCommentInputHtml("답글", "입력",
      `<a class="mentions_tag" style="cursor:pointer; text-decoration: none;" href="javascript:void(0)"`
      + ` data-id="${referenceUserID}"><strong>@${referenceUserName}</strong></a>&nbsp;`,
      "#" + inputID, "btn_openReply", "is_reply_input", "replyText");

  fileFormClear();
  var offset = $("#" + inputID).offset().top - $(window).height() / 2;
  $(window).scrollTop(offset);
});

//답글 입력 버튼
$(document).on('click', '.btn_openReply', function () {
  var editorName = $(this).closest(".commentHtml").find('textarea').attr('id');
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  var commentText = CKEDITOR.instances[editorName].getData();
  var commentReferencedID = $(this).closest(".referenceCommentContainer").attr(
      "data-id");
  //alert("commentText ("+commentReferencedID+"): "+ commentText);
  if (commentText == "") {
    alert("내용을 입력하세요.");
    return;
  }
  $(function () {
    insertReply(boardID, postID, commentText, commentReferencedID, '', editorName);
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

$(document).on('click', '.open_attached_file_list', function () {
  var container = $(this).closest(".commentContainer");
  var commentID = container.attr("id").substring(COMMENT_PREFIX.length);
  var fileContainer = $(this).closest(".localCommentContainer").find(".attached_file_list_container_comment");
  var switchText = $(this);
  if (switchText.html()=="첨부파일 보기"){
    getFileList(0,commentID,fileContainer,updateFileListInCommentUI);
    switchText.html("첨부파일 닫기");
  }else if("첨부파일 닫기"){
    fileContainer.html("");
    switchText.html("첨부파일 보기");
  } else {
    changedDataError();
  }
});

/**
 * 파일첨부 수정 버튼 클릭시
 */
$(document).on('click', '.open_edit_file_form_btn', function () {
  var commentID = $(this).closest(".commentContainer").attr("id").substring(COMMENT_PREFIX.length);
  console.log("commentID :"+commentID);
  fileFormClear();
  openFileAttachForm("",commentID,$(this).closest(".commentHtml"));
});


