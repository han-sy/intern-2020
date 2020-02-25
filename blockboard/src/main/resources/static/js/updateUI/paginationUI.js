/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file paginationUI.js
 */

//페이지 리스트 업데이트
function updatePostPageList(data, pageList) {
  let source = $('#pageList-template').html();
  let template = Handlebars.compile(source);
  let pagesInfo = {pagesInfo: data, pageList: pageList};
  let item = template(pagesInfo);
  $('#post_pagination_content').html(item);
  $('#post_page' + data.currentPage).css('color', '#28A745');
  $('#post_page' + data.currentPage).css('font-weight', 'bold');
  $('#post_page' + data.currentPage).css('font-size', '110%');
  let boardId = parseInt(getCurrentActiveBoardId());
  getPostListByPageNum(data.currentPage, boardId);
}

function updateCommentPageList(data, pageList) {
  let source = $('#pageList-template').html();
  let template = Handlebars.compile(source);
  let pagesInfo = {pagesInfo: data, pageList: pageList};
  let item = template(pagesInfo);
  $('.comments_pagination_content').html(item);
  $('#comments_page' + data.currentPage).css('color', '#28A745');
  $('#comments_page' + data.currentPage).css('font-weight', 'bold');
  $('#comments_page' + data.currentPage).css('font-size', '110%');
  let boardId = getBoardIdInPost();
  let postId = getPostIdInPost();
  getCommentListByPageNum(data.currentPage, boardId, postId,
      updateCommentListUI);//성공하면 댓글목록 갱신
}

//페이지 리스트 업데이트
function updateSearchPostPageList(data, pageList) {
  let source = $('#pageList-template').html();
  let template = Handlebars.compile(source);
  let pagesInfo = {pagesInfo: data, pageList: pageList};
  let item = template(pagesInfo);
  $('#post_pagination_content').html(item);
  $('#search_page' + data.currentPage).css('color', '#28A745');
  $('#search_page' + data.currentPage).css('font-weight', 'bold');
  $('#search_page' + data.currentPage).css('font-size', '110%');
  getSearchPostListByPageNum(data.currentPage);
}