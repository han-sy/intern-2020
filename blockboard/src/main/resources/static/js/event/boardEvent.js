/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file boardEvent.js
 */
const KEYCODE = {
  ENTER: 13
};
var MAX_LENGTH = {
  BOARD_NAME: 150
};
$(function () {
  $('#input_board_name').keyup(function () {
    bytesHandler(this, "#board_name_length", MAX_LENGTH.BOARD_NAME);
  });
});
$(document).on('keydown', '#input_board_name', function (event) {
  if (event.keyCode == KEYCODE.ENTER) {
    clickSaveAddedBoard();
  }
});
$(document).on('click', '#add_board_save_btn', function () {
  clickSaveAddedBoard();
});

//게시판 추가버튼 클릭
$(document).on('click', '#add_board_btn', function () {
  $('#input_board_name').val("");
})

//게시판 추가저장하기 버튼
function clickSaveAddedBoard() {
  var userData = new User();
  var boardName = $('#input_board_name').val();
  var boardName = boardName.trim();
  if (boardName == "") {
    alert("게시판 제목을 입력하세요.");
    return;
  }
  var textLength = getByteLength(boardName, MAX_LENGTH.BOARD_NAME);
  var byteLength = textLength[0];
  var charLength = textLength[1];
  if (byteLength >= MAX_LENGTH.BOARD_NAME) {
    alert("입력 글자수 초과입니다. 초과된 문자들은 삭제됩니다.");
    $('#input_board_name').val(boardName.substr(0, charLength).trim());
    return;
  }
  console.log("boardName :" + boardName);
  var askSave = confirm("<" + boardName + ">게시판을 추가하시겠습니까?");
  if (askSave) {
    $(function () {
      updateTabByNewBoardListAfterAddBoard(boardName, userData.getJsonString());
    });
  }
}

//게시판 삭제버튼 누를시
function clickDeleteBoardBtn() {
  $(function () {
    getBoardList(getBoardListToDelete);
  });
}

1

//게시판 삭제- 삭제하기버튼 누를시
function clickSaveDelteBoard() {
  var boardDataList = new Array();

  $("input[name=boardDelete]").each(function () {
    var checkboxObj = $(this);
    if (checkboxObj.is(":checked")) { //삭제할때 체크 이렇게 불러옴
      var boardData = new Object();
      boardData.boardID = checkboxObj.val();
      console.log("boardID:" + boardData.boardID);
      boardData.boardName = checkboxObj.attr("data-boardName");
      boardDataList.push(boardData);
    }
  });

  var userData = new User();
  var jsonData = JSON.stringify(boardDataList);
  var askSave = confirm("선택한 게시판을 정말 삭제하시겠습니까? 게시물또한 모두 삭제됩니다.");
  if (askSave) {
    $(function () {
      updateTabByNewBoardListAfterDeleteBoard(jsonData,
          userData.getJsonString()); //삭제이후 tab에 게시판목록 업데이트
    });
  }
}

//게시판 이름변경 버튼 클릭시
function clickchangeBoardBtn() {
  $(function () {
    getBoardList(getBoardListToChangeName);
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
    newBoardName = newBoardName.trim();
    if (newBoardName == "") {
      alert("기존" + oldBoardName + "게시판 제목이 비었습니다.");
      return;
    }
    var boardID = clickObj.attr("data-boardid");
    if (oldBoardName != newBoardName) {
      boardData.boardName = newBoardName;
      boardData.boardID = boardID;
      boardDataList.push(boardData);
    }
  });

  var userData = new User();
  var jsonData = JSON.stringify(boardDataList);
  var askSave = confirm("게시판 이름변경 내용을 저장하시겠습니까?");
  if (askSave) {
    $(function () {
      updateTabByNewBoardListAfterUpdateBoardName(jsonData,
          userData.getJsonString());
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
  var boardID = getActiveBoardID();
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

$(document).on("mouseenter", ".tabmenu", function () {
  $(this).css('background-color', 'WhiteSmoke');
});
$(document).on("mouseleave", ".tabmenu", function () {
  $(this).css('background-color', 'white');
});

// 탭 메뉴 클릭 이벤트 - 해당 게시판의 게시글 불러옴
$(document).on("click", ".tabmenu", function clickTabEvent() {
  var boardID = $(this).attr('data-tab');
  var btn_write = $('#btn_write');
  $('.tabmenu').css('color', 'black');
  $('.tabmenu').removeClass("font-weight-bold");
  $('.tabmenu').removeClass("active_tab");
  $(this).css('color', '#40A745');//success색
  $(this).addClass("active_tab");
  $(this).addClass("font-weight-bold");
  postClear();
  editorClear();
  console.log("클릭한 boardID = " + boardID);
  if (boardID > 0) {
    $(function () {
      btn_write.attr('style', 'visibility:visible');
      getPostsAfterTabClick(boardID);
    });
  } else {
    btn_write.attr('style', 'visibility:hidden');
    getTempPosts();
  }
})

// 현재 선택된 게시판 ID 찾기
function getActiveBoardID() {
  var boardID;
  var tabs = $('#tab_id').children();
  $.each(tabs, function () {
    if (tabs.hasClass("active_tab") == true) {
      boardID = tabs.attr('data-tab');
    }
  });
  return boardID;
}

//기능변경 on/off버튼 텍스트 바꾸기
$(document).on('click', '._function-switch', function () {
  var switchText = $(this).find("._switch");
  var checkBox = $(this).find(".function_checkbox");
  console.log(switchText.html() == "ON");
  if (checkBox.prop("checked")) {
    $(this).removeClass('btn-success');
    $(this).addClass('btn-default');
    switchText.html("OFF");
    checkBox.removeAttr("checked");
  } else {
    $(this).removeClass('btn-default');
    $(this).addClass('btn-success');
    switchText.html("ON");
    checkBox.prop("checked", true);
  }
  $(this).removeClass("active");
});
