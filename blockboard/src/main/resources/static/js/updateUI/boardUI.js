/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file boardUI.js
 */
//새로운 탭 내용으로 교체
function updateTab(data) {
  var source = $('#boards-template').html();
  var template = Handlebars.compile(source);
  var board = {boards: data};
  var itemList = template(board);
  $('#tab_id').html(itemList);
  updateboardListInEditor(board);

  $("#tab_id").children().each(function() {
    if($(this).attr('data-tab') > 0) {
      $(this).trigger('click');
      return;
    }
  });
}

//게시글 내용
function loadPostContent(data) {
  var source = $('#postcontent-template').html();
  var template = Handlebars.compile(source);
  var post = {post: data};
  var item = template(post);
  $('#postcontent').html(item);
}

//게시글 목록
function loadPostList(data) {
  var source = $('#posts-template').html();
  var template = Handlebars.compile(source);
  var post = {posts: data};
  var itemList = template(post);
  $('#post_table').html(itemList);

}

//삭제를 위한 UI
function getBoardListToDelete(data) {
  var source = $('#deleteboards-template').html();
  var template = Handlebars.compile(source);
  var boardList = {boards: data};
  var itemList = template(boardList);
  $('.modal-body-deleteBoard').html(itemList);
}

//이름변경을 위한 UI
function getBoardListToChangeName(data) {
  var source = $('#changeBoardName-template').html();
  var template = Handlebars.compile(source);
  var boardList = {boards: data};
  var itemList = template(boardList);
  $('.modal-body-changeBoardName').html(itemList);
}

/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 */
// 내 게시글/댓글 목록
function loadMyPostList(data) {
  var source = $('#my-posts-template').html();
  var template = Handlebars.compile(source);
  var post = {posts: data};
  var itemList = template(post);
  $('#postlist').html(itemList);
}

function showEmptyList() {
  var source = $('#empty-posts-template').html();
  $('#postlist').html(source);
}