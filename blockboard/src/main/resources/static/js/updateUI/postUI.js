/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postUI.js
 */
function loadPostList(data) {
  var source = $('#posts-template').html();
  var template = Handlebars.compile(source);
  var post = {posts: data};
  var itemList = template(post);
  $('#post_table').html(itemList);
}

function loadPostContent(data) {
  var source = $('#post-content-template').html();
  var template = Handlebars.compile(source);
  var post = {post: data};
  var item = template(post);
  $('#post-content').html(item);
}

function showEmptyPostList() {
  var source = $('#empty-posts-template').html();
  $('#post-list').html(source);
}

function postClear() {  // 게시글 조회 화면 Clear
  $('#post-content').html("");
}

function postsClear() { // 게시글 목록 화면 Clear
  $('#post-list').html("");
}

function showWriteContentArea() {
  $('#write-content').show();
  $('#btn_write').hide();
}

function hideWriteContentArea() {
  $('#write-content').hide();
  $('#btn_write').show();
}

function showTempSaveButton() {
  $('#btn_temp').show();
}

function hideTempSaveButton() {
  $('#btn_temp').hide();
}

function updateButtonOnRecycleBoard() {
  var btn_deletePost = $('.btn_delete');
  var btn_updatePost = $('.btn_modify');
  btn_deletePost.show();
  btn_updatePost.show();
  btn_updatePost.html("복원");
  btn_updatePost.removeClass("btn_modify");
  btn_updatePost.addClass("btn_restore");
}

// 게시글 작성, 수정, 삭제 시 해당 게시판 refresh 하는 함수
function refreshPostListAfterPostCRUD() {
  let boardId = getCurrentActiveBoardID();
  postClear();
  getPageList(1, boardId, 0, updatePostPageList)
}

/**
 * 수정 삭제 버튼 나타내기
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function showEditAndDeleteButtonInPost(data, userID) {
  var btn_deletePost = $('.btn_delete');
  var btn_updatePost = $('.btn_modify');
  if (data.userID == userID) {
    btn_deletePost.show();
    btn_updatePost.show();
  } else {
    btn_deletePost.hide();
    btn_updatePost.hide();
  }
}