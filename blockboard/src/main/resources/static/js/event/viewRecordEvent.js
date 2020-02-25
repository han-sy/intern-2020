/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    viewRecordEvent.js
 */

var hasRemainData;
$(document).on("click", ".read_check", function () {
  let postId = getPostIdInPost();
  let boardId = getBoardIdInPost();
  $('.modal-body-viewRecordList-container').html("");
  hasRemainData = true;
  getViewRecords(postId, boardId, 0, loadViewRecordUI);
  //안띄움

});
$('.modal-read-user-container').on("scroll", function () {
  let scrollTop = $(this).scrollTop();
  let innerHeight = $(this).innerHeight();
  let scrollHeight = $(this).prop('scrollHeight');
  let resultCount = $(".view_record_data").length;
  let postId = getPostIdInPost();
  let boardId = getBoardIdInPost();
  if (scrollTop + innerHeight >= scrollHeight - 0.25 && hasRemainData) {
    console.log(scrollTop + innerHeight - scrollHeight);
    getViewRecords(postId, boardId, resultCount, loadViewRecordUI);
  }
});

function isWriter() {
  var writerId = getWriterId($(this));
  var user = new User();
  if (user.getUserId == writerId) {
    return true;
  } else {
    return false;
  }

}

function getWriterId(obj) {
  return obj.closest('.postcontent').find('.writer_info').attr("data-id");

}

