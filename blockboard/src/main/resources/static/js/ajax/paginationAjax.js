/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file paginationAjax.js
 */

//리스트 받아오기
function getPageList(pageNumber, boardID, postID, successFunction) {
  $.ajax({
    type: 'GET',
    url: `/pages`,
    data: {
      boardID: boardID,
      postID: postID,
      pageNumber: pageNumber
    },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      successFunction(data, data.pageList);
    }
  });
}

// 검색 결과 리스트 받아오기
function getSearchPageList(pageNumber, keyword, option, successFunction) {
  console.log("keyword = ", keyword);
  console.log("option = ", option);
  $.ajax({
    type: 'GET',
    url: `/pages/search`,
    contentType: 'charset=UTF-8',
    data: {
      keyword: keyword,
      option: option,
      pageNumber: pageNumber
    },
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      console.log("검색 페이징 데이터 = ", data);
      successFunction(data, data.pageList, true);
    }
  });
}