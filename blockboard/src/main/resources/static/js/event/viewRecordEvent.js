/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    viewRecordEvent.js
 */

var hasRemainData;
$(document).on("click", ".read_check", function () {
  var postID = $('#postID').html();
  var boardID = $('#selectedBoardIDinEditor option:selected').attr('data-tab');
  $('.modal-body-viewRecordList-container').html("");
  getViewRecords(postID,boardID,0,loadViewRecordUI);
  //안띄움
  hasRemainData=true;
  console.log(hasRemainData);
});
$('.modal-read-user-container').on("scroll",function () {
  var scrollTop = $(this).scrollTop();
  var innerHeight = $(this).innerHeight();
  var scrollHeight = $(this).prop('scrollHeight');
  var resultCount =  $(".view_record_data").length;
  var postID = getPostIDInPost();
  var boardID = getBoardIDInPost();
  if (scrollTop + innerHeight >= scrollHeight-1 && hasRemainData){
    console.log("resultCount : "+resultCount);
    getViewRecords(postID,boardID,resultCount,loadViewRecordUI);
  }
});


function isWriter(){
  var writerId = getWriterId($(this));
  var user = new User();
  if(user.getUserID ==writerId){
    return true;
  }else{
    return false;
  }

}

function getWriterId(obj){
  return obj.closest('.postcontent').find('.writer_info').attr("data-id");

}

