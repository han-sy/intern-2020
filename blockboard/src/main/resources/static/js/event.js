document.write("<script src='/static/ckeditor/ckeditor.js'></script>");
document.write("<script src='/static/ckeditor/adapters/jquery.js'></script>");

//색변경 탭에 mouseover시 실행
function changeTrColor(trObj) {
  trObj.style.backgroundColor = "green";
  trObj.onmouseout = function () {
    trObj.style.backgroundColor = "lightgreen";
  }
}

//탭 버튼 클릭시
function clickTrEvent(trObj) {
  //alert(trObj.getAttribute("data-post"));
  var postID = trObj.getAttribute("data-post");
  //console.log(postID);
  //$('#postcontent').html("activerRow : " + trObj.getAttribute("data-post"));
  $.ajax({
    type: 'GET',                 //get방식으로 통신
    url: "/board/postlist/" + postID,    //탭의 data-tab속성의 값으로 된 html파일로 통신
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      console.log("success" + data);
      $('#writecontent').hide();
      $('#btn_write').show();
      $('#postcontent').html("");
      $('#postcontent').append("<h2>" + data.postTitle + "</h2>");
      $('#postcontent').append("<h5>작성자 : " + data.userName + "</h4>");
      $('#postcontent').append("<h5>작성시간 : " + data.postRegisterTime + "</h4>");
      $('#postcontent').append("<a>" + data.postContent + "</a>");
      $('#postcontent').append("<a id=postID style=visibility:hidden>" + data.postID + "</a>");
      // 작성글의 userID와 현재 로그인한 userID가 같으면 삭제버튼 표시
      console.log("canDelete = " + data.canDelete);
      if (data.canDelete == true) {
        $('#postcontent').append("</br><button id=btn_updatePost>수정</button>");
        $('#postcontent').append("</br><button id=btn_deletePost>삭제</button>");
      }
    }
  });
}

