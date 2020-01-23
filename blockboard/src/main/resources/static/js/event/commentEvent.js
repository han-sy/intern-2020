/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentEvent.js
 */
function clickSendCommentBtn() {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  var commentText = $('#commentText').val();
  if (commentText == "") {
    alert("내용을 입력하세요.");
    return;
  }
  $(function () {
    updateComment(boardID, postID, commentText);
  });
  //$('#config_container').html("");

}

//TODO 삭제이벤트 구현하기
//삭제클릭한곳의 부모의 div의 id뽑게했음
$(document).on('click', '#delete_comment', function () {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();

  var deleteDivID = $(this).parents("div").parents("div").attr("id");
  var commentID = deleteDivID.substring(7);

  $(function () {
    deleteCommentByCommentID(postID, boardID, commentID);
  });
})

function clickCommentDeleteBtn(clickObj) {

  console.log(clickObj);
  console.log($(this));
  console.log(clickObj.parents("div").parents("div"));

}