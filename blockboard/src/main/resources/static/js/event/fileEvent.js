/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file fileEvent.js
 */
//파일 열고닫기 텍스트 바꾸기
$(document).on('click', '.file_drag_and_drop_btn', function () {
  var switchText = $(this);
  if (switchText.html() == "열기") {
    openDragAndDropForm();
    switchText.html("닫기");
  } else if (switchText.html() == "닫기") {
    var isAcceptance = confirm("첨부했던 파일들이 지워집니다 계속하시겠습니까?");
    if (isAcceptance) {
      $(function () {
        $("#file_drop_container").html("");
        switchText.html("열기");
      });
    }

  } else {
    changedDataError();
  }
});

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