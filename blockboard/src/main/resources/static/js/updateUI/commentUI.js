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
  var boardId = getBoardIdInPost();
  var postId = getPostIdInPost();
}

//댓글 inputform 받아오기
function getCommentInputHtml(type, buttonName, tag, className, buttonSelector,
    isReplyInput, editorName) {
  data = {
    type: type,
    className,
    buttonName: buttonName,
    tag: tag,
    buttonSelector: buttonSelector,
    isReplyInput: isReplyInput,
    editorName: editorName
  };
  var source = $('#commentInputForm-template').html();
  var template = Handlebars.compile(source);
  var attribute = {attribute: data};
  var itemList = template(attribute);
  $(className).append(itemList + "</div>");

  createCommentEditor(editorName, type, tag);
}

function extractPluginAtFunction() {
  let add_on = "";
  if (functionOn.commentSticker) {
    add_on += ",emoji";
  }
  if (functionOn.commentInlineImage) {
    add_on += ",image2";
  }
  return add_on;
}

function createCommentEditor(editorName, type, tag, oldText) {
  let original_config = CKEDITOR.config.plugins;
  let add_on = extractPluginAtFunction();
  CKEDITOR.replace(editorName, {
    height: 200,
    toolbarLocation: 'bottom',
    toolbarGroups: [{name: 'insert'}],
    plugins: original_config + add_on,
    on: {
      instanceReady: function () {
        if (type === "답글") {
          CKEDITOR.instances[editorName].insertHtml(tag, 'unfiltered_html');
        }
        if (type === "수정") {
          CKEDITOR.instances[editorName].setData(oldText);
        }
      }
    }
  });
}

//댓글수정모드
function editCommentByCommentId(postId, boardId, commentId) {
  var oldText = $('#comment' + commentId).find(".comment_content").html();
  data = {oldText: oldText};
  var source = $('#editCommentForm-template').html();
  var template = Handlebars.compile(source);
  var attribute = {attribute: data};
  var itemList = template(attribute);
  $('#comment' + commentId).html(itemList + "</div>");
  createCommentEditor("commentEditText", "수정", '', oldText);
}


function updateCommentsCountUI(data) {
  console.log("commentsCount : " + data);
  $(".commentCount").html(data);
}
