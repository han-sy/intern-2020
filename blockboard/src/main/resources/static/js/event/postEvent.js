/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postEvent.js
 */
var autosave = null;

// 현재 선택된 게시판 id 가져온다.
function getCurrentBoardID() {
  var tabs = $('#tab_id').children();
  var boardID = 0;

  $.each(tabs, function () {
    if ($(this).hasClass("active_tab")) {
      boardID = $(this).attr("data-tab");
    }
  });
  return boardID;
}

// '글쓰기' 버튼 이벤트
$(document).on("click", "#btn_write", function () {
  editorAreaCreate("insert");
  initBoardIdOptionInEditor(getCurrentBoardID());
  var funcionOn = new FunctionOn();
  if (funcionOn.isFileAttachOn()) {
    console.log("파일 첨부 on");
    openFileAttachForm();
  }
});

// '임시저장' 이벤트 함수
function tempsaveFunction() {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var postID = $('#editor_postID').html();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');
  // 제목 & 내용 비었는지 검사
  if (checkEmpty()) {
    insertTempPost(boardID, postID, postTitle, postContent, true);
    refreshPostList();
  }
}

// '저장' 이벤트 함수
function postFunction() {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var postID = $('#editor_postID').html();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');
  // 제목 & 내용 비었는지 검사
  if (checkEmpty()) {
    // 게시글 ID가 존재하지 않으면? 바로 저장
    if (typeof postID == "undefined") {
      insertPost(boardID, postTitle, postContent);
    }
    // 임시 or 자동 저장된 글을 한번 더 '저장' 버튼을 누를 때
    else {
      insertTempPost(boardID, postID, postTitle, postContent, false);
      getPageList(1, getCurrentBoardID(), updatePageList);
    }
    editorClear();
  }
}

// 게시글 제목 or 내용 비었는지 검사
function checkEmpty() {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  if (postTitle.trim() == "") {
    alert("게시글 제목을 입력해주세요.");
    return false;
  }
  if (postContent.trim() == "") {
    alert("게시글 내용을 입력해주세요.");
    return false;
  }
  // 서버에 나중에 적용하기
  if (!isValidLength(postTitle, 150)) {
    alert("게시글 제목 길이를 초과하였습니다.");
    return false;
  }
  if (!isValidLength(postContent, 4000)) {
    alert("게시글 내용 길이를 초과하였습니다.");
    return false;
  } else {
    return true;
  }
}

// '수정' 버튼 클릭 후 '수정하기' 버튼 이벤트
function postUpdate() {
  var postID = $("#editor_postID").html();
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');
  updatePost(boardID, postID, postTitle, postContent);
}

// 게시글 조회 후 '수정' 버튼 이벤트
function postUpdateFunction() {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  postClear();
  editorAreaCreate("modify");
  var post_button = $('#btn_post');
  post_button.html('수정하기'); // 게시글 올리기 버튼 텍스트 변경
  post_button.attr('onclick', 'javascript:postUpdate()');
  setTimeout(function () {
    loadPost(boardID, postID)
  }, 100); // 에디터로 게시글 정보 불러옴.
}

// 게시글 조회 후 삭제 버튼 이벤트 -> 휴지통으로
function movePostToTrashBox() {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  alert("휴지통으로 이동됩니다.");
  temporaryDeletePost(boardID, postID);
}

// 게시글 검색 버튼 이벤트
function search() {
  var option = $('#search_option option:selected').attr('value');
  var keyword = $('#search_keyword');
  searchPost(option, keyword);
}

// 작성 취소 버튼 이벤트
function writeCancel() {
  if (confirm("작성된 내용이 저장되지 않을 수도 있습니다. 이동하시겠습니까?") == true) {
    editorClear();
  }
}

// 자동 저장 on
function on_autosave() {
  autosave = setInterval(function () {
    tempsaveFunction()
  }, 1000 * (60 * 3));
}

// 자동 저장 off
function off_autosave() {
  clearInterval(autosave);
}

// 임시저장 게시물 클릭 이벤트
function clickTempPostEvent(evt) {
  var postID = evt.getAttribute("data-post");
  postClear();
  editorAreaCreate("insert");
  var btn_cancel = $('#btn_cancel');
  btn_cancel.html("삭제");
  btn_cancel.attr('onclick', 'javascript:clickCompleteDeletePost()');
  addPostIdToEditor(postID);
  getTempPost(postID);
}

// 임시저장 게시글 삭제 이벤트 -> 휴지통 거치지 않고 바로 삭제됨.
function clickTempDeletePost() {
  var postID = $('#editor_postID').html();
  var boardID = getCurrentBoardID();
  completeDeletePost(boardID, postID);
}

