//새로운 탭 내용으로 교체
function updateTab(data){
      $('#tab_id').html("");
      $.template("tabListTmpl","<li data-tab=${boardID} class=tabmenu id=default> ${boardName} </li>");
      $.tmpl("tabListTmpl", data).appendTo("#tab_id");
}
//게시글 내용
function loadPostContent(data){
     var postContentHtml = "";
          postContentHtml += "<h2>" + data.postTitle + "</h2>";
          postContentHtml += "<h5>작성자 : " + data.userName + "</h4>";
          postContentHtml +="<h5>작성시간 : " + data.postRegisterTime + "</h4>";
          postContentHtml +="<a>" + data.postContent + "</a>" ;
          postContentHtml +="<a id=postID style=visibility:hidden>" + data.postID + "</a>";
          $('#postcontent').html(postContentHtml);
}
//게시글 목록
function loadPostList(data){
    $('#postlist').html("");
    var postContentHtml =  "<tr height='30' class = 'postclick' data-post = ${postID}"+
                         " onclick='javascript:clickTrEvent(this)' onmouseover = 'javascript:changeTrColor(this)' >" +
                         "<td width='379'>${postTitle}</td>" +
                         "<td width='73'>${userName}</td>" +
                         "<td width='164'>${postRegisterTime}</td></tr>" +
                         "<td style='visibility:hidden'>${postID}</td>";
    $.template("postListTmpl",postContentHtml);
    $.tmpl("postListTmpl", data).appendTo("#postlist");
}


//탭 업데이트 새로운 게시판 목록으로
function updateTabByNewBoardListAfterAddBoard(boardName) {
  $.ajax({
    type: 'POST',
    url: "/boards/" + boardName,
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //들어오는 data는 boardDTOlist
        updateTab(data);//새로운 탭 내용으로 교체
    }
  });
}

//게시판 삭제후 탭업데이트
function updateTabByNewBoardListAfterDeleteBoard(jsonData) {
  $.ajax({
    type: 'DELETE',
    url: "/boards/list",
    data: { deleteList: jsonData },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      updateTab(data);//새로운 탭 내용으로 교체
    }
  });
  $('#config_container').html("");
}

//게시판 이름변경후 탭업데이트
function updateTabByNewBoardListAfterUpdateBoardName(jsonData) {
  $.ajax({
    type: 'POST',
    url: "/boards/newtitles",
    data: { newTitles: jsonData },
    error: function (error) {  //통신 실패시
      alert(error);
    },
    success: function (data) {
        updateTab(data);//새로운 탭 내용으로 교체
    }
  });
}

//삭제를 위한 리스트 받아오기
function getBoardListToDelete() {
  $.ajax({
    type: 'GET',
    url: '/boards/list',
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      $('#config_container').html("삭제할 게시판을 선택하시오");
      $.template("deleteListTmpl","<div><span>${boardName}</span><input type=checkbox name=boardDelete value=${boardID}></div>")
      $.tmpl("deleteListTmpl", data).appendTo("#config_container");
      $('#config_container').append(" <a id ='addFuncBtn' onclick = javascript:clickSaveDelteBoard(this) style=cursor:pointer>삭제하기</a>" +
        "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
    }
  });
}

//게시물 클릭후 게시물 데이터 받아오기
function getPostDataAfterPostClick(postID, boardID) {
  var postContentObj = $('#postcontent');
  postContentObj.html("");
  $.ajax({
    type: 'GET',
    url: "/boards/" + boardID + "/posts/" + postID,
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      $('#writecontent').hide();
      $('#btn_write').show();

      //게시글 내용 출력
     loadPostContent(data);

      // 작성글의 userID와 현재 로그인한 userID가 같으면 삭제버튼 표시
      var commentAbleObj = $('#functionAble1');
      if (data.canDelete == true) {
        postContentObj.append(
        "</br><button id=btn_updatePost>수정</button>"+
        "</br><button id=btn_deletePost>삭제</button><br>"
        );
      }
            var postContentHtml ="";

      if (commentAbleObj.attr("value") == "on") {

        $(function () {
        postContentObj.append("<br><br><div class= comment_section <div><span><strong>댓글</strong></span> <span id=commentCount></span></div>");
        postContentHtml +="<div class = comment_list_container></div>";
        postContentHtml +="<div class = comment_input_container></div>";
        postContentObj.append(postContentHtml);

          getCommentAllContents(postID, boardID); //삭제이후 tab에 게시판목록 업데이트 //CommentAjax.js 에 있음
        });
      }
    }
  });
}


//탭클릭후 게시판 목록 불러오기
function getPostsAfterTabClick(boardID) {
  $.ajax({
    type: 'GET',
    url: '/boards/' + boardID + "/posts",
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      loadPostList(data);
    }
  });
}

