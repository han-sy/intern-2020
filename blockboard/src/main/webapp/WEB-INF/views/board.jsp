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
<<<<<<< HEAD:blockboard/src/main/webapp/WEB-INF/views/board.jsp
  <script src="/static/ckeditor/ckeditor.js"></script>
  <script src="/static/ckeditor/adapters/jquery.js"></script>

  <style>
    #container {
      width: 100%;
      margin: 0 auto;
      text-align: center;
    }
    .tab {
      list-style: none;
      margin: 0;
      padding: 0;
      overflow: hidden;
    }
    #tabcontent {
      display: block;
      background-color: lightgreen;
      padding: 6px 12px;
      color: black;
    }
    #postcontent {
      display: block;
      background-color: lightgreen;
      padding: 6px 12px;
      color: black;
    }
    a {
      display: inline-block;
      color: #000;
      text-align: center;
      text-decoration: none;
      padding: 14px 16px;
      font-size: 17px;
      transition: 0.3s;
    }
    a:visited {
      color: black;
      text-decoration: none;
    }
    li {
      float: left;
      display: inline-block;
      color: #000;
      text-align: center;
      text-decoration: none;
      padding: 14px 16px;
      font-size: 17px;
    }
  </style>

  <script src="/static/js/event.js"></script>
=======



  <script src="/static/SE2/js/service/HuskyEZCreator.js" charset="utf-8"></script>
  <script type="text/javascript">
    var oEditors = [];
    nhn.husky.EZCreator.createInIFrame({
      oAppRef : oEditors,
      elPlaceHolder : "smarteditor",
      //SmartEditor2Skin.html 파일이 존재하는 경로
      sSkinURI : "/static/SE2/SmartEditor2Skin.html",
      htParams : {
        // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
        bUseToolbar : true,
        // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
        bUseVerticalResizer : true,
        // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
        bUseModeChanger : true,
        fOnBeforeUnload : function() {
        }
      },
      fOnAppLoad : function() {
        //기존 저장된 내용의 text 내용을 에디터상에 뿌려주고자 할때 사용
        oEditors.getById["smarteditor"].exec("PASTE_HTML", [ "" ]);
      },
      fCreator : "createSEditor2"
      });
  </script>

  <link rel="stylesheet" type="text/css" href="/static/css/boardstyle.css">
  <script src="/static/js/event.js"></script>
  <script type="text/javascript">
    // 게시판에서 '글쓰기' 버튼 클릭하면 화면에 에디터 표시
    $(function () {
      $('#btn_write').click(function () {
        $('#writecontent').show();
        $('#postcontent').html("");
        $(this).hide();
      });
    });

// 에디터에서 '글쓰기' 버튼 클릭하면 게시글 저장
    $(function () {
      $('#btn_postwrite').click(function () {
        $('#writecontent').hide();
        $('#btn_write').show();
      });
    });
  </script>
>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11:blockboard/src/main/webapp/WEB-INF/jsp/board.jsp
</head>

<body>

  <script>

  </script>

  <script>
    $(function () {
      $('#writecontent').hide();
      $('#btn_write').show();
    });
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
                "<tr height='30' class = 'postclick' data-post = '" + value.post_id +
                "' onclick='javascript:clickTrEvent(this)' onmouseover = 'javascript:changeTrColor(this)' >" +
                "<td width='73'>" + value.post_id + "</td>" +
                "<td width='379'>" + value.post_title + "</td>" +
                "<td width='73'>" + value.user_name + "</td>" +
                "<td width='164'>" + value.post_reg_time + "</td></tr>"
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


<<<<<<< HEAD:blockboard/src/main/webapp/WEB-INF/views/board.jsp
=======
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

>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11:blockboard/src/main/webapp/WEB-INF/jsp/board.jsp

  </script>
  <h1>${com_name} 게시판</h1>
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
      <li data-tab="${list.board_id}" class='tabmenu' id="default">
        <c:out value="${list.board_name}" />
      </li>
    </c:forEach>
  </ul>

<<<<<<< HEAD:blockboard/src/main/webapp/WEB-INF/views/board.jsp
  <div id="writecontent" style="display:none">
    <h2> 글쓰기 영역 - 우혁 부분 </h2>
    <div id="boardlistcontent">
      <select id="post_board_id">
        <c:forEach items="${list}" var="list" varStatus="status">
          <option data-tab="${list.board_id}" class='tabmenu' id="default">
            <c:out value="${list.board_name}" />
          </option>
        </c:forEach>
      </select>
    </div>
    <div id="editorcontent">
      게시글 제목<input id="post_title" type="text"/> 
      <textarea class="ckeditor" id ="editor" name="editor" rows="6" cols="80" placeholder = "내용을 입력하세요"></textarea>
      <input id="btn_postwrite" type="submit" type="button" value="글쓰기"/>
    </div>
=======
  <div id="writecontent">
    <h2> 글쓰기 영역 - 우혁 부분 </h2>
    <input type="text" id="post_title" placeholder="게시글 제목"/>
    <textarea name="smarteditor" id="smarteditor" rows="10" cols="100" placeholder="내용을 입력하세요">afwefwe</textarea></br>
    <input id="btn_postwrite" type="submit" value="글쓰기"/>
>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11:blockboard/src/main/webapp/WEB-INF/jsp/board.jsp
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
<<<<<<< HEAD:blockboard/src/main/webapp/WEB-INF/views/board.jsp
      <input type="button" id="btn_write" type="button" value="글쓰기"></button>
=======
      <button id="btn_write" type="button">글쓰기</button>
>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11:blockboard/src/main/webapp/WEB-INF/jsp/board.jsp
  </div>

</body>

</html>