/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postEvent.js
 */
var autosave = null;

// '글쓰기' 버튼 이벤트
$(document).on("click", "#btn_write", function () {
  postClear();
  createEditorArea("insert");
  selectOptionOfCurrentBoardId(getCurrentActiveBoardId());
});

// '임시저장' 이벤트
$(document).on('click', '.btn_tempSave', function () {
  let postTitle = getPostTitleInEditor();
  let postContent = CKEDITOR.instances.editor.getData();
  let postId = getPostIdInEditor();
  let boardId = getSelectedBoardIdInEditor();

  if (isValidPostData()) {
    insertTempPost(boardId, postId, postTitle, postContent, "temp");
    refreshPostListAfterPostCRUD();
  }
});

// '게시글 저장' 이벤트
$(document).on('click', '.btn_post', function () {
  let postTitle = getPostTitleInEditor();
  let postContent = CKEDITOR.instances.editor.getData();
  let postId = getPostIdInEditor();
  let selectedBoardId = getSelectedBoardIdInEditor();

  if (isValidPostData()) {
    if (typeof postId == "undefined") { // 임시 or 자동 저장 되지 않았을 때
      insertPost(postId, selectedBoardId, postTitle, postContent);
    } else {  // 임시 or 자동 저장이 된 후 '저장' 버튼을 누를 때
      insertTempPost(selectedBoardId, postId, postTitle, postContent, "normal");
      getPageList(1, getCurrentActiveBoardId(), 0, updatePostPageList);
    }
    clearEditor();
  }
});

// 게시글 제목, 내용 유효성 검사
function isValidPostData() {
  let postTitle = getPostTitleInEditor();
  let postContent = CKEDITOR.instances.editor.getData();
  if (postTitle.trim() === "") {
    alert("게시글 제목을 입력해주세요.");
    return false;
  }
  if (postContent.trim() === "") {
    alert("게시글 내용을 입력해주세요.");
    return false;
  }
  if (!isValidLength(postTitle, 150)) {
    alert("게시글 제목 길이를 초과하였습니다.");
    return false;
  }
  if (!isValidLength(postContent, 4000)) {
    alert("게시글 내용 길이를 초과하였습니다.");
    return false;
  }
  return true;
}

// '수정' 버튼 클릭 후 '수정하기' 버튼 이벤트
$(document).on('click', '.btn_update', function () {
  let postId = getPostIdInEditor();
  let originalBoardId = getOriginalBoardIdInEditor();
  let postTitle = getPostTitleInEditor();
  let postContent = CKEDITOR.instances.editor.getData();
  let selectedBoardId = getSelectedBoardIdInEditor();

  if (functionOn.postFileAttach) {
    updateIDToFiles("post", postId, "");
  }
  updatePost(selectedBoardId, originalBoardId, postId, postTitle, postContent);
});

function updateButtonOfSavePostToUpdatePost() {
  let post_button = $('.btn_post');
  post_button.html('수정하기');
  post_button.removeClass("btn_post");
  post_button.addClass("btn_update");
}

// 게시글 조회 후 '수정' 버튼 이벤트
$(document).on('click', '.btn_modify', function () {
  let postId = getPostIdInPost();
  let boardId = getBoardIdInPost();
  postClear();
  createEditorArea("modify");
  updateButtonOfSavePostToUpdatePost();
  CKEDITOR.instances['editor'].on('instanceReady', function() {
    loadPost(boardId, postId);
  });
});

// 게시글 조회 후 삭제 버튼 이벤트
$(document).on('click', '.btn_delete', function () {
  let postId = getPostIdInPost();
  let boardId = parseInt(getCurrentActiveBoardId());
  if (boardId === BOARD_ID.RECYCLE || boardId === BOARD_ID.TEMP_BOX) {
    if (confirm("영구 삭제됩니다. 삭제하시겠습니까?")) {
      deletePost(boardId, postId);
    }
  } else {
    deletePost(boardId, postId);
  }
});

// 게시글 검색 버튼 이벤트
function search() {
  let option = $('#search_option option:selected').attr('value');
  let keyword = $('#search_keyword');
  searchPost(option, keyword);
}

// 작성 취소 버튼 이벤트
$(document).on('click', '.btn_cancel', function () {
  if (confirm("작성된 내용이 저장되지 않을 수도 있습니다. 이동하시겠습니까?") === true) {
    deleteAllAttachedFile();
    clearEditor();
  }
});

// 자동 저장 on
function on_autosave() {
  autosave = setInterval(function () {
    $('.btn_tempSave').trigger('click');
  }, 1000 * 600);
}

// 자동 저장 off
function off_autosave() {
  clearInterval(autosave);
}

function updateButtonOfCancelToDeletePost() {
  var btn_cancel = $('.btn_cancel');
  btn_cancel.html("삭제");
  btn_cancel.addClass("btn_delete");
  btn_cancel.removeClass("btn_cancel");
}

// 임시저장 게시물 클릭 이벤트
$(document).on('click', '.temp_post_click', function () {
  let postId = getPostIdInPostList.call(this);
  let boardId = getBoardIdInPostList.call(this);
  postClear();
  createEditorArea("insert");
  updateButtonOfCancelToDeletePost();
  loadPost(boardId, postId);
  openFileAttachForm(postId);//파일첨부폼
});

// length Check 이벤트
function isValidLength(str, limit) {
  if (getByteLength(str)[0] <= limit) {
    return true;
  } else {
    return false;
  }
}

// 게시글 목록에서 게시글 클릭시
$(document).on('click', '.normal_post_click', function () {
  var postId = getPostIdInPostList.call(this);
  var boardId = getBoardIdInPostList.call(this);
  $(function () {
    getPostDataAfterPostClick(postId, boardId); //boardAjax.js 참고
  });
});

// 휴지통 게시물 클릭 이벤트
$(document).on('click', '.recycle_post_click', function () {
  var postId = $(this).attr("data-post");
  var boardId = getCurrentActiveBoardId();
  getRecyclePost(postId, boardId);
});

// 휴지통 게시글 복원 이벤트
$(document).on('click', '.btn_restore', function () {
  var postId = $('#postId').html();
  if (confirm("원래 게시판으로 복원하시겠습니까?")) {
    restorePost(postId);
  }
});
