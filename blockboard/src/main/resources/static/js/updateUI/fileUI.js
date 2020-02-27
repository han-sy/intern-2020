/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file fileUI.js
 */

function updateFileListInPostUI(data, obj) {
  let source = $('#attached-file-list-template').html();
  let template = Handlebars.compile(source);
  let files = {files: data};
  let itemList = template(files);
  obj.html(itemList);
}

function updateFileListInCommentUI(data, obj) {
  let source = $('#attached-file-list-template').html();
  let template = Handlebars.compile(source);
  let files = {files: data};
  let itemList = template(files);
  obj.html(itemList);
}

function deleteStatusbarUI(obj) {
  obj.remove();
}

function createStatusbarUI(data, container) {
  let source = $('#attached-file-statusbar-template').html();
  let template = Handlebars.compile(source);
  let files = {files: data};
  let itemList = template(files);
  container.html(itemList);
}

/**
 * 드래그앤 드랍 영역
 */
function openDragAndDropForm(obj) {
  source = $('#file-attach-drag-and-drop-form-template').html();
  template = Handlebars.compile(source);
  item = template();
  obj.closest(".file_attach_form").find('.file_drop_container').html(item);

}

function makePostFileAttachForm() {
  $('.file_attach_form').html(item);
}

function makeCommentFileAttachForm(obj) {
  obj.find('.file_attach_form').html(item);
}

function makeFileAttachForm(obj, container) {
  if (isNullData(obj)) {//게시판 파일첨부
    makePostFileAttachForm();
    container = $('.file_attach_form').find(".upload_list_start_point");
  } else {//댓글 파일첨부
    makeCommentFileAttachForm(obj);
    container = obj.find('.file_attach_form').find(".upload_list_start_point");
  }
  return container;
}

/**
 * 구분하고 fileList가져오기
 */
function distinguishEditorForGetFileList(postId, container, commentId) {
  if (!isNullData(postId)) {
    getFileList(postId, 0, container, createStatusbarUI);
  }
  if (!isNullData(commentId)) {
    getFileList(0, commentId, container, createStatusbarUI);
  }
}

/**
 * 파일첨부 폼
 */
function openFileAttachForm(postId, commentId, obj) {
  source = $('#file-attach-form-template').html();
  template = Handlebars.compile(source);
  item = template();
  let container = null;
  container = makeFileAttachForm(obj, container);
  distinguishEditorForGetFileList(postId, container, commentId);
}

function fileFormClear() {
  $('.file_attach_form').html("");
}

/**
 * 게시글 내용 조회시 첨부파일관련 컨텐츠들
 */
function showAttachFileContents(postId) {
  if (functionOn.postFileAttach) {
    let container = $("#post-content").find(
        ".attached_file_list_container_post");
    getFileList(postId, 0, container, updateFileListInPostUI);
  }
}