/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    editorUI.js
 */

function openPostFileAttachForm(postId) {
  if (functionOn.postFileAttach) {
    if (isNullData(postId)) {
      openFileAttachForm();
    } else {
      openFileAttachForm(postId);
    }
  }
}

function createEditorArea(method, postId) { // 에디터 div 생성
  clearEditor();
  createEditorTemplate();
  applyFunctionPluginOnEditor();
  showWriteContentArea();

  switch (method) {
    case "insert":  // 게시글 작성시에만 자동저장 & 임시저장 적용
      showTempSaveButton();
      if (functionOn.postTempSave) {
        on_autosave();
      }
      break;
    case "modify":
      hideTempSaveButton();
      break;
  }
  openPostFileAttachForm(postId);
}

function clearEditor() {  // 작성 폼 초기화
  if (CKEDITOR.instances.editor) {
    CKEDITOR.instances.editor.setData("");
  }
  let post_button = $('#btn_post');
  post_button.html("저장");
  post_button.attr('onclick', 'javascript:postFunction()');

  let btn_cancel = $('#btn_cancel');
  btn_cancel.html("작성취소");
  btn_cancel.attr('onclick', 'javascript:writeCancel()');

  let writeContent = $('#write-content');
  writeContent.css("display", "none");

  $('#post_title').val("");
  off_autosave();
}

function applyFunctionPluginOnEditor() {
  let add_on = "";
  let original_config = CKEDITOR.config.plugins;
  if (functionOn.postSticker) {
    add_on += ",emoji";
  }
  if (functionOn.postInlineImage) {
    add_on += ",image2";
  }
  CKEDITOR.replace('editor', {
    plugins: original_config + add_on
  });
}

// 작성, 수정 버튼 클릭시 해당 게시판 선택 되어있게
function selectOptionOfCurrentBoardId(currentBoardId) {
  let selectableBoardId = $('#selectedBoardIdinEditor').children();
  $(selectableBoardId).each(function (index, item) {
    let boardId = $(item).attr('data-tab');
    if (boardId === currentBoardId) {
      $(item).prop("selected", true);
    } else {
      $(item).prop("selected", false);
    }
  });
}

function createEditorTemplate() {
  let source = $('#editor-content-template').html();
  let template = Handlebars.compile(source);
  $('#editor-content').html(template);
}

function updateSelectableBoardIdInEditor(board) {
  let source = $('#selectable-boardId-template').html();
  let template = Handlebars.compile(source);
  let itemList = template(board);
  $('#selectableBoardIdInEditor').html(itemList);
}

function addHiddenTypePostIdAndBoardIdToEditor(postId, boardId) {
  let source = $('#postId-template').html();
  let template = Handlebars.compile(source);
  let item = {postId: postId, boardId: boardId};
  let itemList = template(item);
  $('#editor-content-hidden').html(itemList);
}

