/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file paginationAjax.js
 */

//리스트 받아오기
function getPageList(pageNumber, boardId, postId, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/pages`,
    data: {
      boardId: boardId,
      postId: postId,
      pageNumber: pageNumber
    },
    error: function (error) {  //통신 실패시
      errorFunction(error);
    },
    success: function (data) {
      successFunction(data, data.pageList);
    }
  });
}

// 검색 결과 리스트 받아오기
function getSearchPageList(pageNumber, keyword, option, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/pages/search`,
    contentType: 'charset=UTF-8',
    data: {
      keyword: keyword,
      option: option,
      pageNumber: pageNumber
    },
    error: function (error) {  //통신 실패시
      errorFunction(error);
    },
    success: function (data) {
      successFunction(data, data.pageList, true);
    }
  });
}