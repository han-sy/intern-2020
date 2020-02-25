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
function updateTabByNewBoardListAfterDeleteBoard(deleteBoards) {
  $.ajax({
    type: 'DELETE',
    url: `/boards`,
    data: JSON.stringify(deleteBoards),
    dataType: "json",
    contentType: 'application/json',
    error: function (xhr) {  //통신 실패시
      errorFunction(xhr);
    },
    complete: function () {
      getBoardList(updateTab);//새로운 탭 내용으로 교체
    }
  });
  $('#config_container').html("");
}

//게시판 이름변경후 탭업데이트
function updateTabByNewBoardListAfterUpdateBoardName(newTitles) {
  $.ajax({
    type: 'PUT',
    url: `/boards`,
    data: newTitles,
    dataType: "json",
    contentType: 'application/json',
    error: function (xhr) {  //통신 실패시
      errorFunction(xhr);
    },
    complete: function () {
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
function getPostDataAfterPostClick(postId, boardId) {
  let userId = $('#current_user_info').attr('data-id');
  postClear();
  $.ajax({
    type: 'GET',
    url: `/boards/${boardId}/posts/${postId}`,
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      $('#writecontent').hide();
      $('#btn_write').show();

      //게시글 내용 출력
      loadPostContent(data);
      showAttachFileContents(postId);
      // 작성글의 userId와 현재 로그인한 userId가 같으면 삭제버튼 표시
      showEditAndDeleteButtonInPost(data, userId);

      showCommentContents(boardId, postId);
    }
  });
}

function getSearchPostListByPageNum(pageNum) {
  getSearchPost(getSearchBannerKeyword(), getSearchBannerOption(), pageNum);
}
//탭클릭후 게시판 목록 불러오기
function getPostListByPageNum(pageNum, boardId) {
  clearSearchBanner();
  let btn_write = $('#btn_write');
  if (boardId < 0) {
    btn_write.attr('style', 'visibility:hidden');
  }

  switch (boardId) {
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
        url: `/boards/${boardId}/posts`,
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


