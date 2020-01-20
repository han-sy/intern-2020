//게시판 저장하기 버튼
function clickSaveaddedBoard() {
  var boardName = $('#input_board_name').val();
  console.log(boardName);
  $.ajax({
    type: 'POST',                 //get방식으로 통신
    url: "/boards/"+boardname+"/newboard",    //탭의 data-tab속성의 값으로 된 html파일로 통신
    data: { boardName: boardName },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      $('#tab_id').html("");
      $.each(data, function (key, value) {
        $("#tab_id").append("<li data-tab=" + value.boardID + "  class=tabmenu id=default>" + value.boardName + "</li>");
      });
      $('#config_container').html("");
    }
  });
  $('#config_container').html("");
}