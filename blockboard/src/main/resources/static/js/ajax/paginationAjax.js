/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file paginationAjax.js
 */

//리스트 받아오기
function getPageList(pageNumber, boardID,postID, successFunction) {
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
      console.log("data",data);
      successFunction(data, data.pageList);
    }
  });
}

