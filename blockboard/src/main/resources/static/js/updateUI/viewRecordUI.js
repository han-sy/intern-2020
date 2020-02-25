/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    viewRecordUI.js
 */

function loadViewRecordUI(data) {
  let source = $('#view_record-list-template').html();
  let template = Handlebars.compile(source);
  let records = {records: data};
  let itemList = template(records);
  $('.modal-body-viewRecordList-container').append(itemList);
}