/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    viewRecordEvent.js
 */


$(document).on("click", ".read_check", function () {
  var postID = $('#postID').html();
  var boardID = $('#selectedBoardIDinEditor option:selected').attr('data-tab');
  getViewRecords(postID,boardID,loadViewRecordUI)
  //안띄움
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