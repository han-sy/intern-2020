/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postEvent.js
 */

 // 에디터 div 생성
function editorAreaCreate() {
  editorClear();
  var writecontent = $('#writecontent');
  var btn_write = $('#btn_write');
  writecontent.css("display", "");
  btn_write.css("display", "none");
  
  // textarea에 CKEditor 적용
  $('#editor').ckeditor();
}

// 작성 폼 초기화
function editorClear() {
  if(CKEDITOR.instances.editor) {
    CKEDITOR.instances.editor.setData("");
    CKEDITOR.instances.editor.destroy();
  }
  $('#post_title').val("");
  $('#writecontent').css("display", "none");
  $('#btn_write').css("display", "");
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
    var color = $(this).css('background-color');
    if (color == "rgb(144, 238, 144)") {
      boardID = $(this).attr('data-tab');
    }
  });

  return boardID;
}

// '글쓰기' 버튼 이벤트
$(document).on("click", "#btn_write", function () {
  editorAreaCreate();
});

// '글쓰기' 버튼 클릭 후 '올리기' 버튼 이벤트
$(document).on("click", "#btn_post", function () {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');
  if (postTitle == "") {
    alert("게시글 제목을 입력해주세요.");
  } else {
    if (postContent == "") {
      alert("게시글 내용을 입력해주세요.")
    }
    else {
      editorClear();
      insertPost(boardID, postTitle, postContent);
    }
  }
});

// 게시글 조회 후 '수정' 버튼 이벤트
$(document).on("click", "#btn_updatePost", function () {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  postClear();
  editorAreaCreate();
  var post_button = $('#btn_post');
  post_button.html('수정하기'); // 게시글 올리기 버튼 텍스트 변경
  post_button.attr('id', 'btn_update'); // 버튼 ID 변경 (btn_post -> btn_update)
  loadPost(boardID, postID); // 에디터로 게시글 정보 불러옴.
});

// '수정' 버튼 클릭 후 '수정하기' 버튼 이벤트
$(document).on("click", "#btn_update", function () {
  var postID = $("#editor_postID").html();
  console.log("수정하기 버튼 눌렀을 때 postID = " + postID);
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');
  editorClear();
  updatePost(boardID, postID, postTitle, postContent);
});


// 게시글 조회 후 삭제 버튼 이벤트
$(document).on("click", "#btn_deletePost", function () {
  var postID = $("#postID").html();
  var boardID = getCurrentBoardID();
  deletePost(boardID, postID);
});

// 게시글 검색 버튼 이벤트
$(document).on("click", "#search", function () {
  var option = $('#search_option option:selected').attr('value');
  var keyword = $('#search_keyword');
  searchPost(option, keyword);
});

$(document).on("click", "#btn_cancel", function () {
  if(confirm("작성된 내용이 저장되지 않을 수도 있습니다. 이동하시겠습니까?") == true) {
    editorClear();
    // 브라우저의 history값 구현 후에 마저 구현하기
    console.log("확인버튼 클릭");
  } else {
    console.log("취소버튼 클릭");
  }
});