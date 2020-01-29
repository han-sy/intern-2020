<!--
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file board.jsp
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>BLOCK BOARD</title>
  <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
  <link rel="stylesheet" type="text/css" href="/static/css/boardstyle.css">
  <script type="text/javascript" src="/js/jquery-1.4.1.min.js"></script>
  <script type="text/javascript" src="./jquery.cookie.js"></script>
  <script type="text/javascript" src="/js/jquery.tmpl.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/handlebars@latest/dist/handlebars.js"></script>
  <script src="/static/js/event/boardEvent.js"></script>
  <script src="/static/js/util/jquery.tmpl.js"></script>
  <script src="/static/js/event/postsEvent.js"></script>
  <script src="/static/js/event/commentEvent.js"></script>
  <script src="/static/js/event/functionEvent.js"></script>
  <script src="/static/js/ajax/functionAjax.js"></script>
  <script src="/static/js/ajax/commentAjax.js"></script>
  <script src="/static/js/ajax/boardAjax.js"></script>
  <script src="/static/ckeditor/ckeditor.js"></script>
  <script src="/static/ckeditor/adapters/jquery.js"></script>
</head>

<body>
  <h1 id="serviceTitle" value=${companyID}>${companyName} 게시판</h1>
  <h3 class="currentUser">User :
    <span id="current_user_id">${userID}</span>(
    <span id="current_user_name">${userName}</span>)
  </h3>
  <!--게시판 하위에 관리자일 경우 추가되는 버튼 (기능변경, 게시판추가)-->

  <div id="fuctionListContainer">
    현재 사용중인 기능 :
    <c:forEach items="${functionInfoList}" var="functionList" varStatus="status">
      <c:if test="${functionList.companyID == 1}">
        <span id=functionAble${functionList.functionID} value=on> ${functionList.functionName} </span>
      </c:if>
      <c:if test="${functionList.companyID == 0}">
        <span id=functionAble${functionList.functionID} style=display:none value=off> ${functionList.functionName}
        </span>
      </c:if>

    </c:forEach>
  </div>
  <c:if test="${isadmin}">
    <br>
    <a id='addFuncBtn' onclick="javascript:changeFunction(this)" style="cursor:pointer">기능 변경</a>
    <a id='addBoardBtn' onclick="javascript:clickaddBoardBtn(this)" style="cursor:pointer">게시판 추가</a>
    <a id='addBoardBtn' onclick="javascript:clickchangeBoardBtn(this)" style="cursor:pointer">게시판 이름변경</a>
    <a id='addBoardBtn' onclick="javascript:clickDeleteBoardBtn(this)" style="cursor:pointer">게시판 삭제</a>
  </c:if>
  <a href="logout">로그아웃</a>
  <div id="config_container">
    <!--게시판 추가버튼 누를때 -->
  </div>
  <!--게시판 삭제 템플릿-->
  <script id="deleteboards-template" type="text/x-handlebars-template">
    <h3>삭제할 게시판을 선택하시오.</h3>
    {{#boards}}
    <div>
      <span>{{boardName}}</span>
      <input type='checkbox' name='boardDelete' value={{boardID}} />
    </div>
    {{/boards}}
    <a id ='addFuncBtn' onclick = javascript:clickSaveDelteBoard(this) style=cursor:pointer>삭제하기</a>
    <button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>
  </script>
  <div id="container">

  </div>
  <ul class="tab" id="tab_id">
    <c:forEach items="${boardList}" var="boardList" varStatus="status">
      <li data-tab="${boardList.boardID}" class='tabmenu' id="default">
        <c:out value="${boardList.boardName}" />
      </li>
    </c:forEach>
  </ul>
  <!--게시판 목록 템플릿-->
  <script id="boards-template" type="text/x-handlebars-template">
    {{#boards}}
      <li data-tab=${boardID} class=tabmenu id=default> {{boardName}} </li>
    {{/boards}}
  </script>

  <div id="writecontent" style="display:none">
    <div id="boardlistcontent">
      <h2> 게시판 선택 </h2>
      <select id="boardIDinEditor">
        <c:forEach items="${boardList}" var="boardList" varStatus="status">
          <option data-tab="${boardList.boardID}" class='tabmenu' id="default">
            <c:out value="${boardList.boardName}" />
          </option>
        </c:forEach>
      </select>
    </div>
    <div id="editorcontent"></div>
  </div>

  <div id="postcontent"></div>
  <!--게시물 내용 템플릿-->
  <script id="postcontent-template" type="text/x-handlebars-template">
    {{#post}}
      <h2>{{postTitle}}</h2>
      <h5>작성자 : {{userName}}</h5>
      <h5>작성시간 : {{postRegisterTime}}</h5>
      <a>{{{postContent}}}</a>
      <a id=postID style="visibility: hidden;">{{postID}}</a>
    {{/post}}
  </script>
  <div id="tabcontent">
    <table width="90%" cellpadding="0" cellspacing="0" border="0">
      <thead>
        <tr height="5">
          <td width="5"></td>
        </tr>
        <tr>
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
      <tbody id="postlist"></tbody>
      <script id="posts-template" type="text/x-handlebars-template">
              {{#posts}}
                <tr height="30" class="postclick" data-post={{postID}} onclick="javascript:clickTrEvent(this)" onmouseover="javascript:changeTrColor(this)">
                  <td width="379">{{postTitle}}</td>
                  <td width="73">{{userName}}</td>
                  <td width="164">{{postRegisterTime}}</td>
                  <td style="visibility:hidden">{{postID}}</td>
                  <td style="visibility:hidden">{{boardID}}</td>
                </tr>
              {{/posts}}
            </script>
      <!--게시글 목록 -->

    </table>
  </div>
  <div>
    <button id="btn_write">글쓰기</button>
  </div>
  <div id="searchcontent">
    <select id="search_option" style="width:100px;font-size:15px;">
      <option>제목</option>
      <option>작성자</option>
      <option>내용</option>
      <option>제목+내용</option>
    </select>
    <input id="search_keyword" type="text" />
    <button id="search">조회</button>
  </div>

</body>

</html>