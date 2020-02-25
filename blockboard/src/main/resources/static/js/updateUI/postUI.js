/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postUI.js
 */
function loadPostList(data) {
  let source = $('#posts-template').html();
  let template = Handlebars.compile(source);
  let post = {posts: data};
  let itemList = template(post);
  $('#post_table').html(itemList);
}

function loadPostContent(data) {
  let source = $('#post-content-template').html();
  let template = Handlebars.compile(source);
  let post = {post: data};
  let item = template(post);
  $('#post-content').html(item);
}

function showEmptyPostList() {
  let source = $('#empty-posts-template').html();
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
  let btn_deletePost = $('.btn_delete');
  let btn_updatePost = $('.btn_modify');
  btn_deletePost.show();
  btn_updatePost.show();
  btn_updatePost.html("복원");
  btn_updatePost.removeClass("btn_modify");
  btn_updatePost.addClass("btn_restore");
}

// 게시글 작성, 수정, 삭제 시 해당 게시판 refresh 하는 함수
function refreshPostListAfterPostCRUD() {
  let boardId = getCurrentActiveBoardId();
  postClear();
  getPageList(1, boardId, 0, updatePostPageList)
}

function addBannerOfSearchResult(keyword, option) {
  let source = $('#search-banner-template').html();
  let template = Handlebars.compile(source);
  let searchItems = {keyword: keyword, option:option};
  let searchBanner = template(searchItems);
  $('#search-banner').html(searchBanner);
}

function clearSearchBanner() {
  $('#search-banner').html('');
}

function clearSearchKeyword() {
  $('#search_keyword').val('');
}
/**
 * 수정 삭제 버튼 나타내기
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function showEditAndDeleteButtonInPost(data, userId) {
  var btn_deletePost = $('.btn_delete');
  var btn_updatePost = $('.btn_modify');
  if (data.userId == userId) {
    btn_deletePost.show();
    btn_updatePost.show();
  } else {
    btn_deletePost.hide();
    btn_updatePost.hide();
  }
}

/**
 * 게시글 내용 조회시 댓글관련 컨텐츠들
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function showCommentContents(boardId, postId) {
  if (functionOn.comments) {
    $(function () {
      getPageList(1,0,postId,updateCommentPageList);
      //getCommentListByPageNum(1,boardId, postId, getCommentAllContents); //삭제이후 tab에 게시판목록 업데이트 //CommentAjax.js 에 있음
      getCommentInputHtml("댓글", "입력", "", ".comment_input_container",
          "btn_open_comment", '', "commentText");
      updateCommentsCount(boardId, postId);
      fileFormClear();
    });
  }
}