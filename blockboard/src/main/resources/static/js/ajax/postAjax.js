/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postAjax.js
 */
function insertPost(boardID, postTitle, postContent) {
    $.ajax({
        type: 'POST',
        url: "/boards/" + boardID + "/posts/",
        data: {
            postTitle: postTitle,
            postContent: postContent
        },
        error: function () {
            alert("게시글 작성 실패");
        },
        success: function () {
            refreshPostList();
        }
    });
}

function loadPost(boardID, postID) {
    var post_title = $('#post_title');
    var editorcontent = $('#editorcontent');
    var editor = $('#editor');
    $.ajax({
        type: 'GET',
        url: "/boards/" + boardID + "/posts/" + postID + "/editor",
        async: false,
        error: function (response) {
            response.
                console.log("Error");
        },
        success: function (data) {
            editorcontent.append("<a id=postID style=visibility:hidden>" + postID + "</a>");
            post_title.val(data.postTitle);
            editor.val(data.postContent);
            //CKEDITOR.instances.editor.updateElement();
        }
    });
}

function updatePost(boardID, postID, postTitle, postContent) {
    $.ajax({
        type: 'PUT',
        url: "/boards/" + boardID + "/posts/" + postID,
        data: {
            postTitle: postTitle,
            postContent: postContent
        },
        error: function () {
            alert('게시글 수정 실패');
        },
        success: function (data) {
            refreshPostList();
        }
    });
}

function deletePost(boardID, postID) {
    $.ajax({
        type: 'DELETE',
        url: "/boards/" + boardID + "/posts/" + postID,
        error: function () {
            alert('게시글 삭제 실패');
        },
        success: function (data) {
            refreshPostList();
        }
    });
}

// 게시글 작성, 수정, 삭제 시 해당 게시판 refresh 하는 함수
function refreshPostList() {
    var boardID = getCurrentBoardID();
    postClear();
    getPostsAfterTabClick(boardID);
}

function searchPost(option, keyword) {
    $.ajax({
        type: 'GET',
        url: '/boards/0/posts/search',
        data: {
            option: option.html(),
            keyword: keyword.val()
        },
        dataType: 'JSON',
        error: function () {
            alert('검색 실패');
        },
        success: function (data) {
            postClear(); // 게시글 조회 화면 Clear
            postsClear(); // 게시글 목록 화면 Clear
            keyword.val("");

            $.template("searchResultTmpl",
                '<tr height="30" class=postclick data-post=${postID} onclick="javascript:clickTrEvent(this)"' +
                'onmouseover = "javascript:changeTrColor(this)">' +
                '<td width="379">${postTitle}</td>' +
                '<td width="73">${userName}</td>' +
                '<td width="164">${postRegisterTime}</td></tr>' +
                '<td style="visibility:hidden">${postID}</td>' +
                '<td style="visibility:hidden">${boardID}</td>'
            );

            $.tmpl("searchResultTmpl", data).appendTo("#postlist");
        }
    });
}