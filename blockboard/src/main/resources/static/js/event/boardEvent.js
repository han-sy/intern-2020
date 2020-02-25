/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file boardEvent.js
 */

$(function () {
  $('#input_board_name').keyup(function () {
    bytesHandler(this, "#board_name_length", MAX_LENGTH.BOARD_NAME);
  });
});

//게시판 이름 입력시 엔터키로 저장
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
});

//게시판 추가저장하기 버튼
function clickSaveAddedBoard() {
  let boardName = $('#input_board_name').val();
  boardName = boardName.trim();
  if (boardName == "") {
    alert("게시판 제목을 입력하세요.");
    return;
  }
  let textLength = getByteLength(boardName, MAX_LENGTH.BOARD_NAME);
  let byteLength = textLength[0];
  let charLength = textLength[1];
  if (byteLength >= MAX_LENGTH.BOARD_NAME) {
    alert("입력 글자수 초과입니다. 초과된 문자들은 삭제됩니다.");
    $('#input_board_name').val(boardName.substr(0, charLength).trim());
    return;
  }
  let isAcceptance = confirm("<" + boardName + ">게시판을 추가하시겠습니까?");
  if (isAcceptance) {
    $(function () {
      updateTabByNewBoardListAfterAddBoard(boardName);
    });
  }
}

//게시판 삭제버튼 누를시
function clickDeleteBoardBtn() {
  $(function () {
    getBoardList(getBoardListToDelete);
  });
}

//게시판 삭제- 삭제하기버튼 누를시
function clickSaveDeleteBoard() {
  let boardDataList = new Array();

  $("input[name=boardDelete]").each(function () {
    if (isCheckedBoardDelete.call(this)) { //삭제할때 체크 이렇게 불러옴
      let boardData = new Boards(getCheckedBoardId.call(this),
          getCheckedBoardName.call(this));
      boardDataList.push(boardData);
    }
  });

  let isAcceptance = confirm("선택한 게시판을 정말 삭제하시겠습니까? 게시물또한 모두 삭제됩니다.");
  if (isAcceptance) {
    $(function () {
      updateTabByNewBoardListAfterDeleteBoard(boardDataList); //삭제이후 tab에 게시판목록 업데이트
    });
  }
}

//게시판 이름변경 버튼 클릭시
function clickChangeBoardBtn() {
  $(function () {
    getBoardList(getBoardListToChangeName);
  });
}

function insertChangeBoard(oldBoardName, newBoardName, boardId, boardDataList) {
  if (oldBoardName != newBoardName) {
    let boardData = new Boards(boardId, newBoardName);
    boardDataList.push(boardData);
  }
}

//게시판 이름변경 저장하기
function clickSaveChangeBoard() {
  let boardDataList = new Array();

  $("input[name=boardname]").each(function () {
    var oldBoardName = getOldBoardName.call(this);
    let newBoardName = getNewBoardName.call(this);
    newBoardName = newBoardName.trim();
    if (newBoardName == "") {
      alert("기존" + oldBoardName + "게시판 제목이 비었습니다.");
      return;
    }
    var boardId = $(this).attr("data-boardId");
    insertChangeBoard(oldBoardName, newBoardName, boardId, boardDataList);
  });

  var jsonData = JSON.stringify(boardDataList);
  var isAcceptance = confirm("게시판 이름변경 내용을 저장하시겠습니까?");
  if (isAcceptance) {
    $(function () {
      updateTabByNewBoardListAfterUpdateBoardName(jsonData);
    });
  }
  $('#config_container').html("");
}

//닫기 버튼 클릭
$(document).on('click', '.functionClose', clickConfigClose());

function clickConfigClose() {
  $('#config_container').html("");
}

$(document).on("mouseenter", ".tabmenu", function () {
  $(this).css('background-color', 'WhiteSmoke');
});

$(document).on("mouseleave", ".tabmenu", function () {
  $(this).css('background-color', 'white');
});

function changeTabDesign() {
  $('.tabmenu').css('color', 'black');
  $('.tabmenu').removeClass("font-weight-bold");
  $('.tabmenu').removeClass("active_tab");
  $(this).css('color', '#40A745');//success색
  $(this).addClass("active_tab");
  $(this).addClass("font-weight-bold");
}

// 탭 메뉴 클릭 이벤트 - 해당 게시판의 게시글 불러옴
$(document).on("click", ".tabmenu", function clickTabEvent(event) {
  event.stopPropagation();
  let boardId = $(this).attr('data-tab');
  changeTabDesign.call(this);
  postClear();
  clearEditor();
  getPageList(1, boardId, 0, updatePostPageList);
});

function switchFunctionOnOff(checkBox, switchText) {
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
}

//기능변경 on/off버튼 텍스트 바꾸기
$(document).on('click', '._function-switch', function () {
  let switchText = $(this).find("._switch");
  let checkBox = $(this).find(".function_checkbox");
  switchFunctionOnOff.call(this, checkBox, switchText);
  $(this).removeClass("active");
});

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
function clickBoardEventByBoardId(boardId) {
  $(".tabmenu").each(function (index, item) {
    if (parseInt(item.dataset.tab) === parseInt(boardId)) {
      $(item).trigger("click");
      return false;
    }
  });
}

/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
function getCurrentActiveBoardId() {
  let boardId = 0;
  $('#tab_id').children().each(function () {
    if ($(this).hasClass("active_tab")) {
      boardId = $(this).attr("data-tab");
    }
  });
  return boardId;
}