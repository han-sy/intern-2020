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
      updateTabByNewBoardListAfterDeleteBoard(jsonData);
    });
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
        var tabUlObj = $('#tab_id');
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



//색변경 탭에 mouseover시 실행
function changeTrColor(trObj) {
  trObj.style.backgroundColor = "green";
  trObj.onmouseout = function () {
    trObj.style.backgroundColor = "lightgreen";
  }
}

// 게시글 목록에서 게시글 클릭시
function clickTrEvent(trObj) {

  //alert(trObj.getAttribute("data-post"));
  var postID = trObj.getAttribute("data-post");
  var boardID;
  var tabs = $('#tab_id').children();

  var postContentObj = $('#postcontent');
  $.each(tabs, function () {
    var clickObj = $(this);
    var color = clickObj.css('background-color');
    if (color == "rgb(144, 238, 144)") {
      boardID = clickObj.attr('data-tab');
    }
  });
  //console.log(postID);
  //$('#postcontent').html("activerRow : " + trObj.getAttribute("data-post"));
  $.ajax({
    type: 'GET',                 //get방식으로 통신
    url: "/boards/" + boardID + "/posts/" + postID,    //탭의 data-tab속성의 값으로 된 html파일로 통신
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      console.log("success" + data);
      $('#writecontent').hide();
      $('#btn_write').show();
      postContentObj.html("");
      postContentObj.append("<h2>" + data.postTitle + "</h2>");
      postContentObj.append("<h5>작성자 : " + data.userName + "</h4>");
      postContentObj.append("<h5>작성시간 : " + data.postRegisterTime + "</h4>");
      postContentObj.append("<a>" + data.postContent + "</a>");
      postContentObj.append("<a id=postID style=visibility:hidden>" + data.postID + "</a>");
      // 작성글의 userID와 현재 로그인한 userID가 같으면 삭제버튼 표시

      var commentAbleObj = $('#functionAble1');
      console.log("comment 여부 : " + commentAbleObj.attr("value"));

      if (data.canDelete == true) {
        postContentObj.append("</br><button id=btn_updatePost>수정</button>");
        postContentObj.append("</br><button id=btn_deletePost>삭제</button>");
      }

      if (commentAbleObj.attr("value") == "on") {
        var commentHtml = "";
        commentHtml += "<div><span><strong>Comments</strong></span> <span id=commentCount></span></div>";
        commentHtml += "<table class=commentHtml>";
        commentHtml += "<tr><td>";
        commentHtml += "<textarea style='width: 80% rows=3 cols=30' id=commentText name=commentTrxt placeholder='댓글을 입력하세요'></textarea>";
        commentHtml += "<div></br><button id=btn_openComment>등록</button></div>";
        commentHtml += "</td></tr></table>";
        postContentObj.append(commentHtml);
      }


    }
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
  //var activeTab = $(this).attr('data-tab');
  console.log("!!!!");
  var boardID = $(this).attr('data-tab');
  console.log(boardID);
  $('li').css('background-color', 'white');
  $(this).css('background-color', 'lightgreen');
  $('#postcontent').html("");
  $('#writecontent').hide();
  $('#btn_write').show();
  $.ajax({
    type: 'GET',                 //get방식으로 통신
    url: '/boards/' + boardID + "/posts",    //탭의 data-tab속성의 값으로 된 html파일로 통신
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      console.log("success" + data);
      $('#postlist').html("");
      $.each(data, function (key, value) {
        $('#postlist').append(
          "<tr height='30' class = 'postclick' data-post = '" + value.postID +
          "' onclick='javascript:clickTrEvent(this)' onmouseover = 'javascript:changeTrColor(this)' >" +
          "<td width='379'>" + value.postTitle + "</td>" +
          "<td width='73'>" + value.userName + "</td>" +
          "<td width='164'>" + value.postRegisterTime + "</td></tr>" +
          "<td style='visibility:hidden'>" + value.postID + "</td>"
        );
        //alert($this);
      });
    }
  });
})