/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file fileUI.js
 */

function updateFileListInPostUI(data,obj){
  var source = $('#attached-file-list-template').html();
  var template = Handlebars.compile(source);
  var files = {files: data};
  var itemList = template(files);
  obj.html(itemList);
}

function updateFileListInCommentUI(data,obj){
  var source = $('#attached-file-list-template').html();
  var template = Handlebars.compile(source);
  var files = {files: data};
  var itemList = template(files);
  obj.html(itemList);
}

function deleteStatusbarUI(obj) {
  obj.remove();
}

function createStatusbarUI(data,container){
  var source = $('#attached-file-statusbar-template').html();
  var template = Handlebars.compile(source);
  var files = {files: data};
  var itemList = template(files);
  container.html(itemList);
}

/**
 * 드래그앤 드랍 영역
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function openDragAndDropForm(obj) {
  source = $('#file-attach-drag-and-drop-form-template').html();
  template = Handlebars.compile(source);
  item = template();
  obj.closest(".file_attach_form").find('.file_drop_container').html(item);

}

/**
 * 파일첨부 폼
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function openFileAttachForm(postID,commentID,obj) {
  source = $('#file-attach-form-template').html();
  template = Handlebars.compile(source);
  item = template();
  var container = null;
  if(isNullData(obj)){
    $('.file_attach_form').html(item);
    container = $('.file_attach_form').find(".upload_list_start_point");
  }else{
    obj.find('.file_attach_form').html(item);
    container = obj.find('.file_attach_form').find(".upload_list_start_point");
  }
  if(!isNullData(postID)){
    getFileList(postID,0,container,createStatusbarUI);
  }
  if(!isNullData(commentID)){
    getFileList(0,commentID,container,createStatusbarUI);
  }
}

function fileFormClear(){
  $('.file_attach_form').html("");
}