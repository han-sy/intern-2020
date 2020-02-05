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
    <link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP|Roboto&display=swap"
          rel="stylesheet">
    <style>
        body {
            font-family: "Roboto", sans-serif;
            font-family: 'Noto Sans JP', sans-serif;
        }
    </style>
    <script src="/static/js/util/data.js"></script>
</head>

<body>

<nav class="navbar navbar-expand navbar-dark bg-success">
    <a class="navbar-brand" id="companyInfo" href="JavaScript:window.location.reload()"
       value="${companyID}">${companyName}</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse"
            data-target="#navbarsExample02"
            aria-controls="navbarsExample02" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarsExample02">
        <ul class="navbar-nav mr-auto " style="
        <c:if test="${userType!='관리자'}">
                visibility:hidden;
        </c:if>
                nav-right: auto;">


            <li class="nav-item active">
                <a class="nav-link" id='addBoardBtn'
                   style="cursor:pointer" data-toggle="modal" data-target="#addBoardModal">게시판
                    추가</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" id='changeBoardsNameBtn' data-toggle="modal"
                   data-target="#changeBoardNameModal"
                   onclick="javascript:clickchangeBoardBtn(this)" style="cursor:pointer">게시판
                    이름변경</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" id='deleteBoardsBtn' data-toggle="modal"
                   data-target='#deleteBoardModal'
                   onclick="javascript:clickDeleteBoardBtn(this)"
                   style="cursor:pointer">게시판 삭제</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle active" href="#" id="dropdown03"
                   data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">사용중인 기능</a>
                <div class="dropdown-menu" id="fuctionListContainer" aria-labelledby="dropdown03">
                    <a class="dropdown-item text-success" id='changeFuncBtn' data-toggle="modal"
                       data-target="#changeFunctionModal"
                       onclick="javascript:changeFunction(this)" style="cursor:pointer"><strong>기능
                        변경</strong></a>
                    <div class="dropdown-divider"></div>
                    <c:forEach items="${functionInfoList}" var="functionList" varStatus="status">
                        <c:if test="${functionList.functionOn}">
                            <a class="dropdown-item" id=functionAble${functionList.functionID}
                               value=on> ${functionList.functionName} </a>
                        </c:if>
                        <c:if test="${!(functionList.functionOn)}">
                            <a class="dropdown-item d-none"
                               id=functionAble${functionList.functionID}
                               value=off> ${functionList.functionName}</a>
                        </c:if>
                    </c:forEach>
                </div>
                <!--현재 기능 사용 여부 현황 템플릿-->
                <script id="functionList-template" type="text/x-handlebars-template">
                    <a class="dropdown-item text-success" id='changeFuncBtn' data-toggle="modal"
                       data-target="#changeFunctionModal"
                       onclick="javascript:changeFunction(this)"
                       style="cursor:pointer"><strong>기능 변경</strong></a>
                    <hr>
                    {{#functions}}
                    {{#isAbleFunction}}
                    <a class="dropdown-item d-none" id='functionAble{{functionID}}' value='off'>{{functionName}}</a>
                    {{else}}
                    <a class="dropdown-item" id='functionAble{{functionID}}' value='on'>{{functionName}}</a>
                    {{/isAbleFunction}}
                    {{/functions}}
                </script>
            </li>

            <%--<li class="nav-item active" style="float: right">

            </li>--%>
        </ul>
        <a class="nav-link text-white" id="current_user_info" style="nav-right: auto"
           data-id="${userID}" data-type="${userType}">${userName}</a>
        <a class="nav-link text-white" style="nav-right: auto"
           href="<c:url value='/logout' />">로그아웃</a>
    </div>
</nav>
<div class="row">
    <br>
</div>
<div class="container-fluid row-cols-1">

    <div class="row bg-success text-white">
    </div>
    <div class="row">
        <div class="col-2">
            <ul class="tab" id="tab_id">
            </ul>
            <!--게시판 목록 템플릿-->
            <script id="boards-template" type="text/x-handlebars-template">
                {{#boards}}
                <li data-tab={{boardID}} class=tabmenu id=default style="cursor:pointer">
                    {{boardName}}
                </li>
                {{/boards}}
                <li data-tab="-1" class=tabmenu id=default style="cursor:pointer"> 임시보관함</li>
            </script>
        </div>
        <div class="border-left border-success col">

            <!-- 게시판 추가 Modal -->
            <div class="modal" id="addBoardModal" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-success">게시판 추가</h5>
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body-addboard">
                            <p style="padding: 15px 1px 5px 10px;">
                                <a>입력 </a>
                                <input type="text" name="게시판 이름" id="input_board_name"
                                       class="addBoard"
                                       placeholder="게시판 이름">
                            </p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                Close
                            </button>
                            <button type="button" id='addFuncBtn' class="btn btn-success"
                                    onclick=javascript:clickSaveaddedBoard(this)
                                    data-dismiss="modal">Save changes
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 게시판 삭제 Modal -->
            <div class="modal" id="deleteBoardModal" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-success">삭제할 게시판을 선택하시오.</h5>
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body-deleteBoard">

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                Close
                            </button>
                            <button type="button" id='deleteBoardBtn' class="btn btn-success"
                                    onclick=javascript:clickSaveDelteBoard(this)
                                    data-dismiss="modal">Save changes
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <!--게시판 삭제 템플릿-->
            <script id="deleteboards-template" type="text/x-handlebars-template">
                {{#boards}}

                <div class="custom-control custom-checkbox checkbox-success"
                     style="padding: 15px 1px 10px 30px;">
                    <input class="custom-control-input" type='checkbox' name='boardDelete'
                           id="checkDelBoard{{boardID}}" value='{{boardID}}'/>
                    <label class="custom-control-label"
                           for="checkDelBoard{{boardID}}">{{boardName}}</label>
                </div>
                {{/boards}}
            </script>
            <!-- 게시판 이름변경 Modal -->
            <div class="modal" id="changeBoardNameModal" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-success">게시판 이름 변경</h5>
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body-changeBoardName">

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                Close
                            </button>
                            <button type="button" id='changeNameBtn' class="btn btn-success"
                                    onclick=javascript:clickSaveChangeBoard(this)
                                    data-dismiss="modal">Save changes
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <!--게시판 이름변경 템플릿-->
            <script id="changeBoardName-template" type="text/x-handlebars-template">
                {{#boards}}
                <div class='boardInfo' id='board{{boardID}}' style="padding: 15px 1px 5px 10px;">
                    <span class='deleteBoard' data-board='board{{boardID}}'> {{boardName}} </span>
                    <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
                    <input class="form-control" type='text' name='boardname'
                           data-boardid={{boardID}} data-oldname="{{boardName}}"
                           value="{{boardName}}">
                </div>
                {{/boards}}
            </script>
            <!-- 기능변경 Modal -->
            <div class="modal" id="changeFunctionModal" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title text-success">기능 변경</h5>
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body-changeFunctions">

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                Close
                            </button>
                            <button type="button" id='changeFunction' class="btn btn-success"
                                    onclick=javascript:clickSaveFunctionChange(this)
                                    data-dismiss="modal">Save changes
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <!--기능변경 템플릿-->
            <script id="changeFunctionInfo-template" type="text/x-handlebars-template">
                {{#functions}}
                {{#isAbleFunction}}
                <div class="btn-group-toggle" data-toggle="buttons"
                     style="padding: 3px 1px 1px 10px;">
                    <label class="btn btn-default _function-switch">
                        <span>{{functionName}}</span>
                        <input class='function_checkbox' type='checkbox' name='function'
                               value={{functionID}}>
                        <span class='_switch'>OFF</span>
                    </label>
                </div>
                {{else}}
                <div class="btn-group-toggle" data-toggle="buttons"
                     style="padding: 3px 1px 1px 10px;">
                    <label class="btn btn-success _function-switch">
                        <span>{{functionName}}</span>
                        <input class='function_checkbox' type='checkbox' name='function'
                               value={{functionID}} checked>
                        <span class='_switch'>ON</span>
                    </label>
                </div>
                {{/isAbleFunction}}
                {{/functions}}
            </script>
            <!--게시글 작성 폼-->
            <div id="writecontent" style="display:none">
                <!--게시글 작성 시 게시판 선택 폼-->
                <div class="form-group row">
                    <div class="col-sm-2">
                        <label class="font-weight-light"> 게시판 선택 </label>
                    </div>
                    <div class="col-2 d-flex">
                        <select class="form-control-sm" id="boardIDinEditor">
                        </select>
                    </div>
                    <!--게시글 작성 시 게시판 목록 템플릿-->
                    <script id="writecontent-boards-template" type="text/x-handlebars-template">
                        {{#boards}}
                        <option data-tab={{boardID}} class=tabmenu id=default>{{boardName}}</option>
                        {{/boards}}
                    </script>
                </div>
                <!--에디터 폼-->
                <div class="form-group row">
                    <div class="col-2">
                        <label class="font-weight-light">게시글 제목</label>
                    </div>
                    <div class="col-6">
                        <input type="text" class="form-control" id="post_title"/>
                    </div>
                </div>
                <div id="editorcontent" class="form-group">
                    <textarea id="editor"></textarea>
                    <button id="btn_post" class="btn btn-success"
                            onclick="javascript:postFunction()">저장
                    </button>
                    <button id="btn_cancel" class="btn btn-success"
                            onclick="javascript:writeCancel()">작성취소
                    </button>
                    <button id="btn_temp" class="btn btn-success"
                            onclick="javascript:tempsaveFunction()">임시저장
                    </button>
                    <div id="editorcontent-hidden">
                    </div>
                    <script id="postid-template" type="text/x-handlebars-template">
                        <a id="editor_postID" style="display:none">{{postID}}</a>
                    </script>
                </div>
            </div>

            <div id="postcontent" class="border-primary"></div>
            <!--게시물 내용 템플릿-->
            <script id="postcontent-template" type="text/x-handlebars-template">
                {{#post}}
                <p class="h4">{{postTitle}}</p>
                <p class="h6" align="right">{{userName}}</p>
                <p class="h6" align="right">{{postRegisterTime}}</p>
                <hr>
                <div class="d-block">
                    <p>{{{postContent}}}</p>
                </div>
                <a id="postID" style="visibility: hidden;">{{postID}}</a>
                <br>
                <button id="btn_updatePost" class="btn btn-success" style="visibility:hidden"
                        onclick="javascript:postUpdateFunction()">수정
                </button>
                <button id="btn_deletePost" class="btn btn-success" style="visibility:hidden"
                        onclick="javascript:postDeleteFunction()">삭제
                </button>
                {{/post}}
                {{#isCommentAble}}
                <div class=comment_section>
                    <br><br>
                    <div class="row">
                        <span class="col-2">
                            <strong class="c">댓글 </strong>
                        (<span id=commentCount></span>)
                        </span>
                        <a class=' commentBtn text-success font-weight-bold text-button'
                           style="cursor: pointer;">댓글 달기</a>
                    </div>
                    <div class=comment_list_container></div>
                    <div class=comment_input_container></div>
                    {{else}}
                    {{/isCommentAble}}
            </script>
            <!--댓글리스트 템플릿-->
            <script id="commentList-template" type="text/x-handlebars-template">
                {{#comments}}
                <hr>
                <div class='referenceCommentContainer ' data-id='{{commentID}}'>

                    <div class="row border-left-comment">
                        <div></div>
                        <div class='commentContainer ' id='comment{{commentID}}'
                             style="padding-left: 50px">
                            <div class="user"><h5><strong class=name
                                                          data-id={{userID}}>{{userName}}</strong>
                            </h5></div>
                            <div>
                                <div>
                                    <div class="comment_area comment_content" id=translate_area
                                         style="width: 100%;">{{{commentContent}}}
                                    </div>
                                    <br>
                                    <div class="date text-muted">{{commentRegisterTime}}</div>
                                </div>
                                <div class="btn">
                                    {{#isReplyAble}}
                                    <a class='text-success text-button text-button font-weight-bold replyBtn'
                                       style="cursor: pointer;">답글</a>
                                    {{else}}
                                    {{/isReplyAble}}

                                    {{#isSameUser}}
                                    <a class="text-success text-button text-button font-weight-bold"
                                       id='edit_comment' style="cursor: pointer;">수정</a>
                                    <a class="text-success text-button text-button font-weight-bold"
                                       id='delete_comment' style="cursor: pointer;">삭제</a>
                                    {{else}}
                                    {{/isSameUser}}
                                </div>
                            </div>
                        </div>
                    </div>
                    {{#isReplyAble}}
                    <div class='replyContainer' id='reply_container{{commentID}}'
                         style='padding: 5px 1px 3px 30px;'>
                    </div>
                    <div id='reply_input_container{{commentID}}'
                         style='padding: 5px 1px 3px 30px;'></div>
                    {{else}}
                    {{/isReplyAble}}
                    <div>

                    </div>
                </div>
                {{/comments}}
            </script>
            <!--답글 List 템플릿-->
            <script id="replyList-template" type="text/x-handlebars-template">
                {{#replies}}
                <hr>
                <div class='row '>
                    <div class="col-1">
                    </div>
                    <div class='commentContainer col-11' id='comment{{commentID}}'>
                        <div class="user"><h5><strong class=name
                                                      data-id={{userID}}>{{userName}}</strong></h5>
                        </div>
                        <div>
                            <div class="comment_area row" id=translate_area">
                                <strong class="nametag text-primary "
                                        data-id={{commentReferencedUserID}}
                                        style="cursor:pointer;padding: 0px 0px 0px 15px">{{commentReferencedUserName}}</strong>
                                <div class="comment_content col-10" style="float:left;">
                                    {{{commentContent}}}
                                </div>
                            </div>
                            <br>
                            <div class="date text-muted">{{commentRegisterTime}}</div>
                        </div>
                        <div class=btn>
                            {{#isReplyAble}}
                            <a class='text-success text-button font-weight-bold replyBtn'
                               style="cursor: pointer;">답글</a>
                            {{else}}
                            {{/isReplyAble}}
                            {{#isSameUser}}
                            <a class="text-success text-button font-weight-bold" id='edit_comment'
                               style="cursor: pointer;">수정</a>
                            <a class="text-success text-button font-weight-bold" id='delete_comment'
                               style="cursor: pointer;">삭제</a>
                            {{else}}
                            {{/isSameUser}}
                        </div>
                    </div>
                </div>
                {{/replies}}
                <div>
                </div>
            </script>
            <!--댓글 답글 input form 템플릿-->
            <script id="commentInputForm-template" type="text/x-handlebars-template">
                {{#attribute}}
                <br>
                <div style='width: 100%' class=commentHtml>
                    {{{tag}}}
                    <textarea class="form-control" id=commentText placeholder='{{type}}을 입력하세요'
                              name=commentTxt></textarea>
                    <div align="right">
                        {{#isReply}}
                        <a class="text-success text-button font-weight-bold {{{buttonSelector}}}"
                           style="cursor: pointer;">{{buttonName}}</a>
                        <a class="btn_close_cmt_input text-success text-button font-weight-bold"
                           style="cursor: pointer;">취소</a>
                        {{else}}
                        <a class="text-success text-button font-weight-bold {{{buttonSelector}}}"
                           style="cursor: pointer;">{{buttonName}}</a>
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
                <textarea class="form-control" style='width: 100%' id='commentText'
                          placeholder='댓글을 입력하세요'
                          name=commentTxt>{{{oldText}}}</textarea>
                    <div>
                        <a id=btn_edit_comment_complete
                           class="text-success text-button font-weight-bold"
                           style="cursor: pointer;">수정하기</a>
                    </div>
                </div>
                {{/attribute}}
            </script>
            <!--게시글 목록 템플릿-->
            <div id="tabcontent" class="container-fluid">
                <table class="table table-hover" cellpadding="0" cellspacing="0" border="0">
                    <thead>
                    <tr>
                        <th scope="col">제목</th>
                        <th scope="col">작성자</th>
                        <th scope="col">작성일</th>
                    </tr>
                    </thead>
                    <!--게시글 목록 -->
                    <tbody id="postlist"></tbody>
                    <script id="posts-template" type="text/x-handlebars-template">
                        {{#posts}}
                        {{#isTemp}}
                        <tr class="postclick" data-post={{postID}}
                            onclick="javascript:clickTempPostEvent(this)">
                            <td scope="row">{{postTitle}}</td>
                            {{else}}
                        <tr class="postclick" data-post={{postID}}
                            onclick="javascript:clickTrEvent(this)">
                            <td scope="row">{{postTitle}}</td>
                            {{/isTemp}}
                            <td>{{userName}}</td>
                            <td>{{postRegisterTime}}</td>
                            <a style="visibility:hidden">{{postID}}</a>
                            <a style="visibility:hidden">{{boardID}}</a>
                        </tr>
                        {{/posts}}
                    </script>
                </table>

                <div id="searchcontent">
                    <div class="form-group row">
                        <div class="col-sm-1">
                            <select id="search_option" class="form-control"
                                    style="width:100px;font-size:15px;">
                                <option value="title">제목</option>
                                <option value="writer">작성자</option>
                                <option value="content">내용</option>
                                <option value="titleAndContent">제목+내용</option>
                            </select>
                        </div>
                        <div class="col-5">
                            <input id="search_keyword" class="form-control"
                                   placeholder="검색어를 입력하세요." type="text"/>
                        </div>
                        <div class="col-3">
                            <button id="search" class="btn btn-success"
                                    onclick="javascript:search(this)">조회
                            </button>
                        </div>
                        <div class="col">
                            <button id="btn_write" class="btn btn-success" style="float:right">글쓰기
                            </button>
                        </div>
                    </div>

                </div>
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
<script src="/static/js/updateUI/boardUI.js"></script>
<script src="/static/js/updateUI/commentUI.js"></script>
<script src="/static/js/updateUI/functionUI.js"></script>
<script src="/static/js/updateUI/postUI.js"></script>
<script src="/static/js/util/windowLoad.js"></script>
<script src="/static/js/util/constant.js>"></script>

</body>

</html>