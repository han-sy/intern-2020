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
                  $('#postcontent').html("");
                  $('#postcontent').append("<h3>"+data.post_title+"</h3>");
                  $('#postcontent').append("<h5>작성자 :+"+data.user_name+"</h4>");
                  $('#postcontent').append("<h5>작성시간 :+"+data.post_reg_time+"</h4>");
                  $('#postcontent').append("<a>"+data.post_content+"</a>");
                }
              });
    }
  </script>
</head>

<body>
  <script>
    $(function () {
      // tab operation
      $('.tabmenu').click(function () {
        //var activeTab = $(this).attr('data-tab');
        var activeTab = $(this).attr('data-tab');
        //console.log(activeTab);
        $('li').css('background-color', 'white');
        $(this).css('background-color', 'lightgreen');
        $('#postcontent').html("");
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
                "<tr class = 'postclick' data-post = '" + value.post_id +
                "' onclick='javascript:clickTrEvent(this)' onmouseover = 'javascript:changeTrColor(this)' ><td width='5'><img src='img/table_left.gif' width='5' height='30' /></td>" +
                "<td width='73'>" + value.post_id + "</td>" +
                "<td width='379'>" + value.post_title + "</td>" +
                "<td width='73'>" + value.user_name + "</td>" +
                "<td width='164'>" + value.post_reg_time + "</td></tr>" +
                "<td width='7'><img src='img/table_right.gif' width='5' height='30' /></td>"
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
  <h1> 게시판</h1>
  <h3>Login ID : <%=(String)session.getAttribute("USER") %></h3>
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
  <div id="postcontent"></div>
  <div id="tabcontent">
    <table width="90%" cellpadding="0" cellspacing="0" border="0">
      <thead>
        <tr height="5">
          <td width="5"></td>
        </tr>
        <tr>
          <td width='5'><img src='img/table_left.gif' width='5' height='30' /></td>
          <td width='73'>번호</td>
          <td width='379'>제목</td>
          <td width='73'>작성자</td>
          <td width='164'>작성일</td>
          <td width='7'><img src='img/table_right.gif' width='5' height='30' /></td>
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

  </div>

</body>

</html>