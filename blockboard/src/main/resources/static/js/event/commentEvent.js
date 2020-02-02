/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentEvent.js
 */
//댓글 추가버튼 누를때
$(document).on('click', '.btn_openComment', function () {
    var postID = $("#postID").html();
    var boardID = getCurrentBoardID();
    var commentText = $(this).closest(".commentHtml").find("#commentText").val().replace(/\n/g,"<br>");//엔터 적용 위해
    if (commentText == "") {
        alert("내용을 입력하세요.");
        return;
    }
    $(function () {
        insertComment(boardID, postID, commentText);
    });
})

//댓글삭제버튼 누를때
//삭제클릭한곳의 부모의 div의 id뽑게했음
$(document).on('click', '#delete_comment', function () {
    var postID = $("#postID").html();
    var boardID = getCurrentBoardID();

    var deleteDivID = $(this).closest(".commentContainer").attr("id");
    var commentID = deleteDivID.substring(7);
    var askSave = confirm("선택한 댓글 or 답글을 정말 삭제하시겠습니까?");
    if (askSave) {
        $(function () {
            deleteCommentByCommentID(postID, boardID, commentID);
        });
    }
})

//수정 버튼 눌렀을때
$(document).on('click', '#edit_comment', function () {
    var postID = $("#postID").html();
    var boardID = getCurrentBoardID();
    var editDivID = $(this).closest(".commentContainer").attr("id");
    var commentID = editDivID.substring(7);
    $(function () {
        editCommentByCommentID(postID, boardID, commentID);
    });
})

//댓글수정후 수정하기 버튼 눌렀을때
$(document).on('click', '#btn_edit_comment_complete', function () {
    commentDiv = $(this).parents("div").parents("div");
    var newComment = commentDiv.children('#commentText').val().replace(/\n/g,"<br>");
    var commentID = commentDiv.parents("div").attr("id").substring(7);
    var postID = $("#postID").html();
    var boardID = getCurrentBoardID();
    var askSave = confirm("댓글을 수정 하시겠습니까?");
    if (askSave) {
        $(function () {
            editComment(postID, boardID, commentID, newComment);
        });
    }
})

//댓글달기 버튼
$(document).on('click', '.commentBtn', function () {
    var offset = $(".comment_input_container").offset().top - $(window).height() / 2;
    $(window).scrollTop(offset);
})

//답글달기 버튼
$(document).on('click', '.replyBtn', function () {
    var referenceCommentContainer = $(this).closest(".referenceCommentContainer");
    var referenceUserName = $(this).closest(".commentContainer").find(".name").html();
    var referenceUserID = $(this).closest(".commentContainer").find(".name").attr("data-id");
    var inputID = referenceCommentContainer.find("#reply_input_container" + referenceCommentContainer.attr("data-id")).attr("id");
    getCommentInputHtml("답글", "입력",
        "To <strong class =tag style ='cursor:pointer;' data-id=" + referenceUserID + " >" + referenceUserName + "</strong>",
        "#" + inputID, "btn_openReply");
    var offset = $("#"+inputID).offset().top - $(window).height() / 2;
    $(window).scrollTop(offset);
})

//답글 입력 버튼
$(document).on('click', '.btn_openReply', function () {
    var postID = $("#postID").html();
    var boardID = getCurrentBoardID();
    var commentText = $(this).closest(".commentHtml").find("#commentText").val().replace(/\n/g,"<br>");
    var commentReferencedID = $(this).closest(".referenceCommentContainer").attr("data-id");
    var commentReferencedUserID = $(this).closest(".commentHtml").find(".tag").attr("data-id");
    //alert("commentText ("+commentReferencedID+"): "+ commentText);
    if (commentText == "") {
        alert("내용을 입력하세요.");
        return;
    }
    $(function () {
        insertReply(boardID, postID, commentText, commentReferencedID,commentReferencedUserID);
    });
})

//답글 취소버튼
$(document).on('click', '.btn_close_cmt_input', function () {
    var referenceCommentContainer = $(this).closest(".referenceCommentContainer");
    console.log($(this).closest(".replyContainer").html());
    var replyInputContainer = $("#reply_input_container"+referenceCommentContainer.attr("data-id"));
    replyInputContainer.html("");
})