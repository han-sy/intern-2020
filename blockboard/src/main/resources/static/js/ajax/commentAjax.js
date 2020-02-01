/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentAjax.js
 */

//댓글 출력
function updateCommentListUI(data) {
    var source = $('#commentList-template').html();
    var template = Handlebars.compile(source);
    var comments = {comments: data};
    var itemList = template(comments);
    $('.comment_list_container').html(itemList);
    if ($('#functionAble2').attr("value") == "on") { //대댓글 기능 on 일때
        getAllReplyList(data);
    }
}

//댓글 inputform 받아오기
//TODO handlebar 적용
function getCommentInputHtml(type, buttonName, tag, className, buttonSelector) {
    data ={type : type, buttonName:buttonName, tag:tag, buttonSelector:buttonSelector};
    var source = $('#commentInputForm-template').html();
    var template = Handlebars.compile(source);
    var attribute = {attribute: data};
    var itemList = template(attribute);
    $(className).html(itemList+"</div>");
}

//댓글 컨텐츠 모두 불러오기
function getCommentAllContents(data) {
    updateCommentListUI(data);
    getCommentInputHtml("댓글", "입력", "", ".comment_input_container", "id=btn_openComment");
}

//댓글리스트 받아오기
function getCommentList(boardID, postID, successFunction) {
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
function insertComment(boardID, postID, commentText) {
    $.ajax({
        type: 'POST',
        url: "/boards/" + boardID + "/posts/" + postID + "/comments",
        data: {boardID: boardID, postID: postID, commentContent: commentText},
        error: function () {  //통신 실패시
            alert('통신실패!');
        },
        success: function (data) {
            getCommentList(boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
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
            getCommentList(boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
        }
    });
}

//댓글수정모드
//TODO handlebar 적용
function EditCommentByCommentID(postID, boardID, commentID) {
    var oldText = $('#comment' + commentID).find(".comment_content").html().replace(/<br>/g,"\n");
    data = {oldText:oldText};
    var source = $('#editCommentForm-template').html();
    var template = Handlebars.compile(source);
    var attribute = {attribute: data};
    var itemList = template(attribute);
    $('#comment' + commentID).html(itemList+"</div>");
}

//댓글 수정
function editComment(postID, boardID, commentID, newComment) {
    $.ajax({
        type: 'PUT',
        url: "/boards/" + boardID + "/posts/" + postID + "/comments/" + commentID,
        data: {newComment, newComment},
        error: function () {  //통신 실패시
            alert('통신실패!수정');
        },
        success: function (data) {
            getCommentList(boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
        }
    });
}

//답글 ui 구성
//TODO handlebar 적용
function getReplyListUI(commentID, data) {
    var source = $('#replyList-template').html();
    var template = Handlebars.compile(source);
    var replies = {replies: data};
    var itemList = template(replies);
    $("#reply_container" + commentID).html(itemList);
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
            successFunction(commentID, data);
        }
    });
}

//답글 추가
function insertReply(boardID, postID, commentContent, commentReferencedID,commentReferencedUserID) {
    $.ajax({
        type: 'POST',
        url: "/boards/" + boardID + "/posts/" + postID + "/comments/" + commentReferencedID,
        data: {
            boardID:boardID,
            postID:postID,
            commentContent:commentContent,
            commentReferencedID:commentReferencedID,
            commentReferencedUserID:commentReferencedUserID},
        error: function () {  //통신 실패시
            alert('통신실패!');
        },
        success: function (data) {
            getReplyList(boardID, postID, commentReferencedID, getReplyListUI);//성공하면 댓글목록 갱신
            $('#reply_input_container' + commentReferencedID).html("");
        }
    });
}