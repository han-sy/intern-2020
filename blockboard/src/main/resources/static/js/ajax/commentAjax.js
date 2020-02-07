/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentAjax.js
 */
function updateCommentsCount(boardID, postID) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/comments/counts`,
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //들어오는 data는 boardDTOlist
      $("#commentCount").html(data);
    }
  });
}

//댓글리스트 받아오기
function getCommentList(boardID, postID, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/comments`,
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
    url: `/boards/${boardID}/posts/${postID}/comments`,
    data: {boardID: boardID, postID: postID, commentContent: commentText},
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      getCommentList(boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
      $('#commentText').val("");
      updateCommentsCount(boardID, postID);
    }
  });
}

//댓글삭제
function deleteCommentByCommentID(postID, boardID, commentID) {
  $.ajax({
    type: 'DELETE',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentID}`,
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      getCommentList(boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
      updateCommentsCount(boardID, postID);
    }
  });
}

//댓글 수정
function editComment(postID, boardID, commentID, newComment) {
  $.ajax({
    type: 'PUT',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentID}`,
    data: {newComment, newComment},
    error: function () {  //통신 실패시
      alert('통신실패!수정');
    },
    success: function (data) {
      getCommentList(boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
    }
  });
}

//답글리스트 받아오기
function getReplyList(boardID, postID, commentID, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentID}`,
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      successFunction(commentID, data);
    }
  });
}

//답글 추가
function insertReply(boardID, postID, commentContent, commentReferencedID,
    commentReferencedUserID) {
  //alert(commentReferencedUserID);
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentReferencedID}`,
    data: {
      boardID: boardID,
      postID: postID,
      commentContent: commentContent,
      commentReferencedID: commentReferencedID,
      commentReferencedUserID: commentReferencedUserID
    },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      getReplyList(boardID, postID, commentReferencedID, getReplyListUI);//성공하면 댓글목록 갱신
      $('#reply_input_container' + commentReferencedID).html("");
      updateCommentsCount(boardID, postID);
    }
  });
}