/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postUI.js
 */

// 에디터 div 생성
function editorAreaCreate(method) {
  editorClear();
  createEditorTemplate();
  $('#writecontent').css("display", "");
  $('#btn_write').css("display", "none");
  $('#btn_deletePost').attr('style', 'visibility:hidden');
  $('#btn_updatePost').attr('style', 'visibility:hidden');
  // textarea에 CKEditor 적용
  var add_on = "";
  if (functionOn.postSticker) {
    add_on += ",emoji";
  }
  if (functionOn.postInlineImage) {
    add_on += ",image2";
  }
  var original_config = CKEDITOR.config.plugins;
  CKEDITOR.replace('editor', {
    plugins: original_config + add_on
  });

  // 게시글 작성시에만(수정 X) 자동저장 & 임시저장 기능 작동
  if (method == "insert") {
    $('#btn_temp').attr('style', 'visibility:visible');
    if (functionOn.postTempSave) {
      on_autosave();
    }
  } else {
    $('#btn_temp').attr('style', 'visibility:hidden');
  }

  console.log(CKEDITOR.instances.editor.config);
}

// 작성 폼 초기화
function editorClear() {
  if (CKEDITOR.instances.editor) {
    CKEDITOR.instances.editor.setData("");
  }
  var post_button = $('#btn_post');
  var writecontent = $('#writecontent');
  var btn_cancel = $('#btn_cancel');
  $('#post_title').val("");
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

// 에디터 UI에 hidden type으로 PostID 추가
function addPostInfoToEditor(postID, boardID) {
  var source = $('#postid-template').html();
  var template = Handlebars.compile(source);
  var IDitem = {postID: postID, boardID: boardID};
  var itemList = template(IDitem);
  $('#editorcontent-hidden').html(itemList);
}

// 작성, 수정 버튼 클릭시 해당 게시판 선택 되어있게
function initBoardIdOptionInEditor(currentBoardID) {
  var options = $('#selectedBoardIDinEditor').children();
  $(options).each(function (index, item) {
    var data = $(item).attr('data-tab'); // option의 boardID
    if (data == currentBoardID) {
      $(item).prop("selected", true);
    } else {
      $(item).prop("selected", false);
    }
  });
}

function updateboardListInEditor(board) {
  source = $('#writecontent-boards-template').html();
  template = Handlebars.compile(source);
  itemList = template(board);
  $('#selectedBoardIDinEditor').html(itemList);
}




function createEditorTemplate() {
  var source = $('#editorcontent-template').html();
  var template = Handlebars.compile(source);
  $('#editorcontent').html(template);
}

/**
 * 게시글 내용 조회시 첨부파일관련 컨텐츠들
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function showAttachFileContents(postID) {
  if (functionOn.postFileAttach) {
    var container = $("#postcontent").find(
        ".attached_file_list_container_post");
    getFileList(postID, 0, container, updateFileListInPostUI);
  }
}

/**
 * 게시글 내용 조회시 댓글관련 컨텐츠들
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function showCommentContents(boardID, postID) {
  if (functionOn.comments) {
    $(function () {
      getPageList(1,0,postID,updateCommentPageList);
      //getCommentListByPageNum(1,boardID, postID, getCommentAllContents); //삭제이후 tab에 게시판목록 업데이트 //CommentAjax.js 에 있음
      getCommentInputHtml("댓글", "입력", "", ".comment_input_container",
          "btn_open_comment", '', "commentText");
      updateCommentsCount(boardID, postID);
      fileFormClear();
    });
  }
}

/**
 * 수정 삭제 버튼 나타내기
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function showEditAndDeleteButtonInPost(data, userID) {
  var btn_deletePost = $('.btn_delete');
  var btn_updatePost = $('.btn_modify');
  if (data.userID == userID) {
    btn_deletePost.attr('style', 'visibility:visible');
    btn_updatePost.attr('style', 'visibility:visible');
  } else {
    btn_deletePost.attr('style', 'display:none');
    btn_updatePost.attr('style', 'display:none');
  }
}
