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
      postStatus: `{"isTemp":false, "isRecycle":false}`
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
      postStatus: `{"isTemp":${is_temp}, "isRecycle":false}`
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

function getTempPosts(pageNum) {
  console.log("getTempPosts");
  $.ajax({
    type: 'GET',
    url: "/boards/-1/posts/temp",
    data: {
      pageNumber: pageNum
    },
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
function getRecyclePosts(pageNum) {
  console.log("휴지통 호출");
  $.ajax({
    type: 'GET',
    url: `/boards/-4/posts/recycleBin`,
    data: {
      pageNumber: pageNum
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      console.log(JSON.stringify(data));
      if(data == "") {
        showEmptyList();
      } else {
        loadPostList(data);
      }
    }
  })
}

// 휴지통에 있는 게시글 복원하기
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
  });
}

// 내가 작성한 게시글 불러오기
function getMyPosts(pageNum) {
  $.ajax({
    type: 'GET',
    url: `/boards/-1/posts/myArticle`,
    data: {
      pageNumber: pageNum
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      console.log(JSON.stringify(data));
      if(data == "") {
        showEmptyList();
      } else {
        loadPostList(data);
      }
    }
  });
}

// 내 댓글이 달린 게시글 받아오기
function getMyReplies(pageNum) {
  $.ajax({
    type: 'GET',
    url: `/boards/-2/posts/myReply`,
    data: {
      pageNumber: pageNum
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      console.log(JSON.stringify(data));
      if(data == "") {
        showEmptyList();
      } else {
        loadPostList(data);
      }
    }
  });
}

// 휴지통 게시글 받아오기
function getRecyclePost(postID, boardID) {
  postClear();
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}`,
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      $('#writecontent').hide();
      $('#btn_write').show();
      //게시글 내용 출력
      loadPostContent(data);
      var btn_deletePost = $('#btn_deletePost');
      var btn_updatePost = $('#btn_updatePost');
      btn_deletePost.attr('style', 'visibility:visible');
      btn_updatePost.attr('style', 'visibility:visible');
      btn_deletePost.html("완전 삭제");
      btn_updatePost.html("복원");
      btn_updatePost.attr('onclick', 'javascript:clickRestorePost()');
      btn_deletePost.attr('onclick', 'javascript:clickCompleteDeletePost()');
    }
  });
}