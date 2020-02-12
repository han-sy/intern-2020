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
