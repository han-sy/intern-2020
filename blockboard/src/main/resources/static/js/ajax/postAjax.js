/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postAjax.js
 */


function insertPost(postID,boardID, postTitle, postContent) {
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts`,
    data: {
      postTitle: postTitle,
      postContent: postContent,
      postStatus: `{"isTemp": false}`
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      console.log("data : "+data);
      if(isNullData(postID)){
        updatePostIDToFiles(data);
      }else{
        updatePostIDToFiles(postID);
      }
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
      postStatus: `{"isTemp":${is_temp}}`
    },
    error: function (xhr) {
      errorFunction(xhr);
      editorClear();
      refreshPostList();
    },
    success: function (data) {
      if (is_temp) {
        addPostInfoToEditor(data, boardID);
        if(isNullData(postID)){
          updatePostIDToFiles(data);
        }else{
          updatePostIDToFiles(postID);
        }
        alert("임시저장 되었습니다.")
      } else {
        alert("게시물이 작성되었습니다.");
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
      addPostInfoToEditor(postID, boardID);
      initBoardIdOptionInEditor(boardID);
      post_title.val(data.postTitle);
      CKEDITOR.instances['editor'].setData(data.postContent);
    }
  });
}

function updatePost(boardID, originalBoardID, postID, postTitle, postContent) {
  $.ajax({
    type: 'PUT',
    url: `/boards/${originalBoardID}/posts/${postID}`,
    data: {
      boardID: boardID,
      postTitle: postTitle,
      postContent: postContent
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

function deletePost(boardID, postID) {
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

      if (data == "") {
        showEmptyList();
      } else {
        loadPostList(data);
      }
    }
  });
}

// 임시보관함 게시글 목록을 가져온다.
function getTempPosts(pageNum) {
  $.ajax({
    type: 'GET',
    url: "/boards/-1/posts/tempBox",
    data: {
      pageNumber: pageNum
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      if (data == "") {
        showEmptyList();
      } else {
        loadPostList(data);
      }
    }
  })
}

// 휴지통 게시글 목록을 가져온다.
function getRecyclePosts(pageNum) {
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
      if (data == "") {
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

// 내가 작성한 게시글 목록 불러오기
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
      if (data == "") {
        showEmptyList();
      } else {
        loadPostList(data);
      }
    }
  });
}

// 내 댓글이 달린 게시글 목록 받아오기
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
      if (data == "") {
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
    error: function (xhr) {
      errorFunction(xhr);
      refreshPostList();
    },
    success: function (data) {
      $('#writecontent').hide();
      $('#btn_write').show();

      loadPostContent(data);
      var btn_deletePost = $('.btn_delete');
      var btn_updatePost = $('.btn_modify');
      btn_deletePost.attr('style', 'visibility:visible');
      btn_updatePost.attr('style', 'visibility:visible');
      btn_updatePost.html("복원");
      btn_updatePost.removeClass("btn_modify");
      btn_updatePost.addClass("btn_restore");
    }
  });
}

// 최신 게시글 목록 받아오기
function getRecentPosts(pageNum) {
  $.ajax({
    type: 'GET',
    url: `/boards/-5/posts/recent`,
    data: {
      pageNumber: pageNum
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      if (data == "") {
        showEmptyList();
      } else {
        loadPostList(data);
      }
    }
  });
}

// 인기 게시글 목록 받아오기
function getPopularPostList(pageNum) {
  $.ajax({
    type: 'GET',
    url: `/boards/-6/posts/popular-board`,
    data: {
      pageNumber: pageNum
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      if (data == "") {
        showEmptyList();
      } else {
        loadPostList(data);
      }
    }
  });
}