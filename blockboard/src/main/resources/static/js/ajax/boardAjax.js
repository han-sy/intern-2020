/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file boardAjax.js
 */

//탭 업데이트 새로운 게시판 목록으로
function updateTabByNewBoardListAfterAddBoard(boardName,userData) {
  $.ajax({
    type: 'POST',
    url: "/boards",
    data: {boardName: boardName,userData:userData},
    error: function (xhr) {  //통신 실패시
      errorFunction(xhr);
    },
    success: function (data) {    //들어오는 data는 boardDTOlist
      getBoardList(updateTab);//새로운 탭 내용으로 교체
    }
  });
}

//게시판 삭제후 탭업데이트
function updateTabByNewBoardListAfterDeleteBoard(jsonData,userData) {
  $.ajax({
    type: 'DELETE',
    url: "/boards",
    data: {deleteList: jsonData,userData:userData},
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
function updateTabByNewBoardListAfterUpdateBoardName(jsonData,userData) {
  $.ajax({
    type: 'PUT',
    url: "/boards",
    data: {newTitles: jsonData,userData:userData},
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

//TODO handlebar 적용하기
//게시물 클릭후 게시물 데이터 받아오기
function getPostDataAfterPostClick(postID, boardID) {
  var postContentObj = $('#postcontent');
  var userID = $('#current_user_info').attr('data-id');
  postContentObj.html("");
  $.ajax({
    type: 'GET',
    url: "/boards/" + boardID + "/posts/" + postID,
    error: function (error) {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      $('#writecontent').hide();
      $('#btn_write').show();

      //게시글 내용 출력
      loadPostContent(data);

      // 작성글의 userID와 현재 로그인한 userID가 같으면 삭제버튼 표시
      var commentAbleObj = $('#functionAble1');
      var btn_deletePost = $('#btn_deletePost');
      var btn_updatePost = $('#btn_updatePost');
      if (data.userID == userID) {
        console.log("수정 삭제 표시");
        btn_deletePost.attr('style', 'visibility:visible');
        btn_updatePost.attr('style', 'visibility:visible');
      } else {
        console.log("수정 삭제 미표시");
        btn_deletePost.attr('style', 'visibility:hidden');
        btn_updatePost.attr('style', 'visibility:hidden');
      }
      var postContentHtml = "";

      if (commentAbleObj.attr("value") == "on") {

        $(function () {
          getCommentList(boardID, postID, getCommentAllContents); //삭제이후 tab에 게시판목록 업데이트 //CommentAjax.js 에 있음
          updateCommentsCount(boardID, postID);
        });
      }
    }
  });
}

//탭클릭후 게시판 목록 불러오기
function getPostsAfterTabClick(boardID) {
  if (boardID == -1) {
    getTempPosts();
  } else {
    $.ajax({
      type: 'GET',
      url: '/boards/' + boardID + "/posts",
      error: function () {  //통신 실패시
        alert('통신실패!');
      },
      success: function (data) {
        loadPostList(data);
      }
    });
  }
}

