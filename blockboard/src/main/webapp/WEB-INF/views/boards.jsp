<!--
* @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
* @file board.jsp
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>BLOCK BOARD</title>
    <link rel="stylesheet" href="/webjars/bootstrap/4.4.1/dist/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/static/css/boardstyle.css">
</head>

<body>
<div class="container-fluid row-cols-1">
    <div class="row">
        <a class="navbar-brand" value= ${companyID}>${companyName} 게시판</a>
        <a id="current_user_id">${userID}</a>
        <a id="current_user_name">${userName}</a>
        <div id="fuctionListContainer">
            <h5>현재 사용중인 기능 </h5>
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
        <!--현재 기능 사용 여부 현황 템플릿-->
        <script id="functionList-template" type="text/x-handlebars-template">
            <h5>현재 사용중인 기능</h5>
            {{#functions}}
                {{#isAbleFunction}}
                    <span id='functionAble{{functionID}}' style='display:none;' value='off'>{{functionName}}</span>
                {{else}}
                    <span id='functionAble{{functionID}}' value='on'>{{functionName}}</span>
                {{/isAbleFunction}}
            {{/functions}}
        </script>
        <c:if test="${isadmin}">
            <br>
            <a id='addFuncBtn' onclick="javascript:changeFunction(this)" style="cursor:pointer">기능 변경</a>
            <a id='addBoardBtn' onclick="javascript:clickaddBoardBtn(this)" style="cursor:pointer">게시판 추가</a>
            <a id='addBoardBtn' onclick="javascript:clickchangeBoardBtn(this)" style="cursor:pointer">게시판 이름변경</a>
            <a id='addBoardBtn' onclick="javascript:clickDeleteBoardBtn(this)" style="cursor:pointer">게시판 삭제</a>
        </c:if>
        <a href="<c:url value='/logout' />">로그아웃</a>
    </div>
    <div class="row">
    <div class="col-2">
        <ul class="tab" id="tab_id">
            <c:forEach items="${boardList}" var="boardList" varStatus="status">
                <li data-tab="${boardList.boardID}" class='tabmenu' id="default">
                    <c:out value="${boardList.boardName}"/>
                </li>
            </c:forEach>
            <li data-tab="-1" class=tabmenu id=default> 임시보관함</li>
        </ul>
        <!--게시판 목록 템플릿-->
        <script id="boards-template" type="text/x-handlebars-template">
            {{#boards}}
            <li data-tab={{boardID}} class=tabmenu id=default> {{boardName}}</li>
            {{/boards}}
            <li data-tab="-1" class=tabmenu id=default> 임시보관함</li>
        </script>
    </div>
    <div class="col">
        <div id="config_container">
            <!--게시판 추가버튼 누를때 -->
        </div>
        <!--게시판 삭제 템플릿-->
        <script id="deleteboards-template" type="text/x-handlebars-template">
            <h5>삭제할 게시판을 선택하시오.</h5>
            {{#boards}}
            <div>
                <span>{{boardName}}</span>
                <input type='checkbox' name='boardDelete' value={{boardID}}/>
            </div>
            {{/boards}}
            <a id='deleteBoardBtn' onclick=javascript:clickSaveDelteBoard(this) style=cursor:pointer>삭제하기</a>
            <button class='functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>
        </script>
        <!--게시판 이름변경 템플릿-->
        <script id="changeBoardName-template" type="text/x-handlebars-template">
            <h5>게시판 이름 변경.</h5>
            {{#boards}}
            <div class='boardInfo' id='board{{boardID}}'>
                <input type='text' name='boardname' data-boardid={{boardID}} data-oldname={{boardName}}
                       value={{boardName}}>
                <span class='deleteBoard' data-board='board{{boardID}}'> 기존 게시판 이름 : {{boardName}} </span>
            </div>
            {{/boards}}
            <br>
            <a id='changeBoardNameBtn' onclick=javascript:clickSaveChangeBoard(this) style=cursor:pointer>변경하기</a>
            <button class='functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>
        </script>
        <!--기능변경 템플릿-->
        <script id="changeFunctionInfo-template" type="text/x-handlebars-template">
            <h5>기능 변경</h5>
            {{#functions}}
            {{#isAbleFunction}}
            <div class="btn-group" data-toggle="buttons">
                <label class="btn btn-default _function-switch">
                    <span>{{functionName}}</span>
                    <input type='checkbox' name='function' value={{functionID}}>
                    <span class='_switch'>OFF</span>
                </label>
            </div>
            {{else}}
            <div class="btn-group" data-toggle="buttons">

                <label class="btn btn-success active _function-switch">
                    <span>{{functionName}}</span>
                    <input type='checkbox' name='function' value={{functionID}} checked>
                    <span class='_switch'>ON</span>
                </label>
            </div>
            {{/isAbleFunction}}
            <br>
            <br>
            {{/functions}}
            <a id='saveFuncBtn' onclick=javascript:clickSaveFunctionChange(this) style=cursor:pointer>저장하기</a>
            <button class='functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>
        </script>
        <!--게시글 작성 폼-->
        <div id="writecontent" style="display:none">
            <!--게시글 작성 시 게시판 선택 폼-->
            <div id="boardlistcontent">
                <h2> 게시판 선택 </h2>
                <select id="boardIDinEditor">
                    <c:forEach items="${boardList}" var="boardList" varStatus="status">
                        <option data-tab="${boardList.boardID}" class='tabmenu' id="default">
                            <c:out value="${boardList.boardName}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <!--에디터 폼-->
            <div id="editorcontent">
                <h2>게시글제목</h2>
                <input type="text" id="post_title"/>
                <textarea id="editor"></textarea>
                <button id="btn_post" onclick="javascript:postFunction()">저장</button>
                <button id="btn_cancel" onclick="javascript:writeCancel()">작성취소</button>
                <button id="btn_temp" onclick="javascript:tempsaveFunction()">임시저장</button>
                <div id="editorcontent-hidden">
                </div>
                <script id="postid-template" type="text/x-handlebars-template">
                    <a id="editor_postID" style="visibility:hidden">{{postID}}</a>
                </script>
            </div>
        </div>

        <div id="postcontent"></div>
        <!--게시물 내용 템플릿-->
        <script id="postcontent-template" type="text/x-handlebars-template">
            {{#post}}
            <h2>{{postTitle}}</h2>
            <h5>작성자 : {{userName}}</h5>
            <h5>작성시간 : {{postRegisterTime}}</h5>
            <a>{{{postContent}}}</a>
            <a id="postID" style="visibility: hidden;">{{postID}}</a>
            <br>
            <button id="btn_updatePost" style="visibility:hidden" onclick="javascript:postUpdateFunction()">수정
            </button>
            <button id="btn_deletePost" style="visibility:hidden" onclick="javascript:postDeleteFunction()">삭제
            </button>
            {{/post}}
        </script>
        <!--댓글리스트 템플릿-->
        <script id="commentList-template" type="text/x-handlebars-template">
            {{#comments}}
            <hr>
            <div class='referenceCommentContainer' data-id='{{commentID}}'>
                <div class='commentContainer' id='comment{{commentID}}'>
                    <div>
                        <p class=user>
                            <span class=name data-id={{userID}}>{{userName}}</span>
                            <span class=date>{{commentRegisterTime}}</span>
                        </p>
                        <p class=comment_area id=translate_area>{{{commentContent}}}</p>
                    </div>
                    <div class="btn">
                        {{#isReplyAble}}
                        <button type=button class='replyBtn'>답글</button>
                        {{else}}
                        {{/isReplyAble}}

                        {{#isSameUser}}
                        <button type=button id='edit_comment'>수정</button>
                        <button type=button id='delete_comment'>삭제</button>
                        {{else}}
                        {{/isSameUser}}
                    </div>
                    {{#isReplyAble}}
                    <div class='replyContainer' id='reply_container{{commentID}}'
                         style='padding: 5px 1px 3px 30px;'>
                    </div>
                    <div id='reply_input_container{{commentID}}' style='padding: 5px 1px 3px 30px;'></div>
                    {{else}}
                    {{/isReplyAble}}
                </div>
                <div>

                </div>
            </div>
            {{/comments}}
        </script>
        <!--답글 List 템플릿-->
        <script id="replyList-template" type="text/x-handlebars-template">
            {{#replies}}
            <div class='commentContainer' id='comment{{commentID}}'>
                <div>
                    <p class=user>
                        <span class=name data-id={{userID}}>{{userName}}</span>
                        <span class=date>{{commentRegisterTime}}</span>
                    </p>
                    <p class=comment_area id=translate_area><strong class=nametag
                                                                    data-id={{commentReferencedUserID}}
                                                                    style="cursor:pointer">{{commentReferencedUserName}}</strong>
                        {{{commentContent}}}</p>
                </div>
                <div class=btn>
                    {{#isReplyAble}}
                    <button type=button class='replyBtn'>답글</button>
                    {{else}}
                    {{/isReplyAble}}
                    {{#isSameUser}}
                    <button type=button id='edit_comment'>수정</button>
                    <button type=button id='delete_comment'>삭제</button>
                    {{else}}
                    {{/isSameUser}}
                </div>
            </div>
            {{/replies}}
            <div></div>
        </script>
        <!--댓글 답글 input form 템플릿-->
        <script id="commentInputForm-template" type="text/x-handlebars-template">
            {{#attribute}}
            <br>
            <div style='width: 100%' class=commentHtml>
                {{{tag}}}
                <textarea style='width: 1100px' id=commentText placeholder='{{type}}을 입력하세요'
                          name=commentTxt></textarea>
                <div>
                    <button {{{buttonSelector}}}>{{buttonName}}</button>
                    {{#isReply}}
                    <button class=btn_close_cmt_input>취소</button>
                    {{else}}
                    {{/isReply}}
                </div>
            </div>
            {{/attribute}}
        </script>
        <!--댓글 수정 템플릿-->
        <script id="editCommentForm-template" type="text/x-handlebars-template">
            {{#attribute}}
            <br>
            <div style='width: 100%' class=commentHtml>
            <textarea style='width: 1100px' id='commentText' placeholder='댓글을 입력하세요'
                      name=commentTxt>{{oldText}}</textarea>
                <div>
                    <button id=btn_edit_comment_complete>수정하기</button>
                </div>
            </div>
            {{/attribute}}
        </script>

        <div id="tabcontent" class="container-fluid">
            <table class="table table-active table-bordered table-hover" cellpadding="0" cellspacing="0" border="0">
                <thead class="thead-light">
                    <tr>
                        <th scope="col">제목</th>
                        <th scope="col">작성자</th>
                        <th scope="col">작성일</th>
                    </tr>
                </thead>
                <tbody id="postlist"></tbody>
                <script id="posts-template" type="text/x-handlebars-template">
                    {{#posts}}
                        {{#isTemp}}
                        <tr class="postclick" data-post={{postID}} onclick="javascript:clickTempPostEvent(this)">
                            <td scope="row">{{postTitle}}</td>
                        {{else}}
                        <tr class="postclick" data-post={{postID}} onclick="javascript:clickTrEvent(this)">
                            <td scope="row">{{postTitle}}</td>
                        {{/isTemp}}
                        <td>{{userName}}</td>
                        <td>{{postRegisterTime}}</td>
                        <a style="visibility:hidden">{{postID}}</a>
                        <a style="visibility:hidden">{{boardID}}</a>
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
                <option value=0>제목</option>
                <option value=1>작성자</option>
                <option value=2>내용</option>
                <option value=3>제목+내용</option>
            </select>
            <input id="search_keyword" type="text"/>
            <button id="search" onclick="javascript:search(this)">조회</button>
        </div>
    </div>
    </div>
</div>

<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="/webjars/bootstrap/4.4.1/dist/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="https://cdn.jsdelivr.net/npm/handlebars@latest/dist/handlebars.js"></script>
<script src="/static/js/event/boardEvent.js"></script>
<script src="/static/js/event/postEvent.js"></script>
<script src="/static/js/event/commentEvent.js"></script>
<script src="/static/js/event/functionEvent.js"></script>
<script src="/static/js/event/common.js"></script>
<script src="/static/js/ajax/functionAjax.js"></script>
<script src="/static/js/ajax/commentAjax.js"></script>
<script src="/static/js/ajax/postAjax.js"></script>
<script src="/static/js/ajax/boardAjax.js"></script>
<script src="/static/ckeditor/ckeditor.js"></script>
<script src="/static/ckeditor/adapters/jquery.js"></script>
<script src="/static/js/util/handlebarsHelper.js"></script>

</body>

</html>