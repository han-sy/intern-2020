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
  editorAreaCreate("insert");
  initBoardIdOptionInEditor(getCurrentBoardID());
});

// '임시저장' 이벤트 함수
function tempsaveFunction() {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var postID = $('#editor_postID').html();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');
  // 제목 & 내용 비었는지 검사
  if (checkEmpty()) {
    insertTempPost(boardID, postID, postTitle, postContent, true);
    refreshPostList();
  }
}

// '저장' 이벤트 함수
function postFunction() {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var postID = $('#editor_postID').html();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');
  // 제목 & 내용 비었는지 검사
  if (checkEmpty()) {
    // 게시글 ID가 존재하지 않으면? 바로 저장
    if (typeof postID == "undefined") {
      insertPost(boardID, postTitle, postContent);
    }
    // 임시 or 자동 저장된 글을 한번 더 '저장' 버튼을 누를 때
    else {
      insertTempPost(boardID, postID, postTitle, postContent, false);
      getTempPosts();
    }
    editorClear();
  }
}

// 게시글 제목 or 내용 비었는지 검사
function checkEmpty() {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  if (postTitle == "") {
    alert("게시글 제목을 입력해주세요.");
    return false;
  } else {
    if (postContent == "") {
      alert("게시글 내용을 입력해주세요.");
      return false;
    } else {
      return true;
    }
  }
}

// '수정' 버튼 클릭 후 '수정하기' 버튼 이벤트
function postUpdate() {
  var postID = $("#editor_postID").html();
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');
  updatePost(boardID, postID, postTitle, postContent);
}

// 게시글 조회 후 '수정' 버튼 이벤트
function postUpdateFunction() {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  postClear();
  editorAreaCreate("modify");
  var post_button = $('#btn_post');
  post_button.html('수정하기'); // 게시글 올리기 버튼 텍스트 변경
  post_button.attr('onclick', 'javascript:postUpdate()');
  loadPost(boardID, postID); // 에디터로 게시글 정보 불러옴.
}

// 게시글 조회 후 삭제 버튼 이벤트
function postDeleteFunction() {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  if (confirm("정말 삭제하시겠습니까? 삭제 후 복원되지 않습니다.") === true) {
    deletePost(boardID, postID);
  }
}

// 게시글 검색 버튼 이벤트
function search() {
  var option = $('#search_option option:selected').attr('value');
  var keyword = $('#search_keyword');
  searchPost(option, keyword);
}

// 작성 취소 버튼 이벤트
function writeCancel() {
  if (confirm("작성된 내용이 저장되지 않을 수도 있습니다. 이동하시겠습니까?") == true) {
    editorClear();
    // 브라우저의 history값 구현 후에 마저 구현하기
    console.log("확인버튼 클릭");
  } else {
    console.log("취소버튼 클릭");
  }
}

// 자동 저장 on
function on_autosave() {
  autosave = setInterval(function () {
    tempsaveFunction()
  }, 1000 * (60 * 3));
}

// 자동 저장 off
function off_autosave() {
  clearInterval(autosave);
}

// 임시저장 게시물 클릭 이벤트
function clickTempPostEvent(evt) {
  var postID = evt.getAttribute("data-post");
  postClear();
  editorAreaCreate("insert");
  var btn_cancel = $('#btn_cancel');
  btn_cancel.html("삭제");
  btn_cancel.attr('onclick', 'javascript:clickDeleteTempPost()');
  addPostIdToEditor(postID);
  //getTempPost(postID);
  setTimeout(function () {
    getTempPost(postID);
  }, 5);
}

// 임시저장 게시물 삭제 이벤트
function clickDeleteTempPost() {
  var postID = $('#editor_postID').html();
  deletePost(-1, postID);
}