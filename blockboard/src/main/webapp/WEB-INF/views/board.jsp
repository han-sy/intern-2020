<!-- board.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>

  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>mysql 연동</title>
  <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
  <link rel="stylesheet" type="text/css" href="/static/css/boardstyle.css">
  <script src="/static/js/event.js"></script>
  <script src="/static/ckeditor/ckeditor.js"></script>
  <script src="/static/ckeditor/adapters/jquery.js"></script>
</head>

<body>
  <script>
    $(function () {
      // tab operation
      $(document).on("click",".tabmenu",function () {
        //var activeTab = $(this).attr('data-tab');
        var activeTab = $(this).attr('data-tab');
        console.log(activeTab);
        $('li').css('background-color', 'white');
        $(this).css('background-color', 'lightgreen');
        $('#postcontent').html("");
        $('#writecontent').hide();
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
    });
    $(function () {
      var table = $('#postlist').DataTable();
      $('.postclick').click(function () {
        var activeRow = table.rows(this).data();
        console.log("row : ", activeRow);
        //alert("row : ", activeRow);
        $('#postcontent').append(
          activeRow
        );
      });
    });


$(document).on('click', '.toggleBG', function () {
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


  </script>
  <h1>${companyName} 게시판</h1>
  <!--게시판 하위에 관리자일 경우 추가되는 버튼 (기능변경, 게시판추가)-->
  <c:if test="${isadmin}">
        <a id ='addFuncBtn'  onclick = "javascript:changeFunction(this)" style ="cursor:pointer">기능 변경</a>
        <a id ='addBoardBtn' onclick = "javascript:clickaddBoardBtn(this)"  style="cursor:pointer">게시판 추가</a>
  </c:if>

  <a href="logout">로그아웃</a>
  <div id = "config_container">
      <!--게시판 추가버튼 누를때 -->
    </div>
  <div id="container">

  </div>
  <ul class="tab" id = "tab_id">
    <c:forEach items="${list}" var="list" varStatus="status">
      <li data-tab="${list.boardID}" class='tabmenu' id="default">
        <c:out value="${list.boardName}" />
      </li>
    </c:forEach>
  </ul>

  <div id="writecontent" style="display:none">
    <h2> 글쓰기 영역 - 우혁 부분 </h2>
    <div id="boardlistcontent">
      <select id="post_board_id">
        <c:forEach items="${list}" var="list" varStatus="status">
          <option data-tab="${list.boardID}" class='tabmenu' id="default">
            <c:out value="${list.boardName}" />
          </option>
        </c:forEach>
      </select>
    </div>
    <div id="editorcontent"></div>
  </div>

  <div id="postcontent"></div>
  <div id="tabcontent">
    <table width="90%" cellpadding="0" cellspacing="0" border="0">
      <thead>
        <tr height="5">
          <td width="5"></td>
        </tr>
        <tr>
          <td width='73'>번호</td>
          <td width='379'>제목</td>
          <td width='73'>작성자</td>
          <td width='164'>작성일</td>
        </tr>
        <tr height="25" align="center">
        </tr>
        <tr height="1" bgcolor="#D2D2D2">
          <td colspan="6"></td>
        </tr>

        <tr height="1" bgcolor="#82B5DF">
          <td colspan="6" width="752"></td>
        </tr>
      </thead>
      <tbody id="postlist">

      </tbody>
      <!--게시글 목록 -->

    </table>
  </div>
  </div>
      <button id="btn_write" onclick="javascript:click_write(this)">글쓰기</button>
  </div>

</body>

</html>