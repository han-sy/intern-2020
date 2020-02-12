/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file    fileAjax.js
 */

function sendFileToServer(formData,status)
{
  var uploadURL = "/files"; //Upload URL
  var extraData ={}; //Extra Data.
  var jqXHR=$.ajax({
    xhr: function() {
      var xhrobj = $.ajaxSettings.xhr();
      if (xhrobj.upload) {
        xhrobj.upload.addEventListener('progress', function(event) {
          var percent = 0;
          var position = event.loaded || event.position;
          var total = event.total;
          if (event.lengthComputable) {
            percent = Math.ceil(position / total * 100);
          }
          //Set progress
          status.setProgress(percent);
        }, false);
      }
      return xhrobj;
    },
    url: uploadURL,
    type: "POST",
    contentType:false,
    processData: false,
    cache: false,
    data: formData,
    success: function(data){
      status.setProgress(100);
      console.log(data);
      status.filename.attr("data-filename",data);
      //$("#status1").append("File upload Done<br>");
    },
    error:function(request,status,error){
      alert("code = "+ request.status + " message = " + request.responseText + " error = " + error); // 실패 시 처리
    },
    complete:function(data){
    }
  });

  status.setAbort(jqXHR);
}

function updatePostIDToFiles(postID) {
  var fileList = getAttachedFileList(postID);
  console.log(fileList);
  $.ajax({
    type: 'PUT',
    url: `/files`,
    data: JSON.stringify(fileList),
    dataType: "json",
    contentType: 'application/json',
    error: function (error, msg) {  //통신 실패시
      errorFunction(error);
    },
    success: function () {
      alert("게시글 작성완료 :"+postID);
    }
  });
}

function getFileList(postID,successFunction) {
  $.ajax({
    type: 'GET',
    url: `/files`,
    data: {postID:postID},
    dataType: "json",
    contentType: 'application/json',
    error: function (error, msg) {  //통신 실패시
      errorFunction(error);
    },
    success: function (data) {
      successFunction(data);
    }
  });
}

function deleteFile(storedFileName,obj,successFunction) {
  console.log("storedFileName : "+storedFileName);
  $.ajax({
    type: 'DELETE',
    url: `/files`,
    data: {storedFileName:storedFileName},
    error: function (error, msg) {  //통신 실패시
      errorFunction(error);
    },
    success: function () {
      successFunction(obj);
    }
  });
}