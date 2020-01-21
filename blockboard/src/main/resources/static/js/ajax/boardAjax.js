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
            console.log(value.functionInfoData);
            containerObj.append("<div><span>" + value.boardName + "</span><input type=checkbox name=boardDelete value=" +
              value.boardID + "></div>");
          });
          containerObj.append(" <a id ='addFuncBtn' onclick = javascript:clickSaveDelteBoard(this) style=cursor:pointer>삭제하기</a>" +
            "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
        }
      });
}