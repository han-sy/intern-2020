console.log("시작실행");
$.queue(getBoardList(updateTab).queue(function () {
  $('#tab_id').children().first().trigger('click');
}));

