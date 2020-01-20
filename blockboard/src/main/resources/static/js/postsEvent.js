function editorAreaCreate() {
  var editorcontent = $('#editorcontent');
  var writecontent = $('#writecontent');
  var btn_write = $('#btn_write');

  writecontent.css("display", "");
  btn_write.css("display", "none");
  editorcontent.append("<h2> 게시글제목 </h2> <input type=text id=post_title />");
  editorcontent.append("<textarea id=editor></textarea>");
  editorcontent.append("<button id=btn_post>올리기</button>")

  // textarea에 CKEditor 적용
  $('#editor').ckeditor();
}
function editorClear() {
  $('#editorcontent').html("");
  $('#writecontent').css("display", "none");
  $('#btn_write').css("display", "");
}

function postsClear() {
  $('#postcontent').html("");
}

// 게시글 작성 버튼 이벤트
$(document).on("click", "#btn_write", function () {
    editorAreaCreate();
});

// 게시글 작성 후 올리기 버튼 클릭
$(document).on("click", "#btn_post", function () {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#post_board_id option:selected').attr('data-tab');

  editorClear();
  $.ajax({
    type: 'POST',
    url: "/boards/" + boardID + "/posts/",
    data: {postTitle: postTitle,
          postContent: postContent},
    error: function() {
      alert('게시글 작성 실패');
    },
    success: function() {
      refreshPostList();
      console.log("게시글 저장 완료");
    }
  });
});

// 게시글 조회 후 수정 버튼 클릭
$(document).on("click", "#btn_updatePost", function () {
    var postID = $("#currentPostID").html();
    var post_button = $('#btn_post');
    var post_title = $('#post_title');
    postsClear(); // 게시글 리스트 Clear

    if($('#editorcontent').html() == "")    // 게시글 작성 중이 아닐 경우 게시글 작성 폼 불러오기
      editorAreaCreate();

    post_button.html('수정하기'); // 게시글 올리기 버튼 텍스트 변경
    post_button.attr('id', 'btn_update'); // 버튼 ID 변경 (btn_post -> btn_update)

    // 수정 화면에 저장되어 있던 게시글 정보(postTitle, postContent)를 띄워 준다.
    $.ajax({
        type: 'GET',
        url: "/boards/" + boardID + "/posts/" + postID + "/editor",
        async: false,
        error: function() {
          alert('게시글 수정 실패');
        },
        success: function (data) {
          post_title.val(data.postTitle); //
          CKEDITOR.instances.editor.setData(data.postContent);
        }
    });
});


// 수정한 게시물 서버에 저장할 때 이벤트
$(document).on("click", "#btn_updatePost", function () {
  var postID = $("#currentPostID").html();
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#post_board_id option:selected').attr('data-tab');

  editorClear();
  $.ajax({
    type: 'PUT',
    url: "/boards/" + boardID + "/posts/" + postID,
    async: false,
    data: {postTitle: postTitle,
      postContent: postContent},
    error: function() {
      alert('게시글 수정 실패');
    },
    success: function (data) {
      refreshPostList();
    }
  });
});


// 게시글 조회 후 삭제 버튼 클릭
$(document).on("click", "#btn_deletePost", function () {
    var postID = $("#currentPostID").html();
    postsClear();
    $.ajax({
        type: 'DELETE',
        url: "/boards/" + boardID + "/posts/" + postID,
        async: false,
        error: function() {
          alert('게시글 삭제 실패');
        },
        success: function (data) {
          refreshPostList();
        }
    });
});

// 게시글 작성, 수정, 삭제 시 해당 게시판 refresh 하는 함수
function refreshPostList() {
  var tabs = $('#tab_id').children();
  var boardID = 0;

  $.each(tabs, function() {
    var color = $(this).css('background-color');
    if(color == "rgb(144, 238, 144)") {
      boardID = $(this).attr('data-tab');
    }
  });
  
  if(boardID != 0) {
    $.ajax({
      type: 'GET',
      url: '/boards/' + boardID + "/posts",
      async: false,
      error: function () {
        alert('통신실패!');
      },
      success: function (data) {
        postsClear();
        $.each(data, function (key, value) {
          $('#postlist').append(
            "<tr height='30' class = 'postclick' data-post = '" + value.postID +
            "' onclick='javascript:clickTrEvent(this)' onmouseover = 'javascript:changeTrColor(this)' >" +
            "<td width='379'>" + value.postTitle + "</td>" +
            "<td width='73'>" + value.userName + "</td>" +
            "<td width='164'>" + value.postRegisterTime + "</td></tr>" +
            "<td style='visibility:hidden'>" + value.postID + "</td>"
          );
        });
      }
    });
  }
}