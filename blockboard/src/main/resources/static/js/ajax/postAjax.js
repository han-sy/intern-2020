/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postAjax.js
 */


function insertPost(boardID, postTitle, postContent) {
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts`,
    data: {
      postTitle: postTitle,
      postContent: postContent,
      postStatus: `{"isTemp":false, "isTrash":false}`
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function () {
      alert("게시글 작성완료");
      refreshPostList();
    }
  });
}

function insertTempPost(boardID, postID, temp_title, temp_content, is_temp) {
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts`,
    async: false,
    data: {
      postID: postID,
      postTitle: temp_title,
      postContent: temp_content,
      postStatus: `{"isTemp":${is_temp}, "isTrash":false}`
    },
    error: function (xhr) {
      errorFunction(xhr);
      editorClear();
      refreshPostList();
    },
    success: function () {
      addRecentTempPostIdToEditor(boardID);
      if (is_temp) {
        alert("임시저장 되었습니다.");
      }
    }
  });
}

function loadPost(boardID, postID) {
  var post_title = $('#post_title');
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}`,
    async: false,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      addPostIdToEditor(postID);
      initBoardIdOptionInEditor(boardID);
      post_title.val(data.postTitle);
      CKEDITOR.instances['editor'].setData(data.postContent);
    }
  });
}

function updatePost(boardID, postID, postTitle, postContent) {
  $.ajax({
    type: 'PUT',
    url: `/boards/${boardID}/posts/${postID}`,
    data: {
      postTitle: postTitle,
      postContent: postContent,
    },
    error: function (xhr) {
      errorFunction(xhr);
      editorClear();
      refreshPostList();
    },
    success: function () {
      editorClear();
      refreshPostList();
    }
  });
}

// 게시글 완전 삭제 = 휴지통에서 삭제
function completeDeletePost(boardID, postID) {
  $.ajax({
    type: 'DELETE',
    url: `/boards/${boardID}/posts/${postID}`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function () {
      editorClear();
      refreshPostList();
    }
  });
}

// 게시글 임시 삭제 = 휴지통으로 이동
function temporaryDeletePost(boardID, postID) {
  $.ajax({
    type: 'PUT',
    url: `/boards/${boardID}/posts/${postID}/trash`,
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
  getPageList(1, boardID, updatePageList)
}

function searchPost(option, keyword) {
  var boardID = getCurrentBoardID();
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/search`,
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
    url: `/boards/0/posts/temp/${postID}`,
    error: function (xhr) {  //통신 실패시
      errorFunction(xhr);
      editorClear();
      refreshPostList();
    },
    success: function (data) {
      $('#btn_write').show();
      loadPost(data.boardID, postID);
    }
  });
}

function addRecentTempPostIdToEditor(boardID) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/recent`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      addPostIdToEditor(data.postID);
    }
  })
}

// 휴지통에 있는 게시글들을 가져온다.
function getPostsInTrashBox() {
  $.ajax({
    type: 'GET',
    url: `/boards/0/posts/trash`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      loadPostList(data);
    }
  })
}

function restorePost(postID) {
  $.ajax({
    type: 'PUT',
    url: `/boards/0/posts/${postID}/restore`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function () {
      alert("게시글이 복원되었습니다.");
      refreshPostList();
    }
  })

}