// length Check 이벤트
function isValidLength(str, limit) {
  if (getByteLength(str)[0] <= limit) {
    return true;
  } else {
    return false;
  }
}

// 휴지통 게시물 클릭 이벤트
function clickRecyclePostEvent(evt) {
  var postID = evt.getAttribute("data-post");
  var boardID = getCurrentBoardID();
  getRecyclePost(postID, boardID);
}

// 휴지통 게시글 완전 삭제 이벤트
function clickCompleteDeletePost() {
  var postID = $('#postID').html();
  var boardID = getCurrentBoardID();
  if (confirm("삭제하면 복구되지 않습니다. 삭제하시겠습니까?")) {
    completeDeletePost(boardID, postID);
  }
}

// 휴지통 게시글 복원 이벤트
function clickRestorePost() {
  var postID = $('#postID').html();
  if (confirm("원래 게시판으로 복원하시겠습니까?")) {
    restorePost(postID);
  }
}

//파일 열고닫기 텍스트 바꾸기
$(document).on('click', '.file_drag_and_drop_btn', function () {
  var switchText = $(this);
  if (switchText.html() == "열기") {
    openDragAndDropForm();
    switchText.html("닫기");
  } else if (switchText.html() == "닫기") {
    $("#file_drop_container").html("");
    switchText.html("열기");
  } else {
    changedDataError();
  }
});

// 파일 리스트 번호
var fileIndex = 0;
// 등록할 전체 파일 사이즈
var totalFileSize = 0;
// 파일 리스트
var fileList = new Array();
// 파일 사이즈 리스트
var fileSizeList = new Array();
// 등록 가능한 파일 사이즈 MB
var uploadSize = 50;
// 등록 가능한 총 파일 사이즈 MB
var maxUploadSize = 500;

$(document).ready(function(){
  var objDragAndDrop = $(".dragAndDropDiv");

  $(document).on("dragenter",".dragAndDropDiv",function(e){
    e.stopPropagation();
    e.preventDefault();
    $(this).css('border', '2px solid #0B85A1');
  });
  $(document).on("dragover",".dragAndDropDiv",function(e){
    e.stopPropagation();
    e.preventDefault();
  });
  $(document).on("drop",".dragAndDropDiv",function(e){

    $(this).css('border', '2px dotted #0B85A1');
    e.preventDefault();
    var files = e.originalEvent.dataTransfer.files;

    handleFileUpload(files);
  });

  function handleFileUpload(files)
  {
    for (var i = 0; i < files.length; i++)
    {
      var fd = new FormData();
      fd.append('file', files[i]);

      var status = new createStatusbar($(".dragAndDropDiv")); //Using this we can set progress.
      status.setFileNameSize(files[i].name,files[i].size);
      sendFileToServer(fd,status);

    }
  }

  var rowCount=0;
  function createStatusbar(obj){

    rowCount++;
    var row="odd";
    if(rowCount %2 ==0) row ="even";
    this.statusbar = $("<div class='statusbar "+row+"'></div>");
    this.filename = $("<div class='filename'></div>").appendTo(this.statusbar);
    this.size = $("<div class='filesize'></div>").appendTo(this.statusbar);
    this.progressBar = $("<div class='progressBar'><div></div></div>").appendTo(this.statusbar);
    this.abort = $("<div class='abort'>중지</div>").appendTo(this.statusbar);

    obj.after(this.statusbar);

    this.setFileNameSize = function(name,size){
      var sizeStr="";
      var sizeKB = size/1024;
      if(parseInt(sizeKB) > 1024){
        var sizeMB = sizeKB/1024;
        sizeStr = sizeMB.toFixed(2)+" MB";
      }else{
        sizeStr = sizeKB.toFixed(2)+" KB";
      }

      this.filename.html(name);
      this.size.html(sizeStr);
    }

    this.setProgress = function(progress){
      var progressBarWidth =progress*this.progressBar.width()/ 100;
      this.progressBar.find('div').animate({ width: progressBarWidth }, 10).html(progress + "% ");
      if(parseInt(progress) >= 100)
      {
        this.abort.hide();
      }
    }

    this.setAbort = function(jqxhr){
      var sb = this.statusbar;
      this.abort.click(function()
      {
        jqxhr.abort();
        sb.hide();
      });
    }
  }

  function sendFileToServer(formData,status)
  {
    var uploadURL = "/fileUpload"; //Upload URL
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

        //$("#status1").append("File upload Done<br>");
      }
    });

    status.setAbort(jqXHR);
  }

});