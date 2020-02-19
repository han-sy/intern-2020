/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file    fileAjax.js
 */

/**
 * 서버에 파일을 전송
 * @param formData 파일 데이터
 * @param status statusbar ui를 위한 객체
 */
function sendFileToServer(formData, status) {
  var uploadURL = "/files"; //Upload URL
  var extraData = {}; //Extra Data.
  var jqXHR = $.ajax({
    xhr: function () {
      var xhrobj = $.ajaxSettings.xhr();
      if (xhrobj.upload) {
        xhrobj.upload.addEventListener('progress', function (event) {
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
    contentType: false,
    processData: false,
    cache: false,
    data: formData,
    success: function (data) {
      status.setProgress(100);
      console.log(data);
      status.filename.attr("data-filename", data);
      //$("#status1").append("File upload Done<br>");
    },
    error: function (request, status, error) {
      alert("code = " + request.status + " message = " + request.responseText
          + " error = " + error); // 실패 시 처리
    },
    complete: function (data) {
    }
  });

  status.setAbort(jqXHR);
}

/**
 * postID 업데이트
 */
function updateIDToFiles(postID, commentID, boardID, commentReferencedID) {
  var fileList = getAttachedFileList(postID, commentID);
  console.log("insertComment", fileList);
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

    },
    complete() {
      if (isNullData(postID)) {
        postID = $('#postID').html();
      }
      if (isNullData(boardID)) {
        boardID = getCurrentBoardID();
      }
      if (isNullData(commentReferencedID)) {
        getCommentList(boardID, postID, updateCommentListUI);//성공하면 댓글목록 갱신
      } else {
        getReplyList(boardID, postID, commentReferencedID, getReplyListUI);//성공하면 댓글목록 갱신
        $('#reply_input_container' + commentReferencedID).html("");
      }

      getCommentInputHtml("댓글", "입력", "", ".comment_input_container",
          "btn_openComment");
      updateCommentsCount(boardID, postID);
      fileFormClear();
    }
  });
}

/**
 * postID에 일치하는 파일리스트 반환
 */
function getFileList(postID, commentID, obj, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/files`,
    data: {postID: postID, commentID: commentID},
    dataType: "json",
    contentType: 'application/json',
    error: function (error, msg) {  //통신 실패시
      errorFunction(error);
    },
    success: function (data) {
      successFunction(data, obj);
    }
  });
}

/**
 * 파일 삭제
 */
function deleteFile(storedFileName, obj, successFunction) {
  console.log("storedFileName : " + storedFileName);
  $.ajax({
    type: 'DELETE',
    url: `/files`,
    data: {storedFileName: storedFileName},
    error: function (error, msg) {  //통신 실패시
      if(error.status==409){
        obj.remove();
      }
      errorFunction(error);
    },
    success: function () {
      successFunction(obj);
    }
  });
}
