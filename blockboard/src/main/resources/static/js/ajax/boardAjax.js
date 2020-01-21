//탭 업데이트 새로운 게시판 목록으로
function updateTabByNewBoardListAfterAddBoard(boardName){
    $.ajax({
        type: 'POST',                 //get방식으로 통신
        url: "/boards/" + boardName + "/newboard",    //탭의 data-tab속성의 값으로 된 html파일로 통신
        error: function () {  //통신 실패시
          alert('통신실패!');
        },
        success: function (data) {    //들어오는 data는 boardDTOlist
          var tabUlObj = $('#tab_id');
          tabUlObj.html("");
          $.each(data, function (key, value) {
            tabUlObj.append("<li data-tab=" + value.boardID + "  class=tabmenu id=default>" + value.boardName + "</li>");
          });
        }
      });
}

function updateTabByNewBoardListAfterDeleteBoard(jsonData){
    $.ajax({
          type: 'DELETE',
          url: "/boards/list",    //탭의 data-tab속성의 값으로 된 html파일로 통신
          data: { deleteList: jsonData },
          error: function () {  //통신 실패시
            alert('통신실패!');
          },
          success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
            var tabUlObj =$('#tab_id');
            tabUlObj.html("");
            $.each(data, function (key, value) {
              tabUlObj.append("<li data-tab=" + value.boardID + "  class=tabmenu id=default>" + value.boardName + "</li>");
            });
          }
        });
        $('#config_container').html("");
}

function getBoardListToDelete(){
    $.ajax({
        type: 'GET',
        url: '/boards/list',    //탭의 data-tab속성의 값으로 된 html파일로 통신
        error: function () {  //통신 실패시
          alert('통신실패!');
        },
        success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
          var containerObj = $('#config_container');
          containerObj.html("삭제할 게시판을 선택하시오");
          $.each(data, function (key, value) {
            containerObj.append("<div><span>" + value.boardName + "</span><input type=checkbox name=boardDelete value=" +
              value.boardID + "></div>");
          });
          containerObj.append(" <a id ='addFuncBtn' onclick = javascript:clickSaveDelteBoard(this) style=cursor:pointer>삭제하기</a>" +
            "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
        }
      });
}

function getPostDataAfterPostClick(postID, boardID){
  var postContentObj = $('#postcontent');
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
          //console.log("comment 여부 : " + commentAbleObj.attr("value"));

          if (data.canDelete == true) {
            postContentObj.append("</br><button id=btn_updatePost>수정</button>");
            postContentObj.append("</br><button id=btn_deletePost>삭제</button>");
          }


          if (commentAbleObj.attr("value") == "on") {

          $(function () {

                          getCommentAllContents(postID, boardID, postContentObj); //삭제이후 tab에 게시판목록 업데이트
                     });

          }


        }
      });
}

