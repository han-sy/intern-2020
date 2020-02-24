/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    viewRecordAjax.js
 */

function getViewRecords(postID,boardID, startIndex,successFunction) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/view-records`,
    data :{startIndex:startIndex},
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      console.log("data : ",data);
      console.log("data length :"+data.length);
      if(data.length==0){
        hasRemainData=false;
      }
      successFunction(data);
    }
  });
}