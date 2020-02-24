/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    viewRecordAjax.js
 */

function getViewRecords(postId,boardId, startIndex,successFunction) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardId}/posts/${postId}/view-records`,
    data :{startIndex:startIndex},
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      if(data.length==0){
        hasRemainData=false;
      }
      successFunction(data);
    }
  });
}