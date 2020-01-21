
//"기능변경" 버튼 클릭시
function changeFunction() {
  var companyID = $('#serviceTitle').attr("value");
  console.log("companyID : " + companyID);
  $.ajax({
    type: 'GET',                 //POST 통신
    url: "/functions/" + companyID + "/info",    //탭의 data-tab속성의 값으로 된 html파일로 통신
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      console.log("success" + data);
      var containerObj = $('#config_container');
      containerObj.html("");
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
//기능변경사항 저장하기 버튼
function clickSaveFunctionChange() {
  var companyID = $('#serviceTitle').attr("value");
  var functionDataList = new Array();

  $("input[name=function]").each(function () {
    var functionData = new Object();
    functionData.functionID = $(this).val();
    if ($(this).is(":checked")) {
      functionData.functionCheck = 1;
    }
    else {
      functionData.functionCheck = 0;
    }
    functionDataList.push(functionData);
  });

  var jsonData = JSON.stringify(functionDataList);
  var askSave = confirm("기능변경 내용을 저장하시겠습니까?");
  if (askSave) {
    $(function () {
      getNewFunctionInfo(companyID, jsonData);
    });
  }
  $('#config_container').html("");

}
