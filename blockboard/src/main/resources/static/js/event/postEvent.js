/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postEvent.js
 */
var autosave = null;

// 에디터 div 생성
function editorAreaCreate(method) {
  editorClear();
  $('#editorcontent-hidden').html("");
  $('#post_title').val("");
  $('#writecontent').css("display", "");
  $('#btn_write').css("display", "none");
  $('#btn_deletePost').attr('style', 'visibility:hidden');
  $('#btn_updatePost').attr('style', 'visibility:hidden');
  // textarea에 CKEditor 적용
  $('#editor').ckeditor();

  // 게시글 작성시에만(수정 X) 자동저장 & 임시저장 기능 작동
  if (method == "insert") {
    $('#btn_temp').attr('style', 'visibility:visible');
    on_autosave();
  } else {
    $('#btn_temp').attr('style', 'visibility:hidden');
  }
}

// 작성 폼 초기화
function editorClear() {
  if (CKEDITOR.instances.editor) {
    CKEDITOR.instances.editor.setData("");
  }
  var post_button = $('#btn_post');
  var writecontent = $('#writecontent');
  var btn_write = $('#btn_write');
  var btn_cancel = $('#btn_cancel');
  btn_write.css("display", "");
  btn_cancel.html("작성취소");
  post_button.html("저장");
  btn_cancel.attr('onclick', 'javascript:writeCancel()');
  post_button.attr('onclick', 'javascript:postFunction()');
  writecontent.css("display", "none");
  off_autosave();
}

// 게시글 조회 화면 Clear
function postClear() {
  $('#postcontent').html("");
}

// 게시글 목록 화면 Clear
function postsClear() {
  $('#postlist').html("");
}

// 현재 선택된 게시판 id 가져온다.
function getCurrentBoardID() {
  var tabs = $('#tab_id').children();
  var boardID = 0;

  $.each(tabs, function () {
    if($(this).hasClass("active_tab")) {
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
  deletePost(boardID, postID);
}

// 게시글 검색 버튼 이벤트
function search() {
  var option = $('#search_option option:selected').attr('value');
  var keyword = $('#search_keyword');
  searchPost(option, keyword);
}

function writeCancel() {
  if (confirm("작성된 내용이 저장되지 않을 수도 있습니다. 이동하시겠습니까?") == true) {
    editorClear();
    // 브라우저의 history값 구현 후에 마저 구현하기
    console.log("확인버튼 클릭");
  } else {
    console.log("취소버튼 클릭");
  }
}

// 에디터 UI에 hidden type으로 PostID 추가
function addPostIdToEditor(postID) {
  var source = $('#postid-template').html();
  var template = Handlebars.compile(source);
  var IDitem = { postID: postID };
  var itemList = template(IDitem);
  $('#editorcontent-hidden').html(itemList);
}

// 작성, 수정 버튼 클릭시 해당 게시판 선택 되어있게
function initBoardIdOptionInEditor(currentBoardID) {
  var options = $('#boardIDinEditor').children();
  console.log("받은 currentBoardID = " + currentBoardID);
  $(options).each(function(index, item) {
    var data = $(item).attr('data-tab'); // option의 boardID
    if(data == currentBoardID) {
      $(item).prop("selected", true);
    } else {
      $(item).prop("selected", false);
    }
  });
}

function on_autosave() {
  autosave = setInterval(function () {
    tempsaveFunction()
  }, 1000 * (60 * 3));
}

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