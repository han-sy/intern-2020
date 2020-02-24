/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file boardUI.js
 */
//새로운 탭 내용으로 교체
function updateTab(data) {
  var source = $('#boards-template').html();
  var template = Handlebars.compile(source);
  var board = {boards: data};
  var itemList = template(board);
  $('#tab_id').html(itemList);
  updateSelectableBoardIdInEditor(board);

  $("#tab_id").children().each(function () {
    if ($(this).attr('data-tab') > 0) {
      $(this).trigger('click');
      return false;
    }
  });
}



//삭제를 위한 UI
function getBoardListToDelete(data) {
  var source = $('#deleteboards-template').html();
  var template = Handlebars.compile(source);
  var boardList = {boards: data};
  var itemList = template(boardList);
  $('.modal-body-deleteBoard').html(itemList);
}

//이름변경을 위한 UI
function getBoardListToChangeName(data) {
  var source = $('#changeBoardName-template').html();
  var template = Handlebars.compile(source);
  var boardList = {boards: data};
  var itemList = template(boardList);
  $('.modal-body-changeBoardName').html(itemList);
}