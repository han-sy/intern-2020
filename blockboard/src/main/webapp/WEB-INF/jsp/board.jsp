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
    }
  </style>

</head>

<body>
  <script>
    $(function () {
      // tab operation
      $('.tabmenu').click(function () {
        var activeTab = $(this).attr('data-tab');
        $('li').css('background-color', 'white');
        $(this).css('background-color', 'lightgreen');
        $(this).css('color', 'white');
      });

    });
  </script>
  <h1> 게시판</h1>
  <h3>Login ID : <%=(String)session.getAttribute("USER") %></h3>
  <a href="logout">로그아웃</a>
  <div id="container">
    <ul class="tab">
      <c:forEach items="${list}" var="list" varStatus="status">
        <li data-tab="tab${status.count+1}" class='tabmenu' id="default"><a href="#">
            <c:out value="${list.board_name}" />
          </a>
        </li>
      </c:forEach>
    </ul>
    <div id="tabcontent">
      <table width="90%" cellpadding="0" cellspacing="0" border="0">
        <tr height="5">
          <td width="5"></td>
        </tr>
        <tr>
          <td width="5"><img src="img/table_left.gif" width="5" height="30" /></td>
          <td width="73">번호</td>
          <td width="379">제목</td>
          <td width="73">작성자</td>
          <td width="164">작성일</td>
          <td width="7"><img src="img/table_right.gif" width="5" height="30" /></td>
        </tr>
        <tr height="25" align="center">
        </tr>
        <tr height="1" bgcolor="#D2D2D2">
          <td colspan="6"></td>
        </tr>

        <tr height="1" bgcolor="#82B5DF">
          <td colspan="6" width="752"></td>
        </tr>
      </table>
      <section class="content">
        <h4> hello jk</h4>
        <p>hello hello hello</p>
      </section>
    </div>
  </div>

  </div>

</body>

</html>