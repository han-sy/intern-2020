/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file replyAjax.js
 */

function updateRepliesCount(boardID, postID, commentReferencedID) {

  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentReferencedID}/replies/counts`,
    error: function () {
      alert('통신실패!');
    },
    success: function (data) {
      updateRepliesCountUI(data, commentReferencedID);
    }
  });
}

//답글리스트 받아오기
function getReplyList(boardID, postID, commentReferencedID, startIndex, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentReferencedID}/replies`,
    data: {startIndex: startIndex},
    dataType: "json",
    contentType: 'application/json',
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    complete : function (data) {
      updateRepliesCount(boardID, postID, commentReferencedID);
      successFunction(commentReferencedID, data);
    }
  });
}

//답글 추가
function insertReply(boardID, postID, commentContent, commentReferencedID, editorName) {
  var commentDTO = new Comment(boardID, postID,0, commentReferencedID, commentContent);
  var replyData = JSON.stringify(commentDTO);
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts/${postID}/comments/${commentReferencedID}/replies`,
    data: replyData,
    dataType: "json",
    contentType: 'application/json',
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    complete : function (data) {
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