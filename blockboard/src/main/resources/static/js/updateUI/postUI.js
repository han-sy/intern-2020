/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postUI.js
 */
var isCreated = false;

// 에디터 div 생성
function editorAreaCreate(method) {
  editorClear();
  $('#editorcontent-hidden').html("");
  $('#post_title').val("");
  $('#writecontent').css("display", "");
  $('#btn_write').css("display", "none");
  $('#btn_deletePost').attr('style', 'visibility:hidden');
  $('#btn_updatePost').attr('style', 'visibility:hidden');
  // textarea에 CKEditor 적용
  if (!isCreated) {
    isCreated = true;
    CKEDITOR.replace('editor');
  }

  // 게시글 작성시에만(수정 X) 자동저장 & 임시저장 기능 작동
  if (method == "insert") {
    $('#btn_temp').attr('style', 'visibility:visible');
    on_autosave();
  } else {
    $('#btn_temp').attr('style', 'visibility:hidden');
  }
}

// 작성 폼 초기화
function editorClear() {
  if (CKEDITOR.instances.editor) {
    CKEDITOR.instances.editor.setData("");
  }
  var post_button = $('#btn_post');
  var writecontent = $('#writecontent');
  var btn_cancel = $('#btn_cancel');
  btn_cancel.html("작성취소");
  post_button.html("저장");
  btn_cancel.attr('onclick', 'javascript:writeCancel()');
  post_button.attr('onclick', 'javascript:postFunction()');
  writecontent.css("display", "none");
  off_autosave();
}

// 게시글 조회 화면 Clear
function postClear() {
  $('#postcontent').html("");
}

// 게시글 목록 화면 Clear
function postsClear() {
  $('#postlist').html("");
}

// 에디터 UI에 hidden type으로 PostID 추가
function addPostIdToEditor(postID) {
  var source = $('#postid-template').html();
  var template = Handlebars.compile(source);
  var IDitem = {postID: postID};
  var itemList = template(IDitem);
  $('#editorcontent-hidden').html(itemList);
}

// 작성, 수정 버튼 클릭시 해당 게시판 선택 되어있게
function initBoardIdOptionInEditor(currentBoardID) {
  var options = $('#boardIDinEditor').children();
  $(options).each(function (index, item) {
    var data = $(item).attr('data-tab'); // option의 boardID
    if (data == currentBoardID) {
      $(item).prop("selected", true);
    } else {
      $(item).prop("selected", false);
    }
  });
}

function updateboardListInEditor(board) {
  source = $('#writecontent-boards-template').html();
  template = Handlebars.compile(source);
  itemList = template(board);
  $('#boardIDinEditor').html(itemList);
}
function openFileAttachForm() {
  source = $('#file-attach-form-template').html();
  template = Handlebars.compile(source);
  item = template();
  $('#fileAttachForm').html(item);
}

function openDragAndDropForm(){
  source = $('#file-attach-drag-and-drop-form-template').html();
  template = Handlebars.compile(source);
  item = template();
  $('#file_drop_container').html(item);

}
