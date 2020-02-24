/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postAjax.js
 */
function insertPost(postID, boardID, postTitle, postContent) {
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts`,
    data: {
      postTitle: postTitle,
      postContent: postContent,
      postStatus: "normal",
      boardID: boardID
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      attachFileToPost(postID, data);
    },
    complete: function () {
      refreshPostListAfterPostCRUD();
    }
  });
}

function attachFileToPost(postID, data) {
  if (functionOn.postFileAttach) {
    if (isNullData(postID)) {
      updateIDToFiles("post", data, "");
    } else {
      updateIDToFiles("post", postID, "");
    }
  }
}

function insertTempPost(boardID, postID, temp_title, temp_content,
    new_post_status) {
  $.ajax({
    type: 'POST',
    url: `/boards/${boardID}/posts`,
    async: false,
    data: {
      postID: postID,
      postTitle: temp_title,
      postContent: temp_content,
      postStatus: new_post_status
    },
    error: function (xhr) {
      errorFunction(xhr);
      clearEditor();
      refreshPostListAfterPostCRUD();
    },
    success: function (data) {
      if (new_post_status === "normal") {
        alert("게시물이 작성되었습니다.");
      } else {
        addHiddenTypePostIdAndBoardIdToEditor(data, boardID);
        attachFileToPost(postID, data);
        alert("임시저장 되었습니다.");
      }
    }
  });
}

function loadPost(boardID, postID) {
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}`,
    async: false,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      addHiddenTypePostIdAndBoardIdToEditor(postID, boardID);
      selectOptionOfCurrentBoardId(boardID);
      $('#post_title').val(data.postTitle);
      CKEDITOR.instances['editor'].setData(data.postContent);
    }
  });
}

function updatePost(selectedBoardId, originalBoardID, postID, postTitle, postContent) {
  $.ajax({
    type: 'PUT',
    url: `/boards/${originalBoardID}/posts/${postID}`,
    data: {
      boardID: selectedBoardId,
      postTitle: postTitle,
      postContent: postContent
    },
    error: function (xhr) {
      errorFunction(xhr);
      clearEditor();
    },
    success: function () {
      clearEditor();
    },
    complete: function () {
      refreshPostListAfterPostCRUD();
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
      clearEditor();
    },
    complete: function () {
      refreshPostListAfterPostCRUD();
    }
  });
}

function getSearchPost(keyword, option, pageNum) {
  let boardId = getCurrentActiveBoardID();
  $.ajax({
    type: 'GET',
    url: `/boards/${boardId}/posts/search`,
    data: {
      keyword: keyword,
      option: option,
      pageNumber: pageNum
    },
    dataType: 'JSON',
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      postClear(); // 게시글 조회 화면 Clear
      postsClear(); // 게시글 목록 화면 Clear
      clearSearchKeyword();
      if (data === "") {
        showEmptyPostList();
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
      if (data === "") {
        showEmptyPostList();
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
        showEmptyPostList();
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
      refreshPostListAfterPostCRUD();
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
        showEmptyPostList();
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
        showEmptyPostList();
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
      refreshPostListAfterPostCRUD();
    },
    success: function (data) {
      $('#write-content').hide();
      $('#btn_write').hide();

      loadPostContent(data);
      updateButtonOnRecycleBoard();
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
        showEmptyPostList();
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
        showEmptyPostList();
      } else {
        loadPostList(data);
      }
    }
  });
}