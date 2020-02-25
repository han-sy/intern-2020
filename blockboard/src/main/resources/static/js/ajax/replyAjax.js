/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file replyAjax.js
 */

function updateRepliesCount(boardId, postId, commentReferencedId) {

  $.ajax({
    type: 'GET',
    url: `/boards/${boardId}/posts/${postId}/comments/${commentReferencedId}/replies/counts`,
    error: function () {
      alert('통신실패!');
    },
    success: function (data) {
      updateRepliesCountUI(data, commentReferencedId);
    }
  });
}

//답글리스트 받아오기
function getReplyList(boardId, postId, commentReferencedId, startIndex, successFunction) {
  console.log("startIndex",startIndex);

  $.ajax({
    type: 'GET',
    url: `/boards/${boardId}/posts/${postId}/comments/${commentReferencedId}/replies`,
    data: {startIndex: startIndex},
    error: function (error) {  //통신 실패시
      errorFunction(error);
    },
    success : function (data) {
      updateRepliesCount(boardId, postId, commentReferencedId);
      successFunction(commentReferencedId, data);
    }
  });
}

//답글 추가
function insertReply(boardId, postId, commentContent, commentReferencedId, editorName) {
  var commentDTO = new Comment(boardId, postId,0, commentReferencedId, commentContent);
  var replyData = JSON.stringify(commentDTO);
  $.ajax({
    type: 'POST',
    url: `/boards/${boardId}/posts/${postId}/comments/${commentReferencedId}/replies`,
    data: replyData,
    dataType: "json",
    contentType: 'application/json',
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    complete : function (data) {
      if (data != null) {
        if (functionOn.commentFileAttach) {
          updateIDToFiles("reply",postId, data, boardId, commentReferencedId);
        }
        updateRepliesCount(boardId, postId, commentReferencedId);
      }
      var printedRepliesCount =  getCountPrintedReplies();
      getReplyList(boardId, postId, commentReferencedId,printedRepliesCount, getReplyListUI);
      updateCommentsCount(boardId, postId);
      CKEDITOR.instances[editorName].setData("");
      $('#comment-alarm-modal').modal('hide');
    }
  });
}