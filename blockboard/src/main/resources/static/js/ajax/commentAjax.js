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
      updateCommentsCountUI(data);
    }
  });
}

function updateRepliesCount(boardID, postID, commentReferencedID) {

  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentReferencedID}/replies/counts`,
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //들어오는 data는 boardDTOlist
      console.log("commentReferencedID : " + commentReferencedID);
      console.log("count : " + data);
      updateRepliesCountUI(data, commentReferencedID);
    }
  });
}

//댓글리스트 받아오기
function getCommentListByPageNum(pageNum, boardID, postID, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/comments`,
    data: {
      pageNumber: pageNum
    },
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      successFunction(data);
    }
  });
}

//댓글 추가
function insertComment(boardID, postID, commentText) {//댓글 임시저장 기능이 추가될수도있어 commentID 파라미터 추가해놓음
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts/${postID}/comments`,
    data: {boardID: boardID, postID: postID, commentContent: commentText},
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      if (functionOn.commentFileAttach) {
        updateIDToFiles("comment",postID, data, boardID);
      }
      getPageList(1, 0, postID, updateCommentPageList);
      //getCommentListByPageNum(1,boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
      updateCommentsCount(boardID, postID);
      CKEDITOR.instances['commentText'].setData("");
      /*      var commentReferencedID = getCommentReferencedIDInReferenceCommentContainer();
            updateRepliesCount(boardID,postID,commentReferencedID);*/
    }
  });
}

//댓글삭제
function deleteCommentByCommentID(postID, boardID, commentID,
    commentReferencedID) {
  $.ajax({
    type: 'DELETE',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentID}`,
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      getPageList(1, 0, postID, updateCommentPageList);
      //getCommentListByPageNum(1,boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
      updateCommentsCount(boardID, postID);
      if (!isNullData(commentReferencedID)) {
        updateRepliesCount(boardID, postID, commentReferencedID);
      }
    }
  });
}

//댓글 수정
function editComment(postID, boardID, commentID, newComment) {
  $.ajax({
    type: 'PUT',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentID}`,
    data: {newComment: newComment},
    error: function () {  //통신 실패시
      alert('통신실패!수정');
    },
    success: function (data) {
      getPageList(1, 0, postID, updateCommentPageList);
      //getCommentListByPageNum(1,boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
    }
  });
}

//답글리스트 받아오기
function getReplyList(boardID, postID, commentReferencedID, startIndex, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentReferencedID}/replies`,
    data: {startIndex: startIndex},
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      updateRepliesCount(boardID, postID, commentReferencedID);
      successFunction(commentReferencedID, data);
    }
  });
}

//답글 추가
function insertReply(boardID, postID, commentContent, commentReferencedID, editorName) {
  //alert(commentReferencedUserID);
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentReferencedID}/replies`,
    data: {
      boardID: boardID,
      postID: postID,
      commentContent: commentContent,
      commentReferencedID: commentReferencedID
    },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      if (data != null) {
        if (functionOn.commentFileAttach) {
          updateIDToFiles("reply",postID, data, boardID, commentReferencedID);
        }
        updateRepliesCount(boardID, postID, commentReferencedID);
      }
      var printedRepliesCount =  getCountPrintedReplies();
      getReplyList(boardID, postID, commentReferencedID,printedRepliesCount, getReplyListUI);
      updateCommentsCount(boardID, postID);
      CKEDITOR.instances[editorName].setData("");
      $('#comment-alarm-modal').modal('hide');
    }
  });
}

function getCommentForShowModal(commentId) {
  $.ajax({
    type: 'GET',
    url: `/boards/0/posts/0/comments/${commentId}`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (comment) {
      showCommentInputModalOfAlarm(comment);
    }
  });
}