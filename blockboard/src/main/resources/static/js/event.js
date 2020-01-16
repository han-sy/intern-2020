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
    url: "/board/postlist/"+postID,    //탭의 data-tab속성의 값으로 된 html파일로 통신
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
    }
  });
}

// 게시판 추가버튼 클릭시
function clickaddBoardBtn(){
      $('#config_container').html("<input type='text' name='게시판 이름' id = 'input_board_name' class='addBoard' placeholder='게시판 이름'>");
      $('#config_container').append("<br>");
      $('#config_container').append(" <a id ='addFuncBtn' onclick = javascript:clickSaveaddedBoard(this) style=cursor:pointer>저장하기</a>"+
      "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
      console.log("111");
}

 //게시판 저장하기 버튼
function clickSaveaddedBoard(){
   console.log($('#input_board_name').val());
   $.ajax({
             type: 'POST',                 //get방식으로 통신
             url: "/board/newboard",    //탭의 data-tab속성의 값으로 된 html파일로 통신
             data: { boardName: $('#input_board_name').val() },
             error: function () {  //통신 실패시
               alert('통신실패!');
             },
             success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
               console.log(data.boardID+"삽입성공");
               $("#tab_id").append("<li data-tab="+data.boardID +" class='tabmenu' id=default> "+data.boardName+" </li>");
             }
           });

    $('#config_container').html("");
 }

//기능추가 버튼 클릭시
function changeFunction(){
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
          $('#config_container').append(value
          );
          //alert($this);
        });
      }
    });
  //$('#config_container').html("<div><span>댓글</span> <button class='functionButton' id = 'function-reply' type='button'>OFF</button></div>");
  //$('#config_container').append("<div><span>대댓글</span> <button class = 'functionButton' id = 'function-reReply' type='button'>OFF</button></div>");
  //$('#config_container').append("<div><span>파일첨부</span> <button class = 'functionButton' id = 'function-attachedFile' type='button'>OFF</button></div>");
  //$('#config_container').append("<div><span>inline 이미지</span> <button class = 'functionButton' id = 'function-inlineImage' type='button'>OFF</button></div>");
  //$('#config_container').append("<div><span>임시저장</span> <button class = 'functionButton' id = 'function-temporarySave' type='button'>OFF</button></div>");
  //$('#config_container').append("<div><span>스티커</span> <button class = 'functionButton' id = 'function-sticker' type='button'>OFF</button></div>");
  //$('#config_container').append("<br><div><button class = 'functionSave' type='button'>저장하기</button>"+
  //" <button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button></div>");
}

//닫기 버튼 클릭
$(document).on('click','.functionClose',clickConfigClose());
function clickConfigClose(){
    console.log("닫기버튼");
    $('#config_container').html("");
}

// 탭 메뉴 클릭 이벤트 - 해당 게시판의 게시글 불러옴
$(document).on("click",".tabmenu",function clickTabEvent() {
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
      url: '/board/'+boardID+"/postlist",    //탭의 data-tab속성의 값으로 된 html파일로 통신
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
            "<td width='73'>" + value.postID + "</td>" +
            "<td width='379'>" + value.postTitle + "</td>" +
            "<td width='73'>" + value.userName + "</td>" +
            "<td width='164'>" + value.postRegisterTime + "</td></tr>"
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
    $('#editorcontent').html("게시판선택");
    $('#editorcontent').html("게시글제목<input type=text id=post_title />");
    $('#editorcontent').append("<textarea id=editor></textarea>");
    $('#editorcontent').append("<button id=btn_post onclick=javascript:click_post(this)>올리기</button>")
    $('#editor').ckeditor();
}

// 게시글 작성 후 올리기 버튼 클릭
function click_post() {
    var postTitle = $('#post_title').val();
    var postContent = CKEDITOR.instances.editor.getData();
    var boardName = $('#post_board_id option:selected').val();
    console.log("제목:" + postTitle);
    console.log("내용:" + postContent);
    console.log("선택한 게시판:" + boardName);
    $('#editorcontent').html("");
    $('#writecontent').css("display", "none");
    $('#btn_write').css("display", "");
    $.ajax({
      type: 'POST',
      url: "/board/post/insert",
      data: {postTitle: postTitle,
            postContent: postContent,
            boardName: boardName},
      error: function() {
        alert('게시글 작성 실패');
      },
      success: function() {
        console.log("게시글 저장 완료");
      }
    });
}

