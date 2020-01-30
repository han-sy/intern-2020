/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postAjax.js
 */


function insertPost(boardID, postTitle, postContent) {
    $.ajax({
        type: 'POST',
        url: "/boards/" + boardID + "/posts",
        data: {
            postTitle: postTitle,
            postContent: postContent,
            isTemp: false
        },
        error: function (xhr) {
            errorFunction(xhr);
        },
        success: function () {
            refreshPostList();
        }
    });
}

function insertTempPost(boardID, postID, temp_title, temp_content, is_temp) {
    $.ajax({
        type: 'POST',
        url: "/boards/" + boardID + "/posts",
        async: false,
        data: {
            postID: postID,
            postTitle: temp_title,
            postContent: temp_content,
            isTemp: is_temp
        },
        error: function (xhr) {
            errorFunction(xhr);
        },
        success: function () {
            $.getJSON("/boards/" + boardID + "/posts/recent", function (data) {
                addPostIdToEditor(data.postID);
            });
            if(is_temp)
                alert("임시저장 되었습니다.");
        }
    });
}

function loadPost(boardID, postID) {
    var post_title = $('#post_title');
    var editor = $('#editor');
    $.ajax({
        type: 'GET',
        url: "/boards/" + boardID + "/posts/" + postID + "/editor",
        async: false,
        error: function (xhr) {
            errorFunction(xhr);
        },
        success: function (data) {
            addPostIdToEditor(postID);
            post_title.val(data.postTitle);
            editor.val(data.postContent);
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
        error: function (xhr) {
            errorFunction(xhr);
        },
        success: function () {
            refreshPostList();
        }
    });
}

function deletePost(boardID, postID) {
    $.ajax({
        type: 'DELETE',
        url: "/boards/" + boardID + "/posts/" + postID,
        error: function (xhr) {
            errorFunction(xhr);
        },
        success: function () {
            editorClear();
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
    var boardID = getCurrentBoardID();
    $.ajax({
        type: 'GET',
        url: '/boards/' + boardID + '/posts/search',
        data: {
            option: option,
            keyword: keyword.val()
        },
        dataType: 'JSON',
        error: function (xhr) {
            errorFunction(xhr);
        },
        success: function (data) {
            postClear(); // 게시글 조회 화면 Clear
            postsClear(); // 게시글 목록 화면 Clear
            keyword.val("");

            loadPostList(data);
        }
    });
}

function getTempPosts() {
    $.ajax({
        type: 'GET',
        url: "/boards/-1/posts/temp",
        error: function (xhr) {
            errorFunction(xhr);
        },
        success: function (data) {
            loadPostList(data);
        }
    })
}

function getTempPost(postID) {
    var postContentObj = $('#postcontent');
    postContentObj.html("");
    $.ajax({
        type: 'GET',
        url: "/boards/0/posts/" + postID,
        error: function (error) {  //통신 실패시
            alert('통신실패!' + error);
        },
        success: function (data) {
            $('#btn_write').show();
            loadPost(0, postID);
        }
    });
}