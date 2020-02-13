/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file fileUI.js
 */
function updateFileListInPostUI(data){
  var source = $('#attached-file-list-template').html();
  var template = Handlebars.compile(source);
  var files = {files: data};
  var itemList = template(files);
  $(".attached_file_list_container").html(itemList);
}

function deleteStatusbarUI(obj) {
  obj.remove();
}

function createStatusbarUI(data){
  console.log(data);
  var source = $('#attached-file-statusbar-template').html();
  var template = Handlebars.compile(source);
  var files = {files: data};
  var itemList = template(files);
  $(".upload_list_start_point").html(itemList);
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
  if(isNullData(obj)){
    $('.file_attach_form').html(item);
  }else{
    obj.find('.file_attach_form').html(item);
  }
  console.log("파일리스트받아오기");
  if(!isNullData(postID)){
    console.log(postID);
    getFileList(postID,createStatusbarUI);
  }
  if(!isNullData(commentID)){
    console.log(commentID);
    getFileList(commentID,createStatusbarUI);
  }
}

function fileFormClear(){
  $('.file_attach_form').html("");
}