//게시판 수정삭제 버튼 클릭시
function clickchangeBoardBtn() {
  $.ajax({
    type: 'POST',                 //POST 통신
    url: '/board/boardlist',    //탭의 data-tab속성의 값으로 된 html파일로 통신
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      $('#config_container').html("");
      $.each(data, function (key, value) {
        $('#config_container').append("<div class=boardInfo id=board" + value.boardID + "><input type=text name =boardname data-boardid=" + value.boardID + " value=" + value.boardName + " >" +
          " <span class =deleteBoard data-board =board" + value.boardID + " >" + value.boardName + "</span></div>");
      });
      $('#config_container').append(" <br><a id ='addFuncBtn' onclick = javascript:clickSaveChangeBoard(this) style=cursor:pointer>변경하기</a>" +
        "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
    }
  });
}
//게시판 삭제버튼 누를시
function clickDeleteBoardBtn() {
  $.ajax({
    type: 'POST',                 //POST 통신
    url: '/board/boardlist',    //탭의 data-tab속성의 값으로 된 html파일로 통신
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
    $("input[name=boardDelete]").each(function () {

      var boardID = $(this).val();
      if ($(this).is(":checked")) {
        $.ajax({
          type: 'POST',                 //get방식으로 통신
          url: "/board/deletion/board",    //탭의 data-tab속성의 값으로 된 html파일로 통신
          data: { deleteBoardList: jsonData },
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
      }
    });
    $('#config_container').html("");
  }
}

//게시판 이름변경 저장하기
function clickSaveChangeBoard() {
  var boardDataList = new Array();

  $("input[name=boardname]").each(function () {
    var boardData = new Object();
    boardData.boardName = $(this).val();
    boardData.boardID = $(this).attr("data-boardid");
    boardDataList.push(boardData);
  });

  var jsonData = JSON.stringify(boardDataList);
  var askSave = confirm("게시판 이름변경 내용을 저장하시겠습니까?");
  if (askSave) {
    $.ajax({
      type: 'POST',                 //get방식으로 통신
      url: "/board/changed/boardname",    //탭의 data-tab속성의 값으로 된 html파일로 통신
      data: { boardData: jsonData },
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
  }
  $('#config_container').html("");
}
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
  console.log($('#input_board_name').val());
  $.ajax({
    type: 'POST',                 //get방식으로 통신
    url: "/board/newboard",    //탭의 data-tab속성의 값으로 된 html파일로 통신
    data: { boardName: $('#input_board_name').val() },
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

//기능변경 버튼 클릭시
function changeFunction() {
  $.ajax({
    type: 'POST',                 //POST 통신
    url: '/board/function-info',    //탭의 data-tab속성의 값으로 된 html파일로 통신
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      console.log("success" + data);
      $('#config_container').html("");
      $.each(data, function (key, value) {
        console.log(value.functionInfoData);

        if (value.companyID == 0) {
          $('#config_container').append("<div><span>" + value.functionName + "</span> <label><input type=checkbox name=function value=" +
            value.functionID + ">현재상태 OFF</label></div>");
        }
        else {
          $('#config_container').append("<div><span>" + value.functionName + "</span> <label><input type=checkbox name=function value=" +
            value.functionID + " checked>현재상태 ON</label></div>");
        }

      });
      $('#config_container').append(" <a id ='addFuncBtn' onclick = javascript:clickSaveFunctionChange(this) style=cursor:pointer>저장하기</a>" +
        "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
    }
  });
}
//기능변경사항 저장하기 버튼
function clickSaveFunctionChange() {
  var functionDataList = new Array();

  $("input[name=function]").each(function () {
    var functionData = new Object();
    functionData.functionID = $(this).val();
    if ($(this).is(":checked")) {
      functionData.functionCheck = 1;
    }
    else {
      functionData.functionCheck = 0;
    }
    functionDataList.push(functionData);
  });

  var jsonData = JSON.stringify(functionDataList);
  var askSave = confirm("기능변경 내용을 저장하시겠습니까?");
  if (askSave) {
    $.ajax({
      type: 'POST',                 //get방식으로 통신
      url: "/board/function-change",    //탭의 data-tab속성의 값으로 된 html파일로 통신
      data: { functionInfoData: jsonData },
      error: function () {  //통신 실패시
        alert('통신실패!');
      },
      success: function () {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
        alert("기능이 변경되었습니다.");
      }
    });
  }

  $('#config_container').html("");
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
    url: '/board/' + boardID + "/postlist",    //탭의 data-tab속성의 값으로 된 html파일로 통신
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
});

// 게시글 작성 버튼 이벤트
function click_write() {
    $('postlist').append("test");
    $('#writecontent').css("display", "");
    $('#btn_write').css("display", "none");
    $('#editorcontent').append("<h2> 게시글제목 </h2> <input type=text id=post_title />");
    $('#editorcontent').append("<textarea id=editor></textarea>");
    $('#editorcontent').append("<button id=btn_post onclick=javascript:click_post(this)>올리기</button>")
    $('#editor').ckeditor();
}

// 게시글 작성 후 올리기 버튼 클릭
function click_post() {
    var postTitle = $('#post_title').val();
    var postContent = CKEDITOR.instances.editor.getData();
    var boardID = $('#post_board_id option:selected').attr('data-tab');
    $('#editorcontent').html("");
    $('#writecontent').css("display", "none");
    $('#btn_write').css("display", "");
    $.ajax({
      type: 'POST',
      url: "/boards/" + boardID + "/post/",
      data: {postTitle: postTitle,
            postContent: postContent},
      error: function() {
        alert('게시글 작성 실패');
      },
      success: function() {
        refreshPostList();
        console.log("게시글 저장 완료");
      }
    });

}

// 게시글 조회 후 수정 버튼 클릭
$(document).on("click", "#btn_updatePost", function () {
    var postID = $("#currentPostID").html();
    $('#postcontent').html("");
    if($('#editorcontent').html() == "")
      click_write(); // 게시글 작성 폼 불러오기
    $('#btn_post').html('수정하기'); // 게시글 올리기 버튼 텍스트 변경
    $('#btn_post').attr('id', 'btn_update'); // 저장 버튼 ID 변경
    $('#btn_update').attr('onclick', 'javascript:updatePost(this)'); // 수정게시글 저장 이벤트 등록
    $('#editorcontent').append("<a id=currentPostID style=visibility:hidden>" + postID + "</a>"); // 현재 게시글 ID hidden 으로 저장
    // 게시글 조회
    $.ajax({
        type: 'GET',
        url: "/boards/" + boardID + "/posts/" + postID,
        async: false,
        error: function() {
          alert('게시글 수정 실패');
        },
        success: function (data) {
          $('#post_title').val(data.postTitle);
          CKEDITOR.instances.editor.setData(data.postContent);
        }
    });
});


// 수정한 게시물 서버에 저장할 때 이벤트
function updatePost() {
  var postID = $("#currentPostID").html();
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#post_board_id option:selected').attr('data-tab');
  $('#editorcontent').html("");
  $('#writecontent').css("display", "none");
  $('#btn_write').css("display", "");
  $.ajax({
    type: 'PUT',
    url: "/boards/" + boardID + "/posts/" + postID,
    async: false,
    data: {postTitle: postTitle,
      postContent: postContent},
    error: function() {
      alert('게시글 수정 실패');
    },
    success: function (data) {
      refreshPostList();
    }
  });
}


// 게시글 조회 후 삭제 버튼 클릭
$(document).on("click", "#btn_deletePost", function () {
    var postID = $("#currentPostID").html();
    $('#postcontent').html("");
    console.log("들어왔니?" + postID);
    $.ajax({
        type: 'DELETE',
        url: "/boards/" + boardID + "/post/" + postID,
        async: false,
        error: function() {
          alert('게시글 삭제 실패');
        },
        success: function (data) {
          refreshPostList();
        }
    });
});

// 게시글 작성, 수정, 삭제 시 해당 게시판 refresh 하는 함수
function refreshPostList() {
  var tabs = $('#tab_id').children();
  var boardID = 0;
  $.each(tabs, function() {
    var color = $(this).css('background-color');
    if(color == "rgb(144, 238, 144)") {
      boardID = $(this).attr('data-tab');
    }
  });
  
  if(boardID != 0) {
    $.ajax({
      type: 'GET',
      url: '/board/' + boardID + "/postlist",
      async: false,
      error: function () {
        alert('통신실패!');
      },
      success: function (data) {
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
        });
      }
    });
  }
}