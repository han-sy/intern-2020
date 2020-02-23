/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file functionUI.js
 */
//기능 변경 업데이트
function updateNewFunctionInfoUI(data) {
  var source = $('#functionList-template').html();
  var template = Handlebars.compile(source);
  var functions = {functions: data};
  var itemList = template(functions);
  $('#functionListContainer').html(itemList);
  $('#postcontent').html("");
  alert("기능이 변경되었습니다.");
  getBoardList(updateTab);
  //$("#tab_id").children().first().trigger('click');
  resetFunctionAble();
}

//기능변경 체크 UI
function getFunctionCheckList(data) {
  var source = $('#changeFunctionInfo-template').html();
  var template = Handlebars.compile(source);
  var functions = {functions: data};
  var itemList = template(functions);
  $('.modal-body-changeFunctions').html(itemList);
  //alert($('.btn-group-toggle').first().html());
  //alert($('.btn-group-toggle').first().find(".function_checkbox").attr("value"));
  $('.btn-group-toggle').first().addClass("first_function");
  ChangeFunctionListUI($(".first_function").find("._function-switch"));
}