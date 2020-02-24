/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file functionEvent.js
 */

//"기능변경" 버튼 클릭시
function changeFunction() {
  $(function () {
    getFunctionList(getFunctionCheckList);
  });
}

//기능변경사항 저장하기 버튼
function clickSaveFunctionChange() {
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
  var isAcceptance = confirm("기능변경 내용을 저장하시겠습니까?");
  if (isAcceptance) {
    $(function () {
      updateNewFunctionInfo(jsonData);//새로운 기능목록 불러와 기능목록 변경
    });
  }

}

//기능변경 on/off버튼 텍스트 바꾸기
$(document).on('click', '._function-switch', function () {
  var switchText = $(this).find("._switch");
  var checkBox = $(this).find(".function_checkbox");
  console.log($(this).closest(".btn-group-toggle").hasClass("first_function"));

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
  if($(this).closest(".btn-group-toggle").hasClass("first_function")){
    ChangeFunctionListUI($(".first_function").find("._function-switch"));
  }
});

function ChangeFunctionListUI(obj){
  console.log("첫번째 값:"+obj.find("._switch").html());
  if(obj.find("._switch").html()=="OFF"){
    $('.comment_function').addClass("display_none");
    $('.comment_function').find("._switch").html("OFF");
    $('.comment_function').find('._function-switch').removeClass('btn-success');
    $('.comment_function').find('._function-switch').addClass('btn-default');
    $('.comment_function').find(".function_checkbox").removeAttr("checked");
  }
  else{
    $('.comment_function').removeClass("display_none");
  }
}