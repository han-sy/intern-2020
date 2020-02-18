/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postEvent.js
 */
var autosave = null;

// 현재 선택된 게시판 id 가져온다.
function getCurrentBoardID() {
  var tabs = $('#tab_id').children();
  var boardID = 0;

  $.each(tabs, function () {
    if ($(this).hasClass("active_tab")) {
      boardID = $(this).attr("data-tab");
    }
  });
  return boardID;
}

// '글쓰기' 버튼 이벤트
$(document).on("click", "#btn_write", function () {
  postClear();
  editorAreaCreate("insert");
  initBoardIdOptionInEditor(getCurrentBoardID());
  var funcionOn = new FunctionOn();
  if (funcionOn.isFileAttachOn()) {
    console.log("파일 첨부 on");
    openFileAttachForm();
  }
});

// '임시저장' 이벤트 함수
$(document).on('click', '.btn_tempSave', function () {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var postID = $('#postIdInEditor').html();
  var boardID = $('#selectedBoardIDinEditor option:selected').attr('data-tab');
  // 제목 & 내용 비었는지 검사
  if (checkEmpty()) {
    insertTempPost(boardID, postID, postTitle, postContent, "temp");
    refreshPostList();
  }
});

// '저장' 이벤트 함수
$(document).on('click', '.btn_post', function () {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var postID = $('#postIdInEditor').html();
  var boardID = $('#selectedBoardIDinEditor option:selected').attr('data-tab');

  // 제목 & 내용 비었는지 검사
  if (checkEmpty()) {
    // 게시글 ID가 존재하지 않으면? 바로 저장
    console.log("저장 postID = ", postID);
    if (typeof postID == "undefined") {
      insertPost(postID,boardID, postTitle, postContent);
    }
    // 임시 or 자동 저장된 글을 한번 더 '저장' 버튼을 누를 때
    else {
      insertTempPost(boardID, postID, postTitle, postContent, "normal");
      getPageList(1, getCurrentBoardID(), updatePageList);
    }
    editorClear();
  }
});

// 게시글 제목 or 내용 비었는지 검사
function checkEmpty() {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  if (postTitle.trim() == "") {
    alert("게시글 제목을 입력해주세요.");
    return false;
  }
  if (postContent.trim() == "") {
    alert("게시글 내용을 입력해주세요.");
    return false;
  }
  // 서버에 나중에 적용하기
  if (!isValidLength(postTitle, 150)) {
    alert("게시글 제목 길이를 초과하였습니다.");
    return false;
  }
  if (!isValidLength(postContent, 4000)) {
    alert("게시글 내용 길이를 초과하였습니다.");
    return false;
  } else {
    return true;
  }
}

// '수정' 버튼 클릭 후 '수정하기' 버튼 이벤트
$(document).on('click', '.btn_update', function () {
  var postID = $("#postIdInEditor").html();
  var originalBoardID = $("#boardIdInEditor").html();
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#selectedBoardIDinEditor option:selected').attr('data-tab');
  updateIDToFiles(postID,"");
  updatePost(boardID, originalBoardID, postID, postTitle, postContent);
});

// 게시글 조회 후 '수정' 버튼 이벤트
$(document).on('click', '.btn_modify', function () {
  var postID = $("#postID").html();
  var boardID = $("#boardIdInPost").html();
  postClear();
  editorAreaCreate("modify");
  var post_button = $('.btn_post');
  post_button.html('수정하기'); // 게시글 올리기 버튼 텍스트 변경
  post_button.removeClass("btn_post");
  post_button.addClass("btn_update");
  setTimeout(function () {
    loadPost(boardID, postID);
  }, 100); // 에디터로 게시글 정보 불러옴.
  openFileAttachForm(postID);//파일첨부폼

});

// 게시글 조회 후 삭제 버튼 이벤트
$(document).on('click', '.btn_delete', function () {
  var postID = $("#postID").html();
  var boardID = parseInt(getCurrentBoardID());
  if (boardID == BOARD_ID.RECYCLE || boardID == BOARD_ID.TEMP_BOX) {
    if (confirm("영구 삭제됩니다. 삭제하시겠습니까?")) {
      deletePost(boardID, postID);
    }
  } else {
    deletePost(boardID, postID);
  }
});

// 게시글 검색 버튼 이벤트
function search() {
  var option = $('#search_option option:selected').attr('value');
  var keyword = $('#search_keyword');
  searchPost(option, keyword);
}

// 작성 취소 버튼 이벤트
$(document).on('click', '.btn_cancel', function () {
  if (confirm("작성된 내용이 저장되지 않을 수도 있습니다. 이동하시겠습니까?") == true) {
    deleteAllAttachedFile();
    editorClear();
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

// 임시저장 게시물 클릭 이벤트
$(document).on('click', '.temp_post_click', function () {
  var postID = $(this).attr("data-post");
  var boardID = $(this).attr("data-board");
  postClear();
  editorAreaCreate("insert");
  var btn_cancel = $('.btn_cancel');
  btn_cancel.html("삭제");
  btn_cancel.addClass("btn_delete");
  btn_cancel.removeClass("btn_cancel");
  loadPost(boardID, postID);
  openFileAttachForm(postID);//파일첨부폼
});

// length Check 이벤트
function isValidLength(str, limit) {
  if (getByteLength(str)[0] <= limit) {
    return true;
  } else {
    return false;
  }
}

// 휴지통 게시물 클릭 이벤트
$(document).on('click', '.recycle_post_click', function () {
  var postID = $(this).attr("data-post");
  var boardID = getCurrentBoardID();
  getRecyclePost(postID, boardID);
});

// 휴지통 게시글 복원 이벤트
$(document).on('click', '.btn_restore', function () {
  var postID = $('#postID').html();
  if (confirm("원래 게시판으로 복원하시겠습니까?")) {
    restorePost(postID);
  }
});
