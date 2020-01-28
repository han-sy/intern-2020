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
  if(boardName ==""){
    alert("게시판 제목을 입력하세요.");
    return;
  }
  console.log("boardName :" + boardName);
  $(function () {
    updateTabByNewBoardListAfterAddBoard(boardName);
  });
  $('#config_container').html("");
}

//게시판 삭제버튼 누를시
function clickDeleteBoardBtn() {
  $(function () {
    getBoardListToDelete();
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
    $(function () {
      updateTabByNewBoardListAfterDeleteBoard(jsonData); //삭제이후 tab에 게시판목록 업데이트
    });
  }
}

//게시판 이름변경 버튼 클릭시
function clickchangeBoardBtn() {
  $.ajax({
    type: 'GET',
    url: '/boards',
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
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
    if(newBoardName ==""){
        alert("기존"+oldBoardName+"게시판 제목이 비었습니다.");
        return;
    }
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
    $(function () {
      updateTabByNewBoardListAfterUpdateBoardName(jsonData);
    });
  }
  $('#config_container').html("");
}



//색변경 탭에 mouseover시 실행
function changeTrColor(trObj) {
  trObj.style.backgroundColor = "green";
  trObj.onmouseout = function () {
    trObj.style.backgroundColor = "lightgreen";
  }
}

// 게시글 목록에서 게시글 클릭시
function clickTrEvent(trObj) {
  var postID = trObj.getAttribute("data-post");
  var boardID;
  var tabs = $('#tab_id').children();
  $.each(tabs, function () {
    var clickObj = $(this);
    var color = clickObj.css('background-color');
    if (color == "rgb(144, 238, 144)") {
      boardID = clickObj.attr('data-tab');
    }
  });
  $(function () {
    getPostDataAfterPostClick(postID, boardID); //boardAjax.js 참고
  });
}

//닫기 버튼 클릭
$(document).on('click', '.functionClose', clickConfigClose());
function clickConfigClose() {
  console.log("닫기버튼");
  $('#config_container').html("");
}

// 탭 메뉴 클릭 이벤트 - 해당 게시판의 게시글 불러옴
$(document).on("click", ".tabmenu", function clickTabEvent() {
  console.log("!!!!");
  var boardID = $(this).attr('data-tab');
  console.log(boardID);
  $('li').css('background-color', 'white');
  $(this).css('background-color', 'lightgreen');
  $('#postcontent').html("");
  $('#writecontent').hide();
  $('#btn_write').show();
  $(function () {
    getPostsAfterTabClick(boardID);
  });
})