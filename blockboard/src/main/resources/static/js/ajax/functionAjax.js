/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file functionAjax.js
 */
//리스트 받아오기
function getFunctionList(successFunction) {
  $.ajax({
    type: 'GET',
    url: `/functions`,
    error: function (error) {  //통신 실패시
      errorFunction(error);
    },
    success: function (data) {
      successFunction(data);
    }
  });
}

//기능변경후 새로운 사용중인기능목록 불러오기
function updateNewFunctionInfo(functionDTOList) {
  $.ajax({
    type: 'POST',
    url: `/functions`,
    data: functionDTOList,
    dataType: "json",
    contentType: 'application/json',
    error: function (error) {  //통신 실패시
      errorFunction(error);
    },
    success: function () {
    },
    complete: function () {
      getFunctionList(updateNewFunctionInfoUI);
    }
  });
}



