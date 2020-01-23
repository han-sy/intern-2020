
//"기능변경" 버튼 클릭시
function changeFunction() {
  var companyID = $('#serviceTitle').attr("value");
  console.log("companyID : " + companyID);
  $(function () {
    getOldFunctionInfoForChange(companyID);
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
      functionData.functionCheck = "ON";
    }
    else {
      functionData.functionCheck = "OFF";
    }
    functionDataList.push(functionData);
  });

  var jsonData = JSON.stringify(functionDataList);
  alert(jsonData);
  var askSave = confirm("기능변경 내용을 저장하시겠습니까?");
  if (askSave) {
    $(function () {
      getNewFunctionInfo(companyID, jsonData);//새로운 기능목록 불러와 기능목록 변경
    });
  }
  $('#config_container').html("");

}