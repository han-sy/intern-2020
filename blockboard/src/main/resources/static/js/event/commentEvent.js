
function clickSendCommentBtn(){
    var postID = $("#postID").html();
    var boardID = getCurrentBoardID();
    var commentText = $('#commentText').val();
      if(commentText ==""){
        alert("내용을 입력하세요.");
        return;
      }
      $(function () {
        updateComment(boardID,postID,commentText);
      });
      //$('#config_container').html("");

}