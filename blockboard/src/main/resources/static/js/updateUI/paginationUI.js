/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file paginationUI.js
 */

//기능 변경 업데이트
function updatePageList(data,pageList) {
  var source = $('#pageList-template').html();
  var template = Handlebars.compile(source);
  var pagesInfo = {pagesInfo: data,pageList:pageList};
  var item = template(pagesInfo);
  $('#pagination_content').html(item);
  $('#page'+data.currentPage).css('color','#28A745');
  $('#page'+data.currentPage).css('font-weight','bold');
  $('#page'+data.currentPage).css('font-size','110%');
  var boardID = getCurrentBoardID();
  getPostListByPageNum(data.currentPage,boardID);
}