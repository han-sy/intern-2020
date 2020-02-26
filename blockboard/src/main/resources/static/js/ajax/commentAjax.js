/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentAjax.js
 */

function updateCommentsCount(boardId, postId) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardId}/posts/${postId}/comments/counts`,
    error: function (error) {  //통신 실패시
      errorFunction(error);
    },
    success: function (data) {    //들어오는 data는 boardDTOlist
      updateCommentsCountUI(data);
    }
  });
}

//댓글리스트 받아오기
function getCommentListByPageNum(pageNum, boardId, postId, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardId}/posts/${postId}/comments`,
    data: {
      pageNumber: pageNum
    },
    error: function (error) {  //통신 실패시
      errorFunction(error);
    },
    success: function (data) {
      successFunction(data);
    }
  });
}

//댓글 추가
function insertComment(boardId, postId, commentContent) {//댓글 임시저장 기능이 추가될수도있어 commentId 파라미터 추가해놓음
  let commentDTO = new Comment(boardId,postId,0,0,commentContent);
  let commentData = JSON.stringify(commentDTO);

  $.ajax({
    type: 'POST',
    url: `/boards/${boardId}/posts/${postId}/comments`,
    data: commentData,
    dataType: "json",
    contentType: 'application/json',
    error: function (error) {  //통신 실패시
      errorFunction(error);
    },
    complete : function (data) {
      if (functionOn.commentFileAttach) {
        updateIDToFiles("comment",postId, data, boardId);
      }
      getPageList(1, 0, postId, updateCommentPageList);
      updateCommentsCount(boardId, postId);
      CKEDITOR.instances['commentText'].setData("");

    }
  });
}

//댓글삭제
function deleteCommentByCommentId(postId, boardId, commentId,
    commentReferencedId) {
  $.ajax({
    type: 'DELETE',
    url: `/boards/${boardId}/posts/${postId}/comments/${commentId}`,
    data: {commentId:commentId},
    error: function (error) {  //통신 실패시
      errorFunction(error);
    },
    success: function () {
      getPageList(1, 0, postId, updateCommentPageList);
      updateCommentsCount(boardId, postId);
      if (!isNullData(commentReferencedId)) {
        updateRepliesCount(boardId, postId, commentReferencedId);
      }
    }
  });
}

//댓글 수정
function editComment(postId, boardId, commentId, newComment) {
  let commentDTO = new Comment(boardId,postId,commentId,0,newComment);
  let commentData = JSON.stringify(commentDTO);
  $.ajax({
    type: 'PUT',
    url: `/boards/${boardId}/posts/${postId}/comments/${commentId}`,
    data: commentData,
    dataType: "json",
    contentType: 'application/json',
    error: function (error) {  //통신 실패시
      errorFunction(error)
    },
    complete: function () {
      getPageList(1, 0, postId, updateCommentPageList);
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