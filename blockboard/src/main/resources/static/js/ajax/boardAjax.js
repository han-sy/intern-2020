/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file boardAjax.js
 */

//탭 업데이트 새로운 게시판 목록으로
function updateTabByNewBoardListAfterAddBoard(boardName) {
  $.ajax({
    type: 'POST',
    url: `/boards`,
    data: {boardName: boardName},
    error: function (xhr) {  //통신 실패시
      errorFunction(xhr);
    },
    success: function (data) {    //들어오는 data는 boardDTOlist
      getBoardList(updateTab);//새로운 탭 내용으로 교체
    }
  });
}

//게시판 삭제후 탭업데이트
function updateTabByNewBoardListAfterDeleteBoard(jsonData) {
  $.ajax({
    type: 'DELETE',
    url: `/boards`,
    data: {deleteList: jsonData},
    error: function (xhr) {  //통신 실패시
      errorFunction(xhr);
    },
    success: function () {
      getBoardList(updateTab);//새로운 탭 내용으로 교체
    }
  });
  $('#config_container').html("");
}

//게시판 이름변경후 탭업데이트
function updateTabByNewBoardListAfterUpdateBoardName(jsonData) {
  $.ajax({
    type: 'PUT',
    url: `/boards`,
    data: {newTitles: jsonData},
    error: function (xhr) {  //통신 실패시
      errorFunction(xhr);
    },
    success: function () {
      getBoardList(updateTab);//새로운 탭 내용으로 교체
    }
  });
}

//리스트 받아오기
function getBoardList(successFunction) {
  $.ajax({
    type: 'GET',
    url: '/boards',
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      successFunction(data);
    }
  });
}

//게시물 클릭후 게시물 데이터 받아오기
function getPostDataAfterPostClick(postID, boardID) {
  var userID = $('#current_user_info').attr('data-id');
  postClear();
  $.ajax({
    type: 'GET',
    url: `/boards/${boardID}/posts/${postID}`,
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      $('#writecontent').hide();
      $('#btn_write').show();

      //게시글 내용 출력
      loadPostContent(data);
      if(functionOn.postFileAttach){
        var container = $("#postcontent").find(".attached_file_list_container_post");
        getFileList(postID,0,container,updateFileListInPostUI);
      }


      // 작성글의 userID와 현재 로그인한 userID가 같으면 삭제버튼 표시
      var btn_deletePost = $('.btn_delete');
      var btn_updatePost = $('.btn_modify');
      if (data.userID == userID) {
        btn_deletePost.attr('style', 'visibility:visible');
        btn_updatePost.attr('style', 'visibility:visible');
      } else {
        btn_deletePost.attr('style', 'display:none');
        btn_updatePost.attr('style', 'display:none');
      }
      var postContentHtml = "";

      if (functionOn.comments) {
        $(function () {
          getCommentList(boardID, postID, getCommentAllContents); //삭제이후 tab에 게시판목록 업데이트 //CommentAjax.js 에 있음

          updateCommentsCount(boardID, postID);
          fileFormClear();
        });
      }
    }
  });
}

//탭클릭후 게시판 목록 불러오기
function getPostListByPageNum(pageNum, boardID) {
  var btn_write = $('#btn_write');
  if (boardID < 0) {
    btn_write.attr('style', 'visibility:hidden');
  }

  switch (boardID) {
    case BOARD_ID.MY_POST:
      getMyPosts(pageNum);
      break;
    case BOARD_ID.MY_REPLY:
      getMyReplies(pageNum);
      break;
    case BOARD_ID.TEMP_BOX:
      getTempPosts(pageNum);
      break;
    case BOARD_ID.RECYCLE:
      getRecyclePosts(pageNum);
      break;
    case BOARD_ID.RECENT:
      getRecentPosts(pageNum);
      break;
    case BOARD_ID.POPULAR:
      getPopularPostList(pageNum);
      break;
    default:
      btn_write.attr('style', 'visibility:visible');
      $.ajax({
        type: 'GET',
        url: `/boards/${boardID}/posts`,
        data: {
          pageNumber: pageNum
        },
        error: function () {  //통신 실패시
          alert('통신실패!');
        },
        success: function (data) {
          loadPostList(data);
        }
      });
  }
}

