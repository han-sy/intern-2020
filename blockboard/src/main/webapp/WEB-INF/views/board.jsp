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
  <script src="resources/SE2/js/service/HuskyEZCreator.js" charset="utf-8"></script>
  <script type="text/javascript">
    var oEditors = [];
    nhn.husky.EZCreator.createInIFrame({
      oAppRef : oEditors,
      elPlaceHolder : "smarteditor",
      //SmartEditor2Skin.html 파일이 존재하는 경로
      sSkinURI : "resources/SE2/SmartEditor2Skin.html",
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
    });
  </script>

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
  <script type="text/javascript">
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
                  $('#postcontent').append("<h2>"+data.post_title+"</h2>");
                  $('#postcontent').append("<h5>작성자 : "+data.user_name+"</h4>");
                  $('#postcontent').append("<h5>작성시간 : "+data.post_reg_time+"</h4>");
                  $('#postcontent').append("<a>"+data.post_content+"</a>");
                }
              });
    }

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
</head>

<body>
  <script>
    $(function () {
      $('#writecontent').hide();
      $('#btn_write').show();
    });
    $(function () {
      // tab operation
      $('.tabmenu').click(function () {
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



  </script>
  <h1>${com_name} 게시판</h1>
  <a href="logout">로그아웃</a>
  <div id="container">

  </div>
  <ul class="tab">
    <c:forEach items="${list}" var="list" varStatus="status">
      <li data-tab="${list.board_id}" class='tabmenu' id="default">
        <c:out value="${list.board_name}" />
        </a>
      </li>
    </c:forEach>
  </ul>

  <div id="writecontent">
    <h2> 글쓰기 영역 - 우혁 부분 </h2>
    <input type="text" id="post_title" placeholder="게시글 제목"/>
    <textarea name="smarteditor" id="smarteditor" rows="10" cols="100" placeholder="내용을 입력하세요">afwefwe</textarea></br>
    <input id="btn_postwrite" type="submit" value="글쓰기"/>
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
      <button id="btn_write" type="button">글쓰기</button>
  </div>

</body>

</html>