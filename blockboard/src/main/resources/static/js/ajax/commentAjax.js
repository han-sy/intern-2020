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
function insertComment(boardID, postID, commentContent) {//댓글 임시저장 기능이 추가될수도있어 commentID 파라미터 추가해놓음
  var commentDTO = new Comment(boardID,postID,0,0,commentContent);

  console.log("aaa"+","+boardID+","+postID+","+commentContent)
  var commentData = JSON.stringify(commentDTO);
  console.log("commentData",commentData);
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts/${postID}/comments`,
    data: commentData,
    dataType: "json",
    contentType: 'application/json',
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    complete : function (data) {
      if (functionOn.commentFileAttach) {
        updateIDToFiles("comment",postID, data, boardID);
      }
      getPageList(1, 0, postID, updateCommentPageList);
      updateCommentsCount(boardID, postID);
      CKEDITOR.instances['commentText'].setData("");

    }
  });
}

//댓글삭제
function deleteCommentByCommentID(postID, boardID, commentID,
    commentReferencedID) {
  $.ajax({
    type: 'DELETE',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentID}`,
    data: {commentID:commentID},
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
  var commentDTO = new Comment(boardID,postID,commentID,0,newComment);
  var commentData = JSON.stringify(commentDTO);
  console.log("commentData" ,commentData);
  $.ajax({
    type: 'PUT',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentID}`,
    data: commentData,
    dataType: "json",
    contentType: 'application/json',
    error: function () {  //통신 실패시
      alert('통신실패!수정');
    },
    complete: function (data) {
      getPageList(1, 0, postID, updateCommentPageList);
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