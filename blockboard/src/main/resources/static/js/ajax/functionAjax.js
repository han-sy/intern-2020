//기능변경후 새로운 사용중인기능목록 불러오기
function getNewFunctionInfo(companyID, jsonData) {
  $.ajax({
    type: 'POST',
    url: "/functions/" + companyID ,
    data: { functionInfoData: jsonData },
    error: function () {  //통신 실패시
      alert('통신실패!'+error);
    },
    success: function (data) {
      console.log("success" + data);
      var containerObj = $('#fuctionListContainer');
      containerObj.html("현재 사용중인 기능 : ");
      //TODO Template 적용예정
      $.each(data, function (key, value) {
        console.log(value.functionName + " : " + value.companyID);
        if (value.companyID == 0) {
          containerObj.append("<span id = functionAble" + value.functionID + " style=display:none value=off>" + value.functionName + "</span>");
        }
        else {
          containerObj.append("<span id = functionAble" + value.functionID + " value=on>" + value.functionName + " </span>");
        }

      });
      alert("기능이 변경되었습니다.");
      $('#postcontent').html("");
    }
  });
}

//기존 기능목록 불러와 체크박스와 함께 출력
function getOldFunctionInfoForChange(companyID) {
  $.ajax({
    type: 'GET',
    url: "/functions/" + companyID ,
    error: function (error) {  //통신 실패시
      alert('통신실패!'+error);
    },
    success: function (data) {
      console.log("success" + data);
      var containerObj = $('#config_container');
      containerObj.html("");
      //TODO Template 적용예정
      $.each(data, function (key, value) {
        console.log(value.functionInfoData);
        if (value.companyID == 0) {
          containerObj.append("<div><span>" + value.functionName + "</span> <label><input type=checkbox name=function value=" +
            value.functionID + ">현재상태 OFF</label></div>");
        }
        else {
          containerObj.append("<div><span>" + value.functionName + "</span> <label><input type=checkbox name=function value=" +
            value.functionID + " checked>현재상태 ON</label></div>");
        }

      });
      containerObj.append(" <a id ='addFuncBtn' onclick = javascript:clickSaveFunctionChange(this) style=cursor:pointer>저장하기</a>" +
        "<button class = 'functionClose' type='button' onclick=javascript:clickConfigClose(this)>닫기</button>");
    }
  });
}