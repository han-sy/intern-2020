/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file paginationUI.js
 */

//페이지 리스트 업데이트
function updatePostPageList(data, pageList) {
  var source = $('#pageList-template').html();
  var template = Handlebars.compile(source);
  var pagesInfo = {pagesInfo: data, pageList: pageList};
  var item = template(pagesInfo);
  $('#post_pagination_content').html(item);
  $('#post_page' + data.currentPage).css('color', '#28A745');
  $('#post_page' + data.currentPage).css('font-weight', 'bold');
  $('#post_page' + data.currentPage).css('font-size', '110%');
  var boardID = parseInt(getCurrentActiveBoardID());
  getPostListByPageNum(data.currentPage, boardID);
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
  var boardID = getBoardIDInPost();
  var postID = getPostIDInPost();
  getCommentListByPageNum(data.currentPage, boardID, postID,
      updateCommentListUI);//성공하면 댓글목록 갱신
}

//페이지 리스트 업데이트
function updateSearchPostPageList(data, pageList) {
  var source = $('#pageList-template').html();
  var template = Handlebars.compile(source);
  var pagesInfo = {pagesInfo: data, pageList: pageList};
  var item = template(pagesInfo);
  $('#post_pagination_content').html(item);
  $('#search_page' + data.currentPage).css('color', '#28A745');
  $('#search_page' + data.currentPage).css('font-weight', 'bold');
  $('#search_page' + data.currentPage).css('font-size', '110%');
  getSearchPostListByPageNum(data.currentPage);
}