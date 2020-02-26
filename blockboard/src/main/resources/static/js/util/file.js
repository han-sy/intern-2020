/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file file.js
 */

function handleFileUpload(files) {
  for (let i = 0; i < files.length; i++) {
    let fd = new FormData();
    fd.append('file', files[i]);
    let status = new createStatusbar($(".upload_list_start_point"));
    status.setFileNameSize(files[i].name, files[i].size);
    sendFileToServer(fd, status);
  }
}

var rowCount = 0;

function getFileSize(size) {
  let sizeStr = "";
  let sizeKB = size / 1024;
  if (parseInt(sizeKB) > 1024) {
    let sizeMB = sizeKB / 1024;
    sizeStr = sizeMB.toFixed(2) + " MB";
  } else {
    sizeStr = sizeKB.toFixed(2) + " KB";
  }
  return sizeStr;
}

/**
 * 첨부할 파일 리스트가져오기
 */
function getAttachedFileList(postId, commentId) {
  let fileList = new Array();
  $(".filename").each(function () {
    let fileData = new Object();
    if (isNullData(commentId)) {
      fileData.postId = postId;
      fileData.commentId = 0;
    }
    else{
      fileData.postId = 0;
      fileData.commentId = commentId;
    }
    fileData.storedFileName = $(this).attr("data-filename");
    fileList.push(fileData);

  });
  return fileList;
}

function deleteAllAttachedFile() {
  let fileList = getAttachedFileList();
  $.each(fileList, function (index, item) {
    deleteFile(item.storedFileName);
  })
}

function createStatusbar(obj) {
  this.statusbar = $("<div class='statusbar'></div>");
  this.filename = $("<div class='filename' ></div>").appendTo(this.statusbar);
  this.size = $("<div class='filesize'></div>").appendTo(this.statusbar);
  this.progressBar = $("<div class='progressBar'><div></div></div>").appendTo(
      this.statusbar);
  this.abort = $("<div class='abort'>중지</div>").appendTo(this.statusbar);
  this.delete = $(
      "<a class='delete-statusbar file_upload_btn text-success font-weight-bold text-button'>삭제</a>").appendTo(
      this.statusbar);

  obj.after(this.statusbar);

  this.setFileNameSize = function (name, size) {
    let sizeStr = getFileSize(size);
    this.filename.html(name);
    this.size.html(sizeStr);
  };

  this.setProgress = function (progress) {
    let progressBarWidth = progress * this.progressBar.width() / 100;
    this.progressBar.find('div').animate({width: progressBarWidth}, 10).html(
        progress + "% ");
    if (parseInt(progress) >= 100) {
      this.abort.hide();
    }
  };

  this.setAbort = function (jqxhr) {
    let sb = this.statusbar;
    this.abort.click(function () {
      jqxhr.abort();
      sb.hide();
    });
  };
}

