/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file replyUI.js
 */

//답글 ui 구성
function getReplyListUI(commentReferencedID, data) {
  $(".more-replies").remove();
  var source = $('#replyList-template').html();
  var template = Handlebars.compile(source);
  var replies = {replies: data};
  var itemList = template(replies);
  $("#reply_container" + commentReferencedID).append(itemList);
  //makeMoreBtn(commentReferencedID);
  showMoreRepliesBtn(commentReferencedID);
}

function makeMoreBtn(commentReferencedID) {
  console.log("makeMoreBtn");
  var source = $('#more-replies-template').html();
  var template = Handlebars.compile(source);
  var item = template();
  $("#reply_container" + commentReferencedID).append(item);
}

function showMoreRepliesBtn(commentReferencedID) {
  var repliesCount = $('#replies_count' + commentReferencedID).html();
  var length = getCountPrintedReplies();


  console.log("commentReferencedID : "+commentReferencedID);
  console.log("showMoreRepliesBtn -> replyCount: "+repliesCount+","+length);
  if (length >= repliesCount) {
    $('.more-replies').remove();
  }
  else{
    if($('.more-replies').length<1){
      makeMoreBtn(commentReferencedID);
    }
  }
}

function replyFormClear() {
  $('.is_reply_input').html("");
}


function updateRepliesCountUI(data,commentReferencedID) {
  console.log("repliesCount : " + data);
  $('#replies_count'+commentReferencedID).html(data);
  console.log("!!!!commentReferencedID : "+commentReferencedID);
  showMoreRepliesBtn(commentReferencedID);
}