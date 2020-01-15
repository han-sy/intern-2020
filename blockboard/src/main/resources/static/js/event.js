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

// 글쓰기 버튼 클릭 이벤트
function post_btn_clickEvent() {
  $('#btn_write').click(function() {
    $('#writecontent').css("display", "");
    $('#btn_write').css("display", "none");
    $('#editorcontent').html("게시판선택");
    $('#editorcontent').html("게시글제목 </br> <input type=text id=post_title />");
    $('#editorcontent').append("<textarea id=editor></textarea>");
    $('#editorcontent').append("<input type=button id=btn_post value=올리기 />")
    $('#editor').ckeditor();

    // 올리기 버튼 클릭
    $('#btn_post').click(function () {
      var post_title = $('#post_title').val();
      var post_content = CKEDITOR.instances.editor.getData();
      var board_name = $('#post_board_id option:selected').val();
      console.log("제목:" + post_title);
      console.log("내용:" + post_content);
      console.log("선택한 게시판:" + board_id);
      $('#editorcontent').html("");
      $('#writecontent').css("display", "none");
      $('#btn_write').css("display", "");
      $.ajax({
      type: 'POST',
      url: "/board/post/write",
      data: {post_title: post_title,
            post_content: post_content,
            board_name: board_name},
      error: function() {
        alert('게시글 작성 실패');
      },
      success: function() {
        console.log("게시글 저장 완료");
      }
      });

    });
  });
});