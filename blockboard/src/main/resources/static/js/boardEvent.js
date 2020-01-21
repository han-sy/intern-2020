// 게시판 추가버튼 클릭시
function clickaddBoardBtn() {
  var containerObj = $('#config_container');
  containerObj.html("<input type='text' name='게시판 이름' id = 'input_board_name' class='addBoard' placeholder='게시판 이름'>");
  containerObj.append("<br>");
  containerObj.append(" <a id ='addFuncBtn' onclick = javascript:clickSaveaddedBoard(this) style=cursor:pointer>저장하기</a>" +
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
      var tabUlObj = $('#tab_id');
      tabUlObj.html("");
      $.each(data, function (key, value) {
        tabUlObj.append("<li data-tab=" + value.boardID + "  class=tabmenu id=default>" + value.boardName + "</li>");
      });
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
      var containerObj = $('#config_container');
      containerObj.html("삭제할 게시판을 선택하시오");
      $.each(data, function (key, value) {
        console.log(value.functionInfoData);
        containerObj.append("<div><span>" + value.boardName + "</span><input type=checkbox name=boardDelete value=" +
          value.boardID + "></div>");

      });
      containerObj.append(" <a id ='addFuncBtn' onclick = javascript:clickSaveDelteBoard(this) style=cursor:pointer>삭제하기</a>" +
        "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
    }
  });
}

//게시판 삭제- 삭제하기버튼 누를시
function clickSaveDelteBoard() {
  var boardDataList = new Array();

  $("input[name=boardDelete]").each(function () {
    var checkboxObj = $(this);
    if (checkboxObj.is(":checked")) {
      var boardData = new Object();
      boardData.boardID = checkboxObj.val();
      console.log("boardID:" + boardData.boardID);
      boardData.boardName = checkboxObj.attr("data-boardName");
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
        var tabUlObj =$('#tab_id');
        tabUlObj.html("");
        $.each(data, function (key, value) {
          tabUlObj.append("<li data-tab=" + value.boardID + "  class=tabmenu id=default>" + value.boardName + "</li>");
        });
      }
    });
    $('#config_container').html("");
  }
}

//게시판 이름변경 버튼 클릭시
function clickchangeBoardBtn() {
  $.ajax({
    type: 'GET',                 //POST 통신
    url: '/boards/list',    //탭의 data-tab속성의 값으로 된 html파일로 통신
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      var containerObj = $('#config_container')
      containerObj.html("");
      $.each(data, function (key, value) {
        containerObj.append("<div class=boardInfo id=board" + value.boardID + "><input type=text name =boardname data-boardid=" + value.boardID + " data-oldname=" + value.boardName + " value=" + value.boardName + " >" +
          " <span class =deleteBoard data-board =board" + value.boardID + " > 기존 게시판 이름 : " + value.boardName + "</span></div>");
      });
      containerObj.append(" <br><a id ='addFuncBtn' onclick = javascript:clickSaveChangeBoard(this) style=cursor:pointer>변경하기</a>" +
        "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
    }
  });
}


//게시판 이름변경 저장하기
function clickSaveChangeBoard() {
  var boardDataList = new Array();

  $("input[name=boardname]").each(function () {
    var boardData = new Object();
    var clickObj = $(this);
    var oldBoardName = clickObj.attr("data-oldname");
    var newBoardName = clickObj.val();
    var boardID = clickObj.attr("data-boardid");
    if (oldBoardName != newBoardName) {
      boardData.boardName = newBoardName;
      boardData.boardID = boardID;
      boardDataList.push(boardData);
    }


  });

  var jsonData = JSON.stringify(boardDataList);
  var askSave = confirm("게시판 이름변경 내용을 저장하시겠습니까?");
  if (askSave) {
    $.ajax({
      type: 'POST',                 //get방식으로 통신
      url: "/boards/newtitle",    //탭의 data-tab속성의 값으로 된 html파일로 통신
      data: { newTItles: jsonData },
      error: function () {  //통신 실패시
        alert('통신실패!');
      },
      success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
        var tabUlObj =$('#tab_id');
        tabUlObj.html("");
        $.each(data, function (key, value) {
          $("#tab_id").append("<li data-tab=" + value.boardID + "  class=tabmenu id=default>" + value.boardName + "</li>");
        });
        $('#config_container').html("");
      }
    });
  }
  $('#config_container').html("");
}