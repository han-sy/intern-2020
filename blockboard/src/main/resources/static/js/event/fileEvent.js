/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file fileEvent.js
 */

/**
 * 파일 첨부버튼 클릭시
 */
$(document).on('click', '.open_file_form_btn', function () {

  fileFormClear();
  openFileAttachForm("","",$(this).closest(".commentHtml"));
});

//파일 열고닫기 텍스트 바꾸기
$(document).on('click', '.file_drag_and_drop_btn', function () {
  var switchText = $(this);
  if (switchText.html() == "열기") {
    openDragAndDropForm($(this));
    switchText.html("닫기");
  } else if (switchText.html() == "닫기") {
    $(function () {
      $(".file_drop_container").html("");
      switchText.html("열기");
    });
  } else {
    changedDataError();
  }
});

$(document).on("dragenter", ".dragAndDropDiv", function (e) {
  e.stopPropagation();
  e.preventDefault();
  $(this).css('background', 'whitesmoke');
});

$(document).on("dragover", ".dragAndDropDiv", function (e) {
  e.stopPropagation();
  e.preventDefault();
  $(this).css('background', 'whitesmoke');
});
$(document).on("dragleave", ".dragAndDropDiv", function (e) {
  e.stopPropagation();
  e.preventDefault();
  $(this).css('background', 'white');
});
$(document).on("drop", ".dragAndDropDiv", function (e) {

  $(this).css('background', 'white');
  e.preventDefault();
  var files = e.originalEvent.dataTransfer.files;

  handleFileUpload(files);
});


//스테이터스바 내 삭제 클릭시
$(document).on("click", ".delete-statusbar", function () {
  var isAcceptance = confirm("첨부한 파일을 삭제하시겠습니까?");
  if (isAcceptance) {
    var storedFileName = $(this).closest('.statusbar').find('.filename').attr(
        "data-filename");
    deleteFile(storedFileName, $(this).closest('.statusbar'),
        deleteStatusbarUI);
  }

});