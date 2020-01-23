function editorAreaCreate() {
  editorClear();
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

// 게시글 조회 화면 Clear
function postClear() {
  $('#postcontent').html("");
}

// 게시글 목록 화면 Clear
function postsClear() {
  $('#postlist').html("");
}

function getCurrentBoardID() {
  var tabs = $('#tab_id').children();
  var boardID = 0;

  $.each(tabs, function() {
    var color = $(this).css('background-color');
    if(color == "rgb(144, 238, 144)") {
      boardID = $(this).attr('data-tab');
    }
  });

  return boardID;
}

// 게시글 작성 버튼 이벤트
$(document).on("click", "#btn_write", function () {
    editorAreaCreate();
});

// 게시글 작성 후 올리기 버튼 클릭
$(document).on("click", "#btn_post", function () {
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');
  if(postTitle == "") {
    alert("게시글 제목을 입력해주세요."); 
  } else {
    if(postContent == "") {
        alert("게시글 내용을 입력해주세요.")
    }
    else {
        editorClear();
        $.ajax({
          type: 'POST',
          url: "/boards/" + boardID + "/posts/",
          data: {postTitle: postTitle,
                postContent: postContent},
          error: function(data) {
            console.log("Error");
          },
          success: function() {
            refreshPostList();
            console.log("Success!!");
          }
        });
    }
  }
});

// 게시글 조회 후 수정 버튼 클릭
$(document).on("click", "#btn_updatePost", function () {
    var postID = $("#postID").html();
    var boardID = getCurrentBoardID();
    postClear(); // 게시글 조회 화면 Clear

    if($('#editorcontent').html() == "")    // 게시글 작성 중이 아닐 경우 게시글 작성 폼 불러오기
      editorAreaCreate();
    var post_title = $('#post_title');
    var editorcontent = $('#editorcontent');
    var post_button = $('#btn_post');
    post_button.html('수정하기'); // 게시글 올리기 버튼 텍스트 변경
    post_button.attr('id', 'btn_update'); // 버튼 ID 변경 (btn_post -> btn_update)

    $.getJSON("/boards/" + boardID + "/posts/" + postID + "/editor", function(data) {
      editorcontent.append("<a id=postID style=visibility:hidden>" + postID + "</a>");
      post_title.val(data.postTitle); //
      CKEDITOR.instances.editor.setData(data.postContent);
      console.log(data);
    });
});

// 수정한 게시물 서버에 저장할 때 이벤트
$(document).on("click", "#btn_update", function () {
  var postID = $("#postID").html();
  var postTitle = $('#post_title').val();
  var postContent = CKEDITOR.instances.editor.getData();
  var boardID = $('#boardIDinEditor option:selected').attr('data-tab');

  editorClear();
  $.ajax({
    type: 'PUT',
    url: "/boards/" + boardID + "/posts/" + postID,
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
    var postID = $("#postID").html();
    var boardID = getCurrentBoardID();
    postsClear();
    $.ajax({
        type: 'DELETE',
        url: "/boards/" + boardID + "/posts/" + postID,
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
  var boardID = getCurrentBoardID();
  
  if(boardID != 0) {
    $.ajax({
      type: 'GET',
      url: '/boards/' + boardID + "/posts",
      error: function () {
        alert('통신실패!');
      },
      success: function (data) {
        postClear();
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

// 게시글 검색
$(document).on("click", "#search", function () {
  var option = $('#search_option option:selected');
  var keyword = $('#search_keyword');

  $.ajax({
    type: 'GET',
    url: '/boards/0/posts/search',
    data: {
      option: option.html(),
      keyword: keyword.val()
    },
    dataType: 'JSON',
    error: function() {
      alert('검색 실패');
    },
    success: function (data) {
      postClear(); // 게시글 조회 화면 Clear
      postsClear(); // 게시글 목록 화면 Clear
      console.log("검색결과 = " + data[0]);
      keyword.val("");

      $.template("searchResultTmpl", 
        '<tr height="30" class=postclick data-post=${postID} onclick="javascript:clickTrEvent(this)"' +
        'onmouseover = "javascript:changeTrColor(this)">' +
        '<td width="379">${postTitle}</td>' +
        '<td width="73">${userName}</td>' +
        '<td width="164">${postRegisterTime}</td></tr>' +
        '<td style="visibility:hidden">${postID}</td>' +
        '<td style="visibility:hidden">${boardID}</td>'
      );

      $.tmpl("searchResultTmpl", data).appendTo("#postlist");
    }
  });
});