/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    viewRecordUI.js
 */



function loadViewRecordUI(data){
  console.log("loadViewRecordUI");
  var source = $('#view_record-list-template').html();
  var template = Handlebars.compile(source);
  var records = {records: data};
  var itemList = template(records);
  $('.modal-body-viewRecordList-container').append(itemList);
}