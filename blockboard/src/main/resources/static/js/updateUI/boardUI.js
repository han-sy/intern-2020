/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file boardUI.js
 */
//새로운 탭 내용으로 교체
function updateTab(data) {
  let source = $('#boards-template').html();
  let template = Handlebars.compile(source);
  let board = {boards: data};
  let itemList = template(board);
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
  let source = $('#deleteboards-template').html();
  let template = Handlebars.compile(source);
  let boardList = {boards: data};
  let itemList = template(boardList);
  $('.modal-body-deleteBoard').html(itemList);
}

//이름변경을 위한 UI
function getBoardListToChangeName(data) {
  let source = $('#changeBoardName-template').html();
  let template = Handlebars.compile(source);
  let boardList = {boards: data};
  let itemList = template(boardList);
  $('.modal-body-changeBoardName').html(itemList);
}