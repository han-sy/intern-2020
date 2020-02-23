/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    viewRecordAjax.js
 */

function getViewRecords(postID,boardID, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}/view-records`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      console.log("data : ",data);
      successFunction(data);
    }
  });
}