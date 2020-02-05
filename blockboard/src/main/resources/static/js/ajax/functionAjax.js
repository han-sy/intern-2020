/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file functionAjax.js
 */
//리스트 받아오기
function getFunctionList(companyID, successFunction) {
  $.ajax({
    type: 'GET',
    url: "/functions/" + companyID,
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
      successFunction(data);
    }
  });
}

//기능변경후 새로운 사용중인기능목록 불러오기
function getNewFunctionInfo(companyID, jsonData) {
  //alert(jsonData);
  $.ajax({
    type: 'POST',
    url: "/functions/" + companyID,
    data: {functionInfoData: jsonData},
    error: function () {  //통신 실패시
      alert('통신실패!' + error);
    },
    success: function (data) {
      getFunctionList(companyID, updateNewFunctionInfoUI);
    }
  });
}



