/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file    fileAjax.js
 */

function operateProgressBar(status) {
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
}

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
      return operateProgressBar(status);
    },
    url: uploadURL,
    type: "POST",
    contentType: false,
    processData: false,
    cache: false,
    data: formData,
    success: function (data) {
      status.setProgress(100);
      status.filename.attr("data-filename", data);
    },
    error: function (request, status, error) {
      alert("code = " + request.status + " message = " + request.responseText
          + " error = " + error); // 실패 시 처리
    }
  });

  status.setAbort(jqXHR);
}

function resetBoardIdAndPostId(postId, boardId) {
  if (isNullData(postId)) {
    postId = $('#postId').html();
  }
  if (isNullData(boardId)) {
    boardId = getCurrentActiveBoardId();
  }
  return {postId, boardId};
}

function checkCommentFileForGetCommentPageList(editor, commentReferencedId, postId) {
  //댓글인경우(답글말고)
  if (editor == "comment" && isNullData(commentReferencedId)) {
    getPageList(1, 0, postId, updateCommentPageList);
  }
}

/**
 * 첨부된 에디터에 id를 파일 디비에 업데이트
 */
function updateIDToFiles(editor, postId, commentId, boardId,
    commentReferencedId) {
  var fileList = getAttachedFileList(postId, commentId);
  $.ajax({
    type: 'PUT',
    url: `/files`,
    data: JSON.stringify(fileList),
    dataType: "json",
    contentType: 'application/json',
    error: function (error, msg) {  //통신 실패시
      errorFunction(error);
    }, complete() {
      const __ret = resetBoardIdAndPostId(postId, boardId);
      postId = __ret.postId;
      boardId = __ret.boardId;
      checkCommentFileForGetCommentPageList(editor, commentReferencedId, postId);
      updateCommentsCount(boardId, postId);
      fileFormClear();
    }
  });
}

/**
 * postId에 일치하는 파일리스트 반환
 */
function getFileList(postId, commentId, obj, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/files`,
    data: {postId: postId, commentId: commentId},
    error: function (error) {  //통신 실패시
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
  $.ajax({
    type: 'DELETE',
    url: `/files`,
    data: {storedFileName: storedFileName},
    error: function (error, msg) {  //통신 실패시
      if (error.status == 409) {
        obj.remove();
      }
      errorFunction(error);
    },
    success: function () {
      successFunction(obj);
    }
  });
}
