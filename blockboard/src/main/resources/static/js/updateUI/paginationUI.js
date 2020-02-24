/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file paginationUI.js
 */

//페이지 리스트 업데이트
function updatePostPageList(data, pageList) {
  console.info("data : ",data);
  var source = $('#pageList-template').html();
  var template = Handlebars.compile(source);
  var pagesInfo = {pagesInfo: data, pageList: pageList};
  var item = template(pagesInfo);
  $('#post_pagination_content').html(item);
  $('#post_page' + data.currentPage).css('color', '#28A745');
  $('#post_page' + data.currentPage).css('font-weight', 'bold');
  $('#post_page' + data.currentPage).css('font-size', '110%');
  var boardId = parseInt(getCurrentActiveBoardId());
  getPostListByPageNum(data.currentPage, boardId);
}

function updateCommentPageList(data, pageList) {
  var source = $('#pageList-template').html();
  var template = Handlebars.compile(source);
  var pagesInfo = {pagesInfo: data, pageList: pageList};
  var item = template(pagesInfo);
  $('.comments_pagination_content').html(item);
  $('#comments_page' + data.currentPage).css('color', '#28A745');
  $('#comments_page' + data.currentPage).css('font-weight', 'bold');
  $('#comments_page' + data.currentPage).css('font-size', '110%');
  var boardId = getBoardIdInPost();
  var postId = getPostIdInPost();
  getCommentListByPageNum(data.currentPage,boardId, postId, updateCommentListUI);//성공하면 댓글목록 갱신
}