/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postEvent.js
 */

 // 에디터 div 생성
function editorAreaCreate() {
  editorClear();
  var editorcontent = $('#editorcontent');
  var writecontent = $('#writecontent');
  var btn_write = $('#btn_write');
  writecontent.show();
  writecontent.css("display", "");
  btn_write.css("display", "none");
  
  $.template("editorTmpl",
    '<h2> 게시글제목 </h2> ' +
    '<input type="text" id="post_title" />' +
    '<textarea id=editor></textarea>' + 
    '<button id=btn_post>올리기</button>'
  );
  $.tmpl("editorTmpl").appendTo(editorcontent);
  // textarea에 CKEditor 적용
  $('#editor').ckeditor();
}

// 에디터 div 내용 삭제
function editorClear() {
  if(CKEDITOR.instances.editor) {
    CKEDITOR.instances.editor.destroy();
  }
  $('#editorcontent').html("");
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
  var postID = $("#postID").html();
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