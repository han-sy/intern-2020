/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file replyUI.js
 */

//답글 ui 구성
function getReplyListUI(commentReferencedId, data) {
  $(".more-replies").remove();
  let source = $('#replyList-template').html();
  let template = Handlebars.compile(source);
  let replies = {replies: data};
  let itemList = template(replies);
  $("#reply_container" + commentReferencedId).append(itemList);
  showMoreRepliesBtn(commentReferencedId);
}

function makeMoreBtn(commentReferencedId) {
  let source = $('#more-replies-template').html();
  let template = Handlebars.compile(source);
  let item = template();
  $("#reply_container" + commentReferencedId).append(item);
}

function showMoreRepliesBtn(commentReferencedId) {
  let repliesCount = $('#replies_count' + commentReferencedId).html();
  let length = getCountPrintedReplies();

  if (length >= repliesCount) {
    $('.more-replies').remove();
  } else {
    if ($('.more-replies').length < 1) {
      makeMoreBtn(commentReferencedId);
    }
  }
}

function replyFormClear() {
  $('.is_reply_input').html("");
}

function updateRepliesCountUI(data, commentReferencedId) {
  $('#replies_count' + commentReferencedId).html(data);
  showMoreRepliesBtn(commentReferencedId);
}