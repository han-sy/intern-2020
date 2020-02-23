/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    viewRecordEvent.js
 */


$(document).on("click", ".read_check", function () {
  var postID = $('#postID').html();
  var boardID = $('#selectedBoardIDinEditor option:selected').attr('data-tab');
  getViewRecords(postID,boardID,0,loadViewRecordUI);
  //안띄움
});

$('.modal-read-user-container').on("scroll",function () {
  var scrollTop = $(this).scrollTop();
  var innerHeight = $(this).innerHeight();
  var scrollHeight = $(this).prop('scrollHeight');
  var resultCount =  $(".view_record_data").length;
  var postID = getPostIDInPost();
  var boardID = getBoardIDInPost();
  if (scrollTop + innerHeight >= scrollHeight-1){
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

