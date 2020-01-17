function changeTrColor(trObj) {
  trObj.style.backgroundColor = "green";
  trObj.onmouseout = function () {
    trObj.style.backgroundColor = "lightgreen";
  }
}
function clickTrEvent(trObj) {
  //alert(trObj.getAttribute("data-post"));
  var post_id = trObj.getAttribute("data-post");
  //console.log(post_id);
  //$('#postcontent').html("activerRow : " + trObj.getAttribute("data-post"));
  $.ajax({
    type: 'GET',                 //get방식으로 통신
    url: "/board/post",    //탭의 data-tab속성의 값으로 된 html파일로 통신
    data: { post_id: post_id },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      console.log("success" + data);
      $('#writecontent').hide();
      $('#btn_write').show();
      $('#postcontent').html("");
      $('#postcontent').append("<h2>" + data.post_title + "</h2>");
      $('#postcontent').append("<h5>작성자 : " + data.user_name + "</h4>");
      $('#postcontent').append("<h5>작성시간 : " + data.post_reg_time + "</h4>");
      $('#postcontent').append("<a>" + data.post_content + "</a>");
    }
  });
}
function clickaddBoardBtn(){
      $('#board_add_container').html("<input type='text' name='게시판 이름' id = 'input_board_name' class='addBoard' placeholder='게시판 이름'>");
      $('#board_add_container').append("<br>");
      $('#board_add_container').append(" <a id ='addFuncBtn' onclick = javascript:clickSaveaddedBoard(this) style=cursor:pointer>저장하기</a>");
      console.log("111");
    }
    function clickSaveaddedBoard(){
      console.log($('#input_board_name').val());
      $.ajax({
                type: 'GET',                 //get방식으로 통신
                url: "/board/addboard",    //탭의 data-tab속성의 값으로 된 html파일로 통신
                data: { board_name: $('#input_board_name').val() },
                error: function () {  //통신 실패시
                  alert('통신실패!');
                },
                success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
                  //console.log(data.board_id+"삽입성공");
                  $("#tab_id").append("<li data-tab="+data.board_id +" class='tabmenu' id=default> "+data.board_name+" </li>");
                }
              });

       $('#board_add_container').html("");
    }