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
  console.log("boardName :" + boardName);
  $.ajax({
    type: 'POST',                 //get방식으로 통신
    url: "/boards/" + boardName + "/newboard",    //탭의 data-tab속성의 값으로 된 html파일로 통신
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

//게시판 삭제버튼 누를시
function clickDeleteBoardBtn() {
  $.ajax({
    type: 'GET',
    url: '/boards/list',    //탭의 data-tab속성의 값으로 된 html파일로 통신
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      console.log("success" + data);
      $('#config_container').html("");
      $('#config_container').html("삭제할 게시판을 선택하시오");
      $.each(data, function (key, value) {
        console.log(value.functionInfoData);
        $('#config_container').append("<div><span>" + value.boardName + "</span><input type=checkbox name=boardDelete value=" +
          value.boardID + "></div>");

      });
      $('#config_container').append(" <a id ='addFuncBtn' onclick = javascript:clickSaveDelteBoard(this) style=cursor:pointer>삭제하기</a>" +
        "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
    }
  });
}

//게시판 삭제- 삭제하기버튼 누를시
function clickSaveDelteBoard() {
  var boardDataList = new Array();

  $("input[name=boardDelete]").each(function () {
    if ($(this).is(":checked")) {
      var boardData = new Object();
      boardData.boardID = $(this).val();
      console.log("boardID:" + boardData.boardID);
      boardData.boardName = $(this).attr("data-boardName");
      boardDataList.push(boardData);
    }
  });


  var jsonData = JSON.stringify(boardDataList);
  var askSave = confirm("선택한 게시판을 정말 삭제하시겠습니까? 게시물또한 모두 삭제됩니다.");
  if (askSave) {
    $.ajax({
      type: 'DELETE',
      url: "/boards/list",    //탭의 data-tab속성의 값으로 된 html파일로 통신
      data: { deleteList: jsonData },
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
}