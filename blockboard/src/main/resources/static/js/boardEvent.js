// 게시판 추가버튼 클릭시
function clickaddBoardBtn() {
  $('#config_container').html("<input type='text' name='게시판 이름' id = 'input_board_name' class='addBoard' placeholder='게시판 이름'>");
  $('#config_container').append("<br>");
  $('#config_container').append(" <a id ='addFuncBtn' onclick = javascript:clickSaveaddedBoard(this) style=cursor:pointer>저장하기</a>" +
    "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
  console.log("111");
}

//게시판 저장하기 버튼
function clickSaveaddedBoard() {
  var boardName = $('#input_board_name').val();
  console.log("boardName :"+boardName);
  $.ajax({
    type: 'POST',                 //get방식으로 통신
    url: "/boards/"+boardName+"/newboard",    //탭의 data-tab속성의 값으로 된 html파일로 통신
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