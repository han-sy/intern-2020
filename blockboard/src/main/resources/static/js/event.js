

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

  $.each(tabs, function() {
    var color = $(this).css('background-color');
    if(color == "rgb(144, 238, 144)") {
      boardID = $(this).attr('data-tab');
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





//"기능변경" 버튼 클릭시
function changeFunction() {
  $.ajax({
    type: 'POST',                 //POST 통신
    url: '/boards/function-info',    //탭의 data-tab속성의 값으로 된 html파일로 통신
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
});