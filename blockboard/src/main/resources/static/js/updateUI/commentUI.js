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
  var boardID = getBoardIDInPost();
  var postID = getPostIDInPost();
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

  var add_on = "";
  if (functionOn.commentSticker) {
    add_on += ",emoji";
  }
  if (functionOn.commentInlineImage) {
    add_on += ",image2";
  }

  var original_config = CKEDITOR.config.plugins;

  CKEDITOR.replace('commentText', {
    height: 200,
    toolbarLocation: 'bottom',
    toolbarGroups: [{name: 'insert'}],
    plugins: original_config + add_on,
    on: {
      instanceReady: function () {
        if (type === "답글") {
          CKEDITOR.instances['commentText'].insertHtml(tag, 'unfiltered_html');
        }
      }
    }
  }
  );
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
function getReplyListUI(commentReferencedID, data) {
  $(".more-replies").remove();
  var source = $('#replyList-template').html();
  var template = Handlebars.compile(source);
  var replies = {replies: data};
  var itemList = template(replies);
  console.log("getReplyListUI->commentReferencedID : "+commentReferencedID);
  $("#reply_container" + commentReferencedID).append(itemList);
  console.log("@@@@commentReferencedID : "+commentReferencedID);
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

function updateCommentsCountUI(data) {
  console.log("commentsCount : " + data);
  $(".commentCount").html(data);
}

function updateRepliesCountUI(data,commentReferencedID) {
  console.log("repliesCount : " + data);
  $('#replies_count'+commentReferencedID).html(data);
  console.log("!!!!commentReferencedID : "+commentReferencedID);
  showMoreRepliesBtn(commentReferencedID);
}