/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file commentUI.js
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
function getCommentInputHtml(type, buttonName, tag, className, buttonSelector,
    isReplyInput) {
  data = {
    type: type,
    className,
    buttonName: buttonName,
    tag: tag,
    buttonSelector: buttonSelector,
    isReplyInput: isReplyInput
  };
  var source = $('#commentInputForm-template').html();
  var template = Handlebars.compile(source);
  var attribute = {attribute: data};
  var itemList = template(attribute);
  $(className).html(itemList + "</div>");

  var func = new FunctionOn();

  var add_on = "";
  if (func.postSticker) {
    add_on += ",emoji";
  }
  if (func.postInlineImage) {
    add_on += ",image2";
  }
  var original_config = CKEDITOR.config.plugins;
  CKEDITOR.replace('commentText', {
    height: 150,
    toolbarLocation: 'bottom',
    toolbarGroups: [{name: 'insert'}],
    plugins: original_config + add_on
  });
}

//TODO 댓글과 답글을 분리해도 될것같다.
//댓글 컨텐츠 모두 불러오기
function getCommentAllContents(data) {
  updateCommentListUI(data);
  getCommentInputHtml("댓글", "입력", "", ".comment_input_container",
      "btn_openComment");
  fileFormClear();
}

//댓글수정모드
function editCommentByCommentID(postID, boardID, commentID) {
  var oldText = $('#comment' + commentID).find(".comment_content").html();
  oldText = oldText.replace(/<br>/g, "\n").trim();
  data = {oldText: oldText};
  var source = $('#editCommentForm-template').html();
  var template = Handlebars.compile(source);
  var attribute = {attribute: data};
  var itemList = template(attribute);
  $('#comment' + commentID).html(itemList + "</div>");
}

//답글 ui 구성
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

function replyFormClear() {
  $('.is_reply_input').html("");
}