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
    url: "/board/post",    //탭의 data-tab속성의 값으로 된 html파일로 통신
    data: { postID: postID },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      console.log("success" + data);
      $('#writecontent').hide();
      $('#editorcontent').html("");
      $('#btn_write').show();
      $('#postcontent').html("");
      $('#postcontent').append("<h2>" + data.postTitle + "</h2>");
      $('#postcontent').append("<h5>작성자 : " + data.userName + "</h4>");
      $('#postcontent').append("<h5>작성시간 : " + data.postRegisterTime + "</h4>");
      $('#postcontent').append("<a>" + data.postContent + "</a>");
      $('#postcontent').append("<a id=currentPostID style=visibility:hidden>" + data.postID + "</a>");
      // 작성글의 userID와 현재 로그인한 userID가 같으면 삭제버튼 표시
      console.log("canDelete = " + data.canDelete);
      if(data.canDelete == true) {
        $('#postcontent').append("</br><button id=btn_updatePost>수정</button>");
        $('#postcontent').append("</br><button id=btn_deletePost>삭제</button>");
      }
    }
  });
}

// 게시판 추가버튼 클릭시
function clickaddBoardBtn(){
      $('#editorcontent').html("");
      $('#config_container').html("<input type='text' name='게시판 이름' id = 'input_board_name' class='addBoard' placeholder='게시판 이름'>");
      $('#config_container').append("<br>");
      $('#config_container').append(" <a id ='addFuncBtn' onclick = javascript:clickSaveaddedBoard(this) style=cursor:pointer>저장하기</a>");
      console.log("111");
}

 //저장하기 버튼
function clickSaveaddedBoard(){
   console.log($('#input_board_name').val());
   $.ajax({
             type: 'GET',                 //get방식으로 통신
             url: "/board/addboard",    //탭의 data-tab속성의 값으로 된 html파일로 통신
             data: { boardName: $('#input_board_name').val() },
             error: function () {  //통신 실패시
               alert('통신실패!');
             },
             success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
               //console.log(data.board_id+"삽입성공");
               $("#tab_id").append("<li data-tab="+data.board_id +" class='tabmenu' id=default> "+data.boardName+" </li>");
             }
           });

    $('#config_container').html("");
    location.reload();
 }

//기능추가 버튼 클릭시
function changeFunction(){

  $('#config_container').html("<span>댓글기능</span>");
  $('#config_container').append("<div class='toggleBG'> "+
  "<button class='toggleFG'></button>"+
  "</div>");
  $('#config_container').append("<p id = 'check_val'>OFF</p>");
}

function moveToggle(toggleBtn, LR,l,r) {
    // 0.01초 단위로 실행
    console.log(l+","+r)
    var intervalID = setInterval(
        function() {
            // 버튼 이동
            var toggle = parseInt(toggleBtn.css('left'));
            toggle += (LR == 'TO_RIGHT') ? 5 : -5;
            if(toggle >= l && toggle <= r) {
                toggle += 'px';
                toggleBtn.css('left', toggle);
            }
        }, 10);
    setTimeout(function(){
        clearInterval(intervalID);
    }, 201);
}



// 기능 토글 버튼 클릭 이벤트
$(document).on('click', '.toggleBG', function clickToggleButton() {
    var toggleBG = $(this);
    var toggleFG = $(this).find('.toggleFG');
    console.log($("#check_val").text());
    if($("#check_val").text()=="ON") {
        var right = toggleFG.css('left').replace(/[^\d]/g, '');
        var left= right-20;
        toggleBG.css('background', '#CCCCCC');
        moveToggle(toggleFG, 'TO_LEFT',left,right);
        $("#check_val").html("OFF");
        console.log($("#check_val").text());
    }else if($("#check_val").text()=="OFF") {
        var left = toggleFG.css('left').replace(/[^\d]/g, '');
        var right = left+20;
        toggleBG.css('background', '#53FF4C');
        moveToggle(toggleFG, 'TO_RIGHT',left,right);
        $("#check_val").html("ON");
        console.log($("#check_val").text());
    }
});

// 탭 메뉴 클릭 이벤트 - 해당 게시판의 게시글 불러옴
$(document).on("click",".tabmenu",function clickTabEvent() {
    //var activeTab = $(this).attr('data-tab');
    var activeTab = $(this).attr('data-tab');
    console.log(activeTab);
    $('li').css('background-color', 'white');
    $(this).css('background-color', 'lightgreen');
    $('#postcontent').html("");
    $('#writecontent').hide();
    $('#editorcontent').html("");
    $('#btn_write').show();
    $.ajax({
      type: 'GET',                 //get방식으로 통신
      url: "/board/tab",    //탭의 data-tab속성의 값으로 된 html파일로 통신
      data: { activeTab: activeTab },
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
      url: "/board/post/insert",
      data: {postTitle: postTitle,
            postContent: postContent,
            boardID: boardID},
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
        url: "/board/post/" + postID,
        async: false,
        data: {postID : postID},
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
    url: "/board/post/update",
    async: false,
    data: {postID: postID,
      postTitle: postTitle,
      postContent: postContent,
      boardID: boardID},
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
        url: "/board/post/delete",
        async: false,
        data: {postID : postID},
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
  var activeTab = 0;
  $.each(tabs, function() {
    var color = $(this).css('background-color');
    if(color == "rgb(144, 238, 144)") {
      activeTab = $(this).attr('data-tab');
    }
  });
  if(activeTab != 0) {
    $.ajax({
      type: 'GET',
      url: "/board/tab",
      data: { activeTab: activeTab },
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