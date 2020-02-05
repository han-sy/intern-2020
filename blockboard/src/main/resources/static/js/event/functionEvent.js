/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file functionEvent.js
 */

//"기능변경" 버튼 클릭시
function changeFunction() {
  var companyID = $('.navbar-brand').attr("value");
  console.log("companyID : " + companyID);
  $(function () {
    getFunctionList(companyID, getFunctionCheckList);
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
      functionData.functionOn = true;
    } else {
      functionData.functionOn = false;
    }
    functionDataList.push(functionData);
  });

  var jsonData = JSON.stringify(functionDataList);
  //alert(jsonData);
  var askSave = confirm("기능변경 내용을 저장하시겠습니까?");
  if (askSave) {
    $(function () {
      getNewFunctionInfo(companyID, jsonData);//새로운 기능목록 불러와 기능목록 변경
    });
  }

}

//기능변경 on/off버튼 텍스트 바꾸기
$(document).on('click', '._function-switch', function () {
  var switchText = $(this).find("._switch");
  var checkBox = $(this).find(".function_checkbox");
  console.log(switchText.html() == "ON");
  if (checkBox.prop("checked")) {
    $(this).removeClass('btn-success');
    $(this).addClass('btn-default');
    switchText.html("OFF");
    checkBox.removeAttr("checked");
  } else {
    $(this).removeClass('btn-default');
    $(this).addClass('btn-success');
    switchText.html("ON");
    checkBox.prop("checked", true);
  }
  $(this).removeClass("active");
});