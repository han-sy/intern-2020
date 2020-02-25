/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file replyUI.js
 */

//답글 ui 구성
function getReplyListUI(commentReferencedId, data) {
  $(".more-replies").remove();
  var source = $('#replyList-template').html();
  var template = Handlebars.compile(source);
  var replies = {replies: data};
  var itemList = template(replies);
  $("#reply_container" + commentReferencedId).append(itemList);
  //makeMoreBtn(commentReferencedId);
  showMoreRepliesBtn(commentReferencedId);
}

function makeMoreBtn(commentReferencedId) {
  console.log("makeMoreBtn");
  var source = $('#more-replies-template').html();
  var template = Handlebars.compile(source);
  var item = template();
  $("#reply_container" + commentReferencedId).append(item);
}

function showMoreRepliesBtn(commentReferencedId) {
  var repliesCount = $('#replies_count' + commentReferencedId).html();
  var length = getCountPrintedReplies();

  if (length >= repliesCount) {
    $('.more-replies').remove();
  }
  else{
    if($('.more-replies').length<1){
      makeMoreBtn(commentReferencedId);
    }
  }
}

function replyFormClear() {
  $('.is_reply_input').html("");
}


function updateRepliesCountUI(data,commentReferencedId) {
  $('#replies_count'+commentReferencedId).html(data);
  showMoreRepliesBtn(commentReferencedId);
}